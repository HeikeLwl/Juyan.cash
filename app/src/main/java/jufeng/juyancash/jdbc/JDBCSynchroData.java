package jufeng.juyancash.jdbc;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.List;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.CashierClassifyVo;
import jufeng.juyancash.bean.PersonModel;
import jufeng.juyancash.bean.VashierGoodsVo;
import jufeng.juyancash.dao.AreaEntity;
import jufeng.juyancash.dao.BillAccountEntity;
import jufeng.juyancash.dao.BillAccountPersonEntity;
import jufeng.juyancash.dao.BillAccountSignEntity;
import jufeng.juyancash.dao.CashierClassifyEntity;
import jufeng.juyancash.dao.CashierDishEntity;
import jufeng.juyancash.dao.CashierDisplayEntity;
import jufeng.juyancash.dao.DiscountEntity;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.DishPracticeEntity;
import jufeng.juyancash.dao.DishSpecifyEntity;
import jufeng.juyancash.dao.DishTypeDiscountEntity;
import jufeng.juyancash.dao.DishTypeEntity;
import jufeng.juyancash.dao.DishTypeMaterialEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.MantissaEntity;
import jufeng.juyancash.dao.MaterialEntity;
import jufeng.juyancash.dao.PracticeEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenClassifyEntity;
import jufeng.juyancash.dao.PrintKitchenDishEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.PrintRemarkEntity;
import jufeng.juyancash.dao.RankEntity;
import jufeng.juyancash.dao.SellCheckEntity;
import jufeng.juyancash.dao.SendPersonEntity;
import jufeng.juyancash.dao.ShopMealsEntity;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.dao.ShopTimeEntity;
import jufeng.juyancash.dao.SpecialEntity;
import jufeng.juyancash.dao.SpecifyEntity;
import jufeng.juyancash.dao.StandbyPrinterEntity;
import jufeng.juyancash.dao.SuplusEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.dao.TaocanEntity;
import jufeng.juyancash.dao.TaocanGroupDishEntity;
import jufeng.juyancash.dao.TaocanGroupEntity;
import jufeng.juyancash.dao.TaocanTypeEntity;
import jufeng.juyancash.dao.VipCardEntity;
import jufeng.juyancash.syncdata.BillPersonVo;
import jufeng.juyancash.syncdata.BillSigerVo;
import jufeng.juyancash.syncdata.BillVo;
import jufeng.juyancash.syncdata.CashierEstimateVo;
import jufeng.juyancash.syncdata.DeskAreaVo;
import jufeng.juyancash.syncdata.DeskVo;
import jufeng.juyancash.syncdata.DiscountVo;
import jufeng.juyancash.syncdata.DisplayVo;
import jufeng.juyancash.syncdata.EmployeeVo;
import jufeng.juyancash.syncdata.GoodsHasGuigeVo;
import jufeng.juyancash.syncdata.GoodsHasMakeVo;
import jufeng.juyancash.syncdata.GoodsInventoryVo;
import jufeng.juyancash.syncdata.GoodsMaterialModel;
import jufeng.juyancash.syncdata.GoodsTypeMaterialVo;
import jufeng.juyancash.syncdata.GoodsTypePercentageVo;
import jufeng.juyancash.syncdata.GoodsTypeVo;
import jufeng.juyancash.syncdata.GoodsVo;
import jufeng.juyancash.syncdata.GrouponTaoCanVo;
import jufeng.juyancash.syncdata.GrouponVo;
import jufeng.juyancash.syncdata.GuigeVo;
import jufeng.juyancash.syncdata.KitchenClassifyVo;
import jufeng.juyancash.syncdata.KitchenGoodsVo;
import jufeng.juyancash.syncdata.KitchenVo;
import jufeng.juyancash.syncdata.MakeVo;
import jufeng.juyancash.syncdata.MantissaVo;
import jufeng.juyancash.syncdata.PaymentVo;
import jufeng.juyancash.syncdata.PrinterVo;
import jufeng.juyancash.syncdata.RemarkVo;
import jufeng.juyancash.syncdata.RoleVo;
import jufeng.juyancash.syncdata.SetCashierVo;
import jufeng.juyancash.syncdata.ShopMealsVo;
import jufeng.juyancash.syncdata.ShopTimeVo;
import jufeng.juyancash.syncdata.SpecialVo;
import jufeng.juyancash.syncdata.SurplusVo;
import jufeng.juyancash.syncdata.SynchroVo;
import jufeng.juyancash.syncdata.TaoCanTypeVo;
import jufeng.juyancash.syncdata.TaocanGroupGoodsVo;
import jufeng.juyancash.syncdata.TaocanGroupVo;
import jufeng.juyancash.syncdata.TaocanVo;
import jufeng.juyancash.syncdata.VipCardSetVo;

