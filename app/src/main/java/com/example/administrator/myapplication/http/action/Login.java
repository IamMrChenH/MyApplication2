package com.example.administrator.myapplication.http.action;

import android.content.Context;
import android.util.Log;

import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class Login extends BaseAction {
    // action name
    public static final String TAG = "Login";

    private Context context;

    public Login(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            String username = "";
            String userpwd = "";
            // 解析用户名
            if (jsonRequest.has("username")) {
                username = jsonRequest.getString("username");
            }
            if (jsonRequest.has("userpwd")) {
                userpwd = jsonRequest.getString("userpwd");
            }
            if (username.equals("")) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入用户名");
            } else if (userpwd.equals("")) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入密码");
            } else {
                String phone = "";
                String type = "";
                String temp = DatabaseUtil.getU(context, username, userpwd);
                phone = temp.split(",")[0];
                type = temp.split(",")[1];
                Log.e("233", phone);
                if (phone.equals("") || phone == null) {
                    jsonResponse.put("result", "failed");
                    jsonResponse.put("errorMsg", "所输入的帐号与密码不匹配");
                } else {
                    jsonResponse.put("result", "ok");
                    jsonResponse.put("type", type);
                    String carID = "";
                    carID = DatabaseUtil.getUCar(context, phone);
                    if (carID.equals("")) {
                        jsonResponse.put("errorMsg", "没有对应的小车");
                    } else {
                        jsonResponse.put("carID", carID);
                    }

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
