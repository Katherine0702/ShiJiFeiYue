package com.cshen.tiyu.domain.login;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lingcheng on 15/10/1.
 */
@Table(name = "UserInfo") 
public class UserInfo {
	/**  
	 * isId = true 代表该字段是这张表的主键，类型也可以是String (赋值为false就不是主键了)，虽然数据库表可以设置多个主键，但是在实际操作的时候还是设置一个  
	 * autoGen = true 代表主键自增长，如果不是自增长，那就赋值false (这里主键是否自增长很关键，会影响到后面数据库的操作，下面再结合方法来解释)  
	 */  
	@Column(name = "_id",isId = true,autoGen = true)  
	public int _id;  
	@Column(name = "id")
	private Long id;
	@Column(name = "bankCard")
	private String bankCard;
	@Column(name = "bankName")
	private String bankName;
	@Column(name = "hasPayPassword")
	private boolean hasPayPassword;
	@Column(name = "idCard")
	private String idCard;
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "partBankCity")
	private String partBankCity;
	@Column(name = "partBankName")
	private String partBankName;
	@Column(name = "partBankProvince")
	private String partBankProvince;
	@Column(name = "realName")
	private String realName;
	@Column(name = "remainMoney")
	private String remainMoney;//可用余额
	@Column(name = "frozenMoney")
	private String frozenMoney;//不可提现余额
	@Column(name = "hongbaoNumber")
	private int hongbaoNumber;//不可提现余额
	

	public int get_id() {  
	    return _id;  
	}  
	  
	public void set_id(int _id) {  
	    this._id = _id;  
	} 
	public String getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(String frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	private String userPic;
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

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public boolean getHasPayPassword() {
		return hasPayPassword;
	}

	public void setHasPayPassword(boolean hasPayPassword) {
		this.hasPayPassword = hasPayPassword;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPartBankCity() {
		return partBankCity;
	}

	public void setPartBankCity(String partBankCity) {
		this.partBankCity = partBankCity;
	}

	public String getPartBankName() {
		return partBankName;
	}

	public void setPartBankName(String partBankName) {
		this.partBankName = partBankName;
	}

	public String getPartBankProvince() {
		return partBankProvince;
	}

	public void setPartBankProvince(String partBankProvince) {
		this.partBankProvince = partBankProvince;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRemainMoney() {
		return remainMoney;
	}

	public void setRemainMoney(String remainMoney) {
		this.remainMoney = remainMoney;
	}

	public int getHongbaoNumber() {
		return hongbaoNumber;
	}

	public void setHongbaoNumber(int hongbaoNumber) {
		this.hongbaoNumber = hongbaoNumber;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", bankCard=" + bankCard + ", bankName="
				+ bankName + ", hasPayPassword=" + hasPayPassword + ", idCard="
				+ idCard + ", mobile=" + mobile + ", partBankCity="
				+ partBankCity + ", partBankName=" + partBankName
				+ ", partBankProvince=" + partBankProvince + ", realName="
				+ realName + ", remainMoney=" + remainMoney + "]";
	}

	

}
