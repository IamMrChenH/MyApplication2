package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.CarMessage;
import com.example.administrator.myapplication.ParkLog;
import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class GetPartLog extends BaseAction {
    // action name
    public static final String TAG = "GetPartLog";

    private Context context;

    public GetPartLog(Context context) {
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
                List<ParkLog> parkLogs = DatabaseUtil.getParkLog(context, carId + "");
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                for (ParkLog parkLog : parkLogs) {
                    jsonObject = new JSONObject();
                    int id = parkLog.getParkCar();
                    CarMessage carMessage = DatabaseUtil.getCarMessage(context, id + "");
                    jsonObject.put("longTime", parkLog.getLongTime() + "");
                    jsonObject.put("moneyCast", parkLog.getMoneyCast());
                    jsonObject.put("parkCar", parkLog.getParkCar()+"");
                    jsonObject.put("parkTime", parkLog.getParkTime() + "");
                    jsonObject.put("cost", parkLog.getCost()+"");
                    jsonObject.put("type", parkLog.getType());
                    jsonObject.put("carname", carMessage.getCarName());
                    jsonObject.put("carnum", carMessage.getCarNum());
                    jsonObject.put("carphone", carMessage.getCarPhone());
                    jsonArray.put(jsonObject);
                }
                jsonResponse.put("parkLogs", jsonArray);
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
