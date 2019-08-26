package org.wuneng.web.postcard.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProfilePhotoMapper {
    @Update("UPDATE users set profile_photo = #{file_name} where id = #{id}")
    public Integer update_photo(@Param("id") Integer id,
                                @Param("file_name")String file_name);
}
