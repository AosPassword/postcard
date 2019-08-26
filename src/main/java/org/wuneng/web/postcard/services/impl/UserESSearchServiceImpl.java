package org.wuneng.web.postcard.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.message.ObjectArrayMessage;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.services.UserESSearchService;

import java.io.IOException;
import java.util.Iterator;

@Repository
public class UserESSearchServiceImpl implements UserESSearchService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Value("${elasticsearch.index}")
    String index;
    @Autowired
    RequestOptions requestOptions;
    @Autowired
    ActionListener<UpdateResponse> updatelistener;

    @Override
    public JSONArray get_user_by_keyword(Object keyword, String[] fields, int from, int size) {
        SearchRequest request = new SearchRequest();
        request.indices(index);
        MultiMatchQueryBuilder match = QueryBuilders.multiMatchQuery(keyword, fields);
        JSONArray jsonArray = null;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(match).from(from).size(size);
        request.source(searchSourceBuilder);

        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, requestOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response!=null) {
            SearchHit[] hits = response.getHits().getHits();
            jsonArray = new JSONArray();
            for (SearchHit hit : hits) {
                jsonArray.put(new JSONObject(hit.getSourceAsString()));
            }
        }
        return jsonArray;
    }

    @Override
    public String insert_user(JSONObject user, int user_id) throws IOException {
        if (user_id == 0) {
            return "error";
        }
        UpdateRequest request = new UpdateRequest(index, String.valueOf(user_id));
        request.doc(user.toString(), XContentType.JSON);
        request.docAsUpsert(true);
        request.routing(String.valueOf(user_id));
        restHighLevelClient.updateAsync(request, requestOptions, updatelistener);
        return "insert user not error";
    }

//    @Override
//    public String update_user_field(String field, JSONObject object) throws IOException {
//        int user_id = object.getInt("user_id");
//        System.out.println(object);
//        Object target = object.get(field);
//        if (user_id==0){
//            return "es error:[insert user direction] not get user's id";
//        }
//        UpdateRequest request = new UpdateRequest(index,String.valueOf(user_id));
//        System.out.println("target:"+target);
//        request.doc(field,target);
//
//        restHighLevelClient.update(request,requestOptions);
//        return "insert direction not error";
//    }

    private BoolQueryBuilder getFilter(JSONObject filters) {
        if (filters != null) {
            BoolQueryBuilder builder = new BoolQueryBuilder();
            Iterator<String> iterator = filters.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = filters.optString(key);
                System.out.println("key:" + key + "\tvalue:" + value);
                builder.filter(QueryBuilders.termQuery(key, value));
            }
            return builder;
        } else {
            return null;
        }
    }
}
