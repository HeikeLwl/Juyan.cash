package jufeng.juyancash.bean;

import java.io.Serializable;


/**
 * 请求参数vo
 * @author wujf
 */
public  class ReqParamVo implements Serializable {
	private String orderId;
	private String partnerCode;//商家编号
	private String authCode;
    private String totalAmount;//订单金额
    private String subject;//订单标题
    private String type;//支付类型

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	
	
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
    
    
	
}
