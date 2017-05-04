package jufeng.juyancash.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.andserver.RequestHandler;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.TableEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class RequestGetTablesHandler implements RequestHandler {
    private Context mContext;

    public RequestGetTablesHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        tableEntities.addAll(DBHelper.getInstance(mContext).queryAllTableData());
        StringEntity stringEntity;
        PublicModule publicModule = new PublicModule();
        publicModule.setCode(0);
        publicModule.setData(JSON.toJSONString(tableEntities));
        publicModule.setMessage("获取桌位列表成功！");
        stringEntity = new StringEntity(JSON.toJSONString(publicModule), "utf-8");
        response.setEntity(stringEntity);
    }
}