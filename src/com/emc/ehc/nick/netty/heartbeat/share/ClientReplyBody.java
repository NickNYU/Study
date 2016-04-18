package com.emc.ehc.nick.netty.heartbeat.share;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午11:01:26 
* 
*/
public class ClientReplyBody extends ReplyBody {
	private String clientInfo;

    public ClientReplyBody(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }
}
