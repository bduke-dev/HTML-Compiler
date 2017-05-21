package logic;

import java.io.File;

/**
 * @author brandon
 * @version 5/19/17
 */
public class HTMLFile {
    private String html, currentPage;
    private File file;
    private int depth;

    public HTMLFile(String html, File file, int depth, String currentPage){
        this.html = html;
        this.file = file;
        this.depth = depth;
        this.currentPage = currentPage;
    }

    public String getHtml() {
        return html;
    }

    public File getFile() {
        return file;
    }

    public void appendHtml(String html) {
        this.html = html;
    }

    public int getDepth() {
        return depth;
    }

    public String getCurrentPage() {
        return currentPage;
    }
}
