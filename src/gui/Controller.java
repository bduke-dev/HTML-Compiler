package gui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.File;

public class Controller {
    File navHTML, footerHTML, pathHTML;

    private @FXML GridPane gp;
    //private Stage primaryStage = (Stage) gp.getScene().getWindow();
    private @FXML Label outputLogLabel;

    public void initialize(){
        outputLogLabel.setText("WORK");
    }


    public void getNavDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Nav Directory");
        //navHTML = chooser.showDialog(primaryStage);
    }
}
