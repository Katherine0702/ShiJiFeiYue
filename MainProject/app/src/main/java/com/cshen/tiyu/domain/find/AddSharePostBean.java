package com.cshen.tiyu.domain.find;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Attachment;

public class AddSharePostBean {
   private String userId;
   private String userPwd;
   private String content;
   private String userName;
   private String userimg;
   private ArrayList<Attachment> imageUrl=new ArrayList<>();
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getUserPwd() {
	return userPwd;
}
public void setUserPwd(String userPwd) {
	this.userPwd = userPwd;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getUserimg() {
	return userimg;
}
public void setUserimg(String userimg) {
	this.userimg = userimg;
}
public ArrayList<Attachment> getImageUrl() {
	return imageUrl;
}
public void setImageUrl(ArrayList<Attachment> imageUrl) {
	this.imageUrl = imageUrl;
}
   
   
}
