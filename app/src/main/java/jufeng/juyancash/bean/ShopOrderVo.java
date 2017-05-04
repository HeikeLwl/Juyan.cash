package jufeng.juyancash.bean;




import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * 
 * @author wujf
 *
 */
public class ShopOrderVo  {
	private String id;
    private String partnerCode;//商家编号
    private String orderNo;//订单流水号
    private int orderNum;//单号
    private String deskCode;//桌位编号
    private String deskName;//桌位名称
    private String openTime;//开单时间
    private String checkoutTime;//结账时间
    private int personNum;//人数
    private int totalPrice;//总价格
    private int payPrice;//支付价格
    private int orderState;//订单状态
    private String employeeId;//收银员id
    private String employeeName;//收银员名称
    private int isDiscount;//是否打折0-否,1-是
	private String discountId;//折扣方案id
	private String discountName;//折扣名称
	private int discountMoney;//折扣金额
	private int mlMoney; //抹零金额
	private int bjlMoney; //不吉利尾数
	private int giveMoney; //赠菜金额
	private int couponMoney;//优惠券金额
	private String vipNo;//会员卡卡号
	private int orderType;//订单类型 0-普通订单，1-外卖订单
	private int deliveryMoney;//配送费(分)
	private int boxMoney;//打包费(分)
    private List<ShopOrderDetailVo> orderDetailList;//订单详情
    private List<ShopOrderPayVo> orderPayList;//支付详情
	private List<BillHandleModel> billList;//挂账
	private List<ShopOrderTaocanModel> taocanList;//套餐详情

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getDeliveryMoney() {
		return deliveryMoney;
	}

	public void setDeliveryMoney(int deliveryMoney) {
		this.deliveryMoney = deliveryMoney;
	}

	public int getBoxMoney() {
		return boxMoney;
	}

	public void setBoxMoney(int boxMoney) {
		this.boxMoney = boxMoney;
	}

	public String getVipNo() {
		return vipNo;
	}

	public void setVipNo(String vipNo) {
		this.vipNo = vipNo;
	}

	public void setCouponMoney(int couponMoney) {
		this.couponMoney = couponMoney;
	}

	public int getCouponMoney() {
		return couponMoney;
	}

	public int getGiveMoney() {
		return giveMoney;
	}

	public void setGiveMoney(int giveMoney) {
		this.giveMoney = giveMoney;
	}

	public void setTaocanList(List<ShopOrderTaocanModel> taocanList) {
		this.taocanList = taocanList;
	}

	public List<ShopOrderTaocanModel> getTaocanList() {
		return taocanList;
	}

