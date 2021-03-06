package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.sql.ResultSet;
import java.sql.SQLException;

import jufeng.juyancash.jdbc.ResultObjectBuilder;
import jufeng.juyancash.syncdata.TaocanGroupGoodsVo;

/**
 * Entity mapped to table "TAOCAN_GROUP_DISH_ENTITY".
 */
public class TaocanGroupDishEntity {

    private Long id;
    /** Not-null value. */
    private String taocanGroupDishId;
    private String dishId;
    private String taocanGroupId;
    private String specifyId;
    private Float increasePrice;
    private Integer selectDishCount;

    public TaocanGroupDishEntity() {
    }

    public TaocanGroupDishEntity(Long id) {
        this.id = id;
    }

    public TaocanGroupDishEntity(Long id, String taocanGroupDishId, String dishId, String taocanGroupId, String specifyId, Float increasePrice, Integer selectDishCount) {
        this.id = id;
        this.taocanGroupDishId = taocanGroupDishId;
        this.dishId = dishId;
        this.taocanGroupId = taocanGroupId;
        this.specifyId = specifyId;
        this.increasePrice = increasePrice;
        this.selectDishCount = selectDishCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTaocanGroupDishId() {
        return taocanGroupDishId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTaocanGroupDishId(String taocanGroupDishId) {
        this.taocanGroupDishId = taocanGroupDishId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getTaocanGroupId() {
        return taocanGroupId;
    }

    public void setTaocanGroupId(String taocanGroupId) {
        this.taocanGroupId = taocanGroupId;
    }

    public String getSpecifyId() {
        return specifyId;
    }

    public void setSpecifyId(String specifyId) {
        this.specifyId = specifyId;
    }

    public Float getIncreasePrice() {
        return increasePrice;
    }

    public void setIncreasePrice(Float increasePrice) {
        this.increasePrice = increasePrice;
    }

    public Integer getSelectDishCount() {
        return selectDishCount;
    }

    public void setSelectDishCount(Integer selectDishCount) {
        this.selectDishCount = selectDishCount;
    }

    public TaocanGroupDishEntity(TaocanGroupGoodsVo taocanGroupGoodsModel){
        this.taocanGroupDishId = taocanGroupGoodsModel.getId();
        this.taocanGroupId = taocanGroupGoodsModel.getGroupId();
        this.dishId = taocanGroupGoodsModel.getGoodsId();
        this.specifyId = taocanGroupGoodsModel.getGoodsGuigeId();
        this.increasePrice = ((float)taocanGroupGoodsModel.getAddPrice())/100;
        this.selectDishCount = taocanGroupGoodsModel.getGoodsNum();
    }

    public static ResultObjectBuilder<TaocanGroupDishEntity> builder = new ResultObjectBuilder<TaocanGroupDishEntity>() {
        @Override
        public TaocanGroupDishEntity build(ResultSet rs) throws SQLException {
            TaocanGroupDishEntity taocanGroupDishEntity = new TaocanGroupDishEntity();
            taocanGroupDishEntity.setTaocanGroupDishId(rs.getString("c_id"));
            taocanGroupDishEntity.setTaocanGroupId(rs.getString("c_group"));
            taocanGroupDishEntity.setDishId(rs.getString("c_goods_id"));
            taocanGroupDishEntity.setSpecifyId(rs.getString("c_goods_guige_id"));
            taocanGroupDishEntity.setIncreasePrice((float) rs.getInt("c_add_price")/100);
            taocanGroupDishEntity.setSelectDishCount(rs.getInt("c_goods_num"));
            return taocanGroupDishEntity;
        }
    };

}
