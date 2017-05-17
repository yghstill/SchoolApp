package Bean;

/**
 * Created by Y-GH on 2017/5/17.
 */

public class NewsBean {
    private String newsname;
    private String newscontent;
    private String time;
    private String reporter;

    public NewsBean(String newsname, String newscontent, String time, String reporter) {
        this.newsname = newsname;
        this.newscontent = newscontent;
        this.time = time;
        this.reporter = reporter;
    }

    public String getNewsname() {
        return newsname;
    }

    public String getNewscontent() {
        return newscontent;
    }

    public String getTime() {
        return time;
    }

    public String getReporter() {
        return reporter;
    }

    public void setNewsname(String newsname) {
        this.newsname = newsname;
    }

    public void setNewscontent(String newscontent) {
        this.newscontent = newscontent;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
}
