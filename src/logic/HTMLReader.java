package logic;

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
public class HTMLReader {
    private File navHTML, footerHTML, pathHTML;
    private Scanner scanner;

    public HTMLReader(String navHTML, String footerHTML, String pathHTML){
        this.navHTML = new File(navHTML);
        this.footerHTML = new File(footerHTML);
        this.pathHTML = new File(pathHTML);
    }

    private String readHTML(File path){
        String returnString = "";

        try{
            //read in the html
            scanner = new Scanner(path);
            while (scanner.hasNextLine()) returnString += scanner.nextLine() + "\n";
        }
        catch (FileNotFoundException fnfe){fnfe.printStackTrace();}

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
        for (int i = 0; i < htmlFiles.length; i++) htmlFiles[i] = new HTMLFile(readHTML(listOfFiles[i]), listOfFiles[i]);

        return htmlFiles;
    }

    private HTMLFile[] compileHTML(){
        HTMLFile[] htmlFiles = getProject(pathHTML.listFiles()); //all chosen html files
        String nav = readHTML(navHTML), footer = readHTML(footerHTML); //nav and footer html file

        for (int i = 0; i < htmlFiles.length; i++){
            String[] lines = htmlFiles[i].getHtml().split("\n"); //turn into array to null indices that are in the nav or footer if it exists

            int j = 0;
            //removes nav if it exists
            for (; j < lines.length; j++){
                if (lines[j].contains("<nav")){
                    while (!lines[j].contains("</nav>")){
                        lines[j++] = null;
                    }
                    lines[j++] = null; //catches the last line the while loop doesn't get
                    break;
                }
            }

            //removes footer if it exists
            for (; j < lines.length; j++){
                if (lines[j].contains("<footer")){
                    while (!lines[j].contains("</footer>")){
                        lines[j++] = null;
                    }
                    lines[j++] = null; //catches the last line the while loop doesn't get
                    break;
                }
            }

            //remove null spaces
            String[] temp = lines;
            lines = new String[0];
            int index = 0;
            for (int k = 0; k < temp.length; k++){
                if (temp[k] != null){
                    lines = Arrays.copyOf(lines, lines.length + 1);
                    lines[index++] = temp[k];
                }
            }

            //add the nav and footer
            String compiled = "";
            for (int k = 0; k < lines.length; k++){
                compiled += lines[k] + "\n";
                if (lines[k].contains("<body id=\"top\">")) compiled += nav;
                if (lines[k].contains("</main>")){
                    compiled += lines[k++]  + "\n"; //this is because there is a closing div right after main's closing then the footer goes there
                    compiled += footer;
                }
            }
            htmlFiles[i].appendHtml(compiled);
        }
        return htmlFiles;
    }

    public boolean writeHTML(){
        boolean success = false;

        HTMLFile[] htmlFiles = compileHTML();
        FileWriter fileWriter;
        for (HTMLFile h : htmlFiles) {
            try {
                fileWriter = new FileWriter(h.getFile().getAbsoluteFile());
                fileWriter.write(h.getHtml());
                fileWriter.close();
            } catch (IOException e) {e.printStackTrace();}
        }




        return success;
    }

}