	public ShopOrderVo(Context context, String orderId) throws Exception{
		this.id = orderId;
		SharedPreferences spf = context.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
		this.partnerCode = spf.getString("partnerCode", null);
		this.partnerCode= this.partnerCode ==null ? "":this.partnerCode;
		OrderEntity orderEntity = DBHelper.getInstance(context).getOneOrderEntity(orderId);
		String createTime = CustomMethod.parseTime(orderEntity.getCloseTime(), "yyyy-MM-dd HH:mm:ss");
		String deskId = orderEntity.getTableId();
		deskId= deskId ==null ? "":deskId;
		orderNum = orderEntity.getOrderNumber();
		orderNo = orderEntity.getSerialNumber();
		orderNo= orderNo ==null ? "":orderNo;
		deskCode = DBHelper.getInstance(context).getTableCodeByTableId(deskId);
		deskCode= deskCode ==null ? "":deskCode;
		deskName = DBHelper.getInstance(context).getTableNameByTableId(deskId);
		deskName= deskName ==null ? "":deskName;
		openTime = CustomMethod.parseTime(orderEntity.getOpenTime(), "yyyy-MM-dd HH:mm:ss");
		openTime= openTime ==null ? "":openTime;
		checkoutTime = createTime;
		checkoutTime= checkoutTime ==null ? "":checkoutTime;
		personNum = orderEntity.getOrderGuests() ==null ? 1:orderEntity.getOrderGuests();
		totalPrice = orderEntity.getTotalMoney();
		payPrice = orderEntity.getCloseMoney();
		orderState = 1;
		employeeId = orderEntity.getCashierId();
		employeeId= employeeId ==null ? "":employeeId;
		employeeName = DBHelper.getInstance(context).getEmployeeNameById(employeeId);
		employeeName= employeeName ==null ? "":employeeName;
		isDiscount = 0;
		discountName = null;
		discountId = null;
		discountMoney = 0;
		if(orderEntity.getStoreVersion() != null && orderEntity.getStoreVersion() == 1){
			this.orderType = 2;
		}else{
			this.orderType = orderEntity.getOrderType();
		}

		this.boxMoney = 0;
		this.deliveryMoney = 0;
		if(orderType == 1){
			//外卖订单
			TakeOutOrderEntity takeOutOrderEntity = DBHelper.getInstance(context).getTakeOutOrderById(orderId);
			if(takeOutOrderEntity != null){
				this.boxMoney = takeOutOrderEntity.getBoxFee();
				this.deliveryMoney = takeOutOrderEntity.getDispatchFee();
			}
		}
		DiscountHistoryEntity discountHistoryEntity = DBHelper.getInstance(context).getDiscount(orderId);
		if (discountHistoryEntity != null) {
			isDiscount = 1;
			switch (discountHistoryEntity.getDiscountType()) {
				case 0://整单打折
					discountName = "整单打折";
					discountId = "0";
					break;
				case 1:
					discountName = "部分打折";
					discountId = "1";
					break;
				case 2:
					discountName = DBHelper.getInstance(context).getDiscountEntity(discountHistoryEntity.getDiscountId()).getDiscountName();
					discountId = discountHistoryEntity.getDiscountId();
					break;
			}
			discountMoney = orderEntity.getDiscountMoney();
		}else if(orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1){
			//使用会员卡打折
			isDiscount = 1;
			discountId = "2";
			discountName = "会员卡打折";
			discountMoney = orderEntity.getVipDiscountMoney();
		}
		discountName= discountName ==null ? "":discountName;
		discountId= discountId ==null ? "":discountId;
		mlMoney = orderEntity.getTreatmentMoney()+orderEntity.getSelfTreatMoney();
		bjlMoney = orderEntity.getMantissaMoney();
		giveMoney = orderEntity.getPresentMoney();
		couponMoney = orderEntity.getCouponDiscountMoney();
		if(orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1 && orderEntity.getVipNo() != null && !orderEntity.getVipNo().isEmpty()){
			vipNo = orderEntity.getVipNo();
		}else{
			vipNo = null;
		}

		orderDetailList = new ArrayList<>();
		taocanList = new ArrayList<>();
		ArrayList<OrderDishEntity> orderDishEntities = DBHelper.getInstance(context).queryUploadOrderDish(orderId);
		for (OrderDishEntity orderDishEntity :
				orderDishEntities) {
			if (orderDishEntity.getType() == 0) {//非套餐
				ShopOrderDetailVo shopOrderDetailVo = new ShopOrderDetailVo();
				shopOrderDetailVo.setPartnerCode(partnerCode);
				String orderDishId = orderDishEntity.getOrderDishId();
				orderDishId= orderDishId ==null ? "":orderDishId;
				shopOrderDetailVo.setId(orderDishId);
				String goodsId = orderDishEntity.getDishId();
				goodsId= goodsId ==null ? "":goodsId;
				shopOrderDetailVo.setGoodsId(goodsId);
				String goodsName = orderDishEntity.getDishName();
				goodsName= goodsName ==null ? "":goodsName;
				shopOrderDetailVo.setGoodsName(goodsName);
				String dishPriceFen;
				if(orderDishEntity.getIsOrdered() == -2){
					//赠菜
					dishPriceFen = AmountUtils.changeY2F(0 + "");
					int goodsPrice = Integer.valueOf(dishPriceFen);
					shopOrderDetailVo.setGoodsPrice(goodsPrice);
					shopOrderDetailVo.setIsGive(1);
				}else {
					dishPriceFen = AmountUtils.changeY2F(orderDishEntity.getDishPrice() + "");
					int goodsPrice = Integer.valueOf(dishPriceFen);
					shopOrderDetailVo.setGoodsPrice(goodsPrice);
					shopOrderDetailVo.setIsGive(0);
				}
				double goodsNum = orderDishEntity.getDishCount();
				shopOrderDetailVo.setNum(goodsNum);
				int rate[] = {100,100};
				if (orderDishEntity.getIsOrdered() != -2) {
					rate = DBHelper.getInstance(context).getDishDiscountRate(orderEntity,orderDishEntity, discountHistoryEntity);
				}
				double discountPrice;
				double rate0 = (double)(rate[0])/100;
				double rate1 = (double)(rate[1])/100;
				double dishPrice;
				dishPrice = AmountUtils.changeF2Y(dishPriceFen).doubleValue();
				discountPrice = AmountUtils.multiply(rate0,dishPrice,rate1);
				shopOrderDetailVo.setGoodsRate(rate[0]);
				shopOrderDetailVo.setCouponRate(rate[1]);
				shopOrderDetailVo.setCheckoutTime(checkoutTime);
				shopOrderDetailVo.setRealPrice(AmountUtils.changeY2F(discountPrice));
				int tcMoney = 0;
				if(goodsId != null) {
					DishEntity dishEntity = DBHelper.getInstance(context).queryOneDishEntity(goodsId);
					if (dishEntity != null) {
						switch (dishEntity.getCommissionType()){
							case 0://不提成

								break;
							case 1://按比例
								tcMoney = AmountUtils.changeY2F(AmountUtils.multiply1(dishPrice+"",""+dishEntity.getCommissionValue()));
								break;
							case 2://按金额
								tcMoney = AmountUtils.changeY2F(AmountUtils.multiply1(AmountUtils.multiply(dishEntity.getCommissionValue()+"","1"),""+orderDishEntity.getDishCount()));
								break;
						}
					}
				}
				shopOrderDetailVo.setIsGive(orderDishEntity.getIsOrdered() == -2?1:0);
				shopOrderDetailVo.setIsTaocan(0);
				shopOrderDetailVo.setOrderId(orderId);
				shopOrderDetailVo.setTcAmount(tcMoney);
				shopOrderDetailVo.setGoodsMaterial(DBHelper.getInstance(context).getDishMaterialJson(orderDishId));
				orderDetailList.add(shopOrderDetailVo);
			} else {//套餐
				ShopOrderDetailVo shopOrderDetailVo = new ShopOrderDetailVo();
				shopOrderDetailVo.setPartnerCode(partnerCode);
				String orderDishId = orderDishEntity.getOrderDishId();
				orderDishId= orderDishId ==null ? "":orderDishId;
				shopOrderDetailVo.setId(orderDishId);
				String goodsId = orderDishEntity.getDishId();
				goodsId= goodsId ==null ? "":goodsId;
				shopOrderDetailVo.setGoodsId(goodsId);
				String goodsName = orderDishEntity.getDishName();
				goodsName= goodsName ==null ? "":goodsName;
				shopOrderDetailVo.setGoodsName(goodsName);
				String dishPriceFen = AmountUtils.changeY2F(DBHelper.getInstance(context).getOrderedTaocanPrice(orderDishEntity) + "");
				int goodsPrice = Integer.valueOf(dishPriceFen);
				shopOrderDetailVo.setGoodsPrice(goodsPrice);
				double goodsNum = orderDishEntity.getDishCount();
				shopOrderDetailVo.setNum(goodsNum);
				int rate[] = {100,100};
				if (discountHistoryEntity != null) {
					rate = DBHelper.getInstance(context).getTaocanDishDiscountRate(orderEntity,orderDishEntity, discountHistoryEntity);
				}
				double discountPrice;
				double rate0 = (double)(rate[0])/100;
				double rate1 = (double)(rate[1])/100;
				double dishPrice;
				dishPrice = AmountUtils.changeF2Y(dishPriceFen).doubleValue();
				discountPrice = AmountUtils.multiply(rate0,dishPrice,rate1);
				shopOrderDetailVo.setGoodsRate(rate[0]);
				shopOrderDetailVo.setCouponRate(rate[1]);
				shopOrderDetailVo.setCheckoutTime(checkoutTime);
				shopOrderDetailVo.setIsGive(orderDishEntity.getIsOrdered() == -2?1:0);
				shopOrderDetailVo.setIsTaocan(1);
				shopOrderDetailVo.setOrderId(orderId);
				shopOrderDetailVo.setTcAmount(0);
				shopOrderDetailVo.setRealPrice(AmountUtils.changeY2F(discountPrice));
				orderDetailList.add(shopOrderDetailVo);

				ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
				orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(context).getHadOrderedTaocanDish(orderDishEntity));
				for (OrderTaocanGroupDishEntity orderTaocanDish :
						orderTaocanGroupDishEntities) {
					ShopOrderTaocanModel shopOrderTaocanModel = new ShopOrderTaocanModel();
					shopOrderTaocanModel.setOrderDetailId(orderDishId);
					shopOrderTaocanModel.setId(orderTaocanDish.getOrderTaocanGroupDishId());
					String goodsId1 = orderTaocanDish.getDishId();
					goodsId1= goodsId1 ==null ? "":goodsId1;
					shopOrderTaocanModel.setGoodsId(goodsId1);
					String goodsName1 = orderTaocanDish.getDishName();
					goodsName1= goodsName1 ==null ? "":goodsName1;
					shopOrderTaocanModel.setGoodsName(goodsName1);
					int goodsPrice1 = (int) (orderTaocanDish.getExtraPrice() * 100);
					shopOrderTaocanModel.setGoodsPrice(goodsPrice1);
					int goodsNum1 = orderTaocanDish.getTaocanGroupDishCount();
					shopOrderTaocanModel.setNum(goodsNum1);
					taocanList.add(shopOrderTaocanModel);
				}
			}
		}

