package org.wuneng.web.postcard.beans;

import java.sql.Timestamp;

public class MessageVessel {
    private Integer id;
    private Integer send_user_id;
    private Integer accept_user_id;
    private String payload;
    private boolean is_string;
    private Timestamp timestamp;

    @Override
    public String toString() {
        return "MessageVessel{" +
                "id=" + id +
                ", send_user_id=" + send_user_id +
                ", accept_user_id=" + accept_user_id +
                ", payload='" + payload + '\'' +
                ", is_string=" + is_string +
                ", timestamp=" + timestamp +
                '}';
    }

    public MessageVessel(){}

    public MessageVessel(Integer send_user_id, Integer accept_user_id, String payload, boolean is_string, Timestamp timestamp) {
        this.send_user_id = send_user_id;
        this.accept_user_id = accept_user_id;
        this.payload = payload;
        this.is_string = is_string;
        this.timestamp = timestamp;
    }
    public MessageVessel(PostCardMessage.Message message,String payload,boolean is_string){
        this.send_user_id = message.getSendUserId();
        this.accept_user_id = message.getAcceptUserId();
        this.payload = payload;
        this.is_string = is_string;
        this.timestamp = new Timestamp(message.getTime().getSeconds()*1000+message.getTime().getNanos()/1000000*1000);
    }


    public MessageVessel(PostCardMessage.Message message){
        this(message,message.getPayload().toStringUtf8(),true);
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isIs_string() {
        return is_string;
    }

    public void setIs_string(boolean is_string) {
        this.is_string = is_string;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
