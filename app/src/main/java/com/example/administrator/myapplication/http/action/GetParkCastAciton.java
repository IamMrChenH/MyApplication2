package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.Car;
import com.example.administrator.myapplication.GameView;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zongbingwu 用户登录请求处理类,从客户端请求body中取得用户名和密码
 *         然后从检查该用户是否存在，并将结果返回给http服务器或socket服务器
 */
public class GetParkCastAciton extends BaseAction {
    // action name
    public static final String TAG = "GetParkCastAciton";

    private Context context;
    private DatabaseUtil databaseUtil;

    public GetParkCastAciton(Context context) {
        this.context = context;
    }

    // jason协议的解析函数
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
                // 处理请求
                jsonResponse.put("result", "ok");
                databaseUtil = new DatabaseUtil();
                jsonResponse.put("cast", databaseUtil.getParkMoney(context));
                GameView gameView = MainActivity.getGameView();

                jsonResponse.put("ispark", (gameView.getCars().get(carId - 1).isPark() ? 1 : 0));
                List<Car> parkCars = gameView.getCars();
                jsonResponse.put("freenum", 10 - gameView.getPartCars().size() + "");
                JSONArray array = new JSONArray();
                JSONObject object;
                Car car;
                for (int i = 0; i < parkCars.size(); i++) {
                    car = parkCars.get(i);
                    if (car.isPark()) {
                        object = new JSONObject();
                        object.put("car", car.getName() + "");
                        object.put("parkAdd", car.getParkAdd() + "");
                        array.put(object);
                    }
                }
                jsonResponse.put("parkArray", array);

            }
            return jsonResponse.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // soap协议的解析函数
    @Override
    protected String soapPorcess(String param) {

        // 返回结果
        return null;
    }

}
