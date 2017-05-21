package logic;

import java.util.Scanner;

/**
 * @author brandon
 * @version 5/19/17
 */
public class Test {

    public static void main(String[] args) {
        String navHTML, footerHTML, pathHTML;
        navHTML = "src/files/nav";
        footerHTML = "src/files/footer";
        pathHTML = "src/files/PARTs_Website";
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML);
        htmlReader.writeHTML();
    }
}
