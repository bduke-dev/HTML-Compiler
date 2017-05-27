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

/**
 * @author brandon
 * @version 5/18/17
 */
public class HTMLReader implements Runnable{
    private File navHTML, footerHTML, pathHTML;
    private ArrayList<String> ignore;
    private TextArea console;
    private Scanner scanner;

    public HTMLReader(File navHTML, File footerHTML, File pathHTML, TextArea console, ArrayList<String> ignore){
        this.navHTML = navHTML;
        this.footerHTML = footerHTML;
        this.pathHTML = pathHTML;
        this.console = console;
        this.ignore = ignore;
    }

    private String readHTML(File path){
        StringBuilder returnString = new StringBuilder();

        try{
            //read in the html
            scanner = new Scanner(path);
            while (scanner.hasNextLine()) returnString.append(scanner.nextLine()).append("\n");
        }
        catch (FileNotFoundException fnfe){fnfe.printStackTrace();}

        return returnString.toString();
    }

    private HTMLFile[] getProject(File[] listOfFiles){
        int counter = 0, index;
        File[] temp;

        for (int i = 0; i < listOfFiles.length; i++){
            if (listOfFiles[i] != null) {
                String path = listOfFiles[i].getName();
                //null ignored files and directories in root project
                if (ignore.contains(path)) {
                    listOfFiles[i] = null;
                    System.out.println("ignores: " + path); //TODO remove
                }
            }
        }

        while (counter < listOfFiles.length){
            if (listOfFiles[counter] != null && listOfFiles[counter].isDirectory()){
                temp = listOfFiles[counter].listFiles();

                //null this index
                listOfFiles[counter] = null;

                //expand temp and tak on the end
                index = listOfFiles.length;
                assert temp != null; //TODO learn more
                for (File aTemp : temp) {
                    listOfFiles = Arrays.copyOf(listOfFiles, listOfFiles.length + 1);
                    listOfFiles[index++] = aTemp;
                }
            }
            counter++;
        }

        //null not html files
        for (int i = 0; i < listOfFiles.length; i++){
            if (listOfFiles[i] != null) {// only check not currently null spaces
                String path = listOfFiles[i].getAbsolutePath();
                path = path.substring(path.length() - 5, path.length());
                if (!path.equals(".html")) listOfFiles[i] = null;
            }
        }

        //remove null spaces
        temp = listOfFiles;
        listOfFiles = new File[0];
        index = 0;
        for (File aTemp : temp) {
            if (aTemp != null) {
                listOfFiles = Arrays.copyOf(listOfFiles, listOfFiles.length + 1);
                listOfFiles[index++] = aTemp;
            }
        }

        //get the file and path
        HTMLFile[] htmlFiles = new HTMLFile[listOfFiles.length];

        //depth of each file so certain nav and footer can be used to preserve file paths to resources within project
        int[] depth = new int[htmlFiles.length];
        for (int i = 0; i < listOfFiles.length; i++){
            String path = listOfFiles[i].getAbsolutePath().replace(pathHTML.getAbsolutePath(), "");
            int count = path.length() - path.replace("/", "").length();
            depth[i] = count;
        }

        boolean homePage;
        for (int i = 0; i < htmlFiles.length; i++) {
            homePage = listOfFiles[i].getAbsolutePath().equals(pathHTML.getAbsolutePath() + "/index.html");
            System.out.println(homePage + " current page " + listOfFiles[i].getAbsolutePath() + "PATH " + pathHTML.getAbsolutePath()+"/index.html" );
            htmlFiles[i] = new HTMLFile(readHTML(listOfFiles[i]), listOfFiles[i],
                    depth[i], listOfFiles[i].getParentFile().getAbsolutePath().replace(pathHTML.getAbsolutePath(), "").replace("/", ""),
                    homePage);
        }



        return htmlFiles;
    }

