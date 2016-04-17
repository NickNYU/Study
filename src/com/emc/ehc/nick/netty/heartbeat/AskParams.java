package com.emc.ehc.nick.netty.heartbeat;

import java.io.Serializable;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:54:59 
* 
*/
public class AskParams implements Serializable {
	private static final long serialVersionUID = 1L;
    private String auth;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