/**
 * Created by 15157_000 on 2016/7/7 0007.
 */
public class JDBCSynchroData extends AsyncTask {
    private Context mContext;
    private Handler mHandler;
    private SynchroVo synchroVo;

    public JDBCSynchroData(Context context, SynchroVo synchroVo, Handler handler) {
        this.mContext = context;
        this.mHandler= handler;
        this.synchroVo = synchroVo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        DBHelper.getInstance(mContext).createAllTable();
        getGrouponTaocan(synchroVo.getGrouponTaoCanes());
        getGoodsData(synchroVo.getGoodses());
        getDishPracticeData(synchroVo.getGoodsHasMakes());
        getPracticeData(synchroVo.getMakes());
        getSpecifyData(synchroVo.getGuiges());
        getDishSpecifyData(synchroVo.getGoodsHasGuiges());
        getTableData(synchroVo.getDesks());
        getDishTypeData(synchroVo.getGoodsTypes());
        getAreaData(synchroVo.getDeskAreaes());
        getEmployeeData(synchroVo.getEmployees());
        getRankData(synchroVo.getRoles());
        getTaocanTypeData(synchroVo.getTaocanTypes());
        getTaocanGroupData(synchroVo.getTaocanGroupes());
        getTaocanData(synchroVo.getTaocanes());
        getTaocanGroupDishData(synchroVo.getTaocanGroupGoodses());
        getCashierDishplayData(synchroVo.getDisplaies());
        getCashierMantissaData(synchroVo.getMantissaes());
        getCashierSpecialData(synchroVo.getSpeciales());
        getCashierSurplusData(synchroVo.getSurpluses());
        getMarketDiscountData(synchroVo.getDiscounts());
        getMarketGrouponData(synchroVo.getGroupones());
        getMarketGoodsTypePercentageData(synchroVo.getGoodsTypePercentages());
        getPrintCashierData(synchroVo.getCashieres());
        getCashierClassifyData(synchroVo.getCashierClassifies());
        getCashierDishData(synchroVo.getCashierGoodses());
        getPrintKitchenData(synchroVo.getKitchenes());
        getPrintKitchenClassifyData(synchroVo.getKitchenClassifies());
        getPrintKitchenGoodsData(synchroVo.getKitchenGoodses());
        getPrintPrinterData(synchroVo.getPrinteres());
        getPrintRemarkData(synchroVo.getRemarkes());
        getShopMealsData(synchroVo.getShopMealses());
        getShopPaymentData(synchroVo.getPaymentVoes());
        getBillAccountData(synchroVo.getBilles());
        getBillAccountPersonData(synchroVo.getBillPersones());
        getBillAccountSignData(synchroVo.getBillSigeres());
        getVipCardData(synchroVo.getVipCardSets());
        getStockData(synchroVo.getGoodsInventories());
        getChingData(synchroVo.getCashierEstimates());
        getShopTimeData(synchroVo.getShopTimes());
        getSendPersonData(synchroVo.getPersonList());
        getMaterialData(synchroVo.getMaterialList());
        getDishTypeMaterialData(synchroVo.getGoodsTypeMaterialList());
        return 1;
    }

    @Override
    protected void onPostExecute(Object o) {
        synchroVo = null;
        mHandler.sendEmptyMessage(1);
        super.onPostExecute(o);
    }

