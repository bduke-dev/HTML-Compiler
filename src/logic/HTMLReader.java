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
    String headerHTML, footerHTML, pathHTML;
    Scanner scanner;

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


    private HTMLFile[] getDynamicHTML(File[] listOfFiles, String folderPath){
        int counter = 0, index;
        File[] temp;

        for (int i = 0; i < listOfFiles.length; i++){
            //System.out.println("LOOKING" + listOfFiles[counter].getPath());
            //null .git, .sass-cache, css, scss, sass //TODO make so user can specify
            if (listOfFiles[counter] != null) {
                ArrayList<String> ignored = new ArrayList<>(Arrays.asList("/.git", "/.sass-cache", "/css", "/scss", "/sass", "/DEV FILES", "/google071d8247f50df527.html"));
                String path = listOfFiles[i].getPath();

                //remove path to directory sec/files/otherPath becomes /otherPath
                path = path.replace(folderPath, "");
                //System.out.println("NEW: " + path);
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

        //remove null spaces TODO eventually will remove not html files
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
        String folderPath = "src/files/PARTs_Website";
        File folder = new File(folderPath);//TODO make not static
        File[] listOfFiles = folder.listFiles();

        HTMLFile[] htmlFiles = getDynamicHTML(listOfFiles, folderPath);

        for(HTMLFile h : htmlFiles) {
            if (h != null) {
                System.out.println("Directory: " + h.getPath());
                //System.out.println("HTML\n" + h.getHtml());
            }
        }


        return true;
    }

}
