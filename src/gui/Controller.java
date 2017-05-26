package gui;

import gui.windows.SimpleWindow;
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
import logic.SimpleException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Controller {
    private File navHTML, footerHTML, pathHTML, ignoreFile, loadFile, defaultDirectory;
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private FileChooser fileChooser = new FileChooser();

    private boolean useIgnoreFile = false, useInsertClass = false;
    private SimpleWindow simpleWindow;
    private ArrayList<String> ignore = new ArrayList<>();
    private Scanner scanner;

    private @FXML TextArea outputLog;
    private @FXML Button navDirectoryButton, footerDirectoryButton, projectDirectoryButton,
            compileButton, loadIgnoreFileButton, saveButton;
    private @FXML CheckBox ignoreFileCheckBox, insertClassCheckBox;

    public void initialize() {
        outputLog.setText("Choose a directory to start!");
        footerDirectoryButton.setDisable(true);
        projectDirectoryButton.setDisable(true);
        compileButton.setDisable(true);
        loadIgnoreFileButton.setDisable(true);
        saveButton.setDisable(true);


        //event handler for checkboxes
        EventHandler eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    System.out.println("Action performed on checkbox " + chk.getText());
                    if (chk.getText().equals("Use Ignore File")){
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

    public void getNavDirectory(){
        try {
            directoryChooser.setTitle("Choose Nav Directory");
            navHTML = directoryChooser.showDialog(new Stage());
            defaultDirectory = navHTML;
            setOutput(navHTML, "Nav Directory");
            footerDirectoryButton.setDisable(false);
        }
        catch (Exception e){
            if (e.getMessage() != null) {
                simpleWindow = new SimpleWindow("File Error\n" + e.getMessage(), "File Error", 16);
                simpleWindow.display();
            }
        }
    }

    public void getFooterDirectory(){
        try {
            directoryChooser.setTitle("Choose Footer Directory");
            directoryChooser.setInitialDirectory(defaultDirectory);
            footerHTML = directoryChooser.showDialog(new Stage());
            setOutput(footerHTML, "Footer Directory");
            projectDirectoryButton.setDisable(false);
        }
        catch (Exception e){
            if (e.getMessage() != null) {
                simpleWindow = new SimpleWindow("File Error\n" + e.getMessage(), "File Error", 16);
                simpleWindow.display();
            }
        }
    }

    public void getPathDirectory(){
        try {
            directoryChooser.setTitle("Choose Path Directory");
            directoryChooser.setInitialDirectory(defaultDirectory);
            pathHTML = directoryChooser.showDialog(new Stage());
            setOutput(pathHTML, "Project Directory");
            compileButton.setDisable(false);
            saveButton.setDisable(false);
        }
        catch (Exception e){
            if (e.getMessage() != null) {
                simpleWindow = new SimpleWindow("File Error\n" + e.getMessage(), "File Error", 16);
                simpleWindow.display();
            }
        }
    }

    public void getIgnoreFile(){ //TODO attach to cdoe
        try {
            fileChooser.setTitle("Choose Ignore File");
            fileChooser.setInitialDirectory(defaultDirectory);
            ignoreFile = fileChooser.showOpenDialog(new Stage());
            defaultDirectory = ignoreFile;
            setOutput(ignoreFile, "Ignore File");

            scanner = new Scanner(ignoreFile);
            while (scanner.hasNextLine()) {
                String[] items = scanner.nextLine().split(",");
                for (String s : items) {
                    System.out.println(s);
                    s = s.trim();
                    ignore.add(s);
                }
            }

            if (ignore.isEmpty()) throw new SimpleException("EMPTY IGNORE FILE");
        }
        catch (Exception e){
            if (e.getMessage() != null) {
                simpleWindow = new SimpleWindow("File Error\n" + e.getMessage(), "File Error", 16);
                simpleWindow.display();
            }
        }
    }

    public void saveSettings(){
        File saveLoc;
        if (loadFile.exists()){
            saveLoc = loadFile;
        }
        else {
            fileChooser.setTitle("Save File");
            //directoryChooser.setInitialDirectory(defaultDirectory); //TODO seems to be an error when all things are chones then going to save
            saveLoc = fileChooser.showSaveDialog(new Stage());

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

        String ignoreString = "";
        if (useIgnoreFile && ignore.size() > 0) {
            System.out.println("RUN 1");
            for (String s : ignore) ignoreString += s + ", ";
        }
        else if (useIgnoreFile && ignore.size() == 0){
            try {
                System.out.println("RUN");
                scanner = new Scanner(ignoreFile);
                while (scanner.hasNextLine()) {
                    ignoreString += scanner.nextLine();
                    if (scanner.hasNextLine()) ignoreString += ",";
                }
            }
            catch (FileNotFoundException fnfe){fnfe.printStackTrace();}
        }

        String data = navHTML.getAbsolutePath() + "\n" + footerHTML.getAbsolutePath()
                + "\n" + pathHTML.getAbsolutePath() + "\n" + ignoreFile.getAbsolutePath() //TODO change to ignore file arraylist
                + "\n" + useIgnoreFile + "\n" + useInsertClass;

        if (useIgnoreFile) data += "\n" + ignoreString;
        else data += "\n";

        try {
            FileWriter fw = new FileWriter(saveLoc);
            fw.write(data);
            fw.close();
            System.out.println(data);
        }
        catch (IOException ioe){ioe.printStackTrace();}
    }

    public void loadSettings(){
        fileChooser.setTitle("Choose Settings File");
        //directoryChooser.setInitialDirectory(defaultDirectory); //TODO gave an error
        loadFile = fileChooser.showOpenDialog(new Stage());
        defaultDirectory = loadFile;

        setOutput(loadFile, "Load File");

        try {//TODO get ignore string from file and put it in the array list
            scanner = new Scanner(loadFile);
            String settings = "";
            while (scanner.hasNextLine()) settings += scanner.nextLine() + "\n";
            String[] settingsSplit = settings.split("\n");
            navHTML = new File(settingsSplit[0]);
            footerHTML = new File(settingsSplit[1]);
            pathHTML = new File(settingsSplit[2]);
            ignoreFile = new File(settingsSplit[3]);
            useIgnoreFile = Boolean.parseBoolean(settingsSplit[4]);
            useInsertClass = Boolean.parseBoolean(settingsSplit[5]);

            try {
                String[] s = settingsSplit[6].split(",");
                if (useIgnoreFile && s.length > 0) {
                    System.out.println("READ IN IGNORE");
                    ignore.clear();
                    ignore.addAll(Arrays.asList(s));
                }
            }
            catch (ArrayIndexOutOfBoundsException aiobe){}

            setOutput(navHTML, "Nav Directory");
            setOutput(footerHTML, "Footer Directory");
            setOutput(pathHTML, "Project Directory");
            setOutput(ignoreFile, "Ignore File");

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
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML, outputLog, ignore);
        htmlReader.run();
    }

    private void setOutput(File f, String s){
        String temp = outputLog.getText() + "\n" + s + " Path:\n";
        outputLog.setText(temp + f.getAbsolutePath());
    }
}
