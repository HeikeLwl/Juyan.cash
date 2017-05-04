package jufeng.juyancash.bean;


/**
 * 申请退款vo
 * @author wujf
 */
public  class SMZF004Vo  extends SMZFVo {
	private String reqMsgId;//请求流水号
	private String oriReqMsgId;//原支付交易流水号
    private String refundAmount;//退款金额
    private String refundReason;//退款原因
    private String operatorId;//商户操作员编号  
    private String storeId;//商户门店编号
    private String terminalId;//商户机具终端编号
    private String refundFee;//实际退款金额
    private String backFee;//可退金额
    private String refundTime;//退款支付时间
    private String settleDate;//对账日期
    private String buyerId;//买家编号
    private String isClearOrCancel;//清算撤销标识
    private String extend1;//备用域1
    private String extend2;//备用域2
    private String extend3;//备用域3

	@Override
	public String getReqMsgId() {
		return reqMsgId;
	}

	@Override
	public void setReqMsgId(String reqMsgId) {
		this.reqMsgId = reqMsgId;
	}

	public String getOriReqMsgId() {
		return oriReqMsgId;
	}
	public void setOriReqMsgId(String oriReqMsgId) {
		this.oriReqMsgId = oriReqMsgId;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
	public String getBackFee() {
		return backFee;
	}
	public void setBackFee(String backFee) {
		this.backFee = backFee;
	}
	public String getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	public String getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getIsClearOrCancel() {
		return isClearOrCancel;
	}
	public void setIsClearOrCancel(String isClearOrCancel) {
		this.isClearOrCancel = isClearOrCancel;
	}
	public String getExtend1() {
		return extend1;
	}
	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	public String getExtend2() {
		return extend2;
	}
	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}
	public String getExtend3() {
		return extend3;
	}
	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}
	
    
    

}
