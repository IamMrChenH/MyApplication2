package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.Violation;
import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class GetViolation extends BaseAction {
    // action name
    public static final String TAG = "GetViolation";

    private Context context;

    public GetViolation(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            int carId = 0;
            // 解析用户名
            if (jsonRequest.has("carID")) {
                carId = jsonRequest.getInt("carID");
            }
            if (carId != 0) {
                jsonResponse.put("result", "ok");
                List<Violation> violations = DatabaseUtil.getV(context, carId + "");
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject;
                for (Violation violation : violations) {
                    jsonObject = new JSONObject();
                    jsonObject.put("id", violation.getId());
                    jsonObject.put("vTime", violation.getvTime());
                    jsonObject.put("vMsg", violation.getvMsg());
                    jsonObject.put("overTime", violation.getOverTime());
                    jsonObject.put("flag", violation.getFlag());
                    jsonObject.put("carId", violation.getCarId());
                    jsonObject.put("score", violation.getScore());
                    jsonObject.put("money", violation.getMoney());
                    jsonObject.put("vXY", violation.getvXY());
                    jsonObject.put("vID", violation.getvID());
                    jsonObject.put("vOffice", violation.getvOffice());
                    jsonObject.put("moneyFlag", violation.getMoneyFlag());
                    JSONArray photo = new JSONArray();
                    String[] temp = violation.getPhoto().split(",");
                    for (String s : temp) {
                        photo.put(s);
                    }
                    jsonObject.put("photo", photo);
                    jsonArray.put(jsonObject);
                }
                jsonResponse.put("violations", jsonArray);
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