    private void getGrouponTaocan(List<GrouponTaoCanVo> grouponTaoCanVos){
        if(grouponTaoCanVos != null) {
            for (GrouponTaoCanVo grouponTaoCanVo :
                    grouponTaoCanVos) {
                GrouponTaocanEntity grouponTaocanEntity = new GrouponTaocanEntity(grouponTaoCanVo);
                DBHelper.getInstance(mContext).saveObject(grouponTaocanEntity);
            }
        }
    }

    private void getGoodsData(List<GoodsVo> goodsModels){
        if(goodsModels != null) {
            for (GoodsVo goodsModel :
                    goodsModels) {
                DishEntity dishEntity = new DishEntity(goodsModel);
                DBHelper.getInstance(mContext).saveObject(dishEntity);
            }
        }
    }

    public void getDishPracticeData(List<GoodsHasMakeVo> goodsHasMakes){
        if(goodsHasMakes != null) {
            for (GoodsHasMakeVo goodsHasMake :
                    goodsHasMakes) {
                DishPracticeEntity dishPracticeEntity = new DishPracticeEntity(goodsHasMake);
                DBHelper.getInstance(mContext).saveObject(dishPracticeEntity);
            }
        }
    }

    private void getPracticeData(List<MakeVo> makes){
        if(makes != null) {
            for (MakeVo make :
                    makes) {
                PracticeEntity practiceEntity = new PracticeEntity(make);
                DBHelper.getInstance(mContext).saveObject(practiceEntity);
            }
        }
    }

    private void getSpecifyData(List<GuigeVo> guiges){
        if(guiges != null) {
            for (GuigeVo guige :
                    guiges) {
                SpecifyEntity specifyEntity = new SpecifyEntity(guige);
                DBHelper.getInstance(mContext).saveObject(specifyEntity);
            }
        }
    }

    private void getDishSpecifyData(List<GoodsHasGuigeVo> goodsHasGuiges){
        if(goodsHasGuiges != null) {
            for (GoodsHasGuigeVo goodsHasGuige :
                    goodsHasGuiges) {
                DishSpecifyEntity dishSpecifyEntity = new DishSpecifyEntity(goodsHasGuige);
                DBHelper.getInstance(mContext).saveObject(dishSpecifyEntity);
            }
        }
    }

    private void getTableData(List<DeskVo> deskModels){
        if(deskModels != null) {
            for (DeskVo deskModel :
                    deskModels) {
                TableEntity tableEntity = new TableEntity(deskModel);
                DBHelper.getInstance(mContext).saveObject(tableEntity);
            }
        }
    }

    private void getDishTypeData(List<GoodsTypeVo> goodsTypes){
        if(goodsTypes != null) {
            for (GoodsTypeVo goodsType :
                    goodsTypes) {
                DishTypeEntity dishTypeEntity = new DishTypeEntity(goodsType);
                DBHelper.getInstance(mContext).saveObject(dishTypeEntity);
            }
        }
    }

    private void getAreaData(List<DeskAreaVo> deskAreas){
        if(deskAreas != null) {
            for (DeskAreaVo deskArea :
                    deskAreas) {
                AreaEntity areaEntity = new AreaEntity(deskArea);
                DBHelper.getInstance(mContext).saveObject(areaEntity);
            }
        }
    }

    private void getEmployeeData(List<EmployeeVo> employeeModels){
        if(employeeModels != null) {
            for (EmployeeVo employeeModel :
                    employeeModels) {
                EmployeeEntity employeeEntity = new EmployeeEntity(employeeModel);
                DBHelper.getInstance(mContext).saveObject(employeeEntity);
            }
        }
    }

    private void getRankData(List<RoleVo> roleModels){
        if(roleModels != null) {
            for (RoleVo roleModel :
                    roleModels) {
                RankEntity rankEntity = new RankEntity(roleModel);
                DBHelper.getInstance(mContext).saveObject(rankEntity);
            }
        }
    }

    private void getTaocanTypeData(List<TaoCanTypeVo> taoCanTypeModels){
        if(taoCanTypeModels != null) {
            for (TaoCanTypeVo taocanTypeModel :
                    taoCanTypeModels) {
                TaocanTypeEntity taocanTypeEntity = new TaocanTypeEntity(taocanTypeModel);
                DBHelper.getInstance(mContext).saveObject(taocanTypeEntity);
            }
        }
    }

