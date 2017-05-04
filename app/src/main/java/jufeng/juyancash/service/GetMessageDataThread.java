package jufeng.juyancash.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.GoodsAddVo;
import jufeng.juyancash.bean.GoodsDetailVo;
import jufeng.juyancash.bean.GoodsGiveVo;
import jufeng.juyancash.bean.GoodsRetreatDetailVo;
import jufeng.juyancash.bean.GoodsRetreatVo;
import jufeng.juyancash.bean.OrderModel;
import jufeng.juyancash.bean.OrderTaocanDetailModel;
import jufeng.juyancash.bean.OrderTaocanModel;
import jufeng.juyancash.bean.PreOrderModel;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.QueueUserModel;
import jufeng.juyancash.bean.RemindBean;
import jufeng.juyancash.bean.ShopOrderVo;
import jufeng.juyancash.bean.StockBean;
import jufeng.juyancash.bean.TableOrderModel;
import jufeng.juyancash.dao.JpushMessageEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.UploadDataEntity;
import jufeng.juyancash.dao.WXMessageEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

import static jufeng.juyancash.DBHelper.getInstance;

/**
 * Created by Administrator102 on 2016/9/20.
 */
public class GetMessageDataThread implements Runnable {
    private Context mContext;
    private String partnerCode;
    private long currentTime;

    public GetMessageDataThread(Context context) {
        this.mContext = context;
        partnerCode = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
    }

