package com.emc.ehc.nick.netty.heartbeat.share;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午11:02:14 
* 
*/
public class ServerReplyBody extends ReplyBody {
	private String serverInfo;
	
    public ServerReplyBody(String serverInfo) {
        this.serverInfo = serverInfo;
    }
    public String getServerInfo() {
        return serverInfo;
    }
    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }
}