    //for getting header or footer
    private HTMLFile[] getPartialHTML(File navPath){
        File[] listOfFiles = navPath.listFiles();
        assert listOfFiles != null;//TODO learn more
        HTMLFile[] html = new HTMLFile[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++){
            int depth = Integer.parseInt(listOfFiles[i].getName().replace(".html", ""));
            html[i] = new HTMLFile(readHTML(listOfFiles[i]), listOfFiles[i], depth, "", false);
        }
        return html;
    }

    //required comments in documentation, to know where to place html
    //add error handling to help user diagnose issues, like check tags. also make them aware they can't be on the same line
    private HTMLFile[] compileHTML(){
        HTMLFile[] htmlFiles = getProject(pathHTML.listFiles()); //all chosen html files
        HTMLFile[] nav = getPartialHTML(navHTML), footer = getPartialHTML(footerHTML); //nav and footer html file

        for (HTMLFile htmlFile : htmlFiles) {
            String[] lines = htmlFile.getHtml().split("\n"); //turn into array to null indices that are in the nav or footer if it exists

            int j = 0;
            //removes nav if it exists
            for (; j < lines.length; j++) {
                if (lines[j].contains("<!--nav-->")) {
                    j++;
                    while (!lines[j].contains("<!--/nav-->")) lines[j++] = null;
                    break;
                } else if (lines[j].contains("<!--footer-->")) { //in case the nav doesn't exist, but the footer does
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


            //add the nav and footer
            StringBuilder compiled = new StringBuilder();StringBuilder navString = new StringBuilder();
            String footerString = "";
            int depth = htmlFile.getDepth();
            for (int k = 0; k < nav.length && k < footer.length; k++) { //make sure there are an equal number of files
                if (nav[k].getDepth() == depth) navString = new StringBuilder(nav[k].getHtml());
                if (footer[k].getDepth() == depth) footerString = footer[k].getHtml();
            }

            //optional .currentPage in nav
            //this gets the name of the current page, including the depth
            StringBuilder currentPage = new StringBuilder();
            if (htmlFile.getDepth() == 1) currentPage.append("/").append(htmlFile.getCurrentPage());
            else {
                for (int k = 1; k < htmlFile.getDepth(); k++) currentPage.append("../");
                currentPage.append(htmlFile.getCurrentPage());
            }

            scanner = new Scanner(navString.toString());
            navString = new StringBuilder();
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();//TODO for some reason isnt catching 404
                System.out.println(currentPage.toString());
                if (currentPage.toString().equals("/")) { //catches all files in the root directory, like index, 404, etc.
                    System.out.println(htmlFile.isHomePage());
                    //hopefully a condition that can only be met on the home page
                    if (currentLine.contains("<p><a href=\"/\"></a></p>") && currentLine.toLowerCase().contains("home") && htmlFile.isHomePage()) {
                        StringBuilder sb = new StringBuilder(currentLine);
                        int position = sb.indexOf("<a");
                        sb.insert(position + 2, " class=\"currentPage\"");
                        currentLine = sb.toString();
                    }
                }
                else if (currentLine.contains(currentPage.toString())) {
                    StringBuilder sb = new StringBuilder(currentLine);
                    int position = sb.indexOf("<a");
                    sb.insert(position + 2, " class=\"currentPage\"");
                    currentLine = sb.toString();
                }
                navString.append(currentLine).append("\n");
            }

            for (String line : lines) {
                compiled.append(line).append("\n");
                if (line.contains("<!--nav-->")) compiled.append(navString);
                if (line.contains("<!--footer-->")) compiled.append(footerString);
            }
            htmlFile.appendHtml(compiled.toString());
        }
        return htmlFiles;
    }

    private void writeHTML(){
        HTMLFile[] htmlFiles = compileHTML();
        FileWriter fileWriter;
        for (HTMLFile h : htmlFiles) {
            try {
                fileWriter = new FileWriter(h.getFile().getAbsoluteFile());
                fileWriter.write(h.getHtml());
                fileWriter.close();
                if (console != null) {
                    Platform.runLater(() -> {
                        String temp = console.getText() + "\n";
                        console.setText(temp + h.getFile());
                    });
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    @Override
    public void run() {
        writeHTML();
    }
}
