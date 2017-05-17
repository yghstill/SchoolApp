package Bean;

/**
 * Created by Y-GH on 2017/5/16.
 */

public class PictureBean {
    private String useid;
    private String url;
    private String imgdesc;
    private String imgtime;

    public String getUserid() {
        return useid;
    }

    public String getUrl() {
        return url;
    }

    public String getImgdesc() {
        return imgdesc;
    }

    public String getImgtime() {
        return imgtime;
    }

    public void setUserid(String userid) {
        this.useid = userid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImgdesc(String imgdesc) {
        this.imgdesc = imgdesc;
    }

    public void setImgtime(String imgtime) {
        this.imgtime = imgtime;
    }
}
