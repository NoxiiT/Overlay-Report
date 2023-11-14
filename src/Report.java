import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.scene.paint.Color;
import javafx.scene.media.*;

import java.io.File;

public class Report extends Application {

    private void sendReport(String title, String description) {
        // TODO
        System.out.println("Report envoyé : Titre - " + title + ", Description - " + description);
    }

    private void sendAndClose(String title, String description, Stage reportStage) {
        sendReport(title, description);
        reportStage.close();
    }

    @Override
    public void start(Stage reportStage) {
        // Création des composants de l'interface utilisateur
        TextField titleField = new TextField();
        titleField.setPromptText("Titre du report");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description du report");

        // Chemin du fichier vidéo
        String videoPath = "output.mp4";
        Media media = new Media(new File(videoPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true); // Lecture automatique
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lecture en boucle

        // Création du MediaView
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(600); // Largeur
        mediaView.setFitHeight(450); // Hauteur
        mediaView.setPreserveRatio(true);

        Button playButton = new Button(">");
        playButton.setPrefSize(30, 30);
        playButton.setOnAction(e -> mediaPlayer.play());

        Button pauseButton = new Button("||");
        pauseButton.setPrefSize(30, 30);
        pauseButton.setOnAction(e -> mediaPlayer.pause());

        HBox controlBox = new HBox(5, playButton, pauseButton); // Espacement de 5

        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(event -> sendAndClose(titleField.getText(), descriptionArea.getText(), reportStage));

        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(event -> reportStage.close());
        cancelButton.getStyleClass().add("cancel-button"); // Ajout d'une classe CSS

        VBox mediaBox = new VBox(10, mediaView, controlBox);

        // Création du HBox pour les boutons
        HBox buttonBox = new HBox(10); // 10 est l'espacement entre les boutons
        buttonBox.getChildren().addAll(sendButton, cancelButton);

        // Ajout des composants à un VBox
        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleField, descriptionArea, buttonBox);

        HBox mainBox = new HBox(10, layout, mediaBox);

        // Configuration de la scène et du stage
        Scene scene = new Scene(mainBox, 950, 380);
        scene.setFill(Color.TRANSPARENT); // Fond transparent
        scene.getStylesheets().add("style.css"); // Ajout du style

        reportStage.initStyle(StageStyle.TRANSPARENT); // Stage transparent
        reportStage.centerOnScreen();
        reportStage.setAlwaysOnTop(true);
        reportStage.setScene(scene);
        reportStage.show();
    }
    
}
