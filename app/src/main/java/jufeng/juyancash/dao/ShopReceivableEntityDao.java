package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "SHOP_RECEIVABLE_ENTITY".
*/
public class ShopReceivableEntityDao extends AbstractDao<ShopReceivableEntity, Long> {

    public static final String TABLENAME = "SHOP_RECEIVABLE_ENTITY";

    /**
     * Properties of entity ShopReceivableEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PartnerCode = new Property(1, String.class, "partnerCode", false, "PARTNER_CODE");
        public final static Property AccountType = new Property(2, Integer.class, "accountType", false, "ACCOUNT_TYPE");
        public final static Property ShopBank = new Property(3, String.class, "shopBank", false, "SHOP_BANK");
        public final static Property ShopProvince = new Property(4, String.class, "shopProvince", false, "SHOP_PROVINCE");
        public final static Property ShopCity = new Property(5, String.class, "shopCity", false, "SHOP_CITY");
        public final static Property BankBranch = new Property(6, String.class, "bankBranch", false, "BANK_BRANCH");
        public final static Property AccountName = new Property(7, String.class, "accountName", false, "ACCOUNT_NAME");
        public final static Property BankAccount = new Property(8, String.class, "bankAccount", false, "BANK_ACCOUNT");
    }


    public ShopReceivableEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ShopReceivableEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHOP_RECEIVABLE_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"PARTNER_CODE\" TEXT," + // 1: partnerCode
                "\"ACCOUNT_TYPE\" INTEGER," + // 2: accountType
                "\"SHOP_BANK\" TEXT," + // 3: shopBank
                "\"SHOP_PROVINCE\" TEXT," + // 4: shopProvince
                "\"SHOP_CITY\" TEXT," + // 5: shopCity
                "\"BANK_BRANCH\" TEXT," + // 6: bankBranch
                "\"ACCOUNT_NAME\" TEXT," + // 7: accountName
                "\"BANK_ACCOUNT\" TEXT);"); // 8: bankAccount
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHOP_RECEIVABLE_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ShopReceivableEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String partnerCode = entity.getPartnerCode();
        if (partnerCode != null) {
            stmt.bindString(2, partnerCode);
        }
 
        Integer accountType = entity.getAccountType();
        if (accountType != null) {
            stmt.bindLong(3, accountType);
        }
 
        String shopBank = entity.getShopBank();
        if (shopBank != null) {
            stmt.bindString(4, shopBank);
        }
 
        String shopProvince = entity.getShopProvince();
        if (shopProvince != null) {
            stmt.bindString(5, shopProvince);
        }
 
        String shopCity = entity.getShopCity();
        if (shopCity != null) {
            stmt.bindString(6, shopCity);
        }
 
        String bankBranch = entity.getBankBranch();
        if (bankBranch != null) {
            stmt.bindString(7, bankBranch);
        }
 
        String accountName = entity.getAccountName();
        if (accountName != null) {
            stmt.bindString(8, accountName);
        }
 
        String bankAccount = entity.getBankAccount();
        if (bankAccount != null) {
            stmt.bindString(9, bankAccount);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ShopReceivableEntity readEntity(Cursor cursor, int offset) {
        ShopReceivableEntity entity = new ShopReceivableEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // partnerCode
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // accountType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // shopBank
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // shopProvince
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // shopCity
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // bankBranch
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // accountName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // bankAccount
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ShopReceivableEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPartnerCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAccountType(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setShopBank(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShopProvince(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setShopCity(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBankBranch(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAccountName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBankAccount(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ShopReceivableEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ShopReceivableEntity entity) {
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
