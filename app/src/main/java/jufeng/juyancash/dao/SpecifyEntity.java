package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

import jufeng.juyancash.jdbc.ResultObjectBuilder;
import jufeng.juyancash.syncdata.GuigeVo;

/**
 * Entity mapped to table "SPECIFY_ENTITY".
 */
public class SpecifyEntity {

    private Long id;
    /** Not-null value. */
    private String specifyId;
    private String specifyName;
    private Float materialMultiple;
    private Float priceMultiple;

    public SpecifyEntity() {
    }

    public SpecifyEntity(Long id) {
        this.id = id;
    }

    public SpecifyEntity(Long id, String specifyId, String specifyName, Float materialMultiple, Float priceMultiple) {
        this.id = id;
        this.specifyId = specifyId;
        this.specifyName = specifyName;
        this.materialMultiple = materialMultiple;
        this.priceMultiple = priceMultiple;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getSpecifyId() {
        return specifyId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSpecifyId(String specifyId) {
        this.specifyId = specifyId;
    }

    public String getSpecifyName() {
        return specifyName;
    }

    public void setSpecifyName(String specifyName) {
        this.specifyName = specifyName;
    }

    public Float getMaterialMultiple() {
        return materialMultiple;
    }

    public void setMaterialMultiple(Float materialMultiple) {
        this.materialMultiple = materialMultiple;
    }

    public Float getPriceMultiple() {
        return priceMultiple;
    }

    public void setPriceMultiple(Float priceMultiple) {
        this.priceMultiple = priceMultiple;
    }

    @Override
    public String toString() {
        return "SpecifyEntity{" +
                "id=" + id +
                ", specifyId='" + specifyId + '\'' +
                ", specifyName='" + specifyName + '\'' +
                ", materialMultiple=" + materialMultiple +
                ", priceMultiple=" + priceMultiple +
                '}';
    }

    public SpecifyEntity(GuigeVo guige){
        this.specifyId = guige.getId();
        this.specifyName = guige.getName();
        this.materialMultiple = (float)guige.getMateriaMultiple();
        this.priceMultiple = (float)guige.getPriceMultiple();
    }

    public static ResultObjectBuilder<SpecifyEntity> builder = new ResultObjectBuilder<SpecifyEntity>() {
        @Override
        public SpecifyEntity build(ResultSet rs) throws SQLException {
            SpecifyEntity specifyEntity = new SpecifyEntity();
            specifyEntity.setSpecifyId(rs.getString("c_id"));
            specifyEntity.setSpecifyName(rs.getString("c_name"));
            specifyEntity.setMaterialMultiple(rs.getFloat("c_materia_multiple"));
            specifyEntity.setPriceMultiple(rs.getFloat("c_price_multiple"));
            Log.d("###", "规格："+specifyEntity.toString());
            return specifyEntity;
        }
    };

}
