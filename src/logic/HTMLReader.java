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


    private HTMLFile[] getDynamicHTML(File[] listOfFiles, HTMLFile[] htmlFiles, int counter){

        boolean done = false;
        counter = 0;

        while (!done){
            done = true;

            if (listOfFiles[counter].isDirectory()){
                File[] temp = listOfFiles[counter].listFiles();
                //remoce this inxex, add all on at the end

            }



            counter++
        }

        /*for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());

                htmlFiles = Arrays.copyOf(htmlFiles, htmlFiles.length + 1);
                htmlFiles[i] = new HTMLFile(readHTML(listOfFiles[i].getAbsolutePath()), listOfFiles[i].getAbsolutePath());

            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
                htmlFiles = Arrays.copyOf(htmlFiles, htmlFiles.length + 1);
                getDynamicHTML(listOfFiles[i].listFiles(), htmlFiles, counter++);
                //File f = searchFolder(listOfFiles[i].listFiles());
                //htmlFiles[i] = new HTMLFile(readHTML(f.getAbsolutePath()), f.getAbsolutePath());
            }

        }*/

        return htmlFiles;
    }

    private File searchFolder(File[] listOfFiles){
        File f = null;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                f = listOfFiles[i];
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
                f = searchFolder(listOfFiles);
            }
        }
        return f;
    }

    public boolean compileHTML(){

        HTMLFile[] htmlFiles = new HTMLFile[0];

        File folder = new File("src/files/PARTs_Website");//TODO make not static
        File[] listOfFiles = folder.listFiles();

        htmlFiles = getDynamicHTML(listOfFiles, htmlFiles, 0);

        for(HTMLFile h : htmlFiles) {
            if (h != null) {
                System.out.println("Directory: " + h.getPath());
                //System.out.println("HTML\n" + h.getHtml());
            }
        }


        return true;
    }

}