    private void getTaocanGroupData(List<TaocanGroupVo> taocanGroupModels){
        if(taocanGroupModels != null) {
            for (TaocanGroupVo taocanGroup :
                    taocanGroupModels) {
                TaocanGroupEntity taocanGroupEntity = new TaocanGroupEntity(taocanGroup);
                DBHelper.getInstance(mContext).saveObject(taocanGroupEntity);
            }
        }
    }

    private void getTaocanData(List<TaocanVo> taocanModels){
        if(taocanModels != null) {
            for (TaocanVo taocanModel :
                    taocanModels) {
                TaocanEntity taocanEntity = new TaocanEntity(taocanModel);
                DBHelper.getInstance(mContext).saveObject(taocanEntity);
            }
        }
    }

    private void getTaocanGroupDishData(List<TaocanGroupGoodsVo> taocanGroupGoodsModels){
        if(taocanGroupGoodsModels != null) {
            for (TaocanGroupGoodsVo taocanGroupGood :
                    taocanGroupGoodsModels) {
                TaocanGroupDishEntity taocanGroupDishEntity = new TaocanGroupDishEntity(taocanGroupGood);
                DBHelper.getInstance(mContext).saveObject(taocanGroupDishEntity);
            }
        }
    }

    private void getCashierDishplayData(List<DisplayVo> displays){
        if(displays != null) {
            for (DisplayVo display :
                    displays) {
                CashierDisplayEntity cashierDisplayEntity = new CashierDisplayEntity(display);
                DBHelper.getInstance(mContext).saveObject(cashierDisplayEntity);
            }
        }
    }

    private void getCashierMantissaData(List<MantissaVo> mantissaes){
        if(mantissaes != null) {
            for (MantissaVo mantissa :
                    mantissaes) {
                MantissaEntity mantissaEntity = new MantissaEntity(mantissa);
                DBHelper.getInstance(mContext).saveObject(mantissaEntity);
            }
        }
    }

    private void getCashierSpecialData(List<SpecialVo> specials){
        if(specials != null) {
            for (SpecialVo special :
                    specials) {
                SpecialEntity specialEntity = new SpecialEntity(special);
                DBHelper.getInstance(mContext).saveObject(specialEntity);
            }
        }
    }

    private void getCashierSurplusData(List<SurplusVo> surpluses){
        if(surpluses != null) {
            for (SurplusVo surplus :
                    surpluses) {
                SuplusEntity suplusEntity = new SuplusEntity(surplus);
                DBHelper.getInstance(mContext).saveObject(suplusEntity);
            }
        }
    }

    private void getMarketDiscountData(List<DiscountVo> discountModels){
        if(discountModels != null) {
            for (DiscountVo dishcountModel :
                    discountModels) {
                DiscountEntity discountEntity = new DiscountEntity(dishcountModel);
                DBHelper.getInstance(mContext).saveObject(discountEntity);
            }
        }
    }

    private void getMarketGrouponData(List<GrouponVo> grouponModels){
        if(grouponModels != null) {
            for (GrouponVo grouponModel :
                    grouponModels) {
                GrouponEntity grouponEntity = new GrouponEntity(grouponModel);
                DBHelper.getInstance(mContext).saveObject(grouponEntity);
            }
        }
    }

//    private void getMarketGrouponTaocanData(List<Groupon>)

    private void getMarketGoodsTypePercentageData(List<GoodsTypePercentageVo> goodsTypePercentageModels){
        if(goodsTypePercentageModels != null) {
            for (GoodsTypePercentageVo goodsTypePercentageModel :
                    goodsTypePercentageModels) {
                DishTypeDiscountEntity dishTypeDiscountEntity = new DishTypeDiscountEntity(goodsTypePercentageModel);
                DBHelper.getInstance(mContext).saveObject(dishTypeDiscountEntity);
            }
        }
    }

