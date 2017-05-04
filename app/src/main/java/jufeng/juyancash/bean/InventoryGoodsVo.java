package jufeng.juyancash.bean;

public class InventoryGoodsVo {

	/** 菜品类型 **/
	private String typeName;
	/** 商品ID **/
	private String goodId;
	/** 商品名称 */
	private String goodName;
	/** 库存ID **/
	private String id;
	/** 是否已经设置库存 */
	private int stockFlag;
	/** 库存 */
	private String stock;
	/** 总入库量 */
	private String totalIn;
	/** 上一次入库量 */
	private String preIn;
	/** 商品单位 */
	private String goodUnit;
	/** 商品单价 */
	private String unitPrice;
	/**
	 * 库存预警
	 */
	private int earlyWarning;
	
	
	
	public int getStockFlag() {
		return stockFlag;
	}

	public void setStockFlag(int stockFlag) {
		this.stockFlag = stockFlag;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getGoodUnit() {
		return goodUnit;
	}

	public void setGoodUnit(String goodUnit) {
		this.goodUnit = goodUnit;
	}

	public String getTotalIn() {
		return totalIn;
	}

	public void setTotalIn(String totalIn) {
		this.totalIn = totalIn;
	}

	public String getPreIn() {
		return preIn;
	}

	public void setPreIn(String preIn) {
		this.preIn = preIn;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getEarlyWarning() {
		return earlyWarning;
	}

	public void setEarlyWarning(int earlyWarning) {
		this.earlyWarning = earlyWarning;
	}
	
	
	
}
