package jufeng.juyancash.syncdata;

import java.util.List;

import jufeng.juyancash.bean.CashierClassifyVo;
import jufeng.juyancash.bean.PersonModel;
import jufeng.juyancash.bean.VashierGoodsVo;

public class SynchroVo {
	// t_business_goods
	private List<GoodsHasGuigeVo> goodsHasGuiges;
	// t_business_goods_make
	private List<GoodsHasMakeVo> goodsHasMakes;
	// t_business_goods_has_make
	private List<GoodsVo> goodses;
	// t_business_goods_guige
	private List<GoodsTypeVo> goodsTypes;
	// t_business_goods_has_guige GoodsHasGuige
	private List<GuigeVo> guiges;
	// t_business_goods_type
	private List<MakeVo> makes;
	// t_business_desk_area
	private List<DeskVo> desks;
	// t_business_desk
	private List<DeskAreaVo> deskAreaes;
	// t_basics_employee
	private List<EmployeeVo> employees;
	// t_basics_role
	private List<RoleVo> roles;
	// t_basics_menu
	private List<MenuVo> menus;
	// t_basics_role_authority
	private List<RoleAuthorityVo> roleAuthorities;
	// t_business_taocan_type
	private List<TaoCanTypeVo> taocanTypes;
	// t_business_taocan_group
	private List<TaocanGroupVo> taocanGroupes;
	// t_business_taocan
	private List<TaocanVo> taocanes;
	// t_business_taocan_group_goods
	private List<TaocanGroupGoodsVo> taocanGroupGoodses;
	// t_cashier_display
	private List<DisplayVo> displaies;
	// t_cashier_mantissa
	private List<MantissaVo> mantissaes;
	// t_cashier_special
	private List<SpecialVo> speciales;
	// t_cashier_surplus
	private List<SurplusVo> surpluses;
	// t_marketing_discount
	private List<DiscountVo> discounts;
	// t_marketing_groupon
	private List<GrouponVo> groupones;
	// t_marketing_goodstype_percentage
	private List<GoodsTypePercentageVo> goodsTypePercentages;
	// t_print_cashier
	private List<SetCashierVo> cashieres;
	// t_print_kitchen
	private List<KitchenVo> kitchenes;
	// t_print_kitchen_classify
	private List<KitchenClassifyVo> kitchenClassifies;
	// t_print_kitchen_goods
	private List<KitchenGoodsVo> kitchenGoodses;
	// t_print_printer
	private List<PrinterVo> printeres;
	// t_print_remark
	private List<RemarkVo> remarkes;
	// t_shop_meals
	private List<ShopMealsVo> shopMealses;
	// t_shop_payment_mode
	private List<PaymentVo> paymentVoes;
	// t_shop_receivables
	private List<ReceivablesVo> receivableses;
	// t_shop_time
	private List<ShopTimeVo> shopTimes;
	// t_shop_bill
	private List<BillVo> billes;
	// t_shop_bill_person
	private List<BillPersonVo> billPersones;
	// t_shop_bill_siger
	private List<BillSigerVo> billSigeres;
	// t_marketing_groupon_taocan
	private List<GrouponTaoCanVo> grouponTaoCanes;
	// t_print_cashier_classify
	private List<CashierClassifyVo> cashierClassifies;
	// t_print_cashier_goods
	private List<VashierGoodsVo> cashierGoodses;
	// t_wx_card
	private List<VipCardSetVo> vipCardSets;
	//t_business_goods_inventory
	private List<GoodsInventoryVo> goodsInventories;
	//t_cashier_estimate
	private List<CashierEstimateVo> cashierEstimates;

    private List<PersonModel> personList;

	private List<GoodsMaterialModel> materialList;
	//t_business_goods_type_material
	private List<GoodsTypeMaterialVo> goodsTypeMaterialList;

