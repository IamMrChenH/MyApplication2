package com.example.administrator.myapplication.http.action;

import android.content.Context;
import android.util.Log;

import com.example.administrator.myapplication.db.DatabaseUtil;
import com.example.administrator.myapplication.util.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

/**
 * @author zongbingwu 用户登录请求处理类,从客户端请求body中取得用户名和密码
 *         然后从检查该用户是否存在，并将结果返回给http服务器或socket服务器
 */
public class GetTrafficLightConfigAciton extends BaseAction {
	// action name
	public static final String TAG = "GetTrafficLightConfigAciton";

	private Context context;
	private  DatabaseUtil databaseUtil;
	public GetTrafficLightConfigAciton(Context context) {
		this.context = context;
	}

	// jason协议的解析函数
	@Override
	protected String jasonPorcess(String param) {
		JSONObject jsonResponse = new JSONObject();
		try {
			JSONObject jsonRequest = new JSONObject(param);
			// 处理登陆请求
			int id = 0;
			// 解析
			if (jsonRequest.has("TrafficLightID")) {
				id = jsonRequest.getInt("TrafficLightID");
			}

			if (id!=0) {
				jsonResponse.put("result", "ok");
				databaseUtil=new DatabaseUtil();
				Log.e("233",databaseUtil.getParkMoney(context));
			} else {
				jsonResponse.put("result", "failed");
				jsonResponse.put("RedTime", "0");
				jsonResponse.put("GreenTime", "0");
				jsonResponse.put("YellowTime", "0");
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

	// soap协议的解析函数
	@Override
	protected String soapPorcess(String param) {

		return null;
	}

}
