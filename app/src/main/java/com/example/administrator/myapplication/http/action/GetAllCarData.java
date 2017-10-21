package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.Car;
import com.example.administrator.myapplication.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class GetAllCarData extends BaseAction {
    // action name
    public static final String TAG = "GetAllCarData";

    private Context context;

    public GetAllCarData(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            jsonResponse.put("result", "ok");
            List<Car> cars=MainActivity.getGameView().getCars();
            JSONObject json;
            JSONArray array=new JSONArray();
            for(Car car :cars){
                json=new JSONObject();
                if(car.getLuxian()==0){
                    json.put("Luxian","城市到ETC");}
                if(car.getLuxian()==678){
                    json.put("Luxian","学校--小区");}
                if(car.getLuxian()==1100){
                    json.put("Luxian","学校--医院");}
                json.put("Round",car.getRound());
                json.put("Speed",car.getSpeed());
                json.put("X",car.getX());
                json.put("Y",car.getY());
                array.put(json);
            }
            jsonResponse.put("cars", array);
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
