import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public abstract class DragUtil {
    private static double xOffset = 0;
    private static double yOffset = 0;
    private static boolean dragged = false; // Ajout d'un flag pour indiquer le déplacement

    public static boolean wasDragged() {
        return dragged;
    }

    public static void makeDraggable(Node node) {
        node.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            dragged = false; // Réinitialiser l'indicateur de déplacement lorsqu'un nouveau clic est fait
        });

        node.setOnMouseDragged(event -> {
            double deltaX = event.getScreenX() - xOffset;
            double deltaY = event.getScreenY() - yOffset;
            if (Math.abs(deltaX) > 5 || Math.abs(deltaY) > 5) { // Seuil pour considérer le mouvement comme un glissement
                dragged = true; // Marquer comme déplacé
                node.getScene().getWindow().setX(event.getScreenX() - xOffset);
                node.getScene().getWindow().setY(event.getScreenY() - yOffset);
            }
        });
    }
}
