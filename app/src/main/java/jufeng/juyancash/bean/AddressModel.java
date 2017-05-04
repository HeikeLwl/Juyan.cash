package jufeng.juyancash.bean;

import java.util.Date;

public class AddressModel{
	private String partnerCode;//商家编号
	private String openId;//微信openid
	private String name;//姓名
	private String tel; //手机号码
	private int sex;//性别1-男,2-女
	private String address; //外卖地址
	private Date createTime;//创建时间

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "AddressModel{" +
				"partnerCode='" + partnerCode + '\'' +
				", openId='" + openId + '\'' +
				", name='" + name + '\'' +
				", tel='" + tel + '\'' +
				", sex=" + sex +
				", address='" + address + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
