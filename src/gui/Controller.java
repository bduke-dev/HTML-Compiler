package gui;

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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Controller {
    private File navHTML, footerHTML, pathHTML, ignoreFile, loadFile;
    private boolean useIgnoreFile = false, useInsertClass = false;

    private @FXML TextArea outputLog;
    private @FXML Button navDirectoryButton, footerDirectoryButton, projectDirectoryButton,
            compileButton, loadIgnoreFileButton, saveButton;
    private @FXML CheckBox ignoreFileCheckBox, insertClassCheckBox;
    private File defaultDirectory;

//TODO only allow advacement on no erro when selecting a file
    public void initialize() {
        outputLog.setText("Choose a directory to start!");
        footerDirectoryButton.setDisable(true);
        projectDirectoryButton.setDisable(true);
        compileButton.setDisable(true);
        loadIgnoreFileButton.setDisable(true);
        //saveButton.setDisable(true);


        //event handler for checkboxes
        EventHandler eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    System.out.println("Action performed on checkbox " + chk.getText());
                    if (chk.getText().equals("Use Ignore File")){ //TODO attach chotce to code
                        useIgnoreFile = ignoreFileCheckBox.isSelected();
                        loadIgnoreFileButton.setDisable(!useIgnoreFile);
                    }
                    else if (chk.getText().equals("Insert class=\"currentPage\"")) {
                        useInsertClass = insertClassCheckBox.isSelected();
                        System.out.println("val change: " + useInsertClass);
                    }
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
        setOutput(navHTML);
        footerDirectoryButton.setDisable(false);
    }

    public void getFooterDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Footer Directory");
        chooser.setInitialDirectory(defaultDirectory);
        footerHTML = chooser.showDialog(new Stage());
        setOutput(footerHTML);
        projectDirectoryButton.setDisable(false);
    }

    public void getPathDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Path Directory");
        chooser.setInitialDirectory(defaultDirectory);
        pathHTML = chooser.showDialog(new Stage());
        setOutput(pathHTML);
        compileButton.setDisable(false);
        saveButton.setDisable(false);
    }

    public void getIgnoreFile(){ //TODO attach
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Ignore File");
        chooser.setInitialDirectory(defaultDirectory);
        ignoreFile = chooser.showOpenDialog(new Stage());
        defaultDirectory = ignoreFile;
        setOutput(ignoreFile);
    }

    private void setOutput(File f){
        String temp = outputLog.getText() + "\n";
        outputLog.setText(temp + f.getAbsolutePath());
    }

    public void saveSettings(){
        File saveLoc;
        if (loadFile.exists()){
            saveLoc = loadFile;
        }
        else {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save File");
            //chooser.setInitialDirectory(defaultDirectory); //TODO seems to be an error when all things are chones then going to save
            saveLoc = chooser.showSaveDialog(new Stage());
            String fileName, path;
            if (saveLoc.getName().contains(".")) {
                int index = saveLoc.getName().indexOf(".");
                fileName = saveLoc.getName().substring(0, index);
                path = saveLoc.getParentFile().getAbsolutePath();
                saveLoc = new File(path + "/" + fileName + ".dat");
            } else {
                fileName = saveLoc.getName();
                path = saveLoc.getParentFile().getAbsolutePath();
                saveLoc = new File(path + "/" + fileName + ".dat");
            }
        }

        String data = navHTML.getAbsolutePath() + "\n" + footerHTML.getAbsolutePath()
                + "\n" + pathHTML.getAbsolutePath() + "\n" + ignoreFile.getAbsolutePath()
                + "\n" + useIgnoreFile + "\n" + useInsertClass;

        try {
            FileWriter fw = new FileWriter(saveLoc);
            fw.write(data);
            fw.close();
            System.out.println(data);
        }
        catch (IOException ioe){ioe.printStackTrace();}
    }

    public void loadSettings(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Settings File");
        chooser.setInitialDirectory(defaultDirectory);
        loadFile = chooser.showOpenDialog(new Stage());
        defaultDirectory = loadFile;
        setOutput(loadFile);
        try {
            Scanner scanner = new Scanner(loadFile);
            String settings = "";
            while (scanner.hasNextLine()) settings += scanner.nextLine() + "\n";
            String[] settingsSplit = settings.split("\n");
            navHTML = new File(settingsSplit[0]);
            footerHTML = new File(settingsSplit[1]);
            pathHTML = new File(settingsSplit[2]);
            ignoreFile = new File(settingsSplit[3]);
            useIgnoreFile = Boolean.parseBoolean(settingsSplit[4]);
            useInsertClass = Boolean.parseBoolean(settingsSplit[5]);

            setOutput(navHTML);
            setOutput(footerHTML);
            setOutput(pathHTML);
            setOutput(ignoreFile);
            System.out.println("LOAD BOOLEAN STATUS: IGNORE: " + useIgnoreFile + " INSERT CLASS: " + useInsertClass);

            footerDirectoryButton.setDisable(false);
            projectDirectoryButton.setDisable(false);
            compileButton.setDisable(false);
            loadIgnoreFileButton.setDisable(false);
            saveButton.setDisable(false);
            ignoreFileCheckBox.setSelected(useIgnoreFile);
            insertClassCheckBox.setSelected(useInsertClass);
        }
        catch (FileNotFoundException fnfe){fnfe.printStackTrace();}
    }

    public void compile(){
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML, outputLog);
        htmlReader.run();
    }

}
