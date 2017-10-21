package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class AddViolation extends BaseAction {
    // action name
    public static final String TAG = "AddViolation";

    private Context context;

    public AddViolation(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            int carId = 0;
            String msg = "";
            String vXY = "";
            String vID = "";
            String vOffice = "";
            String photo = "";
            String money = "";
            int grade = 0;
            //解析车ID 违规信息 扣除分数
            if (jsonRequest.has("carID")) {
                carId = jsonRequest.getInt("carID");
            }
            if (jsonRequest.has("msg")) {
                msg = jsonRequest.getString("msg");
            }
            if (jsonRequest.has("grade")) {
                grade = jsonRequest.getInt("grade");
            }
            if (jsonRequest.has("vXY")) {
                vXY = jsonRequest.getString("vXY");
            }
            if (jsonRequest.has("vID")) {
                vID = jsonRequest.getString("vID");
            }
            if (jsonRequest.has("vOffice")) {
                vOffice = jsonRequest.getString("vOffice");
            }
            if (jsonRequest.has("photo")) {
                photo = jsonRequest.getString("photo");
            }
            if (jsonRequest.has("money")) {
                money = jsonRequest.getString("money");
            }
            if (carId != 0) {
                jsonResponse.put("result", "ok");
                DatabaseUtil.insertV(carId + "", System.currentTimeMillis() + "", msg, grade + "",money + "", vXY, vID, vOffice, photo, context);
            } else {
                jsonResponse.put("result", "failed");
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
