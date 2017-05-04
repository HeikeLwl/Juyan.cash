package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "SHOP_TIME_ENTITY".
*/
public class ShopTimeEntityDao extends AbstractDao<ShopTimeEntity, Long> {

    public static final String TABLENAME = "SHOP_TIME_ENTITY";

    /**
     * Properties of entity ShopTimeEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PartnerCode = new Property(1, String.class, "partnerCode", false, "PARTNER_CODE");
        public final static Property ShopDayType = new Property(2, Integer.class, "shopDayType", false, "SHOP_DAY_TYPE");
        public final static Property ShopEndTime = new Property(3, String.class, "shopEndTime", false, "SHOP_END_TIME");
    }


    public ShopTimeEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ShopTimeEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHOP_TIME_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"PARTNER_CODE\" TEXT," + // 1: partnerCode
                "\"SHOP_DAY_TYPE\" INTEGER," + // 2: shopDayType
                "\"SHOP_END_TIME\" TEXT);"); // 3: shopEndTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHOP_TIME_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ShopTimeEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String partnerCode = entity.getPartnerCode();
        if (partnerCode != null) {
            stmt.bindString(2, partnerCode);
        }
 
        Integer shopDayType = entity.getShopDayType();
        if (shopDayType != null) {
            stmt.bindLong(3, shopDayType);
        }
 
        String shopEndTime = entity.getShopEndTime();
        if (shopEndTime != null) {
            stmt.bindString(4, shopEndTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ShopTimeEntity readEntity(Cursor cursor, int offset) {
        ShopTimeEntity entity = new ShopTimeEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // partnerCode
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // shopDayType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // shopEndTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ShopTimeEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPartnerCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setShopDayType(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setShopEndTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ShopTimeEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ShopTimeEntity entity) {
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
