package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import logic.HTMLReader;

import java.io.File;

public class Controller {
    File navHTML, footerHTML, pathHTML;

    private Stage primaryStage;
    private @FXML TextArea outputLog;
    private @FXML Button navDirectoryButton, footerDirectoryButton, projectDirectoryButton, compileButton;
    private File defaultDirectory;


    public void initialize() {
        outputLog.setText("Choose a directory to start!");
        footerDirectoryButton.setDisable(true);
        projectDirectoryButton.setDisable(true);
        compileButton.setDisable(true);
    }


    public void getNavDirectory(){//TODO only allow advancement if chosen file
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Nav Directory");
        navHTML = chooser.showDialog(new Stage());
        defaultDirectory = navHTML;
        outputLog.setText(navHTML.getAbsolutePath());
        footerDirectoryButton.setDisable(false);
    }

    public void getFooterDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Footer Directory");
        chooser.setInitialDirectory(defaultDirectory);
        footerHTML = chooser.showDialog(new Stage());
        String temp = outputLog.getText() + "\n";
        outputLog.setText(temp + footerHTML.getAbsolutePath());
        projectDirectoryButton.setDisable(false);
    }

    public void getPathDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Path Directory");
        chooser.setInitialDirectory(defaultDirectory);
        pathHTML = chooser.showDialog(new Stage());
        String temp = outputLog.getText() + "\n";
        outputLog.setText(temp + pathHTML.getAbsolutePath());
        compileButton.setDisable(false);
    }

    public void compile(){
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML, outputLog);
        htmlReader.run();
    }
}
