package org.wuneng.web.postcard.dao;

import org.apache.ibatis.annotations.*;
import org.wuneng.web.postcard.beans.ChangePasswordBean;
import org.wuneng.web.postcard.beans.Login;
import org.wuneng.web.postcard.beans.User;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO `postcard`.`users`(`stu_id`, `password`, `name`, `is_man`, `in_school`, `graduation_year`, `major_name`," +
            " `qq_account`, `wechat_account`, `email`, `city`, `slogan`, `profile_photo`,`phone_number`,`slat`," +
            "`company`,`job`,`is_deleted`)" +
            " VALUES (#{stu_id}, #{password}, #{name},#{is_man},#{in_school},#{graduation_year},#{major_name}," +
            " #{qq_account}, #{wechat_account},#{email},#{city},#{slogan}, #{profile_photo}," +
            "#{phone_number},#{slat},#{company},#{job},#{is_deleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void userInsert(User user);

    @Select("select id,password,slat,is_deleted,graduation_year,in_school from users where stu_id = #{stu_id} limit 1")
    public Login get_id_password(@Param("stu_id") String stu_id);

    @Select("select * from users where \n" +
            "id = (select user_friend.accept_user_id as ids FROM user_friend \n" +
            "where send_user_id = #{id} and is_accepted = 1 and is_deleted = 0)\n" +
            "union all\n" +
            "select * from users where \n" +
            "id =(select user_friend.send_user_id as ids FROM user_friend where \n" +
            "accept_user_id = #{id} and is_accepted = 1 and is_deleted = 0)")
    public User get_friends_all(@Param("id") Integer id);

    @Select("SELECT `id`,`stu_id`,`name`,`is_man`,`graduation_year`,\n" +
            "`major_name`,`qq_account`,\n" +
            "`wechat_account`,`email`,`city`,`slogan`,`profile_photo`,`phone_number`,`company`,`job`,`in_school`,`is_deleted`\n" +
            "FROM users WHERE id = #{id}")
    public User get_user_by_id(@Param("id") Integer id);

    @Update("UPDATE `postcard`.`users` SET  `graduation_year` = #{graduation_year}, " +
            "`major_name` = #{major_name}, `qq_account` = #{qq_account}, `wechat_account` = #{wechat_account}," +
            " `email` = #{email}, `city` = #{city}, `slogan` = #{slogan}, `company` = #{company} " +
            ", `job` = #{job} WHERE `id` = #{id}")
    public Integer update_user(User user);

    @Update("UPDATE users set password = #{password},slat = #{slat} where id = #{id}")
    Integer change_password(@Param("id") int parseInt,@Param("password") String password,@Param("slat") byte[] slat);

    @Select("select stu_id from users where phone_number = #{phone_number} limit 1")
    String get_stu_id_by_phone_number(@Param("phone_number")long phone_number);

    @Select("select slat from users where id = #{id} limit 1")
    byte[] get_slat_by_id(@Param("id") Integer id);

    @Select("select password from users where id = #{id} limit 1")
    String get_password_by_id(@Param("id") Integer parseInt);

    @Update("update users set password = #{password},slat = #{slat} where phone_number = #{phone_number}")
    Integer change_password_by_phone_number(@Param("phone_number") String phone_number,
                                            @Param("password") String new_password,
                                            @Param("slat") byte[] slat);

    @Update("update users set phone_number = #{phone_number} where id = #{id}")
    Integer update_user_number(@Param("id") int parseInt,@Param("phone_number") long phone_number);

    @Update("update users set is_deleted = 0 where id = #{id}")
    Integer log_in(@Param("id") Integer id);

    @Select("select password,slat from users where id = #{id} limit 1")
    ChangePasswordBean get_change_bean(@Param("id") int parseInt);
}
