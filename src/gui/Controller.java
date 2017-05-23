package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.HTMLReader;

import java.io.File;

public class Controller {
    File navHTML, footerHTML, pathHTML, ignoreFile;
    boolean useIgnoreFile = false, useInsertClass = false;

    private Stage primaryStage;
    private @FXML TextArea outputLog;
    private @FXML Button navDirectoryButton, footerDirectoryButton, projectDirectoryButton,
            compileButton, loadIgnoreFileButton;
    private @FXML CheckBox ignoreFileCheckBox, insertClassCheckBox;
    private File defaultDirectory;


    public void initialize() {
        outputLog.setText("Choose a directory to start!");
        footerDirectoryButton.setDisable(true);
        projectDirectoryButton.setDisable(true);
        compileButton.setDisable(true);
        loadIgnoreFileButton.setDisable(true);


        //event handler for checkboxes
        EventHandler eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    System.out.println("Action performed on checkbox " + chk.getText());
                    if (ignoreFileCheckBox.isSelected() || !ignoreFileCheckBox.isSelected()){ //TODO may not actually need this
                        useIgnoreFile = ignoreFileCheckBox.isSelected();
                        loadIgnoreFileButton.setDisable(!useIgnoreFile);
                    }
                    else if (insertClassCheckBox.isSelected() || !insertClassCheckBox.isSelected()) useInsertClass = insertClassCheckBox.isSelected();
                }
            }
        };
        ignoreFileCheckBox.setOnAction(eventHandler);
        insertClassCheckBox.setOnAction(eventHandler);
    }


    public void getNavDirectory(){//TODO only allow advancement if chosen file
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Nav Directory");
        navHTML = chooser.showDialog(new Stage());
        defaultDirectory = navHTML;
        String temp = outputLog.getText() + "\n";
        outputLog.setText(temp + navHTML.getAbsolutePath());
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

    public void getIgnoreDirectory(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Ignore File");
        chooser.setInitialDirectory(defaultDirectory);
        ignoreFile = chooser.showOpenDialog(new Stage());
        String temp = outputLog.getText() + "\n";
        outputLog.setText(temp + ignoreFile.getAbsolutePath());
    }

    public void compile(){
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML, outputLog);
        htmlReader.run();
    }

}
