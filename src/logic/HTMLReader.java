package logic;

import java.io.File;
import java.io.FileNotFoundException;
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


    private HTMLFile[] getDynamicHTML(File[] listOfFiles){
        int counter = 0, index;
        File[] temp;
        while (counter < listOfFiles.length){
            if (listOfFiles[counter].isDirectory()){
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

        File folder = new File("src/files/PARTs_Website");//TODO make not static
        File[] listOfFiles = folder.listFiles();

        HTMLFile[] htmlFiles = getDynamicHTML(listOfFiles);

        for(HTMLFile h : htmlFiles) {
            if (h != null) {
                System.out.println("Directory: " + h.getPath());
                //System.out.println("HTML\n" + h.getHtml());
            }
        }


        return true;
    }

}
