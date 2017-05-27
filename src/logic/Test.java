package logic;

import java.io.File;
import java.util.Scanner;

/**
 * @author brandon
 * @version 5/19/17
 */
public class Test {

    public static void main(String[] args) {
        File navHTML, footerHTML, pathHTML;
        navHTML = new File("src/files/PARTs_Website/partials/nav");
        footerHTML = new File("src/files/PARTs_Website/partials/footer");
        pathHTML = new File("src/files/PARTs_Website");
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML);
        htmlReader.writeHTML();
    }
}
