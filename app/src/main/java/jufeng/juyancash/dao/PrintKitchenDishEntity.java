package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.sql.ResultSet;
import java.sql.SQLException;

import jufeng.juyancash.jdbc.ResultObjectBuilder;
import jufeng.juyancash.syncdata.KitchenGoodsVo;

/**
 * Entity mapped to table "PRINT_KITCHEN_DISH_ENTITY".
 */
public class PrintKitchenDishEntity {

    private Long id;
    /** Not-null value. */
    private String printKitchenId;
    private String dishId;
    private String dishName;

    public PrintKitchenDishEntity() {
    }

    public PrintKitchenDishEntity(Long id) {
        this.id = id;
    }

    public PrintKitchenDishEntity(Long id, String printKitchenId, String dishId, String dishName) {
        this.id = id;
        this.printKitchenId = printKitchenId;
        this.dishId = dishId;
        this.dishName = dishName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getPrintKitchenId() {
        return printKitchenId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPrintKitchenId(String printKitchenId) {
        this.printKitchenId = printKitchenId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public PrintKitchenDishEntity(KitchenGoodsVo kitchenGoodsModel){
        this.printKitchenId = kitchenGoodsModel.getKitchenId();
        this.dishId = kitchenGoodsModel.getGoodsId();
        this.dishName = kitchenGoodsModel.getGoodsName();
    }

    public static ResultObjectBuilder<PrintKitchenDishEntity> builder = new ResultObjectBuilder<PrintKitchenDishEntity>() {
        @Override
        public PrintKitchenDishEntity build(ResultSet rs) throws SQLException {
            PrintKitchenDishEntity printKitchenDishEntity = new PrintKitchenDishEntity();
            printKitchenDishEntity.setPrintKitchenId(rs.getString("c_kitchen"));
            printKitchenDishEntity.setDishId(rs.getString("c_goods_id"));
            printKitchenDishEntity.setDishName(rs.getString("c_goods_name"));
            return printKitchenDishEntity;
        }
    };

}
