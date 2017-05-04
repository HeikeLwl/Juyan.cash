package jufeng.juyancash.bean;

public class BillHandleModel{
	private String id;
	private String partnerCode ;//商家编号
	private String orderId;//订单id
	private String billId;//挂账类型id
	private String billName;//挂账类型
	private String billPersonId;//挂账人id
	private String billPersonName;//挂账人名称
	private String sigerName;//签字人名称
	private int amount;//金额
	private String serialNo;//流水号
	private int state;//是否还款 0-未还款,1-已还款
	private String time;//挂账时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getBillPersonId() {
		return billPersonId;
	}

	public void setBillPersonId(String billPersonId) {
		this.billPersonId = billPersonId;
	}

	public String getBillPersonName() {
		return billPersonName;
	}

	public void setBillPersonName(String billPersonName) {
		this.billPersonName = billPersonName;
	}

	public String getSigerName() {
		return sigerName;
	}

	public void setSigerName(String sigerName) {
		this.sigerName = sigerName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
