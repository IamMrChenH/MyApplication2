package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class UpdateFees extends BaseAction {
    // action name
    public static final String TAG = "UpdateFees";

    private Context context;

    public UpdateFees(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            String type = "";
            String money = "";
            // 解析用户名
            if (jsonRequest.has("type")) {
                type = jsonRequest.getString("type");
            }
            if (jsonRequest.has("money")) {
                money = jsonRequest.getString("money");
            }

            if (type.equals("")) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入类型");
            } else if (money.equals("")) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入单价");
            } else {
                if (DatabaseUtil.updateFees(type, money, context) == 1) {
                    jsonResponse.put("result", "ok");
                } else {
                    jsonResponse.put("result", "failed");
                    jsonResponse.put("errorMsg", "用户名与电话号码不匹配");
                }

            }
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