	public List<GoodsMaterialModel> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<GoodsMaterialModel> materialList) {
		this.materialList = materialList;
	}

	public List<GoodsTypeMaterialVo> getGoodsTypeMaterialList() {
		return goodsTypeMaterialList;
	}

	public void setGoodsTypeMaterialList(List<GoodsTypeMaterialVo> goodsTypeMaterialList) {
		this.goodsTypeMaterialList = goodsTypeMaterialList;
	}

	public List<PersonModel> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonModel> personList) {
        this.personList = personList;
    }

    public void setCashierEstimates(List<CashierEstimateVo> cashierEstimates) {
		this.cashierEstimates = cashierEstimates;
	}

	public List<CashierEstimateVo> getCashierEstimates() {
		return cashierEstimates;
	}

	public List<GoodsInventoryVo> getGoodsInventories() {
		return goodsInventories;
	}

	public void setGoodsInventories(List<GoodsInventoryVo> goodsInventories) {
		this.goodsInventories = goodsInventories;
	}

	public List<CashierClassifyVo> getCashierClassifies() {
		return cashierClassifies;
	}

	public void setCashierClassifies(List<CashierClassifyVo> cashierClassifies) {
		this.cashierClassifies = cashierClassifies;
	}

	public List<VashierGoodsVo> getCashierGoodses() {
		return cashierGoodses;
	}

	public void setCashierGoodses(List<VashierGoodsVo> cashierGoodses) {
		this.cashierGoodses = cashierGoodses;
	}

	public List<GrouponTaoCanVo> getGrouponTaoCanes() {
		return grouponTaoCanes;
	}

	public void setGrouponTaoCanes(List<GrouponTaoCanVo> grouponTaoCanes) {
		this.grouponTaoCanes = grouponTaoCanes;
	}

	public List<GoodsHasGuigeVo> getGoodsHasGuiges() {
		return goodsHasGuiges;
	}

	public void setGoodsHasGuiges(List<GoodsHasGuigeVo> goodsHasGuiges) {
		this.goodsHasGuiges = goodsHasGuiges;
	}

	public List<GoodsHasMakeVo> getGoodsHasMakes() {
		return goodsHasMakes;
	}

	public void setGoodsHasMakes(List<GoodsHasMakeVo> goodsHasMakes) {
		this.goodsHasMakes = goodsHasMakes;
	}

	public List<GoodsVo> getGoodses() {
		return goodses;
	}

	public void setGoodses(List<GoodsVo> goodses) {
		this.goodses = goodses;
	}

	public List<GoodsTypeVo> getGoodsTypes() {
		return goodsTypes;
	}

	public void setGoodsTypes(List<GoodsTypeVo> goodsTypes) {
		this.goodsTypes = goodsTypes;
	}

	public List<GuigeVo> getGuiges() {
		return guiges;
	}

	public void setGuiges(List<GuigeVo> guiges) {
		this.guiges = guiges;
	}

	public List<MakeVo> getMakes() {
		return makes;
	}

	public void setMakes(List<MakeVo> makes) {
		this.makes = makes;
	}

	public List<DeskVo> getDesks() {
		return desks;
	}

	public void setDesks(List<DeskVo> desks) {
		this.desks = desks;
	}

	public List<DeskAreaVo> getDeskAreaes() {
		return deskAreaes;
	}

	public void setDeskAreaes(List<DeskAreaVo> deskAreaes) {
		this.deskAreaes = deskAreaes;
	}

	public List<EmployeeVo> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeVo> employees) {
		this.employees = employees;
	}

	public List<RoleVo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVo> roles) {
		this.roles = roles;
	}

	public List<MenuVo> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuVo> menus) {
		this.menus = menus;
	}

	public List<RoleAuthorityVo> getRoleAuthorities() {
		return roleAuthorities;
	}

	public void setRoleAuthorities(List<RoleAuthorityVo> roleAuthorities) {
		this.roleAuthorities = roleAuthorities;
	}

	public List<TaoCanTypeVo> getTaocanTypes() {
		return taocanTypes;
	}

	public void setTaocanTypes(List<TaoCanTypeVo> taocanTypes) {
		this.taocanTypes = taocanTypes;
	}

	public List<TaocanGroupVo> getTaocanGroupes() {
		return taocanGroupes;
	}

	public void setTaocanGroupes(List<TaocanGroupVo> taocanGroupes) {
		this.taocanGroupes = taocanGroupes;
	}

	public List<TaocanVo> getTaocanes() {
		return taocanes;
	}

	public void setTaocanes(List<TaocanVo> taocanes) {
		this.taocanes = taocanes;
	}

	public List<TaocanGroupGoodsVo> getTaocanGroupGoodses() {
		return taocanGroupGoodses;
	}

	public void setTaocanGroupGoodses(List<TaocanGroupGoodsVo> taocanGroupGoodses) {
		this.taocanGroupGoodses = taocanGroupGoodses;
	}

	public List<DisplayVo> getDisplaies() {
		return displaies;
	}

	public void setDisplaies(List<DisplayVo> displaies) {
		this.displaies = displaies;
	}

	public List<MantissaVo> getMantissaes() {
		return mantissaes;
	}

	public void setMantissaes(List<MantissaVo> mantissaes) {
		this.mantissaes = mantissaes;
	}

	public List<SpecialVo> getSpeciales() {
		return speciales;
	}

	public void setSpeciales(List<SpecialVo> speciales) {
		this.speciales = speciales;
	}

	public List<SurplusVo> getSurpluses() {
		return surpluses;
	}

	public void setSurpluses(List<SurplusVo> surpluses) {
		this.surpluses = surpluses;
	}

	public List<DiscountVo> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<DiscountVo> discounts) {
		this.discounts = discounts;
	}

	public List<GrouponVo> getGroupones() {
		return groupones;
	}

	public void setGroupones(List<GrouponVo> groupones) {
		this.groupones = groupones;
	}

	public List<GoodsTypePercentageVo> getGoodsTypePercentages() {
		return goodsTypePercentages;
	}

	public void setGoodsTypePercentages(List<GoodsTypePercentageVo> goodsTypePercentages) {
		this.goodsTypePercentages = goodsTypePercentages;
	}

	public List<SetCashierVo> getCashieres() {
		return cashieres;
	}

	public void setCashieres(List<SetCashierVo> cashieres) {
		this.cashieres = cashieres;
	}

	public List<KitchenVo> getKitchenes() {
		return kitchenes;
	}

	public void setKitchenes(List<KitchenVo> kitchenes) {
		this.kitchenes = kitchenes;
	}

	public List<KitchenClassifyVo> getKitchenClassifies() {
		return kitchenClassifies;
	}

	public void setKitchenClassifies(List<KitchenClassifyVo> kitchenClassifies) {
		this.kitchenClassifies = kitchenClassifies;
	}

	public List<KitchenGoodsVo> getKitchenGoodses() {
		return kitchenGoodses;
	}

	public void setKitchenGoodses(List<KitchenGoodsVo> kitchenGoodses) {
		this.kitchenGoodses = kitchenGoodses;
	}

	public List<PrinterVo> getPrinteres() {
		return printeres;
	}

	public void setPrinteres(List<PrinterVo> printeres) {
		this.printeres = printeres;
	}

	public List<RemarkVo> getRemarkes() {
		return remarkes;
	}

	public void setRemarkes(List<RemarkVo> remarkes) {
		this.remarkes = remarkes;
	}

	public List<ShopMealsVo> getShopMealses() {
		return shopMealses;
	}

	public void setShopMealses(List<ShopMealsVo> shopMealses) {
		this.shopMealses = shopMealses;
	}

	public List<PaymentVo> getPaymentVoes() {
		return paymentVoes;
	}

	public void setPaymentVoes(List<PaymentVo> paymentVoes) {
		this.paymentVoes = paymentVoes;
	}

	public List<ReceivablesVo> getReceivableses() {
		return receivableses;
	}

	public void setReceivableses(List<ReceivablesVo> receivableses) {
		this.receivableses = receivableses;
	}

	public List<ShopTimeVo> getShopTimes() {
		return shopTimes;
	}

	public void setShopTimes(List<ShopTimeVo> shopTimes) {
		this.shopTimes = shopTimes;
	}

	public List<BillVo> getBilles() {
		return billes;
	}

	public void setBilles(List<BillVo> billes) {
		this.billes = billes;
	}

	public List<BillPersonVo> getBillPersones() {
		return billPersones;
	}

	public void setBillPersones(List<BillPersonVo> billPersones) {
		this.billPersones = billPersones;
	}

	public List<BillSigerVo> getBillSigeres() {
		return billSigeres;
	}

	public void setBillSigeres(List<BillSigerVo> billSigeres) {
		this.billSigeres = billSigeres;
	}

	public List<VipCardSetVo> getVipCardSets() {
		return vipCardSets;
	}

	public void setVipCardSets(List<VipCardSetVo> vipCardSets) {
		this.vipCardSets = vipCardSets;
	}
}
