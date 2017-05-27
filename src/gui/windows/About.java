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
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * @author Brandon Duke
 * @version 3/24/17
 */

public class About {
    private String windowName ,
            developer,
            version,
            appName,
            website,
            aboutApp;
    public About(String windowName, String appName,
                 String version, String aboutApp,
                 String developer, String website){
        this.windowName = windowName;
        this.appName = appName;
        this.version = version;
        this.aboutApp = aboutApp;
        this.developer = developer;
        this.website = website;
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
        Label appNameLabel = new Label(appName);
        Label websiteLabel = new Label("Website: " + website);
        Label aboutAppLabel = new Label(aboutApp);
        Label developerLabel = new Label("Developer: " + developer);
        Label versionLabel = new Label("Version: " + version);

        // Images
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/graphics/icon.png")));

        // Layout type
        VBox layout = new VBox(10);
        HBox closeBox = new HBox();
        closeBox.getChildren().addAll(closeBtn);
        closeBox.setAlignment(Pos.CENTER_RIGHT);
        closeBox.setPadding(new Insets(5,5,5,5));
        layout.getChildren().addAll(imageView, appNameLabel, developerLabel, versionLabel, aboutAppLabel, websiteLabel, closeBox);
        layout.setAlignment(Pos.CENTER);

        // Building scene and displaying.
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setHeight(400);
        window.setWidth(550);
        window.setResizable(false);
        window.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        window.show();
    }
}
