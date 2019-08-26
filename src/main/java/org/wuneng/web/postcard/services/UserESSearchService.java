package org.wuneng.web.postcard.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public interface UserESSearchService {



        JSONArray get_user_by_keyword(Object keyword, String[] fields,int from,int size);

    public String insert_user(JSONObject user,int user_id) throws IOException;

//    public String update_user_field(String field, JSONObject object) throws IOException;
}