		orderPayList = new ArrayList<>();
		ArrayList<PayModeEntity> payModeEntities = DBHelper.getInstance(context).getPayModeByOrderId(orderId);
		for (PayModeEntity payModeEntity :
				payModeEntities) {
			ShopOrderPayVo shopOrderPayVo = new ShopOrderPayVo();
			String payModeId = payModeEntity.getPayModeId();
			payModeId = payModeId == null ? "":payModeId;
			shopOrderPayVo.setId(payModeId);
			String payMentId = payModeEntity.getPaymentId();
			payMentId = payMentId == null ? "":payMentId;
			shopOrderPayVo.setPayId(payMentId);
			String payMentName = payModeEntity.getPaymentName();
			payMentName = payMentName == null ? "":payMentName;
			shopOrderPayVo.setPayName(payMentName);
			int payMoney = AmountUtils.changeY2F(Double.parseDouble(AmountUtils.multiply(payModeEntity.getPayMoney()+"","1.0")));
			shopOrderPayVo.setPayMoney(payMoney);
			int payType = payModeEntity.getPaymentType();
			shopOrderPayVo.setPayType(payType);
			int groupBalance = 0;
			if(payType == 4){
				//团购
				groupBalance = (int)(payModeEntity.getPayBalance()*100);
			}
			shopOrderPayVo.setCheckoutTime(checkoutTime);
			shopOrderPayVo.setGroupBalance(groupBalance);
			String payTime = CustomMethod.parseTime(payModeEntity.getPayTime(), "yyyy-MM-dd HH:mm:ss");
			payTime = payTime == null ? "":payTime;
			shopOrderPayVo.setPayTime(payTime);
			shopOrderPayVo.setPartnerCode(partnerCode);
			shopOrderPayVo.setOrderId(orderId);
			orderPayList.add(shopOrderPayVo);
		}

