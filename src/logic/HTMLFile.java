package logic;

/**
 * @author brandon
 * @version 5/19/17
 */
public class HTMLFile {
    String html, path;

    public HTMLFile(String html, String path){
        this.html = html;
        this.path = path;
    }

    public String getHtml() {
        return html;
    }

    public String getPath() {
        return path;
    }
}
