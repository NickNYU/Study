package com.emc.ehc.nick.netty.heartbeat.share;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:43:13 
* 
*/
public class Constants {
	private static String clientID;
	
    public static String getClientID() {
        return clientID;
    }
    
    public static void setClientID(String clientID) {
        Constants.clientID = clientID;
    }
}
