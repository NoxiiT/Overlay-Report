import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import javafx.scene.paint.Color;

public class Overlay extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créer le bouton
        Button btn = new Button();
        
        // Création du StackPane et ajout du bouton
        StackPane root = new StackPane();

        btn.setText("Report");

        // Action à effectuer quand le bouton est cliqué
        btn.setOnAction(event -> {
            // On arrête le buffering et enregistre le buffer dans un fichier
            try {
                VideoCaptureBuffer.saveBuffer("output.mp4", 30);
                AudioCaptureBuffer.saveBuffer("output.wav", 44100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // On affiche une fenêtre permettant d'ajouter un rapport
            Report report = new Report();
            report.start(new Stage());
        });

        root.getChildren().add(btn);

        // Rendre le bouton déplaçable
        DragUtil.makeDraggable(btn);

        // Configuration de la scène avec un fond transparent
        Scene scene = new Scene(root, 300, 250);
        scene.setFill(Color.TRANSPARENT); // Important pour le fond transparent
        scene.getStylesheets().add("style.css"); // Ajout du style

        // Configuration du stage
        primaryStage.initStyle(StageStyle.TRANSPARENT); // Stage transparent
        primaryStage.setAlwaysOnTop(true); // Reste au-dessus des autres fenêtres
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        // On démarre le buffering
        VideoCaptureBuffer.startBuffering(10, 30);
        AudioCaptureBuffer.startBuffering(300, 44100, "Microphone (Logitech G430 Gaming Headset)");
        System.out.println("Buffering started.");

        launch(args);
    }
}
