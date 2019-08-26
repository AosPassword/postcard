package org.wuneng.web.postcard.dao;

import org.apache.ibatis.annotations.*;
import java.util.Set;

@Mapper
public interface FriendsMapper {
    @Select("select user_friend.accept_user_id as ids FROM user_friend \n" +
            "where send_user_id = #{id} and is_accepted = 1 and is_deleted = 0\n" +
            "union all\n" +
            "select user_friend.send_user_id as ids FROM user_friend \n" +
            "where accept_user_id = #{id} and is_accepted = 1 and is_deleted = 0")
    public Set<Integer> get_friends_ids(@Param("id") int id);


    @Insert("INSERT INTO user_friend (send_user_id,accept_user_id,is_accepted,is_deleted) " +
            "VALUES (#{send},#{accept},0,0)")
    Integer send_add_request(@Param("send") Integer send,@Param("accept") Integer accept);

    @Update("UPDATE user_friend \n" +
            "SET is_accepted = 1\n" +
            "WHERE send_user_id = #{accept} \n" +
            "and accept_user_id = #{send}")
    Integer accept_add_response(@Param("send") Integer send_user_id,@Param("accept")Integer accept_user_id);

    @Select("SELECT send_user_id FROM user_friend WHERE accept_user_id = #{id} and is_accepted = 0 and is_deleted = 0")
    Set<Integer> get_send_request(@Param("id")int id);
}
