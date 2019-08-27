package org.wuneng.web.postcard.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.FriendVessel;
import org.wuneng.web.postcard.beans.MessageVessel;

import java.io.IOException;
import java.util.Set;

public interface FriendService {
    CheckResult get_friends_ids(Integer id);

    boolean send_add_request(Integer send, Integer accept);

    boolean accept_add_response(Integer send_user_id, Integer accept_user_id);

    boolean has_send_request(int sendUserId, int acceptUserId);

    Set<Integer> get_send_request(int id);

    int refuse(FriendVessel friendVessel);

    void beRefused(Integer id);

    Set<MessageVessel> get_refuse(int send_user_id);

    Integer delete_friend(int send_user_id, int accept_user_id);
}