    //获取外卖数据
    private void getTakeout(final JpushMessageEntity jpushMessageEntity) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", jpushMessageEntity.getMessage());
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.GET_TAKEOUT_BY_ID), jpushMessageEntity.getJpushMessageId(), map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "外卖单：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        String data = publicModule.getData();
                        OrderModel orderModel = JSON.parseObject(data, OrderModel.class);
                        if (orderModel != null) {
                            getInstance(mContext).insertTakeOut(mContext, orderModel);
                            sendMainMessage(mContext, 0);
                        }
                        DBHelper.getInstance(mContext).clearJpushMessage(jpushMessageEntity.getJpushMessageId());
                    } else {
                        DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
            }
        });
    }

    //获取外卖数据
    private void getMeituanTakeout(final JpushMessageEntity jpushMessageEntity) {
        long ts = System.currentTimeMillis();
        String data = "{\"orderId\":\""+jpushMessageEntity.getMessage()+"\"}";
        String sign = MD5Util.getMD5String(partnerCode+data+ts+mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("data", data);
        map.put("ts",ts+"");
        map.put("sign",sign);
        map.put("partnerCode",partnerCode);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.GET_MEITUAN_TAKEOUT_ORDER), jpushMessageEntity.getJpushMessageId(), map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "美团外卖单：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        String data = publicModule.getData();
                        OrderModel orderModel = JSON.parseObject(data, OrderModel.class);
                        if (orderModel != null) {
                            Log.d("###", "获取订单成功");
                            getInstance(mContext).insertMeituanTakeOut(mContext, orderModel,jpushMessageEntity.getMessage());
                            sendMainMessage(mContext, 0);
                        }
                        DBHelper.getInstance(mContext).clearJpushMessage(jpushMessageEntity.getJpushMessageId());
                    } else {
                        DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
            }
        });
    }

    private void getArrangeMessage(final JpushMessageEntity jpushMessageEntity) {
        Map<String, String> map = new HashMap<>();
        map.put("id", jpushMessageEntity.getMessage());
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.ARRANGE_DETAIL), jpushMessageEntity.getJpushMessageId(), map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "排号：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        String data = publicModule.getData();
                        QueueUserModel queueUserModel = JSON.parseObject(data, QueueUserModel.class);
                        if (queueUserModel != null) {
                            getInstance(mContext).insertArrange(queueUserModel);
                            sendMainMessage(mContext, 6);
//                            CustomMethod.xfYunCall(mContext, "您有新的排号单", "xiaoyan");
                        }
                        DBHelper.getInstance(mContext).clearJpushMessage(jpushMessageEntity.getJpushMessageId());
                    } else {
                        DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
            }
        });
    }

    private void getSchedule(final JpushMessageEntity jpushMessageEntity) {
        Map<String, String> map = new HashMap<>();
        map.put("id", jpushMessageEntity.getMessage());
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.SCHEDULE_DETAIL), jpushMessageEntity.getJpushMessageId(), map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "预定：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        String data = publicModule.getData();
                        PreOrderModel preOrderModel = JSON.parseObject(data, PreOrderModel.class);
                        if (preOrderModel != null) {
                            getInstance(mContext).insertSchedule(preOrderModel);
                            sendMainMessage(mContext, 1);
//                            CustomMethod.xfYunCall(mContext, "您有新的预订单", "xiaoyan");
                        }
                        DBHelper.getInstance(mContext).clearJpushMessage(jpushMessageEntity.getJpushMessageId());
                    } else {
                        DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
            }
        });
    }

    //到店点餐
    private void getTableOrder(final JpushMessageEntity jpushMessageEntity) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", jpushMessageEntity.getMessage());
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.GET_ORDER), jpushMessageEntity.getJpushMessageId(), map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "到店点餐：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 && publicModule.getData() != null) {
                        TableOrderModel tableOrderModel = JSON.parseObject(publicModule.getData(), TableOrderModel.class);
                        DBHelper.getInstance(mContext).insertShopOrder(mContext, tableOrderModel);
                        sendMainMessage(mContext, 15);
                        DBHelper.getInstance(mContext).clearJpushMessage(jpushMessageEntity.getJpushMessageId());
                        DBHelper.getInstance(mContext).insertUploadData(tableOrderModel.getId(), tableOrderModel.getId(), 19);
                    } else {
                        DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
            }
        });
    }

    //催菜
    private void getRemind(final JpushMessageEntity jpushMessageEntity) {
        final WXMessageEntity wxMessageEntity = new WXMessageEntity();
        wxMessageEntity.setWxMessageId(UUID.randomUUID().toString());
        wxMessageEntity.setIsRead(0);
        Map<String, String> map = new HashMap<>();
        map.put("id", jpushMessageEntity.getMessage());
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.REMIND_DISH), jpushMessageEntity.getJpushMessageId(), map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "催菜:" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 && publicModule.getData() != null) {
                        wxMessageEntity.setWxContent(publicModule.getData());
                        wxMessageEntity.setWxTitle("微信催菜");
                        wxMessageEntity.setWxType(0);
                        wxMessageEntity.setWxTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"));
                        RemindBean remindBean = JSON.parseObject(wxMessageEntity.getWxContent(), RemindBean.class);
                        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(remindBean.getOrderId());
                        if (orderEntity != null) {
                            DBHelper.getInstance(mContext).insertWXMessage(wxMessageEntity);
                            sendMainMessage(mContext, 16, wxMessageEntity);
                        }
                        DBHelper.getInstance(mContext).clearJpushMessage(jpushMessageEntity.getJpushMessageId());
                    } else {
                        DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
            }
        });
    }

    //快餐版-到店点餐
    private void getSnackOrder(final JpushMessageEntity jpushMessageEntity) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", jpushMessageEntity.getMessage());
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.GET_ORDER), jpushMessageEntity.getJpushMessageId(), map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "快餐版-到店点餐：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 && publicModule.getData() != null) {
                        TableOrderModel tableOrderModel = JSON.parseObject(publicModule.getData(), TableOrderModel.class);
                        DBHelper.getInstance(mContext).insertSnackShopOrder(mContext, tableOrderModel);
                        sendMainMessage(mContext, 15);
                        DBHelper.getInstance(mContext).clearJpushMessage(jpushMessageEntity.getJpushMessageId());
                        DBHelper.getInstance(mContext).insertUploadData(tableOrderModel.getId(), tableOrderModel.getId(), 19);
                    } else {
                        DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeJpushMessageCount(jpushMessageEntity, currentTime);
            }
        });
    }


    //加菜
    public void addDish(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String orderDishId) {
        GoodsAddVo giveGoodsVo = new GoodsAddVo();
        giveGoodsVo.setOrderId(orderId);
        giveGoodsVo.setPayAmount(AmountUtils.changeY2F(getInstance(mContext).getHadPayMoneyByOrderId(orderId)));
        giveGoodsVo.setYhAmount(getInstance(mContext).getTreatMentMoneyByOrderId(orderId, 1));
        giveGoodsVo.setTotalAmount(AmountUtils.changeY2F(DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderId, 1)));
        giveGoodsVo.setPartnerCode(partnerCode);
        ArrayList<GoodsDetailVo> orderDetailModels = new ArrayList<>();
        ArrayList<OrderTaocanModel> orderTaocanModels = new ArrayList<>();
        Log.d("###", "加菜ID："+orderDishId);
        OrderDishEntity orderDishEntity = DBHelper.getInstance(mContext).queryOneOrderDishEntity(orderDishId);
        if (orderDishEntity != null) {
            if (orderDishEntity.getType() == 0) {
                //非套餐
                GoodsDetailVo orderDetailModel = new GoodsDetailVo();
                orderDetailModel.setTypeId(orderDishEntity.getDishTypeId());
                orderDetailModel.setGoodsId(orderDishEntity.getDishId());
                orderDetailModel.setGoodsName(orderDishEntity.getDishName());
                orderDetailModel.setGoodsPrice(AmountUtils.changeY2F(AmountUtils.multiply(orderDishEntity.getDishPrice(),1)));
                orderDetailModel.setGuigeId(orderDishEntity.getSpecifyId());
                orderDetailModel.setGuigeName(orderDishEntity.getDishSpecify());
                orderDetailModel.setDiscount(orderDishEntity.getIsAbleDiscount());
                orderDetailModel.setMakeId(orderDishEntity.getPracticeId());
                orderDetailModel.setMakeName(orderDishEntity.getDishPractice());
                orderDetailModel.setNum(orderDishEntity.getDishCount());
                orderDetailModel.setId(orderDishId);
                orderDetailModel.setIsGive(0);
                orderDetailModels.add(orderDetailModel);
            } else if (orderDishEntity.getType() == 1) {
                //套餐
                OrderTaocanModel orderTaocanModel = new OrderTaocanModel();
                orderTaocanModel.setId(orderDishId);
                orderTaocanModel.setTaocanId(orderDishEntity.getDishId());
                orderTaocanModel.setTaocanName(orderDishEntity.getDishName());
                orderTaocanModel.setTaocanPrice(AmountUtils.changeY2F(AmountUtils.multiply(orderDishEntity.getDishPrice(),1)));
                orderTaocanModel.setNum(orderDishEntity.getDishCount());
                orderTaocanModel.setTcTypeId(orderDishEntity.getDishTypeId());
                orderTaocanModel.setTcTypeName(orderDishEntity.getDishTypeName());
                ArrayList<OrderTaocanDetailModel> orderTaocanDetailModels = new ArrayList<>();
                ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(mContext).getOrderedTaocanDish(orderDishEntity));
                for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                        orderTaocanGroupDishEntities) {
                    OrderTaocanDetailModel orderTaocanDetailModel = new OrderTaocanDetailModel();
                    orderTaocanDetailModel.setId(orderTaocanGroupDish.getOrderTaocanGroupDishId());
                    orderTaocanDetailModel.setNum(orderTaocanGroupDish.getTaocanGroupDishCount());
                    orderTaocanDetailModel.setAddPrice(AmountUtils.changeY2F(AmountUtils.multiply(orderTaocanGroupDish.getExtraPrice(), 1)));
                    orderTaocanDetailModel.setGoodsId(orderTaocanGroupDish.getDishId());
                    orderTaocanDetailModel.setGoodsName(orderTaocanGroupDish.getDishName());
                    orderTaocanDetailModel.setGuigeId(orderTaocanGroupDish.getSpecifyId());
                    orderTaocanDetailModel.setGuigeName(orderTaocanGroupDish.getSpecifyName());
                    orderTaocanDetailModel.setMakeId(orderTaocanGroupDish.getPracticeId());
                    orderTaocanDetailModel.setMakeName(orderTaocanGroupDish.getPracticeName());
                    orderTaocanDetailModels.add(orderTaocanDetailModel);
                }
                orderTaocanModel.setGoods(new HashSet<>(orderTaocanDetailModels));
                orderTaocanModels.add(orderTaocanModel);
            }
        }
        giveGoodsVo.setGoodsList(orderDetailModels);
        giveGoodsVo.setTaocanList(orderTaocanModels);
        String data = JSON.toJSONString(giveGoodsVo);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.ADD_DISH), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "加菜：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //退菜
    public void retreatDish(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String dataId) {
        Log.d("###", "退菜请求：");
        GoodsRetreatVo giveGoodsVo = new GoodsRetreatVo();
        giveGoodsVo.setOrderId(orderId);
        giveGoodsVo.setPayAmount(AmountUtils.changeY2F(getInstance(mContext).getHadPayMoneyByOrderId(orderId)));
        giveGoodsVo.setYhAmount(getInstance(mContext).getTreatMentMoneyByOrderId(orderId, 1));
        giveGoodsVo.setTotalAmount(AmountUtils.changeY2F(DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderId, 1)));
        giveGoodsVo.setPartnerCode(partnerCode);
        ArrayList<GoodsRetreatDetailVo> orderDetailModels = new ArrayList<>();
        String[] ids = dataId.split("`");
        if (ids.length > 1) {
            GoodsRetreatDetailVo orderDetailModel = new GoodsRetreatDetailVo();
            orderDetailModel.setGoodsId(ids[0]);
            Log.d("###", "退菜请求："+ids[0]);
            orderDetailModel.setNum(Double.parseDouble(ids[1]));
            try{
                orderDetailModel.setAmount(AmountUtils.changeY2F(Double.parseDouble(ids[2])));
                orderDetailModel.setType(Integer.parseInt(ids[3]));
            }catch (Exception e){
                orderDetailModel.setAmount(0);
                orderDetailModel.setType(0);
            }
            if(orderDetailModel.getType() == 1){
                orderDetailModels.add(orderDetailModel);
                giveGoodsVo.setTaocanList(orderDetailModels);
            }else{
                orderDetailModels.add(orderDetailModel);
                giveGoodsVo.setGoodsList(orderDetailModels);
            }
        }
        String data = JSON.toJSONString(giveGoodsVo);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        Log.d("###", "retreatDish: ");
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.RETREAT_DISH), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "退菜：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //赠菜
    public void presentDish(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String dataId) {
        GoodsGiveVo giveGoodsVo = new GoodsGiveVo();
        giveGoodsVo.setOrderId(orderId);
        giveGoodsVo.setPayAmount(AmountUtils.changeY2F(DBHelper.getInstance(mContext).getHadPayMoneyByOrderId(orderId)));
        giveGoodsVo.setYhAmount(getInstance(mContext).getTreatMentMoneyByOrderId(orderId, 1));
        giveGoodsVo.setTotalAmount(AmountUtils.changeY2F(DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderId, 1)));
        giveGoodsVo.setPartnerCode(partnerCode);
        ArrayList<GoodsDetailVo> orderDetailModels = new ArrayList<>();
        String[] ids = dataId.split("`");
        if (ids.length > 1) {
            OrderDishEntity presentOrderDish = DBHelper.getInstance(mContext).queryOneOrderDishEntity(ids[1]);
            if (presentOrderDish != null) {
                GoodsDetailVo orderDetailModel = new GoodsDetailVo();
                orderDetailModel.setOrderDetailId(ids[0]);
                orderDetailModel.setTypeId(presentOrderDish.getDishTypeId());
                orderDetailModel.setGoodsId(presentOrderDish.getDishId());
                orderDetailModel.setGoodsName(presentOrderDish.getDishName());
                orderDetailModel.setGoodsPrice(0);
                orderDetailModel.setGuigeId(presentOrderDish.getSpecifyId());
                orderDetailModel.setGuigeName(presentOrderDish.getDishSpecify());
                orderDetailModel.setDiscount(presentOrderDish.getIsAbleDiscount());
                orderDetailModel.setMakeId(presentOrderDish.getPracticeId());
                orderDetailModel.setMakeName(presentOrderDish.getDishPractice());
                orderDetailModel.setNum(presentOrderDish.getDishCount());
                orderDetailModel.setId(ids[1]);
                orderDetailModel.setIsGive(1);
                orderDetailModels.add(orderDetailModel);
            }
        }
        giveGoodsVo.setGoodsList(orderDetailModels);
        String data = JSON.toJSONString(giveGoodsVo);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.PRESENT_DISH), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "赠菜：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //同步金额
    public void syncMoney(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId) {
        GoodsRetreatVo giveGoodsVo = new GoodsRetreatVo();
        giveGoodsVo.setOrderId(orderId);
        giveGoodsVo.setPayAmount(AmountUtils.changeY2F(getInstance(mContext).getHadPayMoneyByOrderId(orderId)));
        giveGoodsVo.setYhAmount(getInstance(mContext).getYHMoney(orderId));
        giveGoodsVo.setTotalAmount(AmountUtils.changeY2F(DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderId, 1)));
        giveGoodsVo.setPartnerCode(partnerCode);
        giveGoodsVo.setCouzheng(AmountUtils.changeY2F(DBHelper.getInstance(mContext).getVoucherDishPrice(orderId)));
        giveGoodsVo.setGiveAmount(DBHelper.getInstance(mContext).getPresentMoney(orderId));
        Log.d("###", "总金额：" + giveGoodsVo.getTotalAmount() + "金额：" + giveGoodsVo.getPayAmount() + "," + giveGoodsVo.getYhAmount());
        String data = JSON.toJSONString(giveGoodsVo);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.DISCOUNT_PRICE), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "同步金额：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else if (publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                arg0.printStackTrace();
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //换桌
    public void changeTable(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String dataId) {
        String[] ids = dataId.split("`");
        if (ids.length > 1) {
            Log.d("###", "桌位：" + ids[0] + "," + ids[1]);
            long ts = System.currentTimeMillis();
            String sign = MD5Util.getMD5String(orderId + ids[0] + ids[1] + ts + mContext.getResources().getString(R.string.APP_KEY));
            Map<String, String> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("deskId", ids[0]);
            map.put("deskName", ids[1]);
            map.put("ts", String.valueOf(ts));
            map.put("sign", sign);
            VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.CHANGE_TABLE), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    try {
                        Log.d("###", "修改桌位：" + arg0);
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() == 0) {
                            DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                        } else {
                            DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                        }
                    } catch (Exception e) {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                }

                @Override
                public void onError(VolleyError arg0) {
                    arg0.printStackTrace();
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            });
        }
    }

    //叫号
    public void arrangeSort(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String dataId) {
        long ts = System.currentTimeMillis();
        final String arrangeId = dataId;
        Log.d("###", "叫号id：" + arrangeId);
        String sign = MD5Util.getMD5String(partnerCode + arrangeId + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("id", arrangeId);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.ARRANGE_CALL), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "叫号：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //叫号完成
    public void arrangeSuccess(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String dataId) {
        long ts = System.currentTimeMillis();
        final String arrangeId = dataId;
        String sign = MD5Util.getMD5String(partnerCode + arrangeId + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("id", arrangeId);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.ARRANGE_OVER), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "排队完成返回结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //同步营业数据
    public void syncOrder(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, final String dataId) {
        try {
            long ts = System.currentTimeMillis();
            ShopOrderVo shopOrderVo = new ShopOrderVo(mContext, dataId);
            String data = JSON.toJSONString(shopOrderVo);
            Log.d("###", "run: " + data);
            String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
            Map<String, String> map = new HashMap<>();
            map.put("partnerCode", partnerCode);
            map.put("data", data);
            map.put("ts", String.valueOf(ts));
            map.put("sign", sign);
            VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.UPLOAD_SHOPDATA), ts+"", map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    Log.d("###", "上传营业数据：" + arg0);
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() == 0) {
                            DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                        } else {
                            DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                        }
                    } catch (Exception e) {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                }

                @Override
                public void onError(VolleyError arg0) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            });
        } catch (Exception e) {

        }
    }

    //反结账
    public void returnOrder(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, final String dataId) {
        long ts = System.currentTimeMillis();
        String data = dataId;
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("id", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.RETURN_ORDER), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "反结账：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //拒绝预定
    public void denySchedule(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, final String dataId) {
        long ts = System.currentTimeMillis();
        String data = "{\"id\":\"" + dataId + "\",\"status\":\"" + 4 + "\"}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.SCHEDULE_UPDATE), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "拒绝预定返回结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //预定审核通过
    public void checkSchedule(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, final String dataId) {
        long ts = System.currentTimeMillis();
        String data = "{\"id\":\"" + orderId + "\",\"status\":\"" + 0 + "\",\"tableName\":\"" + dataId + "\"}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.SCHEDULE_UPDATE), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "预定通过返回结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //到店确认
    public void scheduleConfirm(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String dataId) {
        long ts = System.currentTimeMillis();
        String data = "{\"id\":\"" + orderId + "\",\"status\":\"" + 2 + "\",\"tableName\":\"" + dataId + "\"}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.SCHEDULE_UPDATE), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "到店确认返回结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //预定取消
    public void scheduleCancle(final UploadDataEntity uploadDataEntity, final String uploadDataId, final String orderId, String dataId) {
        long ts = System.currentTimeMillis();
        String data = "{\"id\":\"" + orderId + "\",\"status\":\"" + 5 + "\",\"tableName\":\"" + dataId + "\"}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.SCHEDULE_UPDATE), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "预定（已选桌位）取消返回结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //同步库存
    private void uploadStock(final UploadDataEntity uploadDataEntity, final String uploadDataId, ArrayList<StockBean> stockBeenes) {
        long ts = System.currentTimeMillis();
        String data = JSON.toJSONString(stockBeenes);
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.UPLOAD_STOCK), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "上传库存：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //微信点餐确认
    private void wxOrderConfirm(final UploadDataEntity uploadDataEntity, final String uploadDataId, String orderId) {
        long ts = System.currentTimeMillis();
        String data = "{\"key\":\"" + orderId + "\",\"type\":10}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.WX_ORDER_CONFIRM), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "微信点餐确认：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //微信点餐完成
    private void wxOrderOver(final UploadDataEntity uploadDataEntity, final String uploadDataId, String orderId) {
        long ts = System.currentTimeMillis();
        String data = "{\"id\":\"" + orderId + "\",\"op\":0}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.WXORDER_OVER), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "微信点餐完成：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //微信点餐反结账
    private void wxReturnOrder(final UploadDataEntity uploadDataEntity, final String uploadDataId, String orderId) {
        long ts = System.currentTimeMillis();
        String data = "{\"id\":\"" + orderId + "\",\"op\":1}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.WXORDER_OVER), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "微信订单反结账：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //更换会员卡
    public void syncVip(final UploadDataEntity uploadDataEntity, final String uploadDataId, String orderId, String vipNo) {
        long ts = System.currentTimeMillis();
        String partnerCode = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        String data = "{\"id\":\"" + orderId + "\",\"vipNo\":\"" + vipNo + "\"}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("ts", String.valueOf(ts));
        map.put("data", data);
        map.put("sign", sign);
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.CHANGE_VIP_CARD), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    //电话预订成功后短信通知
    public void sendScheduleMessage(final UploadDataEntity uploadDataEntity, final String uploadDataId, String orderId, String data) {
        long ts = System.currentTimeMillis();
        String partnerCode = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("ts", String.valueOf(ts));
        map.put("data", data);
        map.put("sign", sign);
        Log.d("###", "预定参数：" + map.toString());
        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.SCHEDULE_NOTICE), ""+ts, map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "电话预定短信通知：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        DBHelper.getInstance(mContext).clearUploadData(uploadDataId);
                    } else {
                        DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                    }
                } catch (Exception e) {
                    DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                DBHelper.getInstance(mContext).changeUploadTime(currentTime, uploadDataEntity);
            }
        });
    }

    @Override
    public void run() {
        Log.d("###", "获取数据启动");
        currentTime = System.currentTimeMillis();
        ArrayList<JpushMessageEntity> jpushMessages = DBHelper.getInstance(mContext).getAllJpushMessages();
        for (JpushMessageEntity jpushMessage :
                jpushMessages) {
            if (jpushMessage.getCount() > 10) {
                DBHelper.getInstance(mContext).clearJpushMessage(jpushMessage.getJpushMessageId());
            } else {
                if (jpushMessage.getCount() == 0 || currentTime - jpushMessage.getLastTime() > Math.pow(2, jpushMessage.getCount()) * 40 * 1000) {
                    switch (jpushMessage.getType()) {
                        case 0:
                            //外卖
                            getTakeout(jpushMessage);
                            break;
                        case 1:
                            //预定
                            getSchedule(jpushMessage);
                            break;
                        case 2:
                            //排号
                            getArrangeMessage(jpushMessage);
                            break;
                        case 3:
                            //到店点餐
                            getTableOrder(jpushMessage);
                            break;
                        case 4:
                            //催菜
                            getRemind(jpushMessage);
                            break;
                        case 5:
                            //获取美团外卖
                            getMeituanTakeout(jpushMessage);
                            break;
                        case 6://获取快餐版微信扫码点餐
                            getSnackOrder(jpushMessage);
                            break;
                    }
                }
            }
        }

        ArrayList<UploadDataEntity> uploadDataEntities = getInstance(mContext).getAllUploadData();
        for (UploadDataEntity uploadData :
                uploadDataEntities) {
            if (uploadData.getCount() > 10 && uploadData.getDataType() != 7) {
                DBHelper.getInstance(mContext).clearUploadData(uploadData.getUploadDataId());
            } else {
                if (uploadData.getCount() == 0 || currentTime - uploadData.getLastTime() > Math.pow(2, uploadData.getCount()) * 40 * 1000) {
                    switch (uploadData.getDataType()) {
                        case 0:
                            //加菜
                            addDish(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 1:
                            //退菜
                            retreatDish(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 2:
                            //赠菜
                            presentDish(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 3:
                            //同步金额
                            syncMoney(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId());
                            break;
                        case 4:
                            //修改桌位
                            changeTable(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 5:
                            //叫号
                            arrangeSort(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 6:
                            //叫号完成
                            arrangeSuccess(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 7:
                            //同步营业数据
                            syncOrder(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 8:
                            //反结账
                            returnOrder(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 9:
                            //拒绝预定
                            denySchedule(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 10:
                            //预定审核通过
                            checkSchedule(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 11:
                            //预定到店确认
                            scheduleConfirm(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 12:
                            //预定取消
                            scheduleCancle(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 13:
                            //同步库存
                            ArrayList<StockBean> stockBeenes = DBHelper.getInstance(mContext).getAllStock();
                            if (stockBeenes.size() > 0) {
                                uploadStock(uploadData, uploadData.getUploadDataId(), stockBeenes);
                            }
                            break;
                        case 14://微信单结账完毕同步状态
                            wxOrderOver(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId());
                            break;
                        case 15://微信单取消开台状态同步
                            break;
                        case 16://同步订单会员
                            syncVip(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 17://微信订单反结账时同步状态
                            wxReturnOrder(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId());
                            break;
                        case 18://电话预定短信通知
                            sendScheduleMessage(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId(), uploadData.getDataId());
                            break;
                        case 19://到店点餐确认
                            wxOrderConfirm(uploadData, uploadData.getUploadDataId(), uploadData.getOrderId());
                            break;
                    }
                }
            }
        }
    }

    private void sendMainMessage(Context context, int type) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        context.sendBroadcast(mIntent);
    }

    private void sendMainMessage(Context context, int type, WXMessageEntity wxMessageEntity) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        mIntent.putExtra("wxMessage", wxMessageEntity);
        context.sendBroadcast(mIntent);
    }
}
