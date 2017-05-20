package logic;

import java.util.Scanner;

/**
 * @author brandon
 * @version 5/19/17
 */
public class Test {

    public static void main(String[] args) {
        String headerHTML, footerHTML, pathHTML;
        headerHTML = "src/files/header.html";
        footerHTML = "src/files/footer.html";
        pathHTML = "src/files/PARTs_Website";
        HTMLReader htmlReader = new HTMLReader(headerHTML, footerHTML, pathHTML);
        htmlReader.compileHTML();
    }
}
