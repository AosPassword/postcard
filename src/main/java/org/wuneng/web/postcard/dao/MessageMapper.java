package org.wuneng.web.postcard.dao;

import org.apache.ibatis.annotations.*;
import org.wuneng.web.postcard.beans.MessageVessel;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("INSERT into messages (send_user_id,accept_user_id,is_accepted,payload,is_string,time)" +
            "VALUES(#{send_user_id},#{accept_user_id},false},#{payload},#{is_string},#{time})")
    public void insert_message(MessageVessel messageVessel);

    @Select("SELECT id,send_user_id,accept_user_id,is_string,time,payload FROM messages WHERE accept_user_id = #{send} and is_accepted = 0")
    List<MessageVessel> get_unaccepted_message(@Param("send") int sendUserId);

    @Update("update messages set is_accepted = 1 where id = #{id}")
    void accept(@Param("id") int parseInt);
}
