package org.wuneng.web.postcard.dao;

import org.apache.ibatis.annotations.*;
import org.json.JSONArray;
import org.wuneng.web.postcard.beans.Direction;

import java.util.List;

@Mapper
public interface DirectionsMapper {
    @SelectProvider(type = DirectionsDaoProvider.class,method = "get_direction")
    public List<String> get_direction(@Param("ids") List<Integer> ids);

    @Select("SELECT user_direction.direction_id \n" +
            "FROM user_direction\n" +
            "where\n" +
            "user_direction.uid = #{id} and `is_deleted` = 0")
    public List<Integer> get_direction_id(@Param("id") Integer id);


    @InsertProvider(type = DirectionsDaoProvider.class,method = "insert_directions")
    public int insert_directions(@Param("id")Integer id,
                                 @Param("directions")JSONArray array);

    @Select("SELECT id,direction_name FROM directions WHERE pid = 0 and is_deleted = 0")
    List<Direction> get_large_directions();

    @Select("SELECT id,direction_name FROM directions WHERE pid = #{id} and is_deleted = 0")
    List<Direction> get_children_directions(@Param("id") Integer id);

    @Insert("INSERT into directions (direction_name,is_deleted) VALUES (#{name},0)")
    void insert_direction(@Param("name") String s);

    @Delete("update user_direction set is_deleted = 1 WHERE uid = #{uid} and direction_id = #{did} LIMIT 1")
    int delete(@Param("uid") int user_id,@Param("did") int direction_id);
}
