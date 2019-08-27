package org.wuneng.web.postcard.beans;

public class Login {
    private Integer id;
    private String  password;
    private byte[]  slat;
    private boolean is_deleted;
    private boolean in_school;
    private Integer graduation_year;

    public boolean isIn_school() {
        return in_school;
    }

    public void setIn_school(boolean in_school) {
        this.in_school = in_school;
    }

    public Integer getGraduation_year() {
        return graduation_year;
    }

    public void setGraduation_year(Integer graduation_year) {
        this.graduation_year = graduation_year;
    }

    public byte[] getSlat() {
        return slat;
    }

    public void setSlat(byte[] slat) {
        this.slat = slat;
    }

    public Login(Integer id, String password) {
        this.id = id;
        this.password = password;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
