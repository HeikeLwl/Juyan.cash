package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "VIP_CARD_ENTITY".
*/
public class VipCardEntityDao extends AbstractDao<VipCardEntity, Long> {

    public static final String TABLENAME = "VIP_CARD_ENTITY";

    /**
     * Properties of entity VipCardEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property VipCardId = new Property(1, String.class, "vipCardId", false, "VIP_CARD_ID");
        public final static Property VipCardName = new Property(2, String.class, "vipCardName", false, "VIP_CARD_NAME");
        public final static Property VipCardType = new Property(3, Integer.class, "vipCardType", false, "VIP_CARD_TYPE");
        public final static Property VipCardDiscountType = new Property(4, Integer.class, "vipCardDiscountType", false, "VIP_CARD_DISCOUNT_TYPE");
        public final static Property VipCardDiscountRate = new Property(5, Integer.class, "vipCardDiscountRate", false, "VIP_CARD_DISCOUNT_RATE");
    }


    public VipCardEntityDao(DaoConfig config) {
        super(config);
    }
    
    public VipCardEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"VIP_CARD_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"VIP_CARD_ID\" TEXT," + // 1: vipCardId
                "\"VIP_CARD_NAME\" TEXT," + // 2: vipCardName
                "\"VIP_CARD_TYPE\" INTEGER," + // 3: vipCardType
                "\"VIP_CARD_DISCOUNT_TYPE\" INTEGER," + // 4: vipCardDiscountType
                "\"VIP_CARD_DISCOUNT_RATE\" INTEGER);"); // 5: vipCardDiscountRate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"VIP_CARD_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, VipCardEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String vipCardId = entity.getVipCardId();
        if (vipCardId != null) {
            stmt.bindString(2, vipCardId);
        }
 
        String vipCardName = entity.getVipCardName();
        if (vipCardName != null) {
            stmt.bindString(3, vipCardName);
        }
 
        Integer vipCardType = entity.getVipCardType();
        if (vipCardType != null) {
            stmt.bindLong(4, vipCardType);
        }
 
        Integer vipCardDiscountType = entity.getVipCardDiscountType();
        if (vipCardDiscountType != null) {
            stmt.bindLong(5, vipCardDiscountType);
        }
 
        Integer vipCardDiscountRate = entity.getVipCardDiscountRate();
        if (vipCardDiscountRate != null) {
            stmt.bindLong(6, vipCardDiscountRate);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public VipCardEntity readEntity(Cursor cursor, int offset) {
        VipCardEntity entity = new VipCardEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // vipCardId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // vipCardName
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // vipCardType
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // vipCardDiscountType
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5) // vipCardDiscountRate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, VipCardEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setVipCardId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setVipCardName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setVipCardType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setVipCardDiscountType(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setVipCardDiscountRate(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(VipCardEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(VipCardEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}