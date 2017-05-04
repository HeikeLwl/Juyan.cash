package jufeng.juyancash.util;

import android.util.Log;

import com.chinaums.mis.bank.BankDAO;
import com.chinaums.mis.bank.ICallBack;
import com.chinaums.mis.bean.RequestPojo;
import com.chinaums.mis.bean.ResponsePojo;
import com.chinaums.mis.bean.TransCfx;

/**
 * Created by Administrator102 on 2017/3/28.
 */

public class BankUtil {

    public static void test(){
        BankDAO bankDAO = new BankDAO();
        RequestPojo requestPojo = new RequestPojo();
        requestPojo.setAmount("000000000001");
        requestPojo.setOperId("12345678");
        requestPojo.setPosId("12345678");
        requestPojo.setTransMemo("01");
        requestPojo.setTransType("00");
        TransCfx transCfx = new TransCfx();
        transCfx.setSsl_on(1);
        transCfx.setIp("upos.chinaums.com");
        transCfx.setPort(19003);
        transCfx.setTpdu("6000030000");
        transCfx.setMchtId("103290070111407");
        transCfx.setTermId("12340026");
        transCfx.setAuthSN("25C7A2BD9F3A0F1F1336B552E16D5C614746D50F72A078C7803CD12EDA8B5006");
        transCfx.setDevPath("/dev/ttyS0");
        transCfx.setBaudRate(115200);
        bankDAO.getCallBack(new ICallBack() {
            @Override
            public void getCallBack(String s, String s1) {
                Log.d("bank", "交易返回结果："+s+"--------"+s1);
            }
        });
        ResponsePojo responsePojo = bankDAO.bankall(transCfx,requestPojo);
        Log.d("bank", "test: "+responsePojo.toString());
    }
}
