package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.sql.ResultSet;
import java.sql.SQLException;

import jufeng.juyancash.jdbc.ResultObjectBuilder;
import jufeng.juyancash.syncdata.TaocanGroupVo;

/**
 * Entity mapped to table "TAOCAN_GROUP_ENTITY".
 */
public class TaocanGroupEntity {

    private Long id;
    /** Not-null value. */
    private String taocanGroupId;
    private String taocanId;
    private String taocanGroupName;
    private Integer selectMode;
    private Integer selectCount;

    public TaocanGroupEntity() {
    }

    public TaocanGroupEntity(Long id) {
        this.id = id;
    }

    public TaocanGroupEntity(Long id, String taocanGroupId, String taocanId, String taocanGroupName, Integer selectMode, Integer selectCount) {
        this.id = id;
        this.taocanGroupId = taocanGroupId;
        this.taocanId = taocanId;
        this.taocanGroupName = taocanGroupName;
        this.selectMode = selectMode;
        this.selectCount = selectCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTaocanGroupId() {
        return taocanGroupId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTaocanGroupId(String taocanGroupId) {
        this.taocanGroupId = taocanGroupId;
    }

    public String getTaocanId() {
        return taocanId;
    }

    public void setTaocanId(String taocanId) {
        this.taocanId = taocanId;
    }

    public String getTaocanGroupName() {
        return taocanGroupName;
    }

    public void setTaocanGroupName(String taocanGroupName) {
        this.taocanGroupName = taocanGroupName;
    }

    public Integer getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(Integer selectMode) {
        this.selectMode = selectMode;
    }

    public Integer getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(Integer selectCount) {
        this.selectCount = selectCount;
    }

    public TaocanGroupEntity(TaocanGroupVo taocanGroupModel){
        this.taocanGroupId = taocanGroupModel.getId();
        this.taocanGroupName = taocanGroupModel.getName();
        this.taocanId = taocanGroupModel.getTaocanId();
        this.selectMode = taocanGroupModel.getMode();
        this.selectCount = taocanGroupModel.getNum();
    }

    public static ResultObjectBuilder<TaocanGroupEntity> builder = new ResultObjectBuilder<TaocanGroupEntity>() {
        @Override
        public TaocanGroupEntity build(ResultSet rs) throws SQLException {
            TaocanGroupEntity taocanGroupEntity = new TaocanGroupEntity();
            taocanGroupEntity.setTaocanGroupId(rs.getString("c_id"));
            taocanGroupEntity.setTaocanGroupName(rs.getString("c_name"));
            taocanGroupEntity.setTaocanId(rs.getString("c_taocan"));
            taocanGroupEntity.setSelectMode(rs.getInt("c_mode"));
            taocanGroupEntity.setSelectCount(rs.getInt("c_num"));
            return taocanGroupEntity;
        }
    };

}
