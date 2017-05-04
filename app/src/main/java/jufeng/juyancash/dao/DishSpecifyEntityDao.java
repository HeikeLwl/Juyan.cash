package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "DISH_SPECIFY_ENTITY".
*/
public class DishSpecifyEntityDao extends AbstractDao<DishSpecifyEntity, Long> {

    public static final String TABLENAME = "DISH_SPECIFY_ENTITY";

    /**
     * Properties of entity DishSpecifyEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DishSpecifyId = new Property(1, String.class, "dishSpecifyId", false, "DISH_SPECIFY_ID");
        public final static Property DishId = new Property(2, String.class, "dishId", false, "DISH_ID");
        public final static Property SpecifyId = new Property(3, String.class, "specifyId", false, "SPECIFY_ID");
        public final static Property DefaultPrice = new Property(4, Float.class, "defaultPrice", false, "DEFAULT_PRICE");
        public final static Property CustomPrice = new Property(5, Float.class, "customPrice", false, "CUSTOM_PRICE");
    }


    public DishSpecifyEntityDao(DaoConfig config) {
        super(config);
    }
    
    public DishSpecifyEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DISH_SPECIFY_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"DISH_SPECIFY_ID\" TEXT NOT NULL UNIQUE ," + // 1: dishSpecifyId
                "\"DISH_ID\" TEXT," + // 2: dishId
                "\"SPECIFY_ID\" TEXT," + // 3: specifyId
                "\"DEFAULT_PRICE\" REAL," + // 4: defaultPrice
                "\"CUSTOM_PRICE\" REAL);"); // 5: customPrice
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DISH_SPECIFY_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DishSpecifyEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getDishSpecifyId());
 
        String dishId = entity.getDishId();
        if (dishId != null) {
            stmt.bindString(3, dishId);
        }
 
        String specifyId = entity.getSpecifyId();
        if (specifyId != null) {
            stmt.bindString(4, specifyId);
        }
 
        Float defaultPrice = entity.getDefaultPrice();
        if (defaultPrice != null) {
            stmt.bindDouble(5, defaultPrice);
        }
 
        Float customPrice = entity.getCustomPrice();
        if (customPrice != null) {
            stmt.bindDouble(6, customPrice);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DishSpecifyEntity readEntity(Cursor cursor, int offset) {
        DishSpecifyEntity entity = new DishSpecifyEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // dishSpecifyId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dishId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // specifyId
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // defaultPrice
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5) // customPrice
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DishSpecifyEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDishSpecifyId(cursor.getString(offset + 1));
        entity.setDishId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSpecifyId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDefaultPrice(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setCustomPrice(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DishSpecifyEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DishSpecifyEntity entity) {
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
