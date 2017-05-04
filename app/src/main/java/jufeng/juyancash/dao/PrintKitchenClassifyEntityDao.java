package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "PRINT_KITCHEN_CLASSIFY_ENTITY".
*/
public class PrintKitchenClassifyEntityDao extends AbstractDao<PrintKitchenClassifyEntity, Long> {

    public static final String TABLENAME = "PRINT_KITCHEN_CLASSIFY_ENTITY";

    /**
     * Properties of entity PrintKitchenClassifyEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PrintKitchenId = new Property(1, String.class, "printKitchenId", false, "PRINT_KITCHEN_ID");
        public final static Property DishTypeId = new Property(2, String.class, "dishTypeId", false, "DISH_TYPE_ID");
        public final static Property DishTypeName = new Property(3, String.class, "dishTypeName", false, "DISH_TYPE_NAME");
    }


    public PrintKitchenClassifyEntityDao(DaoConfig config) {
        super(config);
    }
    
    public PrintKitchenClassifyEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PRINT_KITCHEN_CLASSIFY_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"PRINT_KITCHEN_ID\" TEXT NOT NULL ," + // 1: printKitchenId
                "\"DISH_TYPE_ID\" TEXT," + // 2: dishTypeId
                "\"DISH_TYPE_NAME\" TEXT);"); // 3: dishTypeName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PRINT_KITCHEN_CLASSIFY_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PrintKitchenClassifyEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getPrintKitchenId());
 
        String dishTypeId = entity.getDishTypeId();
        if (dishTypeId != null) {
            stmt.bindString(3, dishTypeId);
        }
 
        String dishTypeName = entity.getDishTypeName();
        if (dishTypeName != null) {
            stmt.bindString(4, dishTypeName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PrintKitchenClassifyEntity readEntity(Cursor cursor, int offset) {
        PrintKitchenClassifyEntity entity = new PrintKitchenClassifyEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // printKitchenId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dishTypeId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // dishTypeName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PrintKitchenClassifyEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPrintKitchenId(cursor.getString(offset + 1));
        entity.setDishTypeId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDishTypeName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PrintKitchenClassifyEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PrintKitchenClassifyEntity entity) {
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