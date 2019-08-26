package org.wuneng.web.postcard.services.impl;

import org.apache.ibatis.annotations.Mapper;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.ChildrenDirection;
import org.wuneng.web.postcard.beans.Direction;
import org.wuneng.web.postcard.dao.DirectionsMapper;
import org.wuneng.web.postcard.services.DirectionService;


import java.util.List;

@Service
public class DirectionServiceImpl implements DirectionService {
    @Autowired
    DirectionsMapper directionsMapper;

    @Override
    public List<String> get_direcitons(Integer user_id) {
        CheckResult result = new CheckResult();
        List<Integer> ids =directionsMapper.get_direction_id(user_id);
        if (!ids.isEmpty()) {
            List<String> direction = directionsMapper.get_direction(ids);
            return direction;
        }
        return null;
    }
    @Override
    public List<Integer> get_ids(Integer id){
        return directionsMapper.get_direction_id(id);
    }

    @Override
    public int delete_derections(int user_id,int direction_id){
        return directionsMapper.delete(user_id,direction_id);
    }

    @Override
    public Integer insert_directions(JSONArray array, Integer id) {



        return directionsMapper.insert_directions(id, array);
    }

    @Override
    public List<Direction> get_large_directions() {
        return directionsMapper.get_large_directions();
    }

    @Override
    public ChildrenDirection get_children_directions(int id) {
        Direction direction = new Direction(id);
        ChildrenDirection c = new ChildrenDirection(direction);
        filling_children_directions(c);
        return c;
    }

    @Override
    public void insert_direction(String s) {
        directionsMapper.insert_direction(s);
    }

    private void filling_children_directions(ChildrenDirection childrenDirection){
        List<Direction> directions = directionsMapper.get_children_directions(childrenDirection.getDirection().getId());
        if (!directions.isEmpty()){
            for (Direction d:directions) {
                ChildrenDirection children = new ChildrenDirection(d);
                filling_children_directions(children);
                childrenDirection.addChildren(children);
            }
        }
    }
}
