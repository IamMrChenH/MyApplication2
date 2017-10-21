package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class AddMoney extends BaseAction {
    // action name
    public static final String TAG = "AddMoney";

    private Context context;

    public AddMoney(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            int carId = 0;
            int type = -1;
            double money = 0.12;
            // 解析用户名
            if (jsonRequest.has("carID")) {
                carId = jsonRequest.getInt("carID");
            }
            if (jsonRequest.has("type")) {
                type = jsonRequest.getInt("type");
            }
            if (jsonRequest.has("money")) {
                money = jsonRequest.getDouble("money");
            }
            if (carId == 0) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入正确的小车ID");
            } else if (money <= 0) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入正确的金额");
            } else if (type == -1) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入正确的充值类型");
            } else {
                jsonResponse.put("result", "ok");
                DatabaseUtil.updateCarMoney(context, carId + "", money,type);
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
