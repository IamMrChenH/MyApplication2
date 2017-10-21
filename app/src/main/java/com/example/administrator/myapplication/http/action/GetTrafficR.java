package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.TrafficR;
import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class GetTrafficR extends BaseAction {
    // action name
    public static final String TAG = "GetTrafficR";

    private Context context;

    public GetTrafficR(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put("result", "ok");
            List<TrafficR> trafficRs = DatabaseUtil.getTrafficR(context);
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            for (TrafficR trafficR : trafficRs) {
                jsonObject = new JSONObject();
                jsonObject.put("id", trafficR.getId());
                jsonObject.put("money", trafficR.getMoney());
                jsonObject.put("rule", trafficR.getRule());
                jsonObject.put("score", trafficR.getScore());
                jsonArray.put(jsonObject);
            }
            jsonResponse.put("trafficr", jsonArray);

            // 返回结果
            return jsonResponse.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String soapPorcess(String param) {

        return null;
    }

}
