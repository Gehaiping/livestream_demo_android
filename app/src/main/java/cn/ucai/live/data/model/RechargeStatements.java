package cn.ucai.live.data.model;

public class RechargeStatements {
    private Integer id;

    private String uname;

    private Integer rcount;

    private Integer rmb;

    private Long rdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public Integer getRcount() {
        return rcount;
    }

    public void setRcount(Integer rcount) {
        this.rcount = rcount;
    }

    public Integer getRmb() {
        return rmb;
    }

    public void setRmb(Integer rmb) {
        this.rmb = rmb;
    }

    public Long getRdate() {
        return rdate;
    }

    public void setRdate(Long rdate) {
        this.rdate = rdate;
    }
}