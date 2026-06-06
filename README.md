# Projet MASI - JavaFX Drawing App

A JavaFX vector drawing application implementing various design patterns (Factory, Command, Memento, Observer, Strategy, Singleton, Decorator). 

## 🚀 How to Run (Easy Steps)

This project is pre-configured with everything you need. The JavaFX SDK and SQLite database drivers are already bundled inside the project folder so you don't need to configure any complex environment variables!

### Method 1: Using the provided Script (Windows)
The easiest way to run the app is using the provided batch file, which will automatically compile and launch the application:
1. Double-click on `run.bat` (or run `.\run.bat` from your terminal).
2. That's it! 

### Method 2: Using VS Code (Recommended for editing)
The repository includes all the necessary VS Code configurations (`.vscode` folder):
1. Open this project folder in VS Code.
2. Ensure you have the **Extension Pack for Java** installed.
3. Open `src/App.java`.
4. Click the **"Run"** button that appears right above the `public static void main` method, or press `F5` to start debugging.

## 🛠️ Features
- **Drawing Tools:** Line, Rectangle, Circle.
- **Styling:** Colors, Dashed borders, and Filled shapes.
- **History:** Undo (Command Pattern).
- **Save/Load:** Save and restore your drawing state (Memento Pattern).
- **Logging:** Switch between Console, File, and Database logging strategies on the fly (Strategy Pattern).

## 📁 Architecture 
The source code (`src/`) is organized by design patterns rather than standard functional packages to easily demonstrate the implementation of each pattern.
