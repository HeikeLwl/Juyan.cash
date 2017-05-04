package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "SHOP_PAYMENT_ENTITY".
*/
public class ShopPaymentEntityDao extends AbstractDao<ShopPaymentEntity, Long> {

    public static final String TABLENAME = "SHOP_PAYMENT_ENTITY";

    /**
     * Properties of entity ShopPaymentEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PaymentId = new Property(1, String.class, "paymentId", false, "PAYMENT_ID");
        public final static Property PartnerCode = new Property(2, String.class, "partnerCode", false, "PARTNER_CODE");
        public final static Property PaymentType = new Property(3, Integer.class, "paymentType", false, "PAYMENT_TYPE");
        public final static Property PaymentName = new Property(4, String.class, "paymentName", false, "PAYMENT_NAME");
        public final static Property IsSaleState = new Property(5, Integer.class, "isSaleState", false, "IS_SALE_STATE");
        public final static Property IsOpenCashBox = new Property(6, Integer.class, "isOpenCashBox", false, "IS_OPEN_CASH_BOX");
        public final static Property IsOpenScanGun = new Property(7, Integer.class, "isOpenScanGun", false, "IS_OPEN_SCAN_GUN");
    }


    public ShopPaymentEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ShopPaymentEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHOP_PAYMENT_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"PAYMENT_ID\" TEXT NOT NULL UNIQUE ," + // 1: paymentId
                "\"PARTNER_CODE\" TEXT," + // 2: partnerCode
                "\"PAYMENT_TYPE\" INTEGER," + // 3: paymentType
                "\"PAYMENT_NAME\" TEXT," + // 4: paymentName
                "\"IS_SALE_STATE\" INTEGER," + // 5: isSaleState
                "\"IS_OPEN_CASH_BOX\" INTEGER," + // 6: isOpenCashBox
                "\"IS_OPEN_SCAN_GUN\" INTEGER);"); // 7: isOpenScanGun
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHOP_PAYMENT_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ShopPaymentEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getPaymentId());
 
        String partnerCode = entity.getPartnerCode();
        if (partnerCode != null) {
            stmt.bindString(3, partnerCode);
        }
 
        Integer paymentType = entity.getPaymentType();
        if (paymentType != null) {
            stmt.bindLong(4, paymentType);
        }
 
        String paymentName = entity.getPaymentName();
        if (paymentName != null) {
            stmt.bindString(5, paymentName);
        }
 
        Integer isSaleState = entity.getIsSaleState();
        if (isSaleState != null) {
            stmt.bindLong(6, isSaleState);
        }
 
        Integer isOpenCashBox = entity.getIsOpenCashBox();
        if (isOpenCashBox != null) {
            stmt.bindLong(7, isOpenCashBox);
        }
 
        Integer isOpenScanGun = entity.getIsOpenScanGun();
        if (isOpenScanGun != null) {
            stmt.bindLong(8, isOpenScanGun);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ShopPaymentEntity readEntity(Cursor cursor, int offset) {
        ShopPaymentEntity entity = new ShopPaymentEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // paymentId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // partnerCode
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // paymentType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // paymentName
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // isSaleState
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // isOpenCashBox
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7) // isOpenScanGun
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ShopPaymentEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPaymentId(cursor.getString(offset + 1));
        entity.setPartnerCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPaymentType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setPaymentName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsSaleState(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setIsOpenCashBox(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setIsOpenScanGun(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ShopPaymentEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ShopPaymentEntity entity) {
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