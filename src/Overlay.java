import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import javafx.scene.paint.Color;
import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.opencv.video.Video;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class Overlay extends Application {

    public static boolean isSavingFiles = false;
    private static int frameRate = 15;
    private static int sampleRate = 44100;
    private static int videoDuration = 60;
    private static int audioDuration = 600;

    @Override
    public void start(Stage primaryStage) {
        // Créer le bouton
        Button btn = new Button();
        
        // Création du StackPane et ajout du bouton
        StackPane root = new StackPane();

        btn.setText("Report");

        // Action à effectuer quand le bouton est cliqué
        btn.setOnAction(event -> {
            if (DragUtil.wasDragged()) {
                // Si le bouton a été déplacé, on ne fait rien
                return;
            }
            // On arrête le buffering et enregistre le buffer dans un fichier (la vidéo sera enregistrée 2min après que le bouton ait été cliqué), et 5min pour l'audio
            new Thread(() -> {
                // Faire un timeout de 15 secondes
                try {
                    Thread.sleep(videoDuration * 1000 / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    VideoCaptureBuffer.saveBuffer("output.mp4", frameRate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                // Faire un timeout de 5min
                try {
                    Thread.sleep(audioDuration * 1000 / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    AudioCaptureBuffer.saveBuffer("output.wav", sampleRate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            Overlay.isSavingFiles = true;

            try {
                VideoCaptureBuffer.saveBuffer("output.mp4", frameRate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                AudioCaptureBuffer.saveBuffer("output.wav", sampleRate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (Overlay.isSavingFiles) {
                // On attend que les fichiers soient enregistrés
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

    public static String getMicrophoneName() {
        String command = "ffmpeg -list_devices true -f dshow -i dummy";
        String output = "";
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            output = new String(p.getInputStream().readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] lines = output.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Microphone")) {
                return lines[i].split("\"")[1];
            }
        }

        return "";
    }

    public static void main(String[] args) throws Exception {
        // On démarre le buffering
        VideoCaptureBuffer.startBuffering(videoDuration, frameRate);
        System.out.println(getMicrophoneName());
        AudioCaptureBuffer.startBuffering(audioDuration, sampleRate, getMicrophoneName());
        System.out.println("Buffering started.");

        launch(args);
    }
}
