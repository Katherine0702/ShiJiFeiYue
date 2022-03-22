package com.cshen.tiyu.domain.login;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lingcheng on 15/10/1.
 */
@Table(name = "User") 
public class User implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
    /**  
	 * isId = true 代表该字段是这张表的主键，类型也可以是String (赋值为false就不是主键了)，虽然数据库表可以设置多个主键，但是在实际操作的时候还是设置一个  
	 * autoGen = true 代表主键自增长，如果不是自增长，那就赋值false (这里主键是否自增长很关键，会影响到后面数据库的操作，下面再结合方法来解释)  
	 */  
	@Column(name = "_id",isId = true,autoGen = true)  
	public int _id;  
	@Column(name = "id")
	private Long id;
	@Column(name = "gradeInfo")
	public String gradeInfo;
	@Column(name = "locked")
	public boolean locked;
	@Column(name = "mid")
	public String mid;
	@Column(name = "needPayPassword")
	public boolean needPayPassword;
	@Column(name = "needValidateWhileTk")
	public boolean needValidateWhileTk;
	@Column(name = "payPassword")
	public String payPassword;
	@Column(name = "qqId")
	public String qqId;
	@Column(name = "remainMoney")
	public String remainMoney;
	@Column(name = "remainScore")
	public String remainScore;
	@Column(name = "securityLevel")
	public String securityLevel;
	@Column(name = "userId")
	public String userId;
	@Column(name = "userName")
	public String userName;
	@Column(name = "userPwd")
	public String userPwd;
	@Column(name = "userLevelNew")
	public String userLevelNew;
	@Column(name = "userWay")
	public String userWay;
	@Column(name = "usernickName")
	public String usernickName;//昵称
	@Column(name = "validatedPhoneNo")
	public boolean validatedPhoneNo;
	@Column(name = "version")
	public int version;
	@Column(name = "userPic")
	public String userPic;
	@Column(name = "isComplete")
	public boolean isComplete;//安全认证
	
	public int get_id() {  
	    return _id;  
	}  
	  
	public void set_id(int _id) {  
	    this._id = _id;  
	} 
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public boolean isNeedPayPassword() {
		return needPayPassword;
	}
	public void setNeedPayPassword(boolean needPayPassword) {
		this.needPayPassword = needPayPassword;
	}
	public boolean isNeedValidateWhileTk() {
		return needValidateWhileTk;
	}
	public void setNeedValidateWhileTk(boolean needValidateWhileTk) {
		this.needValidateWhileTk = needValidateWhileTk;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getQqId() {
		return qqId;
	}
	public void setQqId(String qqId) {
		this.qqId = qqId;
	}
	public String getRemainMoney() {
		return remainMoney;
	}
	public void setRemainMoney(String remainMoney) {
		this.remainMoney = remainMoney;
	}
	public String getRemainScore() {
		return remainScore;
	}
	public void setRemainScore(String remainScore) {
		this.remainScore = remainScore;
	}
	public String getSecurityLevel() {
		return securityLevel;
	}
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserWay() {
		return userWay;
	}
	public void setUserWay(String userWay) {
		this.userWay = userWay;
	}
	public boolean isValidatedPhoneNo() {
		return validatedPhoneNo;
	}
	public void setValidatedPhoneNo(boolean validatedPhoneNo) {
		this.validatedPhoneNo = validatedPhoneNo;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getGradeInfo() {
		return gradeInfo;
	}
	public void setGradeInfo(String gradeInfo) {
		this.gradeInfo = gradeInfo;
	}
	public String getUsernickName() {
		return usernickName;
	}
	public void setUsernickName(String usernickName) {
		this.usernickName = usernickName;
	}

	public String getUserLevelNew() {
		return userLevelNew;
	}

	public void setUserLevelNew(String userLevelNew) {
		this.userLevelNew = userLevelNew;
	}
}
 