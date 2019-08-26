package org.wuneng.web.postcard.services;

import org.wuneng.web.postcard.beans.MessageVessel;

import java.util.List;

public interface MessageService {
    void insert_message(MessageVessel messageVessel);

    List<MessageVessel> get_unaccepted_message(int sendUserId);

    void accept(int parseInt);
}
