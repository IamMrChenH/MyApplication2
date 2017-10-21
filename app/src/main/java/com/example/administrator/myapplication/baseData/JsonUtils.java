package com.example.administrator.myapplication.baseData;

import org.json.JSONObject;

public class JsonUtils {
	/**
	 * JSon 工具类
	 * 
	 * @param name
	 * @return
	 */
	public static JSONObject find(String name) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", name);
			return jsonObject;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static JSONObject getname() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", "admin");
			return jsonObject;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static JSONObject con(String name, int value) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", "admin");
			jsonObject.put(name, value);
			return jsonObject;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static JSONObject set(String minname, int min, String maxname,
			int max) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", "admin");
			jsonObject.put(minname, min);
			jsonObject.put(maxname, max);
			return jsonObject;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static String jiexi(String rs) {
		try {
			JSONObject jsonObject = new JSONObject(rs);
			return jsonObject.getString("result");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static String jiexiemail(String rs) {
		try {
			JSONObject jsonObject = new JSONObject(rs);
			return jsonObject.getString("email");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}


	public static void jiexicontrol(String rs) {
		try {
			JSONObject jsonObject = new JSONObject(rs);
			for (int i = 0; i < 4; i++) {
				BaseData.control_data[i] = jsonObject
						.getInt(BaseData.control_name[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



}
