package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class JuyanDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "juyan.cash");
        addAreaEntity(schema);//区域
        addArrangeEntity(schema);//排号
        addDishEntity(schema);//商品
        addDishTypeEntity(schema);//商品分类
        addEmployeeEntity(schema);
        addKichenDishEntity(schema);
        addKichenPrintEntity(schema);
        addKitchenDishTypeEntity(schema);
        addKitchenDisPrintEntity(schema);
        addOrderDishEntity(schema);
        addOrderEntity(schema);
        addPayModeEntity(schema);
        addPermissionEntity(schema);
        addPracticeEntity(schema);
        addDishPracticeEntity(schema);
        addRankEntity(schema);
        addRankPermissionEntity(schema);
        addRoomEntity(schema);
        addScheduleEntity(schema);
        addSellCheckEntity(schema);
        addTableEntity(schema);
        addTaocanEntity(schema);
        addTaocanGroupDishEntity(schema);
        addTaocanGroupEntity(schema);
        addTaocanTypeEntity(schema);
        addSpecifyEntity(schema);
        addDishSpecifyEntity(schema);
        addTakeOutOrderEntity(schema);
        addUnitEntity(schema);
        addCashierDisplayEntity(schema);
        addSpecialEntity(schema);
        addMantissaEntity(schema);
        addSurplusEntity(schema);
        addGrouponEntity(schema);
        addGrouponTaocanEntity(schema);
        addDiscountEntity(schema);
        addDishTypeDiscountEntity(schema);
        addPrintCashierEntity(schema);
        addCashierClassifyEntity(schema);
        addCashierDishEntity(schema);
        addPrintKitchenEntity(schema);
        addPrintKitchenClassifyEntity(schema);
        addPrintKitchenDishEntity(schema);
        addStandbyPrinterEntity(schema);
        addPrintRemarkEntity(schema);
        addBasicsPartnerEntity(schema);
        addShopMealsEntity(schema);
        addShopPaymentEntity(schema);
        addShopReceivableEntity(schema);
        addShopTimeEntity(schema);
        addSystemMessageEntity(schema);
        addWXMessageEntity(schema);
        addStoreMessageEntity(schema);
        addDiscountHistoryEntity(schema);
        addSomeDiscountGoodsEntity(schema);
        addOrderTaocanGroupDishEntity(schema);
        addPrinterFailedHistoryEntity(schema);
        addJpushMessageEntity(schema);
        addBillAccountEntity(schema);
        addBillAccountPersonEntity(schema);
        addBillAccountSignEntity(schema);
        addBillAccountHistoryEntity(schema);
        addUploadDataEntity(schema);
        addVipCardEntity(schema);
        addWxOrderMessageEntity(schema);
        addPrintResultEntity(schema);
        addTurnoverHistoryEntity(schema);
        addTableCodeEntity(schema);
        addSendPersonEntity(schema);
        addMaterialEntity(schema);
        addDishTypeMaterialEntity(schema);
        addDishSelectedMaterialEntity(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    /**
     * 数据库对应表实体
     * @param schema
     */

    //区域管理实体
    private static void addAreaEntity(Schema schema){
        Entity note = schema.addEntity("AreaEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("areaId").notNull().unique();
        note.addStringProperty("areaName").notNull();
    }

    //桌位实体
    private static void addTableEntity(Schema schema){
        Entity note = schema.addEntity("TableEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("tableId").notNull().unique();
        note.addStringProperty("areaId");
        note.addStringProperty("tableName").notNull();
        note.addStringProperty("tableCode");
        note.addIntProperty("tableType");//三种类型，0：散座；1：包厢；2：卡座
        note.addIntProperty("tableSeat");
        note.addIntProperty("tableStatus");//四种状态，0：空闲；1：使用中；2：已预定；3：已结账
        note.addIntProperty("isLock");//0：未锁定；1：锁定
    }

    //账单实体
    private static void addOrderEntity(Schema schema){
        Entity note = schema.addEntity("OrderEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("orderId").unique().notNull();//账单id
        note.addStringProperty("serialNumber");//流水号=当前日期+四位数的单号，下单时才会产生流水号
        note.addIntProperty("orderGuests");//就餐人数
        note.addIntProperty("isLimited");//1:表示限时；0:表示不限时
        note.addIntProperty("limitedTime");//单位是分钟
        note.addIntProperty("remindTime");//限时用餐提醒时间
        note.addLongProperty("openTime");//开单时间
        note.addLongProperty("closeTime");//结账时间
        note.addIntProperty("closeMoney");//结账金额，也就是应收金额
        note.addStringProperty("tableId");//桌位ID
        note.addStringProperty("areaId");//区域id
        note.addIntProperty("orderStatus");//账单状态，0：未结账；1：已结账；2：预定单
        note.addIntProperty("orderNumber");//单号
        note.addStringProperty("cashierId");//收银员Id
        note.addStringProperty("waiterId");//服务员Id
        note.addIntProperty("treatmentMoney");//系统抹零金额
        note.addIntProperty("selfTreatMoney");//收银员抹零金额
        note.addIntProperty("mantissaMoney");//不吉利尾数金额
        note.addIntProperty("presentMoney");//赠菜金额
        note.addIntProperty("totalMoney");//账单总金额 = 优惠总金额+结账金额
        note.addIntProperty("invoiceMoney");//发票金额
        note.addIntProperty("vipDiscountMoney");//会员卡优惠金额
        note.addIntProperty("couponDiscountMoney");//优惠券优惠金额
        note.addIntProperty("discountMoney");//打折金额
        note.addIntProperty("discountTotalMoney");//优惠总金额 = 打折金额（或优惠券+会员卡优惠金额）+赠菜金额+不吉利尾数金额+手动抹零金额+系统抹零金额
        note.addIntProperty("isReturnOrder");//是否是反结账
        note.addStringProperty("returnOrderReason");//反结账原因
        note.addIntProperty("isShift");//是否有交接班，0：未交接；1：已交接
        note.addIntProperty("orderType");//账单类型，0：正常开单；1：外卖单
        note.addIntProperty("isUpload");//是否需要同步
        note.addIntProperty("isJoinedTable");//是否有合台
        note.addIntProperty("isJoinedOrder");//是否有并单
        note.addStringProperty("joinedTableId");//合台的桌位id
        note.addStringProperty("joinedOrderId");//并单的id
        note.addIntProperty("isVip");//是否使用会员卡打折
        note.addStringProperty("vipNo");//会员卡卡号
        note.addIntProperty("vipType");//会员卡类型
        note.addIntProperty("vipBalance");//会员卡余额
        note.addIntProperty("isCoupon");//是否使用优惠券
        note.addStringProperty("userCouponId");//用户领取优惠券的id
        note.addStringProperty("couponId");//优惠券id
        note.addIntProperty("couponType");//优惠券类型 0：满减；1：代金；2：折扣券
        note.addIntProperty("couponFaceValue");//满减券中的减免金额；代金券中的面值；折扣券中的折扣率
        note.addIntProperty("couponCondition");//使用条件：即应付金额满足大于等于该值时才可以使用
        note.addIntProperty("isCouponWithVip");//优惠券是否可与会员卡同时使用
        note.addIntProperty("isCouponDiscountAll");//是有对不允许打折商品也打折
        note.addStringProperty("couponVipno");//优惠券对应的会员卡卡号
        note.addIntProperty("isSync");//0或者null表示未上传；1表示已经上传
        note.addIntProperty("storeVersion");//0表示中餐版；1表示快餐版
        note.addIntProperty("dispacherType");//配送方式
        note.addStringProperty("dispacherName");//配送员名称
        note.addStringProperty("dispacherTel");//配送员电话
        note.addStringProperty("dispacherId");//配送员ID
        note.addIntProperty("dispacherTc");//配送员提成金额
        note.addIntProperty("dispacherExtralMoney");//众包配送增加的小费
    }

    //账单对应的商品实体
    private static void addOrderDishEntity(Schema schema){
        Entity note = schema.addEntity("OrderDishEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("orderDishId").notNull().unique();//点菜id
        note.addStringProperty("orderId");//账单id
        note.addStringProperty("dishId");//菜品id
        note.addStringProperty("dishName");//菜品名称
        note.addDoubleProperty("dishCount");//菜品数量
        note.addStringProperty("specifyId");//规格id
        note.addStringProperty("dishSpecify");//商品规格名称
        note.addStringProperty("practiceId");//做法id
        note.addStringProperty("dishPractice");//商品做法名称
        note.addStringProperty("dishNote");//商品备注
        note.addIntProperty("isOrdered");//是否已下单，true:已下单；false:未下单
        note.addLongProperty("orderedTime");//下单时间
        note.addFloatProperty("dishPrice");//商品价格
        note.addIntProperty("type");//商品类型，0：普通商品；1：套餐
        note.addIntProperty("isAbleDiscount");//是否允许打折
        note.addStringProperty("dishTypeId");//商品分类id
        note.addStringProperty("dishTypeName");//商品分类名称
        note.addIntProperty("isFromWX");//是否来自微信点餐
        note.addIntProperty("isPresent");//是否允许赠菜
        note.addIntProperty("isRetreat");//是否需要退菜权限
        note.addIntProperty("isPrint");//是否已经打印
    }

    //商品实体
    private static void addDishEntity(Schema schema){
        Entity note = schema.addEntity("DishEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("dishId").unique().notNull();//商品id
        note.addIntProperty("isShelved");//是否已上架，true:已上架；false:未上架
        note.addStringProperty("dishTypeId");//商品分类id
        note.addStringProperty("dishName");//商品名称
        note.addStringProperty("dishCode");//商品拼音
        note.addFloatProperty("dishPrice");//商品单价
        note.addStringProperty("checkOutUnit");//结账单位
        note.addStringProperty("orderUnit");//点菜单位
        note.addIntProperty("isSameUnit");//结账单位与点菜单位是否相同，true:相同；false:不相同;
        note.addIntProperty("isAbleDiscount");//是否允许打折
        note.addIntProperty("commissionType");//销售提成类型，0：不提成；1：按比例提成；2：按固定金额提成
        note.addFloatProperty("commissionValue");//销售提成值
        note.addIntProperty("serviceChargeType");//收取服务费类型，0：不收取；1：固定费用；2：商品价格百分比
        note.addFloatProperty("serviceChargeValue");//服务费值
        note.addIntProperty("isAbleChangePrice");//是否允许收银员在收银时修改单价
        note.addIntProperty("isAbleRetreat");//退菜时是否需要权限验证
        note.addIntProperty("isAblePresent");//是否可作为赠菜
        note.addIntProperty("isOnline");//
        note.addStringProperty("dishCode1");//商品编码
    }

    //商品分类实体
    private static void addDishTypeEntity(Schema schema){
        Entity note = schema.addEntity("DishTypeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("dishTypeId").unique().notNull();//商品分类id
        note.addStringProperty("dishTypeName");//商品分类名称
        note.addIntProperty("isHasParent");//是否有上级分类
        note.addStringProperty("parentId");//上级分类的id
        note.addStringProperty("dishTypeCode");//分类编码
        note.addIntProperty("isSaleParent");//是否将销售额归属到其他分类
        note.addStringProperty("saleParentId");//销售额归属分类的id
        note.addIntProperty("commissionType");//销售提成类型，0：不提成；1：按比例提成；2：按固定金额提成
        note.addFloatProperty("commissionValue");//销售提成值
    }

    //商品对应的单位库实体
    private static void addUnitEntity(Schema schema){
        //暂时没有必要
    }

    //做法库实体
    private static void addPracticeEntity(Schema schema){
        Entity note = schema.addEntity("PracticeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("practiceId").unique().notNull();//做法id
        note.addStringProperty("practiceName");//做法名称
    }

    //商品对应的做法库实体
    private static void addDishPracticeEntity(Schema schema){
        Entity note = schema.addEntity("DishPracticeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("dishPracticeId").unique().notNull();//商品做法id
        note.addStringProperty("practiceId");//做法id
        note.addStringProperty("dishId");//商品id
        note.addIntProperty("increaseMode");//加价模式，0：不加价；1：一次性加价；2：每购买单位加价；3：每结账单位加价
        note.addFloatProperty("increaseValue");//加价的值
    }

    //规格库实体
    private static void addSpecifyEntity(Schema schema){
        Entity note = schema.addEntity("SpecifyEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("specifyId").unique().notNull();//规格id
        note.addStringProperty("specifyName");//规格名称
        note.addFloatProperty("materialMultiple");//用料倍数
        note.addFloatProperty("priceMultiple");//价格倍数
    }

    //商品规格库实体
    private static void addDishSpecifyEntity(Schema schema){
        Entity note = schema.addEntity("DishSpecifyEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("dishSpecifyId").unique().notNull();//商品规格id
        note.addStringProperty("dishId");//商品id
        note.addStringProperty("specifyId");//规格id
        note.addFloatProperty("defaultPrice");//默认价格
        note.addFloatProperty("customPrice");//自定义价格
    }

    //套餐分类实体
    private static void addTaocanTypeEntity(Schema schema){
        Entity note = schema.addEntity("TaocanTypeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("taocanTypeId").notNull().unique();
        note.addStringProperty("taocanTypeName");
        note.addStringProperty("taocanTypeCode");
        note.addIntProperty("isHasParent");
        note.addStringProperty("parentId");
        note.addIntProperty("isSaleParent");//是否将销售额归属到其他分类
        note.addStringProperty("saleParentId");//销售额归属套餐分类的id
    }

    //套餐分组实体
    private static void addTaocanGroupEntity(Schema schema){
        Entity note = schema.addEntity("TaocanGroupEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("taocanGroupId").notNull().unique();
        note.addStringProperty("taocanId");
        note.addStringProperty("taocanGroupName");
        note.addIntProperty("selectMode");//点菜方式
        note.addIntProperty("selectCount");//允许点菜的数量
    }

    //套餐实体
    private static void addTaocanEntity(Schema schema){
        Entity note = schema.addEntity("TaocanEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("taocanId").notNull().unique();
        note.addStringProperty("taocanTypeId");
        note.addStringProperty("taocanName");
        note.addStringProperty("taocanCode");
        note.addFloatProperty("taocanPrice");
        note.addStringProperty("unitName");
        note.addIntProperty("isAbleDiscount");
        note.addIntProperty("isAbleRetreat");
        note.addIntProperty("isOnline");
        note.addStringProperty("taocanCode1");
    }

    //套餐分组内商品
    private static void addTaocanGroupDishEntity(Schema schema){
        Entity note = schema.addEntity("TaocanGroupDishEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("taocanGroupDishId").unique().notNull();
        note.addStringProperty("dishId");
        note.addStringProperty("taocanGroupId");
        note.addStringProperty("specifyId");
        note.addFloatProperty("increasePrice");
        note.addIntProperty("selectDishCount");
    }

    //厨打方案实体
    private static void addKichenPrintEntity(Schema schema){
        Entity note = schema.addEntity("KichenPrintEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("kichenId").unique().notNull();//厨打方案id
        note.addIntProperty("printerType");//打印机类型,0:爱普生T88,T81,T82(推荐)；2：佳博U80250；3：北洋热敏；4：爱普生U220；5：爱普生打印服务器+针式打印机；6：其他
        note.addStringProperty("printerName");//打印机名称
        note.addStringProperty("printerIp");//打印机IP
        note.addIntProperty("paperWidth");//打印纸宽度，0:58mm;1:76mm;2:80mm;
        note.addIntProperty("printChars");//打印字符数，0：32字符；1：33字符；2：38字符；3：40字符；4：42字符；5：48字符；6：64字符
        note.addIntProperty("printNumber");//打印份数
        note.addIntProperty("isOneCut");//是否一菜一切
        note.addIntProperty("isTotalCut");//是否同时打印一份总单
        note.addIntProperty("connectStatus");//打印机连接状态
    }

    //厨打对应的商品实体
    private static void addKichenDishEntity(Schema schema){
        Entity note = schema.addEntity("KichenDishEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("dishId");//商品id
        note.addStringProperty("kichenId");//厨打方案id
    }

    //厨打方案对应的分类实体
    private static void addKitchenDishTypeEntity(Schema schema){
        Entity note = schema.addEntity("KitchenDishTypeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("dishTypeId");//商品id
        note.addStringProperty("kichenId");//厨打方案id
    }

    //厨打单据实体
    private static void addKitchenDisPrintEntity(Schema schema){
        Entity note = schema.addEntity("KitchenDisPrintEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addLongProperty("printTime");//打印时间
        note.addStringProperty("orderId");//账单id
        note.addStringProperty("dishId");//商品id
        note.addStringProperty("kitchenId");//厨打方案id
    }

    //外卖记录实体
    private static void addTakeOutOrderEntity(Schema schema){
        Entity note = schema.addEntity("TakeOutOrderEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("takeoutId").notNull().unique();//外卖记录id
        note.addStringProperty("orderId");//账单id
        note.addStringProperty("guestName");//客户姓名
        note.addStringProperty("guestPhone");//客户手机号
        note.addStringProperty("takeoutAddress");//送餐地址
        note.addLongProperty("takeoutTime");//送餐时间
        note.addStringProperty("takeoutMark");//备注
        note.addIntProperty("boxFee");//打包费
        note.addIntProperty("dispatchFee");//配送费
        note.addStringProperty("otherOrderId");//平台orderId
        note.addIntProperty("takeoutFrom");//来源，0：微信餐厅外卖；1：美团外卖
        note.addIntProperty("takeoutStatus");//状态，0：待审核；1：已下单；2：已取消；3：已完成
    }

    //送餐记录实体
    private static void addRoomEntity(Schema schema){
        Entity note = schema.addEntity("RoomEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("roomId").notNull().unique();//送餐记录id
        note.addStringProperty("takeoutId");//外卖id
        note.addStringProperty("employeeId");//员工id
        note.addLongProperty("roomStartTime");//送餐开始时间
        note.addLongProperty("roomEndTime");//送餐结束时间
    }

    //员工实体
    private static void addEmployeeEntity(Schema schema){
        Entity note = schema.addEntity("EmployeeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("employeeId").notNull().unique();//员工id
        note.addStringProperty("rankId");//职级id
        note.addStringProperty("employeeName");//员工姓名
        note.addStringProperty("employeePhone");//员工手机号
        note.addStringProperty("loginName");//登录用户名
        note.addStringProperty("loginPsd");//登录密码
        note.addIntProperty("employeeSex");//员工性别
        note.addStringProperty("employeeHead");//员工头像
        note.addIntProperty("isSellCard");//是否是售卡人
        note.addIntProperty("allowDiscountBargain");//允许对可打折的商品进行打折
        note.addIntProperty("bargainMinPayment");//最低打折额度
        note.addIntProperty("allowDiscountNotBargain");//允许对不可打折的商品进行打折
        note.addIntProperty("notBargainMinPayment");//最低打折额度
        note.addIntProperty("isLimitedPrintCount");//财务联打印次数限制
        note.addIntProperty("maxPrintCount");//最多打印次数
        note.addIntProperty("isLimitedResetAmount");//是否限制去零额度
        note.addFloatProperty("maxResetAmount");//最大去零额度
        note.addIntProperty("authCashier");//是否有收银权限
        note.addIntProperty("authRetreat");//是否有退菜权限
        note.addIntProperty("authPresent");//是否有赠菜权限
        note.addIntProperty("authReturnOrder");//是否有反结账权限
        note.addIntProperty("authClearPrintHistory");//是否有清空厨打记录权限
        note.addIntProperty("authBillAccount");//是否有挂账权限
        note.addIntProperty("isUseIDCard");//是否使用ID卡登录
        note.addStringProperty("IDCardNumber");//ID卡号
        note.addIntProperty("isReturnSomeOrder");//是否有反结账当日未交接订单的权限
        note.addIntProperty("isRetrurnAllOrder");//是否有反结账任意订单的权限
        note.addIntProperty("isBindVip");//是否有绑定会员的权限
    }

    //职级实体
    private static void addRankEntity(Schema schema){
        Entity note = schema.addEntity("RankEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("rankId").notNull().unique();//职级id
        note.addStringProperty("rankName");//职级名称
    }

    //权限实体
    private static void addPermissionEntity(Schema schema){
        Entity note = schema.addEntity("PermissionEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("permissionId").notNull().unique();//权限id
        note.addStringProperty("permissionName");//权限名称
    }

    //职级对应权限实体
    private static void addRankPermissionEntity(Schema schema){
        Entity note = schema.addEntity("RankPermissionEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("rankId");
        note.addStringProperty("permissionId");
    }

    //排号记录实体
    private static void addArrangeEntity(Schema schema){
        Entity note = schema.addEntity("ArrangeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("arrangeId").notNull().unique();//排队记录id
        note.addIntProperty("remainCount");//等待人数
        note.addStringProperty("tel");//用户id
        note.addStringProperty("arrangeNumber");//排队单号
        note.addLongProperty("signTime");//取号时间
        note.addIntProperty("mealPeople");//就餐人数
        note.addIntProperty("arrangeStatus");//排号状态，0：未完成；1：已完成
    }

    //预定记录实体
    private static void addScheduleEntity(Schema schema){
        Entity note = schema.addEntity("ScheduleEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("scheduleId").notNull().unique();//预定id
        note.addStringProperty("orderId");//账单id
        note.addStringProperty("guestName");//客户姓名
        note.addStringProperty("guestPhone");//客户手机号
        note.addStringProperty("tableId");//预定桌位id
        note.addLongProperty("mealTime");//就餐时间
        note.addIntProperty("mealPeople");//就餐人数
        note.addIntProperty("isOrdered");//是否点餐
        note.addIntProperty("scheduleFrom");//预定来源，0：电话预定；1：微信预定
        note.addStringProperty("scheduleMark");//预定备注
        note.addIntProperty("scheduleStatus");//预定状态，0：未到店；1：已到店
    }

    //沽清
    private static void addSellCheckEntity(Schema schema){
        Entity note = schema.addEntity("SellCheckEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("sellCheckId").notNull().unique();//商品沽清id
        note.addIntProperty("isSellOut");//是否已售完
        note.addDoubleProperty("stock");//库存
        note.addStringProperty("unitName");//单位
        note.addStringProperty("dishId");//菜品id
        note.addIntProperty("type");//是否是套餐：0，非套餐；1，套餐
        note.addIntProperty("earlyWarning");//库存预警
    }

    //客显设置
    private static void addCashierDisplayEntity(Schema schema){
        Entity note = schema.addEntity("CashierDisplayEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addIntProperty("isOrderDisplay");//点菜时是否显示
        note.addIntProperty("isCheckoutDisplay");//结账时是否显示
        note.addStringProperty("displayImageUrl");//宣传图片
    }

    //尾数处理
    private static void addMantissaEntity(Schema schema){
        Entity note = schema.addEntity("MantissaEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("mantissaId").notNull().unique();//尾数处理方式的id
        note.addIntProperty("mantissaValue");//尾数
        note.addIntProperty("reduceValue");//扣减额
    }

    //零头处理
    private static void addSurplusEntity(Schema schema){
        Entity note = schema.addEntity("SuplusEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addIntProperty("surplusType");//零头处理方式：0，不处理；1，四舍五入；2，抹零
        note.addIntProperty("accurateValue");//精确度：0，角；1，元；2，十元
        note.addIntProperty("minAmount");//最低消费金额
    }

    //特殊处理
    private static void addSpecialEntity(Schema schema){
        Entity note = schema.addEntity("SpecialEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("specialId").notNull().unique();//特殊处理id
        note.addIntProperty("specialType");//特殊处理类型
        note.addStringProperty("specialName");//特殊处理名称
    }

    //团购网站
    private static void addGrouponEntity(Schema schema){
        Entity note = schema.addEntity("GrouponEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("grouponId").notNull().unique();
        note.addStringProperty("grouponName");
        note.addStringProperty("grouponCode");
    }

    //团购套餐
    private static void addGrouponTaocanEntity(Schema schema){
        Entity note = schema.addEntity("GrouponTaocanEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("grouponTaocanId").notNull().unique();
        note.addStringProperty("grouponId");//团购网站id
        note.addStringProperty("grouponTaocanName");//团购套餐名称
        note.addFloatProperty("grouponTaocanPrice");//团购套餐价格
        note.addFloatProperty("grouponTaocanBalancePrice");//结算金额
        note.addStringProperty("grouponTaocanDesc");//其他
        note.addIntProperty("isAddToCashier");//是否添加到收银
        note.addIntProperty("isOnline");//是否上架
    }

    //打折方案
    private static void addDiscountEntity(Schema schema){
        Entity note = schema.addEntity("DiscountEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("discountId").notNull().unique();//打折方案id
        note.addStringProperty("discountName");//打折方案名称
        note.addIntProperty("discountType");//优惠方式
        note.addIntProperty("discountPercentage");//折扣率
        note.addIntProperty("isEnforcement");//对不允许打折的商品也打折
        note.addIntProperty("isDishSameDiscount");//商品分类折扣是否相同
        note.addIntProperty("isEmployeeSameDiscount");//所有员工均可使用
        note.addIntProperty("isDateAlidity");//日期内有效
        note.addStringProperty("dateBegin");//开始日期
        note.addStringProperty("dateEnd");//结束日期
        note.addIntProperty("isTimeAlidity");//时间内有效
        note.addStringProperty("timeStart");//开始时间
        note.addStringProperty("timeEnd");//结束时间
        note.addIntProperty("isWeekAlidity");//每周特定日期有效
        note.addStringProperty("weekdate");//每周特定日期
    }

    //商品折扣率
    private static void addDishTypeDiscountEntity(Schema schema){
        Entity note = schema.addEntity("DishTypeDiscountEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("dishTypeDiscountId").notNull().unique();
        note.addStringProperty("discountId");//打折方案id
        note.addStringProperty("dishTypeId");//商品分类id
        note.addIntProperty("dishTypePercentage");//商品分类折扣率
    }

    //收银打印
    private static void addPrintCashierEntity(Schema schema){
        Entity note = schema.addEntity("PrintCashierEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addIntProperty("printType");//前台打印机类型，0：usb；1：网口
        note.addStringProperty("printIp");//前台打印机ip地址
        note.addIntProperty("printSortType");//打印排序方式
        note.addIntProperty("isOpenPrintSound");//是否开启蜂鸣声
        note.addStringProperty("printDesc");//客户联尾注
        note.addStringProperty("printLogo");//商家logo
        note.addIntProperty("isPrintVoucher");//是否打印消费底联
        note.addStringProperty("voucherIp");//打印消费底联的IP地址
        note.addIntProperty("voucherCharNum");//打印消费底联的字符数
        note.addIntProperty("voucherType");//消费底联打印机类型，0：usb；1：网口
        note.addIntProperty("isPrintHuacai");//是否打印划菜联
        note.addStringProperty("huacaiIp");//打印划菜联的IP地址
        note.addIntProperty("huacaiCharNum");//打印划菜联的字符数
        note.addIntProperty("huacaiType");//划菜联打印机类型，0：usb；1：网口
        note.addIntProperty("isAutoPrintFinance");//是否自动打印财务联
        note.addIntProperty("isAutoOpenFinanceBox");//打印财务联时是否自动打开钱箱
        note.addIntProperty("isAutoOpenCustomerBox");//打印客户联时是否自动打开钱箱
        note.addIntProperty("isPrintPresenter");//是否打印赠送商品
        note.addIntProperty("isPrintTuicai");//是否打印退菜明细
        note.addIntProperty("isAutoPrintOrder");//打印设备下单时是否自动打印点菜单
        note.addIntProperty("isPrintQRCode");//是否打印二维码
        note.addIntProperty("isPrintDish");//前台是否打印商品
        note.addIntProperty("printDishType");//打印商品的打印机类型，0：usb；1：网口
        note.addStringProperty("printDishIp");//打印商品的打印机ip
    }

    //前台打印机的商品分类
    private static void addCashierClassifyEntity(Schema schema){
        Entity note =  schema.addEntity("CashierClassifyEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("cashierClassifyId").notNull().unique();
        note.addStringProperty("dishTypeId");//商品分类id
        note.addStringProperty("dishTypeName");//商品分类名称
    }

    //前台打印的商品
    private static void addCashierDishEntity(Schema schema){
        Entity note =  schema.addEntity("CashierDishEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("cashierDishId").notNull().unique();
        note.addStringProperty("dishId");//商品分类id
        note.addStringProperty("dishName");//商品分类名称
    }

    //厨打
    private static void addPrintKitchenEntity(Schema schema){
        Entity note = schema.addEntity("PrintKitchenEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("printKitchenId").notNull().unique();//厨打方案id
        note.addIntProperty("printerType");//打印机类型
        note.addStringProperty("printKitchenName");//厨打设备名称
        note.addStringProperty("printKitchenIp");//厨打IP
        note.addIntProperty("printPaperWidth");//打印纸张宽度
        note.addIntProperty("printRowCharacter");//每行打印字符数
        note.addIntProperty("printCount");//打印份数
        note.addIntProperty("isOneDishOneCut");//是否一菜一切
        note.addIntProperty("isPrintTotalOrder");//是否打印总单
        note.addIntProperty("connectStatus");//打印机连接状态
    }

    //厨打分类
    private static void addPrintKitchenClassifyEntity(Schema schema){
        Entity note = schema.addEntity("PrintKitchenClassifyEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("printKitchenId").notNull();//厨打方案id
        note.addStringProperty("dishTypeId");//商品分类id
        note.addStringProperty("dishTypeName");//商品分类名称
    }

    //厨打商品
    private static void addPrintKitchenDishEntity(Schema schema){
        Entity note = schema.addEntity("PrintKitchenDishEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("printKitchenId").notNull();//厨打方案id
        note.addStringProperty("dishId");//商品id
        note.addStringProperty("dishName");//商品名称
    }

    //备用打印机
    private static void addStandbyPrinterEntity(Schema schema){
        Entity note = schema.addEntity("StandbyPrinterEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("standbyPrinterId").notNull().unique();//备用打印机id
        note.addStringProperty("standbyPrinterIp");//备用打印机IP
        note.addStringProperty("oldPrinterIp");//原打印机的IP
    }

    //客单备注
    private static void addPrintRemarkEntity(Schema schema){
        Entity note = schema.addEntity("PrintRemarkEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("printRemarkId").notNull().unique();
        note.addStringProperty("printRemarkName");
    }

    //店家信息
    private static void addBasicsPartnerEntity(Schema schema){
        Entity note = schema.addEntity("BasicsPartnerEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("partnerCode");
        note.addStringProperty("partnerName");
        note.addStringProperty("partnerPassword");
        note.addStringProperty("partnerEmail");
        note.addStringProperty("partnerContacts");
        note.addStringProperty("partnerPhone");
        note.addStringProperty("partnerCreateTime");
    }

    //限时用餐
    private static void addShopMealsEntity(Schema schema){
        Entity note = schema.addEntity("ShopMealsEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("partnerCode");
        note.addIntProperty("mealsState");//是否开启限时用餐
        note.addStringProperty("mealsStartTime");
        note.addStringProperty("mealsRemindTime");
    }

    //付款方式
    private static void addShopPaymentEntity(Schema schema){
        Entity note = schema.addEntity("ShopPaymentEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("paymentId").notNull().unique();//付款方式id
        note.addStringProperty("partnerCode");//商家编号
        note.addIntProperty("paymentType");//付款类型
        note.addStringProperty("paymentName");//付款方式名称
        note.addIntProperty("isSaleState");//是否计入消费额
        note.addIntProperty("isOpenCashBox");//是否打开钱箱
        note.addIntProperty("isOpenScanGun");//是否开启扫码枪
    }

    //结账记录实体
    private static Entity addPayModeEntity(Schema schema){
        Entity note = schema.addEntity("PayModeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("payModeId").unique().notNull();//结账记录id
        note.addStringProperty("orderId");//账单id
        note.addStringProperty("paymentId");//付款方式id
        note.addStringProperty("paymentName");//付款方式名称
        note.addIntProperty("paymentType");//付款类型名称
        note.addFloatProperty("payMoney");//付款金额
        note.addFloatProperty("payBalance");//
        note.addLongProperty("payTime");//付款时间
        note.addIntProperty("isJoinOrderPay");//是否是未知状态
        note.addStringProperty("electricOrderSerial");//微信，支付宝支付的流水号，用于退款时使用
        return note;
    }

    //电子收款设置
    private static void addShopReceivableEntity(Schema schema){
        Entity note = schema.addEntity("ShopReceivableEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("partnerCode");
        note.addIntProperty("accountType");//账户类型，0：个人账号；1：公司账号
        note.addStringProperty("shopBank");//开户银行
        note.addStringProperty("shopProvince");//开户省份
        note.addStringProperty("shopCity");//开户城市
        note.addStringProperty("bankBranch");//开户支行名称
        note.addStringProperty("accountName");//开户人姓名
        note.addStringProperty("bankAccount");//银行账号
    }

    //营业时间
    private static void addShopTimeEntity(Schema schema){
        Entity note = schema.addEntity("ShopTimeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("partnerCode");
        note.addIntProperty("shopDayType");//0：当日；1：次日
        note.addStringProperty("shopEndTime");//结束时间
    }

    //系统通知表
    private static void addSystemMessageEntity(Schema schema){
        Entity note = schema.addEntity("SystemMessageEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("systemMessageId").unique().notNull();//系统通知Id
        note.addStringProperty("systemTitle");//系统通知标题
        note.addStringProperty("systemTime");//系统通知时间
        note.addStringProperty("systemContent");//系统通知内容
        note.addStringProperty("systemImgUrl");//系统通知图片
        note.addIntProperty("isRead");//是否已读
    }

    //商家通知表
    private static void addStoreMessageEntity(Schema schema){
        Entity note = schema.addEntity("StoreMessageEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("storeMessageId").unique().notNull();//商家通知Id
        note.addStringProperty("storeTitle");//商家通知标题
        note.addStringProperty("storeTime");//商家通知时间
        note.addStringProperty("storeContent");//商家通知内容
        note.addStringProperty("storeImgUrl");//商家通知图片
        note.addIntProperty("isRead");//是否已读
    }

    //微信通知表
    private static void addWXMessageEntity(Schema schema){
        Entity note = schema.addEntity("WXMessageEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("wxMessageId").unique().notNull();//微信通知Id
        note.addStringProperty("wxTitle");//微信通知标题
        note.addStringProperty("wxTime");//微信通知时间
        note.addStringProperty("wxContent");//微信通知内容
        note.addIntProperty("wxType");//消息类型
        note.addIntProperty("isRead");//是否已读
    }

    //打折记录表
    private static void addDiscountHistoryEntity(Schema schema){
        Entity note = schema.addEntity("DiscountHistoryEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("discountHistoryId").unique().notNull();//打折记录Id
        note.addStringProperty("orderId");//账单id
        note.addIntProperty("discountType");//打折类型，0：整单打折；1：部分打折；2：方案打折
        note.addIntProperty("discountRate");//整单打折和部分打折对应的折扣率
        note.addStringProperty("discountReason");//打折原因
        note.addStringProperty("discountId");//打折方案的id
        note.addLongProperty("createTime");//最终修改时间
        note.addIntProperty("isAbleDiscount");//是否允许对设置为不允许打折的商品进行打折
    }

    //部分打折对应的商品
    private static void addSomeDiscountGoodsEntity(Schema schema){
        Entity note = schema.addEntity("SomeDiscountGoodsEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("someDiscountGoodsId").unique().notNull();//部分打折记录Id
        note.addStringProperty("discountHistoryId");//打折记录的id
        note.addStringProperty("orderDishId");
        note.addStringProperty("dishId");//商品分类id
    }

    //已点套餐商品
    private static void addOrderTaocanGroupDishEntity(Schema schema){
        Entity note = schema.addEntity("OrderTaocanGroupDishEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("orderTaocanGroupDishId").unique().notNull();//已点的套餐内商品id
        note.addStringProperty("orderId");//订单ID
        note.addStringProperty("taocanGroupDishId");//套餐内商品的id
        note.addStringProperty("taocanGroupId");//套餐内分组id
        note.addStringProperty("orderDishId");//已点套餐id
        note.addStringProperty("orderDishName");//已点套餐名称
        note.addStringProperty("dishId");//商品id
        note.addStringProperty("dishName");//商品名称
        note.addStringProperty("practiceId");//做法id
        note.addStringProperty("practiceName");//做法名称
        note.addStringProperty("specifyId");//规格id
        note.addStringProperty("specifyName");//规格名称
        note.addIntProperty("taocanGroupDishCount");//数量
        note.addFloatProperty("extraPrice");//加价
        note.addStringProperty("remark");//备注
        note.addIntProperty("status");//状态，0：未下单；1：已下单；2：退菜
        note.addLongProperty("createTime");//创建时间
        note.addStringProperty("taocanTypeId");//套餐分类id
        note.addStringProperty("dishTypeId");//商品分类id
        note.addStringProperty("taocanTypeName");//套餐分类名称
        note.addStringProperty("dishTypeName");//商品分类名称
        note.addIntProperty("isPrint");//是否已经打印
    }

    //打印失败
    private static void addPrinterFailedHistoryEntity(Schema schema){
        Entity note = schema.addEntity("PrinterFailedHistoryEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("printerFailedHistoryId").unique().notNull();//打印失败记录id
        note.addStringProperty("printKitchenId");//打印失败的id地址
        note.addStringProperty("orderId");//打印账单id
        note.addLongProperty("printTime");//打印时间
        note.addStringProperty("printDishType");//打印的商品分类
        note.addStringProperty("printDish");//打印的商品
        note.addIntProperty("printStatus");//打印状态
        note.addIntProperty("printType");//打印类型：0普通配菜单；1加菜单；2退菜单；3催菜单
    }

    //外卖消息记录
    private static void addJpushMessageEntity(Schema schema){
        Entity note = schema.addEntity("JpushMessageEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("jpushMessageId").unique().notNull();
        note.addStringProperty("message");//消息内容
        note.addIntProperty("type");//消息类型
        note.addLongProperty("lastTime");//上次调用接口时间
        note.addIntProperty("count");//调用接口的次数
    }

    //挂账
    private static void addBillAccountEntity(Schema schema){
        Entity note = schema.addEntity("BillAccountEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("billAccountId");//挂账id
        note.addIntProperty("payType");//支付类型:6
        note.addStringProperty("billAccountName");//挂账名称
        note.addIntProperty("isSale");//是否计入销售额
        note.addStringProperty("createTime");//创建时间
    }

    //挂账签字人
    private static void addBillAccountSignEntity(Schema schema){
        Entity note = schema.addEntity("BillAccountSignEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("billAccountSignId");//签字人id
        note.addStringProperty("billAccountId");//挂账id
        note.addStringProperty("billAccountSignName");//签字人名称
    }

    //挂账单位
    private static void addBillAccountPersonEntity(Schema schema){
        Entity note = schema.addEntity("BillAccountPersonEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("billAccountPersonId");//挂账单位或挂账人id
        note.addStringProperty("billAccountId");//挂账id
        note.addIntProperty("billAccountPersonType");//挂账对象的类型：0-单位；1-个人
        note.addStringProperty("billAccountPersonName");//挂账人或挂账单位名称
    }

    //挂账记录
    private static void addBillAccountHistoryEntity(Schema schema){
        Entity note = schema.addEntity("BillAccountHistoryEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("billAccountHistoryId");//挂账记录id
        note.addStringProperty("orderId");//账单id
        note.addStringProperty("billAccountId");//挂账id
        note.addStringProperty("billAccountName");//挂账名称
        note.addStringProperty("billAccountPersonId");//挂账人id
        note.addStringProperty("billAccountPersonName");//挂账人名称
        note.addStringProperty("billAccountUnitId");//挂账单位id
        note.addStringProperty("billAccountUnitName");//挂账单位名称
        note.addStringProperty("billAccountSignId");//签字人id
        note.addStringProperty("billAccountSignName");//签字人名称
        note.addIntProperty("billAccountMoney");//挂账金额
        note.addLongProperty("createTime");//挂账时间
        note.addStringProperty("orderNo");//账单流水号
        note.addIntProperty("isJoinOrder");//
    }

    private static void addUploadDataEntity(Schema schema){
        Entity note = schema.addEntity("UploadDataEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("uploadDataId").notNull().unique();
        note.addStringProperty("orderId");//订单id
        /**
         * 数据id，0：orderDishId；1：orderDishId；2：orderDishId；3：orderId；4：orderId；
         */
        note.addStringProperty("dataId");//数据对应的id
        /**
         * 数据类型，0：加菜；1：退菜；2：赠菜；3：优惠；4：支付
         */
        note.addIntProperty("dataType");
        note.addIntProperty("isHand");//是否成功
        note.addIntProperty("count");//调用的次数
        note.addLongProperty("lastTime");//上次调用的时间
    }

    public static  void addVipCardEntity(Schema schema){
        Entity note = schema.addEntity("VipCardEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("vipCardId");//会员卡记录id
        note.addStringProperty("vipCardName");//会员卡名称
        note.addIntProperty("vipCardType");//会员卡类型
        note.addIntProperty("vipCardDiscountType");//会员卡优惠类型
        note.addIntProperty("vipCardDiscountRate");//会员卡折扣率
    }


    public static void addWxOrderMessageEntity(Schema schema){
        Entity note = schema.addEntity("WxOrderMessageEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("wxOrderMessageId");//微信消息记录ID
        note.addIntProperty("wxOrderMessageName");//微信消息名称
        note.addStringProperty("areaName");//区域名称
        note.addStringProperty("tableName");//桌位名称
        note.addStringProperty("tableCode");//桌位编码
        note.addIntProperty("orderNumber");//订单号
        note.addStringProperty("orderId");//订单ID
    }

    public static void addPrintResultEntity(Schema schema){
        Entity note = schema.addEntity("PrintResultEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("printResultId").unique();
        note.addStringProperty("orderId");
        note.addStringProperty("printIp");
        note.addStringProperty("printResult");
        note.addStringProperty("printTime");
    }

    public static void addTurnoverHistoryEntity(Schema schema){
        Entity note = schema.addEntity("TurnoverHistoryEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("turnoverHistoryId").unique();
        note.addStringProperty("startTurnoverTime");
        note.addStringProperty("turnoverStartTime");
        note.addStringProperty("turnoverEndTime");
        note.addStringProperty("cashierName");
        note.addStringProperty("turnoverState");
        note.addStringProperty("payMentType");
        note.addStringProperty("areaType");
        note.addStringProperty("orderTotalCount");
        note.addStringProperty("orderedTotalCount");
        note.addStringProperty("unOrderedTotalCount");
        note.addStringProperty("orderedTotalMoney");
        note.addStringProperty("unOrderedTotalMoney");
        note.addStringProperty("payments");
        note.addStringProperty("dishTypes");
        note.addStringProperty("money0");
        note.addStringProperty("money1");
        note.addStringProperty("money2");
        note.addStringProperty("money3");
        note.addStringProperty("money4");
        note.addStringProperty("money5");
        note.addStringProperty("money6");
        note.addStringProperty("operatorName");
        note.addStringProperty("printTime");
        note.addLongProperty("createTime");
    }

    //牌号表
    public static void addTableCodeEntity(Schema schema){
        Entity note = schema.addEntity("TableCodeEntity");
        note.addIdProperty().autoincrement().unique().primaryKey();
        note.addStringProperty("tableCode").unique();
    }

    public static void addSendPersonEntity(Schema schema){
        Entity note = schema.addEntity("SendPersonEntity");
        note.addIdProperty().unique().autoincrement().primaryKey();
        note.addStringProperty("sendPersonId");
        note.addStringProperty("sendPersonName");
        note.addStringProperty("sendPersonPhone");
        note.addIntProperty("sendPersonTC");
        note.addIntProperty("isSendMessage");
    }

    //加料料库
    public static void addMaterialEntity(Schema schema){
        Entity note = schema.addEntity("MaterialEntity");
        note.addIdProperty().unique().autoincrement().primaryKey();
        note.addStringProperty("materialId");
        note.addStringProperty("materialName");
        note.addIntProperty("materialPrice");
    }

    //商品分类加料关联表
    public static void addDishTypeMaterialEntity(Schema schema){
        Entity note = schema.addEntity("DishTypeMaterialEntity");
        note.addIdProperty().unique().autoincrement().primaryKey();
        note.addStringProperty("dishTypeMaterialId");
        note.addStringProperty("dishTypeId");
        note.addStringProperty("materialId");
    }

    //商品选中的加料
    public static void addDishSelectedMaterialEntity(Schema schema){
        Entity note = schema.addEntity("DishSelectedMaterialEntity");
        note.addIdProperty().unique().autoincrement().primaryKey();
        note.addStringProperty("dishSelectedMaterialId");
        note.addStringProperty("orderId");
        note.addStringProperty("orderDishId");
        note.addStringProperty("dishTypeMaterialId");
        note.addStringProperty("materialId");
        note.addStringProperty("materialName");
        note.addIntProperty("materialPrice");
        note.addIntProperty("selectedCount");
        note.addIntProperty("totalPrice");
    }

}
