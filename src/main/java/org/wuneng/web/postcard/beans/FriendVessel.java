package org.wuneng.web.postcard.beans;

public class FriendVessel {
    private Integer id;
    private Integer send_user_id;
    private Integer accept_user_id;
    private boolean is_accepted;
    private boolean is_deleted;
    private boolean is_refuesd;

    public FriendVessel() {
    }

    public FriendVessel(int sendUserId, int acceptUserId) {
        this.send_user_id = sendUserId;
        this.accept_user_id = acceptUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(Integer send_user_id) {
        this.send_user_id = send_user_id;
    }

    public Integer getAccept_user_id() {
        return accept_user_id;
    }

    public void setAccept_user_id(Integer accept_user_id) {
        this.accept_user_id = accept_user_id;
    }

    public boolean isIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public boolean isIs_refuesd() {
        return is_refuesd;
    }

    public void setIs_refuesd(boolean is_refuesd) {
        this.is_refuesd = is_refuesd;
    }
}
