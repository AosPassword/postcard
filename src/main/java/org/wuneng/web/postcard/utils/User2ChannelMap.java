package org.wuneng.web.postcard.utils;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class User2ChannelMap {

    private volatile static Map<Integer, Channel> user2channel;

    private User2ChannelMap(){
    }

    public static Map<Integer, Channel> getUser2channelMap() {
        if (user2channel == null) {
            synchronized (User2ChannelMap.class) {
                if (user2channel == null) {
                    user2channel = new HashMap<>();
                }
            }
        }
        return user2channel;
    }

}
