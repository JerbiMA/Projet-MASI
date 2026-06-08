package adapter;

import memento.DrawingMemento;
import java.util.List;

public interface StorageAdapter {

    void saveDrawing(String name, DrawingMemento memento);

    DrawingMemento loadDrawing(String name);

    List<String> listDrawings();
}
