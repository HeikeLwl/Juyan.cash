package jufeng.juyancash.bean;

import java.util.Date;

public class PreOrderModel{
	
	private String id;
	
	/** 客户姓名*/
	private String name;
	/** 客户性别*/
	private int sex;
	/** 客户电话*/
	private String mobile;
	/** 就餐时间*/
	private Date eatTime;
	/** 就餐人数*/
	private int eatNum;
	/** 特殊要求*/
	private String otherAsk;
	/** 订餐时间*/
	private Date createTime;
	/** 微信用户OPENID */
	private String openId;
	/** 商家编号 */
	private String partnerCode;
	/** 说明，** 收银可以改动 */
	private String remark;
	/** 状态 */
	private int status;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 桌号
	 */
	private String tableNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getEatTime() {
		return eatTime;
	}

	public void setEatTime(Date eatTime) {
		this.eatTime = eatTime;
	}

	public int getEatNum() {
		return eatNum;
	}

	public void setEatNum(int eatNum) {
		this.eatNum = eatNum;
	}

	public String getOtherAsk() {
		return otherAsk;
	}

	public void setOtherAsk(String otherAsk) {
		this.otherAsk = otherAsk;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}
}