		billList = new ArrayList<>();
		ArrayList<BillAccountHistoryEntity> billAccountHistoryEntities = DBHelper.getInstance(context).queryAllBillAccountHistoryByOrderId(orderId);
		for (BillAccountHistoryEntity billHistory :
				billAccountHistoryEntities) {
			BillHandleModel billHandleModel = new BillHandleModel();
			String cId = billHistory.getBillAccountHistoryId() == null ? "" : billHistory.getBillAccountHistoryId();
			billHandleModel.setId(cId);
			billHandleModel.setPartnerCode(partnerCode);
			billHandleModel.setOrderId(orderId);
			String cBillId = billHistory.getBillAccountId() == null ? "" : billHistory.getBillAccountId();
			billHandleModel.setBillId(cBillId);
			String cBillName = billHistory.getBillAccountName() == null ? "" : billHistory.getBillAccountName();
			billHandleModel.setBillName(cBillName);
			String cBillPersonId = billHistory.getBillAccountPersonId() == null ? "-1" : billHistory.getBillAccountPersonId();
			billHandleModel.setBillPersonId(cBillPersonId);
			String cBillPersonName = billHistory.getBillAccountPersonName() == null ? "" : billHistory.getBillAccountPersonName();
			billHandleModel.setBillPersonName(cBillPersonName);
			String cSignName = billHistory.getBillAccountSignName() == null ? "" : billHistory.getBillAccountSignName();
			billHandleModel.setSigerName(cSignName);
			int cAmount = billHistory.getBillAccountMoney();
			billHandleModel.setAmount(cAmount);
			String cSerialNo = orderNo == null ? "" : orderNo;
			billHandleModel.setSerialNo(cSerialNo);
			int cState = 0;
			billHandleModel.setState(cState);
			String cTime = createTime == null ? "":createTime;
			billHandleModel.setTime(cTime);
			billList.add(billHandleModel);
		}
	}

	public List<BillHandleModel> getBillList() {
		return billList;
	}

	public void setBillList(List<BillHandleModel> billList) {
		this.billList = billList;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getDeskCode() {
		return deskCode;
	}
	public void setDeskCode(String deskCode) {
		this.deskCode = deskCode;
	}
	public String getDeskName() {
		return deskName;
	}
	public void setDeskName(String deskName) {
		this.deskName = deskName;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getCheckoutTime() {
		return checkoutTime;
	}
	public void setCheckoutTime(String checkoutTime) {
		this.checkoutTime = checkoutTime;
	}
	public int getPersonNum() {
		return personNum;
	}
	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
	public int getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(int payPrice) {
		this.payPrice = payPrice;
	}
	public int getOrderState() {
		return orderState;
	}
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	
	public int getIsDiscount() {
		return isDiscount;
	}
	public void setIsDiscount(int isDiscount) {
		this.isDiscount = isDiscount;
	}
	public String getDiscountId() {
		return discountId;
	}
	public void setDiscountId(String discountId) {
		this.discountId = discountId;
	}
	public String getDiscountName() {
		return discountName;
	}
	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}
	public int getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(int discountMoney) {
		this.discountMoney = discountMoney;
	}
	

	public int getMlMoney() {
		return mlMoney;
	}
	public void setMlMoney(int mlMoney) {
		this.mlMoney = mlMoney;
	}
	public int getBjlMoney() {
		return bjlMoney;
	}
	public void setBjlMoney(int bjlMoney) {
		this.bjlMoney = bjlMoney;
	}

	public List<ShopOrderDetailVo> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<ShopOrderDetailVo> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}

	public List<ShopOrderPayVo> getOrderPayList() {
		return orderPayList;
	}

	public void setOrderPayList(List<ShopOrderPayVo> orderPayList) {
		this.orderPayList = orderPayList;
	}
}
