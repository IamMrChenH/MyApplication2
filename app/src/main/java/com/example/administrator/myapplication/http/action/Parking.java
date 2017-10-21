package com.example.administrator.myapplication.http.action;

import android.content.Context;
import android.text.TextUtils;

import com.example.administrator.myapplication.Car;
import com.example.administrator.myapplication.GameView;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class Parking extends BaseAction {
    // action name
    public static final String TAG = "Parking";

    private Context context;

    public Parking(Context context) {
        this.context = context;
    }

    @Override
    protected synchronized String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            int carId = 0;
            int isPark = -1;
            long parkTime = -1;
            // 解析用户名
            if (jsonRequest.has("carID")) {
                carId = jsonRequest.getInt("carID");
            }
            if (jsonRequest.has("isPark")) {
                isPark = jsonRequest.getInt("isPark");
            }
            if (jsonRequest.has("parkTime")) {
                parkTime = jsonRequest.getLong("parkTime");
            }
            GameView gameView = MainActivity.getGameView();
            if (carId != 0 && isPark == 0) {
                Car car = gameView.getCars().get(carId - 1);
                if (TextUtils.isEmpty(car.getPartType())) {
                    jsonResponse.put("msg", "取消预约");
                    DatabaseUtil.addPartLog(context, carId, car.getBeginPartTime(), System.currentTimeMillis() - car.getBeginPartTime(), car.getPartType());
                } else {
                    jsonResponse.put("msg", "出库成功");
                }
                car.setParkAdd(car.getName());
                car.setPark(false);
                jsonResponse.put("result", "ok");
            } else if (carId != 0 && isPark > 0 && isPark < 10) {
                Car car = gameView.getCars().get(carId - 1);
                if (car.isPark() == true) {
                    if (TextUtils.isEmpty(car.getPartType())) {
                        jsonResponse.put("errorMsg", "该车已预约了车库");
                    } else {
                        jsonResponse.put("errorMsg", "该车已停在车库中");
                    }
                    jsonResponse.put("result", "failed");
                    return jsonResponse.toString();
                }
                List<Car> parkCar = gameView.getCars();
                if (isPark > 0) {
                    for (Car carTemp : parkCar) {

                        if (carTemp.isPark() && carTemp.getParkAdd() == (isPark - 1)) {
                            jsonResponse.put("result", "failed");
                            jsonResponse.put("errorMsg", "当前车位已有车辆");
                            return jsonResponse.toString();
                        }
                    }
                    car.setParkAdd(isPark - 1);
                    car.setParkTime(parkTime);
                    car.setPark(true);
                    jsonResponse.put("result", "ok");
                } else {
                    car.setPark(false);
                    jsonResponse.put("result", "ok");
                }
            } else {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "输入的车库参数不正确");
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
