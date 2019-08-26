package org.wuneng.web.postcard.beans;

public class LogInResponse {
    private String token;
    private Boolean first;
    private boolean in_school;
    private int id;

    public Boolean getFirst() {
        return first;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean isFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public boolean isIn_school() {
        return in_school;
    }

    public void setIn_school(boolean in_school) {
        this.in_school = in_school;
    }
}
