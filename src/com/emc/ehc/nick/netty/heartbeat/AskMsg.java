package com.emc.ehc.nick.netty.heartbeat;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:54:18 
* 
*/
public class AskMsg extends AbstractMsg {
	public AskMsg() {
		super();
		setType(MsgType.ASK);
	}
	
	private AskParams params;

    public AskParams getParams() {
        return params;
    }

    public void setParams(AskParams params) {
        this.params = params;
    }
}
