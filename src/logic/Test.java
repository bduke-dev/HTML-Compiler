package logic;

import java.util.Scanner;

/**
 * @author brandon
 * @version 5/19/17
 */
public class Test {

    public static void main(String[] args) {
        String navHTML, footerHTML, pathHTML;
        navHTML = "src/files/header.html";
        footerHTML = "src/files/footer.html";
        pathHTML = "src/files/PARTs_Website";
        HTMLReader htmlReader = new HTMLReader(navHTML, footerHTML, pathHTML);
        htmlReader.compileHTML();
    }
}
