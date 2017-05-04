package jufeng.juyancash.syncdata;

/**
 * Created by Administrator102 on 2017/4/19.
 */

public class GoodsTypeMaterialVo {
    private String id;
    private String goodsTypeId;//商品类型ID
    private String materialId;//原料ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}
