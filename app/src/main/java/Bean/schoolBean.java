package Bean;

/**
 * Created by Y-GH on 2017/5/17.
 */

public class schoolBean {
    private String xueyuanid;
    private String xueyuandesc;

    public schoolBean(String xueyuanid, String xueyuandesc) {
        this.xueyuanid = xueyuanid;
        this.xueyuandesc = xueyuandesc;
    }

    public String getXueyuanid() {
        return xueyuanid;
    }

    public String getXueyuandesc() {
        return xueyuandesc;
    }

    public void setXueyuanid(String xueyuanid) {
        this.xueyuanid = xueyuanid;
    }

    public void setXueyuandesc(String xueyuandesc) {
        this.xueyuandesc = xueyuandesc;
    }
}
