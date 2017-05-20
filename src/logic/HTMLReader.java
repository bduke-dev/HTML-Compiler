package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author brandon
 * @version 5/18/17
 */
public class HTMLReader {
    private String headerHTML, footerHTML, pathHTML;
    private Scanner scanner;

    public HTMLReader(String headerHTML, String footerHTML, String pathHTML){
        this.headerHTML = headerHTML;
        this.footerHTML = footerHTML;
        this.pathHTML = pathHTML;
    }

    private String readHTML(String path){
        String returnString = "";

        try{
            //read in the html
            scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) returnString += scanner.nextLine() + "\n";
        }
        catch (FileNotFoundException fnfe){ fnfe.printStackTrace();}

        return returnString;
    }


    private HTMLFile[] getProject(File[] listOfFiles){
        int counter = 0, index;
        File[] temp;

        for (int i = 0; i < listOfFiles.length; i++){
            //null .git, .sass-cache, css, scss, sass //TODO make so user can specify
            if (listOfFiles[counter] != null) {
                ArrayList<String> ignored = new ArrayList<>(Arrays.asList(".git", ".sass-cache", "css", "scss", "sass", "DEV FILES", "google071d8247f50df527.html"));
                String path = listOfFiles[i].getName();

                //null ignored files and directories in root project
                if (ignored.contains(path)) listOfFiles[i] = null;
            }
        }

        while (counter < listOfFiles.length){
            if (listOfFiles[counter] != null && listOfFiles[counter].isDirectory()){
                temp = listOfFiles[counter].listFiles();

                //null this index
                listOfFiles[counter] = null;

                //expand temp and tak on the end
                index = listOfFiles.length;
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
        for (int i = 0; i < temp.length; i++){
            if (temp[i] != null){
                listOfFiles = Arrays.copyOf(listOfFiles, listOfFiles.length + 1);
                listOfFiles[index++] = temp[i];
            }
        }

        //get the file and path
        HTMLFile[] htmlFiles = new HTMLFile[listOfFiles.length];
        for (int i = 0; i < htmlFiles.length; i++) htmlFiles[i] = new HTMLFile(readHTML(listOfFiles[i].getAbsolutePath()), listOfFiles[i].getAbsolutePath());

        return htmlFiles;
    }

    public boolean compileHTML(){
        HTMLFile[] htmlFiles = getProject(new File(pathHTML).listFiles()); //all chosen html files
        String header = readHTML(headerHTML), footer = readHTML(footerHTML); //header and footer html file

        for (int i = 0; i < htmlFiles.length; i++){

        }

        return true;
    }

}
