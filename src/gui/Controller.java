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
import java.util.Scanner;

/**
 * Controller class for JavaFX
 *
 * @author Brandon Duke
 * @version 5/20/17
 */
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

    /**
     * A method to initialize the GUI
     */
    public void initialize() {
        outputLog.setText("Choose a directory to start!");

        //disable certain buttons to force user to make choices in order
        footerDirectoryButton.setDisable(true);
        projectDirectoryButton.setDisable(true);
        compileButton.setDisable(true);
        loadIgnoreFileButton.setDisable(true);
        saveButton.setDisable(true);


        //event handler for checkboxes
        EventHandler eventHandler = (EventHandler<ActionEvent>) event -> {
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
        };
        ignoreFileCheckBox.setOnAction(eventHandler);
        insertClassCheckBox.setOnAction(eventHandler);
    }

    /**
     * A method to get the navigation directory when the user selects the button
     * The user can only advance to he next step when they choose a directory
     */
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

    /**
     * A method to get the footer directory when the user selects the button
     * The user can only advance to he next step when they choose a directory
     */
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

    /**
     * A method to get the project path directory when the user selects the button
     * The user can only advance to he next step when they choose a directory
     */
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

    /**
     * A method to get the ignore file when the user selects the button
     * The user can only advance to he next step when they choose a directory
     */
    public void getIgnoreFile(){
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

    /**
     * A method to write the current settings to a file
     * This is so they can load settings for a given project
     * and not have to do all of the selections again
     * The user can also append settings to a loaded file
     */
    public void saveSettings(){
        File saveLoc;
        if (loadFile != null){
            //if a load file is chosen, don't ask the user where to save
            //just save back to that file
            saveLoc = loadFile;
        }
        else {
            fileChooser.setTitle("Save File");
            saveLoc = fileChooser.showSaveDialog(new Stage());
            setOutput(saveLoc, "Save Location File");

            //either change the file extension the user typed to .dat
            //or just add it on to the file name
            String fileName, path;
            if (saveLoc.getName().contains(".")) {
                int index = saveLoc.getName().indexOf(".");
                fileName = saveLoc.getName().substring(0, index);
                path = saveLoc.getParentFile().getAbsolutePath();
                saveLoc = new File(path + "/" + fileName + ".dat");
            }
            else {
                fileName = saveLoc.getName();
                path = saveLoc.getParentFile().getAbsolutePath();
                saveLoc = new File(path + "/" + fileName + ".dat");
            }
        }

        //either turn the ignore ArrayList into a string or read it from the ignore file to a string
        StringBuilder ignoreString = new StringBuilder();
        if (useIgnoreFile && ignore.size() > 0) {
            for (String s : ignore) ignoreString.append(s).append(", ");
        }
        else if (useIgnoreFile && ignore.size() == 0){
            try {
                scanner = new Scanner(ignoreFile);
                while (scanner.hasNextLine()) {
                    ignoreString.append(scanner.nextLine());
                    if (scanner.hasNextLine()) ignoreString.append(",");
                }
            }
            catch (FileNotFoundException fnfe){fnfe.printStackTrace();}
        }

        //concatenate all of the settings to a multi-line string
        String data = navHTML.getAbsolutePath() + "\n" + footerHTML.getAbsolutePath()
                + "\n" + pathHTML.getAbsolutePath() + "\n" + ignoreFile.getAbsolutePath()
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

    /**
     * A method to load saved settings
     */
    public void loadSettings(){
        fileChooser.setTitle("Choose Settings File");
        loadFile = fileChooser.showOpenDialog(new Stage());
        defaultDirectory = loadFile;

        setOutput(loadFile, "Load File");

        try {
            //read in the settings and split into an array containing the settings
            scanner = new Scanner(loadFile);
            StringBuilder settings = new StringBuilder();
            while (scanner.hasNextLine()) settings.append(scanner.nextLine()).append("\n");
            String[] settingsSplit = settings.toString().split("\n");

            //index he array and give it to the corresponding setting
            navHTML = new File(settingsSplit[0]);
            footerHTML = new File(settingsSplit[1]);
            pathHTML = new File(settingsSplit[2]);
            ignoreFile = new File(settingsSplit[3]);
            useIgnoreFile = Boolean.parseBoolean(settingsSplit[4]);
            useInsertClass = Boolean.parseBoolean(settingsSplit[5]);

            //if there is an ignore file, put it in the array list
            try {
                String[] s = settingsSplit[6].split(",");
                if (useIgnoreFile && s.length > 0) {
                    ignore.clear();
                    for (String s1 : s) ignore.add(s1.trim());
                }
            }
            catch (ArrayIndexOutOfBoundsException aiobe){aiobe.printStackTrace();}

            //set output and allow use of buttons so user can change settings
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

    /**
     * A method to start compilation process
     */
    public void compile(){
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML, outputLog, ignore, useInsertClass);
        htmlReader.run();
    }

    /**
     * A method to easily put output to the console in the application
     * @param f file that was chosen
     * @param s Message that goes with the selection
     */
    private void setOutput(File f, String s){
        String temp = outputLog.getText() + "\n" + s + " Path:\n";
        outputLog.setText(temp + f.getAbsolutePath());
    }
}
