package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "KITCHEN_DISH_TYPE_ENTITY".
*/
public class KitchenDishTypeEntityDao extends AbstractDao<KitchenDishTypeEntity, Long> {

    public static final String TABLENAME = "KITCHEN_DISH_TYPE_ENTITY";

    /**
     * Properties of entity KitchenDishTypeEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DishTypeId = new Property(1, String.class, "dishTypeId", false, "DISH_TYPE_ID");
        public final static Property KichenId = new Property(2, String.class, "kichenId", false, "KICHEN_ID");
    }


    public KitchenDishTypeEntityDao(DaoConfig config) {
        super(config);
    }
    
    public KitchenDishTypeEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"KITCHEN_DISH_TYPE_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"DISH_TYPE_ID\" TEXT," + // 1: dishTypeId
                "\"KICHEN_ID\" TEXT);"); // 2: kichenId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"KITCHEN_DISH_TYPE_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, KitchenDishTypeEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String dishTypeId = entity.getDishTypeId();
        if (dishTypeId != null) {
            stmt.bindString(2, dishTypeId);
        }
 
        String kichenId = entity.getKichenId();
        if (kichenId != null) {
            stmt.bindString(3, kichenId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public KitchenDishTypeEntity readEntity(Cursor cursor, int offset) {
        KitchenDishTypeEntity entity = new KitchenDishTypeEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // dishTypeId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // kichenId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, KitchenDishTypeEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDishTypeId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setKichenId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(KitchenDishTypeEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(KitchenDishTypeEntity entity) {
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