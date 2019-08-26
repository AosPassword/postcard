package org.wuneng.web.postcard.dao;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class DirectionsDaoProvider {
    public String get_direction(Map<String, List<Integer>> ids) {
        if (!ids.get("ids").isEmpty()) {
            StringBuffer sql = new StringBuffer("SELECT direction_name FROM directions where id in (");
            for (int i = 0; i < ids.get("ids").size(); i++) {
                if (i < ids.get("ids").size() - 1) {
                    System.out.println(ids.get("ids").get(i));
                    sql.append(ids.get("ids").get(i) + ",");
                } else {
                    sql.append(ids.get("ids").get(i) + ")");
                }
            }
            return sql.toString();
        }else {
            System.out.println("null!!!!!!!!!!!!!");
            return null;
        }
    }

    public String insert_directions(Map<String,Object>directions_ids){
        JSONArray array = (JSONArray) directions_ids.get("directions");
        Integer id = (Integer) directions_ids.get("id");
        StringBuffer sql = new StringBuffer("INSERT INTO `postcard`.`user_direction`(`uid`, `direction_id`, `is_deleted`) VALUES");
        for (int i =0;i<array.length();i++) {
            if (i!=0){
                sql.append(",");
            }
            sql.append("("+id+", "+array.getInt(i)+", 0)");
        }
        System.out.println(sql);
        return sql.toString();
    }
}