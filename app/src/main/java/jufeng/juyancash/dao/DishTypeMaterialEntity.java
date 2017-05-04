package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.util.Log;

import jufeng.juyancash.syncdata.GoodsTypeMaterialVo;

/**
 * Entity mapped to table "DISH_TYPE_MATERIAL_ENTITY".
 */
public class DishTypeMaterialEntity {

    private Long id;
    private String dishTypeMaterialId;
    private String dishTypeId;
    private String materialId;

    public DishTypeMaterialEntity() {
    }

    public DishTypeMaterialEntity(Long id) {
        this.id = id;
    }

    public DishTypeMaterialEntity(Long id, String dishTypeMaterialId, String dishTypeId, String materialId) {
        this.id = id;
        this.dishTypeMaterialId = dishTypeMaterialId;
        this.dishTypeId = dishTypeId;
        this.materialId = materialId;
    }

    public DishTypeMaterialEntity(GoodsTypeMaterialVo vo){
        this.dishTypeMaterialId = vo.getId();
        this.dishTypeId = vo.getGoodsTypeId();
        this.materialId = vo.getMaterialId();
        Log.d("###", "分类对应的原料："+this.toString());
    }

    @Override
    public String toString() {
        return "DishTypeMaterialEntity{" +
                "id=" + id +
                ", dishTypeMaterialId='" + dishTypeMaterialId + '\'' +
                ", dishTypeId='" + dishTypeId + '\'' +
                ", materialId='" + materialId + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDishTypeMaterialId() {
        return dishTypeMaterialId;
    }

    public void setDishTypeMaterialId(String dishTypeMaterialId) {
        this.dishTypeMaterialId = dishTypeMaterialId;
    }

    public String getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(String dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

}
