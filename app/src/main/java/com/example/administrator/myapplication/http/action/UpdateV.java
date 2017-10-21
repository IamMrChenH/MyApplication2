package com.example.administrator.myapplication.http.action;

import android.content.Context;

import com.example.administrator.myapplication.db.DatabaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zongbingwu 用户注册请求处理类,从客户端请求body中取得用户名，密码和邮箱
 *         然后向数据库中插入一条记录，并将结果返回给http服务器或socket服务器
 */
public class UpdateV extends BaseAction {
    // action name
    public static final String TAG = "UpdateV";

    private Context context;

    public UpdateV(Context context) {
        this.context = context;
    }

    @Override
    protected String jasonPorcess(String param) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = new JSONObject(param);
            // 处理请求
            String vID = "";
            int flag = -1;
            int moneyFlag = -1;
            String overTime = "";
            int score = -1;
            double money = -1;
            // 解析用户名
            if (jsonRequest.has("vID")) {
                vID = jsonRequest.getString("vID");
            }
            if (jsonRequest.has("flag")) {
                flag = jsonRequest.getInt("flag");
            }
            if (jsonRequest.has("moneyFlag")) {
                moneyFlag = jsonRequest.getInt("moneyFlag");
            }
            if (jsonRequest.has("overTime")) {
                overTime = jsonRequest.getString("overTime");
            }
            if (jsonRequest.has("money")) {
                money = jsonRequest.getDouble("money");
            }
            if (jsonRequest.has("score")) {
                score = jsonRequest.getInt("score");
            }
            if (money == -1 || score == -1) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入正确的罚款金额和应扣分数");
            } else if (flag == -1 || moneyFlag == -1) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入正确的标记");
            } else if (overTime.equals("")) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入时间");
            } else if (vID.equals("")) {
                jsonResponse.put("result", "failed");
                jsonResponse.put("errorMsg", "请输入正确的编号");
            } else {
                if (DatabaseUtil.updateV(score, money, overTime, flag, moneyFlag, vID, context) == 1) {
                    jsonResponse.put("result", "ok");
                } else {
                    jsonResponse.put("result", "failed");
                    jsonResponse.put("errorMsg", "驾照分不足。请尽快去参加学习。");
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
