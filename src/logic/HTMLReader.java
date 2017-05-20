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


    private File[] getDynamicHTML(File[] listOfFiles, HTMLFile[] htmlFiles, int counter){

        boolean done = false;
        counter = 0;

        while (counter < listOfFiles.length){
            if (listOfFiles[counter].isDirectory()){
                File[] temp = listOfFiles[counter].listFiles();
                //remove this index
                listOfFiles[counter] = null;
                File[] frontHalf = Arrays.copyOfRange(listOfFiles, 0, counter);
                File[] backHalf = Arrays.copyOfRange(listOfFiles, counter + 1, listOfFiles.length);

                //merge them back together
                System.arraycopy(frontHalf, 0, listOfFiles, 0, frontHalf.length);
                System.arraycopy(backHalf, 0, listOfFiles, frontHalf.length + 1, backHalf.length);

                //expand temp and tak on the end
                int index = listOfFiles.length;
                for (File aTemp : temp) {
                    listOfFiles = Arrays.copyOf(listOfFiles, listOfFiles.length + 1);
                    listOfFiles[index++] = aTemp;
                }
            }

            counter++;
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

        return listOfFiles;
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

        File[] files = getDynamicHTML(listOfFiles, htmlFiles, 0);

        for(File h : files) {
            if (h != null) {
                System.out.println("Directory: " + h.getAbsolutePath());
                //System.out.println("HTML\n" + h.getHtml());
            }
        }


        return true;
    }

}
