package org.wuneng.web.postcard.services;

import org.json.JSONArray;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.ChildrenDirection;
import org.wuneng.web.postcard.beans.Direction;

import java.util.List;

public interface DirectionService {
    List<String> get_direcitons(Integer user_id);

    List<Integer> get_ids(Integer id);

    int delete_derections(int user_id, int direction_id);

    Integer insert_directions(JSONArray array, Integer id);

    List<Direction> get_large_directions();

    ChildrenDirection get_children_directions(int id);

    void insert_direction(String s);
}
