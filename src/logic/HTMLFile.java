package logic;

import java.io.File;

/**
 * @author brandon
 * @version 5/19/17
 */
public class HTMLFile {
    private String html;
    private File file;
    private int depth;

    public HTMLFile(String html, File file, int depth){
        this.html = html;
        this.file = file;
        this.depth = depth;
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
}
