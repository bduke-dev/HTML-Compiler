package logic;

import java.io.File;

/**
 * A Class to hold all of the information about an HTML File
 *
 * @author Brandon Duke
 * @version 5/19/17
 */
public class HTMLFile {
    private String html, currentPage;
    private boolean homePage;
    private File file;
    private int depth;

    /**
     * Constructor for an HTML File
     *
     * @param html String of html in the file
     * @param file File location
     * @param depth How deep a file is in the project
     * @param currentPage Name of the page
     * @param homePage Flag to let us know if it is the home page
     */
    HTMLFile(String html, File file, int depth, String currentPage, boolean homePage){
        this.html = html;
        this.file = file;
        this.depth = depth;
        this.currentPage = currentPage;
        this.homePage = homePage;
    }

    /**
     * A method to get the html
     *
     * @return String of html
     */
    public String getHtml() {
        return html;
    }

    /**
     * A method to the the file
     *
     * @return File
     */
    File getFile() {
        return file;
    }

    /**
     * A method to let us append the html in a file
     *
     * @param html The updated string
     */
    void appendHtml(String html) {
        this.html = html;
    }

    /**
     * A method to get the depth of a file
     *
     * @return Int of the depth
     */
    int getDepth() {
        return depth;
    }

    /**
     * A method to the the the page name
     *
     * @return String of page name
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * A method to see if its the home page or not
     *
     * @return Boolean telling if it is or not
     */
    boolean isHomePage() {
        return homePage;
    }
}
