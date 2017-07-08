package logic;

import javafx.application.Platform;

import javafx.scene.control.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
//TODO required comments in documentation, to know where to place html
//TODO add error handling to help user diagnose issues, like check tags. also make them aware they can't be on the same line
/**
 * @author Brandon Duke
 * @version 5/18/17
 */
public class HTMLReader implements Runnable{
    private File navHTML, footerHTML, pathHTML;
    private ArrayList<String> ignore;
    private TextArea console;
    private boolean insertCurrentPage;
    private Scanner scanner;

    /**
     * Default constructor for HTMLReader class
     *
     * @param navHTML Directory where all of the nav files are stored
     * @param footerHTML Directory where all of the footer files are stored
     * @param pathHTML Directory of the html files to compile
     * @param console JavaFX TextArea where information to be printed
     * @param ignore ArrayList of directories and files to ignore at the root of the project
     * @param insertCurrentPage Boolean to determine insertion of currentPage in the nav of that page
     */
    public HTMLReader(File navHTML, File footerHTML, File pathHTML, TextArea console, ArrayList<String> ignore, boolean insertCurrentPage){
        this.navHTML = navHTML;
        this.footerHTML = footerHTML;
        this.pathHTML = pathHTML;
        this.console = console;
        this.ignore = ignore;
        this.insertCurrentPage = insertCurrentPage;
    }

    //TODO for dev only
    public HTMLReader(File navHTML, File footerHTML, File pathHTML){
        this.navHTML = navHTML;
        this.footerHTML = footerHTML;
        this.pathHTML = pathHTML;
        this.console = null;
        this.ignore = new ArrayList<>(Arrays.asList("partials", ".git", ".sass-cache", "css", "scss", "sass", "DEV FILES", "google071d8247f50df527.html"));

    }

    /**
     * A method to read a html file
     *
     * @param file File of the html file to read
     * @return String of the html file
     */
    private String readHTML(File file){
        StringBuilder returnString = new StringBuilder();

        try{
            //read in the html
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) returnString.append(scanner.nextLine()).append("\n");
        }
        catch (FileNotFoundException fnfe){fnfe.printStackTrace();}

