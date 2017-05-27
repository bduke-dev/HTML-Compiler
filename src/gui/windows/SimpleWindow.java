package gui.windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author brandon
 * @version 3/28/17
 */
public class SimpleWindow {
    private String message, windowName;
    private int fontSize, width, height;

    public SimpleWindow(String message, String windowName, int fontSize){
        this.message = message;
        this.windowName = windowName;
        this.fontSize = fontSize;
        this.width = 550;
        this.height = 400;
    }

    public SimpleWindow(String message, String windowName, int fontSize, int width, int height){
        this.message = message;
        this.windowName = windowName;
        this.fontSize = fontSize;
        this.width = width;
        this.height = height;
    }

    public void display(){
        // Stage setup
        Stage window = new Stage();
        window.setTitle(windowName);
        window.initModality(Modality.APPLICATION_MODAL); // means that while this window is open, you can't interact with the main program.

        // buttons
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> window.close());


        // Labels
        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font(fontSize));

        // Images
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/graphics/icon.png")));

        // Layout type
        VBox layout = new VBox(10);
        HBox closeBox = new HBox();
        closeBox.getChildren().addAll(closeBtn);
        closeBox.setAlignment(Pos.CENTER_RIGHT);
        closeBox.setPadding(new Insets(5,5,5,5));
        layout.getChildren().addAll(imageView, messageLabel, closeBox);
        layout.setAlignment(Pos.CENTER);

        // Building scene and displaying.
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setHeight(height);
        window.setWidth(width);
        window.setResizable(false);
        window.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        window.show();
    }
}