    private void getPrintCashierData(List<SetCashierVo> setCashiers){
        if(setCashiers != null) {
            for (SetCashierVo setCashier :
                    setCashiers) {
                PrintCashierEntity printCashierEntity = new PrintCashierEntity(setCashier);
                DBHelper.getInstance(mContext).saveObject(printCashierEntity);
            }
        }
    }

    private void getCashierClassifyData(List<CashierClassifyVo> cashierClassifyVos){
        if(cashierClassifyVos != null){
            for (CashierClassifyVo cashierClassify :
                    cashierClassifyVos) {
                CashierClassifyEntity cashierClassifyEntity = new CashierClassifyEntity(cashierClassify);
                DBHelper.getInstance(mContext).saveObject(cashierClassifyEntity);
            }
        }
    }

    private void getCashierDishData(List<VashierGoodsVo> vashierGoodsVos){
        if(vashierGoodsVos != null){
            for (VashierGoodsVo vashierGoodsVo :
                    vashierGoodsVos) {
                CashierDishEntity cashierDishEntity = new CashierDishEntity(vashierGoodsVo);
                DBHelper.getInstance(mContext).saveObject(cashierDishEntity);
            }
        }
    }

    private void getPrintKitchenData(List<KitchenVo> kitchenModels){
        if(kitchenModels != null) {
            for (KitchenVo kitchenModel :
                    kitchenModels) {
                PrintKitchenEntity printKitchenEntity = new PrintKitchenEntity(kitchenModel);
                DBHelper.getInstance(mContext).saveObject(printKitchenEntity);
            }
        }
    }

    private void getPrintKitchenClassifyData(List<KitchenClassifyVo> kitchenClassifyModels){
        if(kitchenClassifyModels != null) {
            for (KitchenClassifyVo kitchenClassifyModel :
                    kitchenClassifyModels) {
                PrintKitchenClassifyEntity printKitchenClassifyEntity = new PrintKitchenClassifyEntity(kitchenClassifyModel);
                DBHelper.getInstance(mContext).saveObject(printKitchenClassifyEntity);
            }
        }
    }

    private void getPrintKitchenGoodsData(List<KitchenGoodsVo> kitchenGoodsModels){
        if(kitchenGoodsModels != null) {
            for (KitchenGoodsVo kitchenGood :
                    kitchenGoodsModels) {
                PrintKitchenDishEntity printKitchenDishEntity = new PrintKitchenDishEntity(kitchenGood);
                DBHelper.getInstance(mContext).saveObject(printKitchenDishEntity);
            }
        }
    }

    private void getPrintPrinterData(List<PrinterVo> printerModels){
        if(printerModels != null) {
            for (PrinterVo printerModel :
                    printerModels) {
                StandbyPrinterEntity standbyPrinterEntity = new StandbyPrinterEntity(printerModel);
                DBHelper.getInstance(mContext).saveObject(standbyPrinterEntity);
            }
        }
    }

    private void getPrintRemarkData(List<RemarkVo> remarkModels){
        if(remarkModels != null) {
            for (RemarkVo remarkModel :
                    remarkModels) {
                PrintRemarkEntity printRemarkEntity = new PrintRemarkEntity(remarkModel);
                DBHelper.getInstance(mContext).saveObject(printRemarkEntity);
            }
        }
    }

    private void getShopMealsData(List<ShopMealsVo> shopMealses){
        if(shopMealses != null) {
            for (ShopMealsVo shopMeals :
                    shopMealses) {
                ShopMealsEntity shopMealsEntity = new ShopMealsEntity(shopMeals);
                DBHelper.getInstance(mContext).saveObject(shopMealsEntity);
            }
        }
    }

    private void getShopPaymentData(List<PaymentVo> paymentmodes){
        if(paymentmodes != null) {
            for (PaymentVo paymentmode :
                    paymentmodes) {
                ShopPaymentEntity shopPaymentEntity = new ShopPaymentEntity(paymentmode);
                DBHelper.getInstance(mContext).saveObject(shopPaymentEntity);
            }
        }
    }

//    private void getShopReceivableData(List<Receivables> receivables){
//        for (Receivables receivable :
//                receivables) {
//            ShopReceivableEntity shopReceivableEntity = new ShopReceivableEntity(receivable);
//            DBHelper.getInstance(mContext).saveObject(shopReceivableEntity);
//        }
//    }

