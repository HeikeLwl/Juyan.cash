package jufeng.juyancash.syncdata;

import java.util.Date;

/**
 * 
 * @author rico
 *
 */
public class GoodsInventoryVo {

	private String id;// char(36) NOT NULL
	private String partnerCode;// varchar(20) NULL商家编号
	private String goodId;// char(36) NULL商品ID
	private int unitPrice;// double NULL商品单价
	private double stock;// int(11) NULL库存
	private int totalIn;// int(11) NULL总入库量
	private int preIn;// int(11) NULL上次入库量
	private Date createTime;// datetime NULL第一次入库时间
	private Date updateTime;// datetime NULL上次入库时间
	private int property;// int(11) NULL

	private int earlyWarning;

	public int getEarlyWarning() {
		return earlyWarning;
	}

	public void setEarlyWarning(int earlyWarning) {
		this.earlyWarning = earlyWarning;
	}

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

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public int getTotalIn() {
		return totalIn;
	}

	public void setTotalIn(int totalIn) {
		this.totalIn = totalIn;
	}

	public int getPreIn() {
		return preIn;
	}

	public void setPreIn(int preIn) {
		this.preIn = preIn;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

}
