package adapter;

import memento.DrawingMemento;
import java.util.List;

/**
 * Interface Adapter (Design Pattern Adaptateur).
 * Adapte le stockage des dessins a une interface simple.
 */
public interface StorageAdapter {

    /** Sauvegarder un dessin avec un nom donne */
    void saveDrawing(String name, DrawingMemento memento);

    /** Charger un dessin par son nom */
    DrawingMemento loadDrawing(String name);

    /** Lister tous les dessins sauvegardes */
    List<String> listDrawings();
}
