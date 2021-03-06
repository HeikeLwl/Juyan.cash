package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.jdbc.ResultObjectBuilder;
import jufeng.juyancash.syncdata.TaoCanTypeVo;

/**
 * Entity mapped to table "TAOCAN_TYPE_ENTITY".
 */
public class TaocanTypeEntity extends DishTypeModel{

    private Long id;
    /** Not-null value. */
    private String taocanTypeId;
    private String taocanTypeName;
    private String taocanTypeCode;
    private Integer isHasParent;
    private String parentId;
    private Integer isSaleParent;
    private String saleParentId;

    public TaocanTypeEntity() {
    }

    public TaocanTypeEntity(Long id) {
        this.id = id;
    }

    public TaocanTypeEntity(Long id, String taocanTypeId, String taocanTypeName, String taocanTypeCode, Integer isHasParent, String parentId, Integer isSaleParent, String saleParentId) {
        this.id = id;
        this.taocanTypeId = taocanTypeId;
        this.taocanTypeName = taocanTypeName;
        this.taocanTypeCode = taocanTypeCode;
        this.isHasParent = isHasParent;
        this.parentId = parentId;
        this.isSaleParent = isSaleParent;
        this.saleParentId = saleParentId;
        setDishTypeModelId(taocanTypeId);
        setDishTypeModelTypeName(taocanTypeName);
        setDishTypeModelHasParent(isHasParent);
        setDishTypeModelParentId(parentId);
        setDishTypeModelType(1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTaocanTypeId() {
        return taocanTypeId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTaocanTypeId(String taocanTypeId) {
        this.taocanTypeId = taocanTypeId;
    }

    public String getTaocanTypeName() {
        return taocanTypeName;
    }

    public void setTaocanTypeName(String taocanTypeName) {
        this.taocanTypeName = taocanTypeName;
    }

    public String getTaocanTypeCode() {
        return taocanTypeCode;
    }

    public void setTaocanTypeCode(String taocanTypeCode) {
        this.taocanTypeCode = taocanTypeCode;
    }

    public Integer getIsHasParent() {
        return isHasParent;
    }

    public void setIsHasParent(Integer isHasParent) {
        this.isHasParent = isHasParent;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsSaleParent() {
        return isSaleParent;
    }

    public void setIsSaleParent(Integer isSaleParent) {
        this.isSaleParent = isSaleParent;
    }

    public String getSaleParentId() {
        return saleParentId;
    }

    public void setSaleParentId(String saleParentId) {
        this.saleParentId = saleParentId;
    }

    public TaocanTypeEntity(TaoCanTypeVo taoCanTypeModel){
        this.taocanTypeId = taoCanTypeModel.getId();
        this.taocanTypeName = taoCanTypeModel.getName();
        this.isHasParent = taoCanTypeModel.getAddParent();
        this.parentId = taoCanTypeModel.getParentId();
        this.taocanTypeCode = taoCanTypeModel.getCode();
        this.isSaleParent = taoCanTypeModel.getSalesOther();
        this.saleParentId = taoCanTypeModel.getOtherType();
        Log.d("###", "套餐分类："+this.toString());
    }

    @Override
    public String toString() {
        return "TaocanTypeEntity{" +
                "id=" + id +
                ", taocanTypeId='" + taocanTypeId + '\'' +
                ", taocanTypeName='" + taocanTypeName + '\'' +
                ", taocanTypeCode='" + taocanTypeCode + '\'' +
                ", isHasParent=" + isHasParent +
                ", parentId='" + parentId + '\'' +
                ", isSaleParent=" + isSaleParent +
                ", saleParentId='" + saleParentId + '\'' +
                '}';
    }

    public static ResultObjectBuilder<TaocanTypeEntity> builder = new ResultObjectBuilder<TaocanTypeEntity>() {
        @Override
        public TaocanTypeEntity build(ResultSet rs) throws SQLException {
            TaocanTypeEntity taocanTypeEntity = new TaocanTypeEntity();
            taocanTypeEntity.setTaocanTypeId(rs.getString("c_id"));
            taocanTypeEntity.setTaocanTypeName(rs.getString("c_name"));
            taocanTypeEntity.setIsHasParent(rs.getInt("c_add_parent"));
            taocanTypeEntity.setParentId(rs.getString("c_parent"));
            taocanTypeEntity.setTaocanTypeCode(rs.getString("c_code"));
            taocanTypeEntity.setIsSaleParent(rs.getInt("c_sales_other"));
            taocanTypeEntity.setSaleParentId(rs.getString("c_other_type"));
            return taocanTypeEntity;
        }
    };
}
