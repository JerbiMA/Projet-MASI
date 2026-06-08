import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import model.*;
import factory.ShapeFactory;
import command.*;
import memento.DrawingMemento;
import observer.ActionSubject;
import strategy.*;
import singleton.Logger;
import decorator.*;
import adapter.SqliteStorageAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class App extends Application {

    private Drawing drawing;
    private CommandHistory commandHistory;
    private ActionSubject actionSubject;
    private ShapeFactory shapeFactory;
    private SqliteStorageAdapter storageAdapter;

    private Canvas canvas;
    private TextArea logArea;

    private String selectedShapeType = "rectangle";
    private Color selectedColor = Color.BLACK;
    private boolean fillEnabled = false;
    private boolean dashedEnabled = false;
    private double startX, startY;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void start(Stage stage) {
        drawing = new Drawing();
        commandHistory = new CommandHistory();
        actionSubject = new ActionSubject();
        shapeFactory = new ShapeFactory();
        storageAdapter = new SqliteStorageAdapter("drawings.db");

        actionSubject.addObserver(Logger.getInstance());

        BorderPane root = new BorderPane();
        root.setTop(createToolbar());
        root.setLeft(createPalette());
        root.setCenter(createCanvasPane());
        root.setBottom(createLogArea());
        root.setStyle("-fx-background-color: #f5f5f5;");

        actionSubject.addObserver(action -> {
            String timestamp = LocalDateTime.now().format(TIME_FORMAT);
            logArea.appendText("[" + timestamp + "] " + action + "\n");
        });

        Scene scene = new Scene(root, 1100, 750);
        stage.setScene(scene);
        stage.setTitle("MASI - Application de Dessin");
        stage.show();

        actionSubject.notifyObservers("Application demarree");
    }

    private ToolBar createToolbar() {
        Button undoBtn = new Button("\u21A9 Annuler");
        undoBtn.setOnAction(e -> Optional.of(commandHistory).filter(CommandHistory::canUndo).ifPresent(ch -> {
            Command cmd = ch.undo();
            actionSubject.notifyObservers("Annulation: " + cmd.getDescription());
            redrawCanvas();
        }));

        Button clearBtn = new Button("\uD83D\uDDD1 Effacer tout");
        clearBtn.setOnAction(e -> {
            drawing.clear();
            commandHistory.clear();
            redrawCanvas();
            actionSubject.notifyObservers("Dessin efface");
        });

        Button saveBtn = new Button("\uD83D\uDCBE Enregistrer");
        saveBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("mon_dessin");
            dialog.setTitle("Enregistrer");
            dialog.setHeaderText("Nom du dessin:");
            dialog.showAndWait().ifPresent(name -> {
                DrawingMemento memento = drawing.createMemento();
                storageAdapter.saveDrawing(name, memento);
                actionSubject.notifyObservers("Dessin enregistre: " + name);
            });
        });

        Button openBtn = new Button("\uD83D\uDCC2 Ouvrir");
        openBtn.setOnAction(e -> {
            List<String> drawings = storageAdapter.listDrawings();
            ChoiceDialog<String> dialog = new ChoiceDialog<>();
            dialog.getItems().addAll(drawings);
            dialog.setTitle("Ouvrir un dessin");
            dialog.setHeaderText("Choisir un dessin:");
            dialog.showAndWait().ifPresent(name -> {
                DrawingMemento memento = storageAdapter.loadDrawing(name);
                drawing.restoreMemento(memento);
                commandHistory.clear();
                redrawCanvas();
                actionSubject.notifyObservers("Dessin ouvert: " + name);
            });
        });

        Label logLabel = new Label("Journal:");
        ComboBox<String> logStrategyBox = new ComboBox<>();
        logStrategyBox.getItems().addAll("Console", "Fichier", "Base de donnees");
        logStrategyBox.setValue("Console");

        Map<String, Supplier<LogStrategy>> strategies = Map.of(
                "Console", ConsoleLogStrategy::new,
                "Fichier", () -> new FileLogStrategy("log.txt"),
                "Base de donnees", () -> new DatabaseLogStrategy(storageAdapter.getConnection())
        );

        logStrategyBox.setOnAction(e -> {
            LogStrategy newStrategy = strategies.get(logStrategyBox.getValue()).get();
            Logger.getInstance().setStrategy(newStrategy);
            actionSubject.notifyObservers("Strategie de log changee: " + logStrategyBox.getValue());
        });

        ToolBar toolbar = new ToolBar(
                undoBtn, clearBtn, new Separator(),
                saveBtn, openBtn, new Separator(),
                logLabel, logStrategyBox);
        toolbar.setStyle("-fx-background-color: #e8e8e8; -fx-padding: 8;");
        return toolbar;
    }

    private VBox createPalette() {
        VBox palette = new VBox(10);
        palette.setPadding(new Insets(15));
        palette.setStyle("-fx-background-color: #f0f0f0; "
                + "-fx-border-color: #d0d0d0; "
                + "-fx-border-width: 0 1 0 0;");
        palette.setPrefWidth(170);

        Label shapesLabel = new Label("Formes");
        shapesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        ToggleGroup shapeGroup = new ToggleGroup();

        ToggleButton rectBtn = new ToggleButton("\u25AD Rectangle");
        rectBtn.setToggleGroup(shapeGroup);
        rectBtn.setSelected(true);
        rectBtn.setMaxWidth(Double.MAX_VALUE);
        rectBtn.setUserData("rectangle");

        ToggleButton circleBtn = new ToggleButton("\u25EF Cercle");
        circleBtn.setToggleGroup(shapeGroup);
        circleBtn.setMaxWidth(Double.MAX_VALUE);
        circleBtn.setUserData("circle");

        ToggleButton lineBtn = new ToggleButton("\u2571 Ligne");
        lineBtn.setToggleGroup(shapeGroup);
        lineBtn.setMaxWidth(Double.MAX_VALUE);
        lineBtn.setUserData("line");

        shapeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> Optional.ofNullable(newVal).ifPresent(toggle -> {
            selectedShapeType = (String) toggle.getUserData();
            actionSubject.notifyObservers("Forme selectionnee: " + selectedShapeType);
        }));

        Label colorsLabel = new Label("Couleur");
        colorsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Color[] colors = {
                Color.BLACK, Color.RED, Color.BLUE,
                Color.GREEN, Color.ORANGE, Color.PURPLE,
                Color.BROWN, Color.DEEPPINK, Color.TEAL
        };

        FlowPane colorPane = new FlowPane(5, 5);
        for (Color color : colors) {
            Button colorBtn = new Button();
            colorBtn.setPrefSize(30, 30);
            String hex = colorToHex(color);
            colorBtn.setStyle("-fx-background-color: " + hex + "; "
                    + "-fx-border-color: #999; -fx-border-radius: 3; "
                    + "-fx-cursor: hand;");
            colorBtn.setOnAction(e -> {
                selectedColor = color;
                actionSubject.notifyObservers("Couleur selectionnee: " + hex);
            });
            colorPane.getChildren().add(colorBtn);
        }

        Label styleLabel = new Label("Style");
        styleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Map<Boolean, String> stateLabels = Map.of(true, "active", false, "desactive");

        CheckBox fillCheckbox = new CheckBox("Remplir");
        fillCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            fillEnabled = newVal;
            actionSubject.notifyObservers("Remplissage: " + stateLabels.get(newVal));
        });

        CheckBox dashedCheckbox = new CheckBox("Bordure tiretee");
        dashedCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            dashedEnabled = newVal;
            actionSubject.notifyObservers("Bordure tiretee: " + stateLabels.get(newVal));
        });

        Label infoLabel = new Label("Clic droit pour\nsupprimer une forme");
        infoLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #888;");

        palette.getChildren().addAll(
                shapesLabel, rectBtn, circleBtn, lineBtn,
                new Separator(),
                colorsLabel, colorPane,
                new Separator(),
                styleLabel, fillCheckbox, dashedCheckbox,
                new Separator(),
                infoLabel
        );

        return palette;
    }

    private StackPane createCanvasPane() {
        canvas = new Canvas(800, 560);

        redrawCanvas();

        Map<Boolean, Function<Shape, Shape>> fillDecorators = Map.of(
                true, s -> new ColoredShapeDecorator(s, selectedColor),
                false, Function.identity()
        );
        Map<Boolean, Function<Shape, Shape>> dashDecorators = Map.of(
                true, DashedBorderDecorator::new,
                false, Function.identity()
        );

        canvas.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();

            Optional.of(e.getButton())
                    .filter(b -> b == MouseButton.SECONDARY)
                    .ifPresent(b -> deleteShapeAt(e.getX(), e.getY()));
        });

        canvas.setOnMouseDragged(e -> Optional.of(e.getButton()).filter(b -> b == MouseButton.PRIMARY).ifPresent(b -> {
            redrawCanvas();
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.save();
            gc.setLineDashes(5, 5);
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(1);
            Shape preview = shapeFactory.createShape(selectedShapeType,
                    startX, startY, e.getX(), e.getY(), Color.GRAY);
            preview.draw(gc);
            gc.restore();
        }));

        canvas.setOnMouseReleased(e -> Optional.of(e.getButton()).filter(b -> b == MouseButton.PRIMARY).ifPresent(b -> {
            Shape shape = shapeFactory.createShape(selectedShapeType,
                    startX, startY, e.getX(), e.getY(), selectedColor);

            shape = fillDecorators.get(fillEnabled).apply(shape);
            shape = dashDecorators.get(dashedEnabled).apply(shape);

            DrawCommand cmd = new DrawCommand(drawing, shape);
            commandHistory.executeCommand(cmd);

            actionSubject.notifyObservers(cmd.getDescription());
            redrawCanvas();
        }));

        StackPane canvasPane = new StackPane(canvas);
        canvasPane.setStyle("-fx-background-color: #e0e0e0;");
        canvasPane.setPadding(new Insets(10));
        return canvasPane;
    }

    private VBox createLogArea() {
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(120);
        logArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12;");

        Label logLabel = new Label("Journal des actions:");
        logLabel.setStyle("-fx-font-weight: bold;");

        VBox logBox = new VBox(5, logLabel, logArea);
        logBox.setPadding(new Insets(5, 10, 10, 10));
        logBox.setStyle("-fx-background-color: #f8f8f8; "
                + "-fx-border-color: #d0d0d0; "
                + "-fx-border-width: 1 0 0 0;");
        return logBox;
    }

    private void deleteShapeAt(double px, double py) {
        drawing.getShapes().stream()
                .filter(shape -> shape.contains(px, py))
                .reduce((a, b) -> b)
                .ifPresent(shape -> {
                    DeleteCommand cmd = new DeleteCommand(drawing, shape);
                    commandHistory.executeCommand(cmd);
                    actionSubject.notifyObservers(cmd.getDescription());
                    redrawCanvas();
                });
    }

    private void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.rgb(235, 235, 235));
        gc.setLineWidth(0.5);
        for (double x = 0; x < canvas.getWidth(); x += 20) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }
        for (double y = 0; y < canvas.getHeight(); y += 20) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }

        for (Shape shape : drawing.getShapes()) {
            shape.draw(gc);
        }
    }

    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch();
    }
}