    private void getBillAccountData(List<BillVo> billModels){
        if(billModels != null) {
            for (BillVo billModel :
                    billModels) {
                BillAccountEntity billAccountEntity = new BillAccountEntity(billModel);
                DBHelper.getInstance(mContext).saveObject(billAccountEntity);
            }
        }
    }

    private void getBillAccountPersonData(List<BillPersonVo> billPersonModels){
        if(billPersonModels != null) {
            for (BillPersonVo billPersonModel :
                    billPersonModels) {
                BillAccountPersonEntity billAccountPersonEntity = new BillAccountPersonEntity(billPersonModel);
                DBHelper.getInstance(mContext).saveObject(billAccountPersonEntity);
            }
        }
    }

    private void getBillAccountSignData(List<BillSigerVo> billSigerModels){
        if(billSigerModels != null) {
            for (BillSigerVo billSigerModel :
                    billSigerModels) {
                BillAccountSignEntity billAccountSignEntity = new BillAccountSignEntity(billSigerModel);
                DBHelper.getInstance(mContext).saveObject(billAccountSignEntity);
            }
        }
    }

    private void getVipCardData(List<VipCardSetVo> vipCardSetVos){
        if(vipCardSetVos != null) {
            for (VipCardSetVo vipCardSetVo :
                    vipCardSetVos) {
                VipCardEntity vipCardEntity = new VipCardEntity(vipCardSetVo);
                DBHelper.getInstance(mContext).saveObject(vipCardEntity);
            }
        }
    }

    private void getStockData(List<GoodsInventoryVo> goodsInventoryVos){
        if(goodsInventoryVos != null) {
            for (GoodsInventoryVo goodsInventoryVo :
                    goodsInventoryVos) {
                SellCheckEntity sellCheckEntity = new SellCheckEntity(goodsInventoryVo);
                DBHelper.getInstance(mContext).saveObject(sellCheckEntity);
            }
        }
    }

    private void getChingData(List<CashierEstimateVo> cashierEstimateVos){
        if(cashierEstimateVos != null) {
            for (CashierEstimateVo cashierEstimateVo :
                    cashierEstimateVos) {
                SellCheckEntity sellCheckEntity = new SellCheckEntity(cashierEstimateVo);
                DBHelper.getInstance(mContext).saveObject(sellCheckEntity);
            }
        }
    }

    private void getShopTimeData(List<ShopTimeVo> shopTimeVos){
        if(shopTimeVos != null) {
            for (ShopTimeVo shopTimeVo :
                    shopTimeVos) {
                ShopTimeEntity shopTimeEntity = new ShopTimeEntity(shopTimeVo);
                DBHelper.getInstance(mContext).saveObject(shopTimeEntity);
            }
        }
    }

    private void getSendPersonData(List<PersonModel> personModels){
        if(personModels != null) {
            for (PersonModel personModel :
                    personModels) {
                SendPersonEntity sendPersonEntity = new SendPersonEntity(personModel);
                DBHelper.getInstance(mContext).saveObject(sendPersonEntity);
            }
        }
    }

    private void getMaterialData(List<GoodsMaterialModel> goodsMaterialModels){
        if(goodsMaterialModels != null) {
            for (GoodsMaterialModel goodsMaterialModel :
                    goodsMaterialModels) {
                MaterialEntity materialEntity = new MaterialEntity(goodsMaterialModel);
                DBHelper.getInstance(mContext).saveObject(materialEntity);
            }
        }
    }

    private void getDishTypeMaterialData(List<GoodsTypeMaterialVo> goodsTypeMaterialVos){
        if(goodsTypeMaterialVos != null) {
            for (GoodsTypeMaterialVo goodsTypeMaterialVo :
                    goodsTypeMaterialVos) {
                DishTypeMaterialEntity dishTypeMaterialEntity = new DishTypeMaterialEntity(goodsTypeMaterialVo);
                DBHelper.getInstance(mContext).saveObject(dishTypeMaterialEntity);
            }
        }
    }
}
