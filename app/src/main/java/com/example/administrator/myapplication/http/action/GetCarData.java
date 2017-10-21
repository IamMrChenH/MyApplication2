package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.Car;
import com.example.administrator.myapplication.GameView;
import com.example.administrator.myapplication.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class GetCarData extends BaseAction {
    // action name
    public static final String TAG = "GetCarData";

    private Context context;

    public GetCarData(Context context) {
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
                GameView gameView=MainActivity.getGameView();
                Car car=gameView.getCars().get(carId-1);
                if(car.getLuxian()==0){
                jsonResponse.put("Luxian","城市到ETC");}
                if(car.getLuxian()==678){
                jsonResponse.put("Luxian","学校--小区");}
                if(car.getLuxian()==1100){
                jsonResponse.put("Luxian","学校--医院");}
                jsonResponse.put("Round",car.getRound());
                jsonResponse.put("Speed",car.getSpeed());
                jsonResponse.put("X",car.getX());
                jsonResponse.put("Y",car.getY());
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
