package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "PRINT_KITCHEN_DISH_ENTITY".
*/
public class PrintKitchenDishEntityDao extends AbstractDao<PrintKitchenDishEntity, Long> {

    public static final String TABLENAME = "PRINT_KITCHEN_DISH_ENTITY";

    /**
     * Properties of entity PrintKitchenDishEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PrintKitchenId = new Property(1, String.class, "printKitchenId", false, "PRINT_KITCHEN_ID");
        public final static Property DishId = new Property(2, String.class, "dishId", false, "DISH_ID");
        public final static Property DishName = new Property(3, String.class, "dishName", false, "DISH_NAME");
    }


    public PrintKitchenDishEntityDao(DaoConfig config) {
        super(config);
    }
    
    public PrintKitchenDishEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PRINT_KITCHEN_DISH_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"PRINT_KITCHEN_ID\" TEXT NOT NULL ," + // 1: printKitchenId
                "\"DISH_ID\" TEXT," + // 2: dishId
                "\"DISH_NAME\" TEXT);"); // 3: dishName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PRINT_KITCHEN_DISH_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PrintKitchenDishEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getPrintKitchenId());
 
        String dishId = entity.getDishId();
        if (dishId != null) {
            stmt.bindString(3, dishId);
        }
 
        String dishName = entity.getDishName();
        if (dishName != null) {
            stmt.bindString(4, dishName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PrintKitchenDishEntity readEntity(Cursor cursor, int offset) {
        PrintKitchenDishEntity entity = new PrintKitchenDishEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // printKitchenId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dishId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // dishName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PrintKitchenDishEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPrintKitchenId(cursor.getString(offset + 1));
        entity.setDishId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDishName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PrintKitchenDishEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PrintKitchenDishEntity entity) {
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
