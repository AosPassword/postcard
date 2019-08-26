package org.wuneng.web.postcard.utils;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class Channel2UserMap {

    private volatile static Map<Channel,Integer> channel2user;

    private Channel2UserMap(){
    }

    public static Map<Channel, Integer> getChannel2UserMap() {
        if (channel2user == null) {
            synchronized (Channel2UserMap.class) {
                if (channel2user == null) {
                    channel2user = new HashMap<>();
                }
            }
        }
        return channel2user;
    }

}