        return returnString.toString();
    }

    /**
     * A method to retrieves the html project to compile
     *
     * @param listOfFiles List of files and directories in pathHTML directory
     * @return Array of HTMLFiles that need compiled
     */
    private HTMLFile[] getProject(File[] listOfFiles){
        int counter = 0, index;
        File[] temp;

        //looks through the list of files and directories at the
        //root of the project and nulls those in the ignore list
        for (int i = 0; i < listOfFiles.length; i++){
            if (listOfFiles[i] != null) {
                String path = listOfFiles[i].getName();
                if (ignore.contains(path)) listOfFiles[i] = null;
            }
        }

        //takes the project and looks through all directories until it is just files in the list
        while (counter < listOfFiles.length){
            //if the file is a directory, we will null that index
            //and expand and add everything in it to the list
            //we will continue until the counter reaches the end of the lit as we add more files
            //at that point we assume we have no directories left and just files
            if (listOfFiles[counter] != null && listOfFiles[counter].isDirectory()){
                temp = listOfFiles[counter].listFiles();

                //null this index
                listOfFiles[counter] = null;

                //expand temp and put on the end of the array
                index = listOfFiles.length;
                assert temp != null; //TODO learn more
                for (File aTemp : temp) {
                    listOfFiles = Arrays.copyOf(listOfFiles, listOfFiles.length + 1);
                    listOfFiles[index++] = aTemp;
                }
            }
            counter++;
        }

        //null not html files, so that we only try to compile html files
        for (int i = 0; i < listOfFiles.length; i++){
            if (listOfFiles[i] != null) {// only check not currently null spaces
                String path = listOfFiles[i].getAbsolutePath();
                //substring of html file will have last 5 characters as .html
                path = path.substring(path.length() - 5, path.length());
                if (!path.equals(".html")) listOfFiles[i] = null;
            }
        }

        //remove null spaces
        //so that we have an array of just files to compile
        temp = listOfFiles;
        listOfFiles = new File[0];
        index = 0;
        for (File aTemp : temp) {
            if (aTemp != null) {
                listOfFiles = Arrays.copyOf(listOfFiles, listOfFiles.length + 1);
                listOfFiles[index++] = aTemp;
            }
        }

        //get the file and pat
        HTMLFile[] htmlFiles = new HTMLFile[listOfFiles.length];

        //depth of each file so certain nav and footer can be
        // This is used to preserve file paths to resources within project
        int[] depth = new int[htmlFiles.length];
        for (int i = 0; i < listOfFiles.length; i++){
            String path = listOfFiles[i].getAbsolutePath().replace(pathHTML.getAbsolutePath(), "");
            int count = path.length() - path.replace("/", "").length();
            depth[i] = count;
        }

        boolean homePage;
        //take the array of files and put them in an array of HTMLFiles
        for (int i = 0; i < htmlFiles.length; i++) {
            homePage = listOfFiles[i].getAbsolutePath().equals(pathHTML.getAbsolutePath() + "/index.html");
            String fileName = listOfFiles[i].getAbsolutePath().replace(pathHTML.getAbsolutePath(), "").replace("index.html", "");


            //if (fileName.equals("/index.html")) fileName = listOfFiles[i].getParentFile().getAbsolutePath().replace(pathHTML.getAbsolutePath(), "").replace("/", "");
            //else fileName = listOfFiles[i].getAbsolutePath().replace(listOfFiles[i].getParentFile().getAbsolutePath(), "").replace("/", "");

            htmlFiles[i] = new HTMLFile(readHTML(listOfFiles[i]), listOfFiles[i], depth[i], fileName, homePage);
        }
        return htmlFiles;
    }

    /**
     * A method to get a directory full of navs or footers
     *
     * @param navPath Directory where the files are stored
     * @return Array of HTMLFiles of the navs or footers
     */
    private HTMLFile[] getPartialHTML(File navPath){
        File[] listOfFiles = navPath.listFiles();
        assert listOfFiles != null;//TODO learn more
        HTMLFile[] html = new HTMLFile[listOfFiles.length];

        //get the depth from the file name so that we know which html file to pair it up with
        //then put it with the string of the contents of the file into an array of HTMLFiles
        for (int i = 0; i < listOfFiles.length; i++){
            int depth = Integer.parseInt(listOfFiles[i].getName().replace(".html", ""));
            html[i] = new HTMLFile(readHTML(listOfFiles[i]), listOfFiles[i], depth, "", false);
        }
        return html;
    }

    /**
     * A method to take the html fiels form a project and compile them,
     * it does to by adding the proper nav and footer to each file in between the comment blocks
     *
     * @return Updated array of HMTLFiles with the nav and footer in each one
     */
    private HTMLFile[] compileHTML(){
        HTMLFile[] htmlFiles = getProject(pathHTML.listFiles()); //all chosen html files
        HTMLFile[] nav = getPartialHTML(navHTML), footer = getPartialHTML(footerHTML); //nav and footer html file

        for (HTMLFile htmlFile : htmlFiles) {
            //for each html file, take its html, turn it into an array of lines and check to see if we need to remove
            //the nav or footer if it is already in there
            String[] lines = htmlFile.getHtml().split("\n");

            int j = 0;
            //removes nav if it exists
            for (; j < lines.length; j++) {
                if (lines[j].contains("<!--nav-->")) {
                    j++;
                    while (!lines[j].contains("<!--/nav-->")) lines[j++] = null;
                    break;
                }
                else if (lines[j].contains("<!--footer-->")) { //in case the nav doesn't exist, but the footer does
                    j--;
                    break;
                }
            }

            //removes footer if it exists
            for (; j < lines.length; j++) {
                if (lines[j].contains("<!--footer-->")) {
                    j++;
                    while (!lines[j].contains("<!--/footer-->")) lines[j++] = null;
                    break;
                }
            }

            //remove null spaces
            String[] temp = lines;
            lines = new String[0];
            int index = 0;
            for (String aTemp : temp) {
                if (aTemp != null) {
                    lines = Arrays.copyOf(lines, lines.length + 1);
                    lines[index++] = aTemp;
                }
            }


            //add the nav and footer of the corresponding depth to each html file
            StringBuilder compiled = new StringBuilder();StringBuilder navString = new StringBuilder();
            String footerString = "";
            int depth = htmlFile.getDepth();
            //make sure there are an equal number of nav and footer files
            //because if there aren't that means the depth is off somewhere or we are missing a file
            //this was we don't try to compile a file without one of them
            for (int k = 0; k < nav.length && k < footer.length; k++) {
                if (nav[k].getDepth() == depth) navString = new StringBuilder(nav[k].getHtml());
                if (footer[k].getDepth() == depth) footerString = footer[k].getHtml();
            }

            if (insertCurrentPage) {
                //optional .currentPage in nav
                //this gets the name of the current page, including the depth
                StringBuilder currentPage = new StringBuilder();
                if (htmlFile.getDepth() == 1) currentPage.append(htmlFile.getCurrentPage());
                else {
                    for (int k = 1; k < htmlFile.getDepth(); k++) {
                        if (k == 1) currentPage.append("..");
                        else currentPage.append("../");
                    }
                    currentPage.append(htmlFile.getCurrentPage());
                }

                scanner = new Scanner(navString.toString());
                navString = new StringBuilder();
                //look through each line of nav to see where we need to put currentPage html class
                while (scanner.hasNextLine()) {
                    String currentLine = scanner.nextLine(), page = "";
                    String[] currentLineArr = currentLine.split(" ");

                    for (String s : currentLineArr){
                        if (s.contains("href=")){
                            StringBuilder sb = new StringBuilder(s.replace("href=\"", ""));
                            int position = sb.indexOf("\"");
                            sb.replace(position, sb.length(), "");
                            page = sb.toString();

                        }
                    }
                    if (currentPage.toString().equals("/")) { //catches all files in the root directory, like index, 404, etc.
                        //hopefully a condition that can only be met on the home page
                        if (currentLine.contains("<a href=\"/\">") && currentLine.toLowerCase().contains("home") && htmlFile.isHomePage()) {
                            StringBuilder sb = new StringBuilder(currentLine);
                            int position = sb.indexOf("<a");
                            sb.insert(position + 2, " class=\"currentPage\"");
                            currentLine = sb.toString();
                        }
                    }
                    else if (page.equals(currentPage.toString())) {
                        StringBuilder sb = new StringBuilder(currentLine);
                        int position = sb.indexOf("<a");
                        sb.insert(position + 2, " class=\"currentPage\"");
                        currentLine = sb.toString();
                    }
                    navString.append(currentLine).append("\n");
                }
            }

            //this finally puts the nav and footer in the html file
            for (String line : lines) {
                compiled.append(line).append("\n");
                if (line.contains("<!--nav-->")) compiled.append(navString);
                if (line.contains("<!--footer-->")) compiled.append(footerString);
            }
            htmlFile.appendHtml(compiled.toString());
        }

        return htmlFiles;
    }

    /**
     * A method to write compiled html files back to their files
     */
    public void writeHTML(){
        HTMLFile[] htmlFiles = compileHTML();
        FileWriter fileWriter;
        for (HTMLFile h : htmlFiles) {
            try {
                fileWriter = new FileWriter(h.getFile().getAbsoluteFile());
                fileWriter.write(h.getHtml());
                fileWriter.close();
                if (console != null) { //output to the console
                    Platform.runLater(() -> {
                        String temp = console.getText() + "\n";
                        console.setText(temp + h.getFile());
                    });
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Overridden run method so that the logic is in its own thread
     */
    @Override
    public void run() {
        writeHTML();
    }
}
