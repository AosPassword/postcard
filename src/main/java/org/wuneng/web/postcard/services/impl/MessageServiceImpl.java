package org.wuneng.web.postcard.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.beans.MessageVessel;
import org.wuneng.web.postcard.dao.MessageMapper;
import org.wuneng.web.postcard.services.MessageService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public void insert_message(MessageVessel messageVessel) {
        messageMapper.insert_message(messageVessel);
    }

    @Override
    public List<MessageVessel> get_unaccepted_message(int sendUserId) {
        return messageMapper.get_unaccepted_message(sendUserId);
    }

    @Override
    public void accept(int parseInt) {
        messageMapper.accept(parseInt);
    }
}
