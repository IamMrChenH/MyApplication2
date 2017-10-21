package com.example.administrator.myapplication.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

public class NotificationServerThread extends Thread {
	public static final String PM25_HIGH = "pm2.5 high";
	public static final String CO2_HIGH = "co2 high";
	public static final String CO2_LOW = "co2 low";
	public static final String LIGHT_HIGH = "light high";
	public static final String LIGHT_LOW = "light low";

	public static final String AIR_TMPER_HIGH = "air tmper high";
	public static final String AIR_TMPER_LOW = "air tmper low";
	public static final String AIR_HUMID_HIGH = "air humid high";
	public static final String AIR_HUMID_LOW = "air humid low";

	public static final String SOIL_TMPER_HIGH = "soil tmper high";
	public static final String SOIL_TMPER_LOW = "soil tmper low";
	public static final String SOIL_HUMID_HIGH = "soil humid high";
	public static final String SOIL_HUMID_LOW = "soil humid low";

	public enum SensorStatus {
		normal, low, high,
	}

	private static final String TAG = "NotificationServerThread";
	private Context mContext;
	private ServerSocket mSever;// 服务器socket
	private boolean mLogEnable = true; // 日志开关
	private volatile boolean mIsRun = true; // 线程控制开关

	private final static byte[] _singleLock = new byte[0];
	private long mNotifiId = 0;
	private SensorStatus mPm25Status = SensorStatus.normal;
	private SensorStatus mCo2Status = SensorStatus.normal;
	private SensorStatus mLightStatus = SensorStatus.normal;
	private SensorStatus mAirTStatus = SensorStatus.normal;
	private SensorStatus mAirHStatus = SensorStatus.normal;
	private SensorStatus mSoilTStatus = SensorStatus.normal;
	private SensorStatus mSoilHStatus = SensorStatus.normal;

	public NotificationServerThread(Context context, int port, boolean logEnable) {
		mContext = context;
		try {
			mSever = new ServerSocket(port); // 创建服务器socket
			mLogEnable = logEnable;
			mIsRun = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		super.run();
		while (mIsRun) {
			try {
				// 当有客户端连接上时，创建客户端
				final Socket socket = mSever.accept();
				new Thread(new Runnable() {
					public void run() {
						if (mLogEnable) {
							Log.d(TAG, "notification socket accept:"
									+ socket.getInetAddress().toString());
						}
						BufferedReader in;
						try {
							in = new BufferedReader(new InputStreamReader(
									socket.getInputStream(), "UTF-8"));
							PrintWriter out = new PrintWriter(socket
									.getOutputStream());

							while (!socket.isClosed()) {
								// 读取客户端发送的内容
								String receiveStr;
								receiveStr = in.readLine();
								if (receiveStr == null)
									break;
								if (receiveStr != null
										&& !receiveStr.equals("")) {
									if (receiveStr.equals("getNotification")) {
										String socketResp = "";
										String notifiMsg = "";

										notifiMsg = getNotificationMsg();
										// }

										JSONObject jsonResponse = new JSONObject();
										try {
											jsonResponse.put("notifiId",
													mNotifiId);
											jsonResponse.put("notifiMsg",
													notifiMsg);
											socketResp = jsonResponse
													.toString();
											Log.d("test1", socketResp);
										} catch (JSONException e) {
											e.printStackTrace();
										}
										out.println(Base64.encodeToString(
												socketResp.getBytes(),
												Base64.DEFAULT));
									}
									out.flush();
								}
							}
							// 主动关闭socket连接
							socket.close();
							if (mLogEnable) {
								Log.d(TAG, "notification socket close:"
										+ socket.getInetAddress().toString());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();// 开始启动线程

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getNotificationMsg() {
		String notifiMsg = "";
		// pm2.5偏高
		if (mPm25Status == SensorStatus.high) {
			notifiMsg += PM25_HIGH + ",";
		}
		// Co2偏高
		if (mCo2Status == SensorStatus.high) {
			notifiMsg += CO2_HIGH + ",";
		}
		if (mCo2Status == SensorStatus.low) {
			notifiMsg += CO2_LOW + ",";
		}
		if (mLightStatus == SensorStatus.high) {
			notifiMsg += LIGHT_HIGH + ",";
		}
		if (mLightStatus == SensorStatus.low) {
			notifiMsg += LIGHT_LOW + ",";
		}
		if (mAirTStatus == SensorStatus.high) {
			notifiMsg += AIR_TMPER_HIGH + ",";
		}
		if (mAirTStatus == SensorStatus.low) {
			notifiMsg += AIR_TMPER_LOW + ",";
		}
		if (mAirHStatus == SensorStatus.high) {
			notifiMsg += AIR_HUMID_HIGH + ",";
		}
		if (mAirHStatus == SensorStatus.low) {
			notifiMsg += AIR_HUMID_LOW + ",";
		}
		if (mSoilTStatus == SensorStatus.high) {
			notifiMsg += SOIL_TMPER_HIGH + ",";
		}
		if (mSoilTStatus == SensorStatus.low) {
			notifiMsg += SOIL_TMPER_LOW + ",";
		}
		if (mSoilHStatus == SensorStatus.high) {
			notifiMsg += SOIL_HUMID_HIGH + ",";
		}
		if (mSoilHStatus == SensorStatus.low) {
			notifiMsg += SOIL_HUMID_LOW + ",";
		}
		return notifiMsg;
	}



	// 停止服务器端server
	public void stopServer() {
		mIsRun = false;
		try {
			mSever.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
