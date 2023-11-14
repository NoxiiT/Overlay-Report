import javafx.scene.Node;

public abstract class DragUtil {
    private static double xOffset = 0;
    private static double yOffset = 0;
    
    public static void makeDraggable(Node node) {
        node.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        node.setOnMouseDragged(event -> {
            node.getScene().getWindow().setX(event.getScreenX() - xOffset);
            node.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }
}