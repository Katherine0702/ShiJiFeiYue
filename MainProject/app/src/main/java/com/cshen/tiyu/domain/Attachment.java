package com.cshen.tiyu.domain;

public class Attachment {
	String attachmentId;//文件唯一标识码
	String attachmentUserId;//文件对应的用户
	String attachmentType;//文件类型（是用户头像）
	String attachmentExtension;//文件类型（.jpg等）
	String attachmentName;//文件名
	long attchmentAllLength;//文件总长
	long attchmentEachLength;//该段文件长
	int attchmentEachIndex;//该段文件编号
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getAttachmentUserId() {
		return attachmentUserId;
	}
	public void setAttachmentUserId(String attachmentUserId) {
		this.attachmentUserId = attachmentUserId;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public String getAttachmentExtension() {
		return attachmentExtension;
	}
	public void setAttachmentExtension(String attachmentExtension) {
		this.attachmentExtension = attachmentExtension;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public long getAttchmentAllLength() {
		return attchmentAllLength;
	}
	public void setAttchmentAllLength(long attchmentAllLength) {
		this.attchmentAllLength = attchmentAllLength;
	}
	public long getAttchmentEachLength() {
		return attchmentEachLength;
	}
	public void setAttchmentEachLength(long attchmentEachLength) {
		this.attchmentEachLength = attchmentEachLength;
	}
	public int getAttchmentEachIndex() {
		return attchmentEachIndex;
	}
	public void setAttchmentEachIndex(int attchmentEachIndex) {
		this.attchmentEachIndex = attchmentEachIndex;
	}


}
