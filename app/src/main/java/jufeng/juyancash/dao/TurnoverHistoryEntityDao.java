package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "TURNOVER_HISTORY_ENTITY".
*/
public class TurnoverHistoryEntityDao extends AbstractDao<TurnoverHistoryEntity, Long> {

    public static final String TABLENAME = "TURNOVER_HISTORY_ENTITY";

    /**
     * Properties of entity TurnoverHistoryEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TurnoverHistoryId = new Property(1, String.class, "turnoverHistoryId", false, "TURNOVER_HISTORY_ID");
        public final static Property StartTurnoverTime = new Property(2, String.class, "startTurnoverTime", false, "START_TURNOVER_TIME");
        public final static Property TurnoverStartTime = new Property(3, String.class, "turnoverStartTime", false, "TURNOVER_START_TIME");
        public final static Property TurnoverEndTime = new Property(4, String.class, "turnoverEndTime", false, "TURNOVER_END_TIME");
        public final static Property CashierName = new Property(5, String.class, "cashierName", false, "CASHIER_NAME");
        public final static Property TurnoverState = new Property(6, String.class, "turnoverState", false, "TURNOVER_STATE");
        public final static Property PayMentType = new Property(7, String.class, "payMentType", false, "PAY_MENT_TYPE");
        public final static Property AreaType = new Property(8, String.class, "areaType", false, "AREA_TYPE");
        public final static Property OrderTotalCount = new Property(9, String.class, "orderTotalCount", false, "ORDER_TOTAL_COUNT");
        public final static Property OrderedTotalCount = new Property(10, String.class, "orderedTotalCount", false, "ORDERED_TOTAL_COUNT");
        public final static Property UnOrderedTotalCount = new Property(11, String.class, "unOrderedTotalCount", false, "UN_ORDERED_TOTAL_COUNT");
        public final static Property OrderedTotalMoney = new Property(12, String.class, "orderedTotalMoney", false, "ORDERED_TOTAL_MONEY");
        public final static Property UnOrderedTotalMoney = new Property(13, String.class, "unOrderedTotalMoney", false, "UN_ORDERED_TOTAL_MONEY");
        public final static Property Payments = new Property(14, String.class, "payments", false, "PAYMENTS");
        public final static Property DishTypes = new Property(15, String.class, "dishTypes", false, "DISH_TYPES");
        public final static Property Money0 = new Property(16, String.class, "money0", false, "MONEY0");
        public final static Property Money1 = new Property(17, String.class, "money1", false, "MONEY1");
        public final static Property Money2 = new Property(18, String.class, "money2", false, "MONEY2");
        public final static Property Money3 = new Property(19, String.class, "money3", false, "MONEY3");
        public final static Property Money4 = new Property(20, String.class, "money4", false, "MONEY4");
        public final static Property Money5 = new Property(21, String.class, "money5", false, "MONEY5");
        public final static Property Money6 = new Property(22, String.class, "money6", false, "MONEY6");
        public final static Property OperatorName = new Property(23, String.class, "operatorName", false, "OPERATOR_NAME");
        public final static Property PrintTime = new Property(24, String.class, "printTime", false, "PRINT_TIME");
        public final static Property CreateTime = new Property(25, Long.class, "createTime", false, "CREATE_TIME");
    };


    public TurnoverHistoryEntityDao(DaoConfig config) {
        super(config);
    }
    
    public TurnoverHistoryEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TURNOVER_HISTORY_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"TURNOVER_HISTORY_ID\" TEXT UNIQUE ," + // 1: turnoverHistoryId
                "\"START_TURNOVER_TIME\" TEXT," + // 2: startTurnoverTime
                "\"TURNOVER_START_TIME\" TEXT," + // 3: turnoverStartTime
                "\"TURNOVER_END_TIME\" TEXT," + // 4: turnoverEndTime
                "\"CASHIER_NAME\" TEXT," + // 5: cashierName
                "\"TURNOVER_STATE\" TEXT," + // 6: turnoverState
                "\"PAY_MENT_TYPE\" TEXT," + // 7: payMentType
                "\"AREA_TYPE\" TEXT," + // 8: areaType
                "\"ORDER_TOTAL_COUNT\" TEXT," + // 9: orderTotalCount
                "\"ORDERED_TOTAL_COUNT\" TEXT," + // 10: orderedTotalCount
                "\"UN_ORDERED_TOTAL_COUNT\" TEXT," + // 11: unOrderedTotalCount
                "\"ORDERED_TOTAL_MONEY\" TEXT," + // 12: orderedTotalMoney
                "\"UN_ORDERED_TOTAL_MONEY\" TEXT," + // 13: unOrderedTotalMoney
                "\"PAYMENTS\" TEXT," + // 14: payments
                "\"DISH_TYPES\" TEXT," + // 15: dishTypes
                "\"MONEY0\" TEXT," + // 16: money0
                "\"MONEY1\" TEXT," + // 17: money1
                "\"MONEY2\" TEXT," + // 18: money2
                "\"MONEY3\" TEXT," + // 19: money3
                "\"MONEY4\" TEXT," + // 20: money4
                "\"MONEY5\" TEXT," + // 21: money5
                "\"MONEY6\" TEXT," + // 22: money6
                "\"OPERATOR_NAME\" TEXT," + // 23: operatorName
                "\"PRINT_TIME\" TEXT," + // 24: printTime
                "\"CREATE_TIME\" INTEGER);"); // 25: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TURNOVER_HISTORY_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TurnoverHistoryEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String turnoverHistoryId = entity.getTurnoverHistoryId();
        if (turnoverHistoryId != null) {
            stmt.bindString(2, turnoverHistoryId);
        }
 
        String startTurnoverTime = entity.getStartTurnoverTime();
        if (startTurnoverTime != null) {
            stmt.bindString(3, startTurnoverTime);
        }
 
        String turnoverStartTime = entity.getTurnoverStartTime();
        if (turnoverStartTime != null) {
            stmt.bindString(4, turnoverStartTime);
        }
 
        String turnoverEndTime = entity.getTurnoverEndTime();
        if (turnoverEndTime != null) {
            stmt.bindString(5, turnoverEndTime);
        }
 
        String cashierName = entity.getCashierName();
        if (cashierName != null) {
            stmt.bindString(6, cashierName);
        }
 
        String turnoverState = entity.getTurnoverState();
        if (turnoverState != null) {
            stmt.bindString(7, turnoverState);
        }
 
        String payMentType = entity.getPayMentType();
        if (payMentType != null) {
            stmt.bindString(8, payMentType);
        }
 
        String areaType = entity.getAreaType();
        if (areaType != null) {
            stmt.bindString(9, areaType);
        }
 
        String orderTotalCount = entity.getOrderTotalCount();
        if (orderTotalCount != null) {
            stmt.bindString(10, orderTotalCount);
        }
 
        String orderedTotalCount = entity.getOrderedTotalCount();
        if (orderedTotalCount != null) {
            stmt.bindString(11, orderedTotalCount);
        }
 
        String unOrderedTotalCount = entity.getUnOrderedTotalCount();
        if (unOrderedTotalCount != null) {
            stmt.bindString(12, unOrderedTotalCount);
        }
 
        String orderedTotalMoney = entity.getOrderedTotalMoney();
        if (orderedTotalMoney != null) {
            stmt.bindString(13, orderedTotalMoney);
        }
 
        String unOrderedTotalMoney = entity.getUnOrderedTotalMoney();
        if (unOrderedTotalMoney != null) {
            stmt.bindString(14, unOrderedTotalMoney);
        }
 
        String payments = entity.getPayments();
        if (payments != null) {
            stmt.bindString(15, payments);
        }
 
        String dishTypes = entity.getDishTypes();
        if (dishTypes != null) {
            stmt.bindString(16, dishTypes);
        }
 
        String money0 = entity.getMoney0();
        if (money0 != null) {
            stmt.bindString(17, money0);
        }
 
        String money1 = entity.getMoney1();
        if (money1 != null) {
            stmt.bindString(18, money1);
        }
 
        String money2 = entity.getMoney2();
        if (money2 != null) {
            stmt.bindString(19, money2);
        }
 
        String money3 = entity.getMoney3();
        if (money3 != null) {
            stmt.bindString(20, money3);
        }
 
        String money4 = entity.getMoney4();
        if (money4 != null) {
            stmt.bindString(21, money4);
        }
 
        String money5 = entity.getMoney5();
        if (money5 != null) {
            stmt.bindString(22, money5);
        }
 
        String money6 = entity.getMoney6();
        if (money6 != null) {
            stmt.bindString(23, money6);
        }
 
        String operatorName = entity.getOperatorName();
        if (operatorName != null) {
            stmt.bindString(24, operatorName);
        }
 
        String printTime = entity.getPrintTime();
        if (printTime != null) {
            stmt.bindString(25, printTime);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(26, createTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TurnoverHistoryEntity readEntity(Cursor cursor, int offset) {
        TurnoverHistoryEntity entity = new TurnoverHistoryEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // turnoverHistoryId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // startTurnoverTime
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // turnoverStartTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // turnoverEndTime
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // cashierName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // turnoverState
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // payMentType
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // areaType
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // orderTotalCount
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // orderedTotalCount
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // unOrderedTotalCount
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // orderedTotalMoney
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // unOrderedTotalMoney
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // payments
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // dishTypes
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // money0
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // money1
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // money2
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // money3
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // money4
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // money5
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // money6
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // operatorName
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // printTime
            cursor.isNull(offset + 25) ? null : cursor.getLong(offset + 25) // createTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TurnoverHistoryEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTurnoverHistoryId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStartTurnoverTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTurnoverStartTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTurnoverEndTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCashierName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTurnoverState(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPayMentType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAreaType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setOrderTotalCount(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setOrderedTotalCount(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setUnOrderedTotalCount(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setOrderedTotalMoney(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setUnOrderedTotalMoney(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setPayments(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setDishTypes(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setMoney0(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setMoney1(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setMoney2(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setMoney3(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setMoney4(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setMoney5(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setMoney6(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setOperatorName(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setPrintTime(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setCreateTime(cursor.isNull(offset + 25) ? null : cursor.getLong(offset + 25));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TurnoverHistoryEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TurnoverHistoryEntity entity) {
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