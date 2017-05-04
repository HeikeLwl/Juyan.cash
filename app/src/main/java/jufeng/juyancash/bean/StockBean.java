package jufeng.juyancash.bean;

import jufeng.juyancash.dao.SellCheckEntity;

/**
 * Created by Administrator102 on 2016/12/17.
 */

public class StockBean {
    private String goodsId;
    private double stock;

    public StockBean(SellCheckEntity sellCheckEntity){
        goodsId = sellCheckEntity.getDishId();
        double mstock = sellCheckEntity.getStock();
        stock = mstock;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }
}
