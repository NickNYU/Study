package com.emc.ehc.nick.netty.heartbeat.Server;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月18日 下午9:41:11 
* 
*/
public class NettyChannelMap {
	private static Map<String, Channel> channels = new ConcurrentHashMap<String, Channel> ();
	
	public static void add(String clientId, Channel socketChannel){
		channels.put(clientId, socketChannel);
    }
	
    public static Channel get(String clientId){
       return channels.get(clientId);
    }
    
    public static void remove(Channel socketChannel){
        for (Map.Entry entry : channels.entrySet()){
            if (entry.getValue() == socketChannel){
            	channels.remove(entry.getKey());
            }
        }
    }
    
    public static int size() {
    	return channels.size();
    }
    
    public static Channel getRandomChannel() {
    	int count = 0;
    	int bound = NettyChannelMap.size();
    	int random = new Random().nextInt(bound);
    	for (Map.Entry entry : channels.entrySet()){
            if(count++ == random) {
            	return (Channel) entry.getValue();
            }
        }
    	return channels.entrySet().iterator().next().getValue();
    }
}
