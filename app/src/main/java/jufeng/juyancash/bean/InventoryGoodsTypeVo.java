package jufeng.juyancash.bean;

import java.util.List;

public class InventoryGoodsTypeVo {

	private String typeName;
	
	private String typeId;
	
	private List<InventoryGoodsVo> inventory;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public List<InventoryGoodsVo> getInventory() {
		return inventory;
	}

	public void setInventory(List<InventoryGoodsVo> inventory) {
		this.inventory = inventory;
	}
	
}
