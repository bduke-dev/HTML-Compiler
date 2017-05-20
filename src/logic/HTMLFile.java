package logic;

import java.io.File;

/**
 * @author brandon
 * @version 5/19/17
 */
public class HTMLFile {
    private String html;
    private File file;

    public HTMLFile(String html, File file){
        this.html = html;
        this.file = file;
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
}
