package com.cshen.tiyu.domain.redpacket;

import java.util.ArrayList;

public class RedPacketData {

	public String errorMsg;
	String processId;
	ArrayList<RedPacket> result=new ArrayList<RedPacket>();
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public ArrayList<RedPacket> getResult() {
		return result;
	}
	public void setResult(ArrayList<RedPacket> result) {
		this.result = result;
	}
}
