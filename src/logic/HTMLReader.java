package logic;

import java.io.File;
import java.io.FileNotFoundException;
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

    private String[] getStaticHTML(){
        String[] returnString = new String[2];

        try{
            //read in the header
            scanner = new Scanner(new File(headerHTML));
            returnString[0] = "";
            while (scanner.hasNextLine()) returnString[0] += scanner.nextLine() + "\n";

            //read in the footer
            scanner = new Scanner(new File(footerHTML));
            returnString[1] = "";
            while (scanner.hasNextLine()) returnString[1] += scanner.nextLine() + "\n";
        }
        catch (FileNotFoundException fnfe){ fnfe.printStackTrace();}

        return returnString;
    }


    private void getDynamicHTML(File[] listOfFiles){

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
                getDynamicHTML(listOfFiles[i].listFiles());
            }
        }

    }

    public boolean compileHTML(){

        String[] returnString, directories;

        File folder = new File("src/files/PARTs_Website");//TODO make not static
        File[] listOfFiles = folder.listFiles();

        getDynamicHTML(listOfFiles);

        return true;
    }

}
