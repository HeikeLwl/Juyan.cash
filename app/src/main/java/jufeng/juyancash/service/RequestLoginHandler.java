package jufeng.juyancash.service;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.EmployeeEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class RequestLoginHandler implements RequestHandler {
    private Context mContext;

    public RequestLoginHandler(Context context){
        this.mContext = context;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);

        Log.d("AndServer", "Params: " + params.toString());

        String userName = params.get("username");
        String password = params.get("password");
        EmployeeEntity employeeEntity = DBHelper.getInstance(mContext).employeeLogin(userName, password);
        StringEntity stringEntity;
        PublicModule publicModule = new PublicModule();
        if(employeeEntity != null && employeeEntity.getAuthCashier() == 1){
            publicModule.setCode(0);
            publicModule.setData(JSON.toJSONString(employeeEntity));
            publicModule.setMessage("登录成功！");
        }else{
            publicModule.setCode(-1);
            publicModule.setData(JSON.toJSONString(employeeEntity));
            publicModule.setMessage("登录失败！");
        }
        stringEntity = new StringEntity(JSON.toJSONString(publicModule), "utf-8");
        response.setEntity(stringEntity);
    }
}