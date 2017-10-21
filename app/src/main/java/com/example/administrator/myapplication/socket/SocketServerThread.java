package com.example.administrator.myapplication.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.example.administrator.myapplication.http.action.BaseAction;

public class SocketServerThread extends Thread {
	private static final String TAG = "SocketServerThread";
	private Context mContext;
	private ServerSocket mSever;// 服务器socket
	private boolean mLogEnable = true; // 日志开关
	private volatile boolean mIsRun = true; // 线程控制开关

	public SocketServerThread(Context context, int port, boolean logEnable) {
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
							Log.d(TAG, "socket accept:" + socket.getInetAddress().toString());
						}
						BufferedReader in;
						try {
							in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
							PrintWriter out = new PrintWriter(socket.getOutputStream());

							while (!socket.isClosed()) {
								// 读取客户端发送的内容
								String receiveStr;
								receiveStr = in.readLine();
								if (receiveStr == null)
									break;
								if (receiveStr != null && !receiveStr.equals("")) {
									String action = "";
									String type = "";
									String strBody = "";

									// 从内容中解析协议类型，action和协议body
									try {
										JSONObject jsonRespObj = new JSONObject(receiveStr);
										if (jsonRespObj != null) {
											// 解析协议类型
											if (jsonRespObj.has("type")) {
												type = jsonRespObj.getString("type");
											}
											// 解析action
											if (jsonRespObj.has("action")) {
												action = jsonRespObj.getString("action");
											}
											// 解析协议body
											if (jsonRespObj.has("body")) {
												String tmpStr = jsonRespObj.getString("body");
												// base64解码
												byte tmpB[] = Base64.decode(tmpStr, Base64.DEFAULT);
												strBody = new String(tmpB);
												
											}

											if (mLogEnable) {
												Log.d(TAG, "socket server receive, type:" + type + ",action:" + action);
												Log.d(TAG, "socket server receive, body:" + strBody);
											}
											// 根据协议类型和action，解析协议内容并返回相关结果
											String socketResp = BaseAction.disposeAction("socket", type, action, strBody, mContext, socket.getInetAddress().toString());
											
											Log.e("get", "socket server receive, body:" + "+get"+socketResp);
											out.println(Base64.encodeToString(socketResp.getBytes(), Base64.DEFAULT));
										}
									} catch (JSONException e) {
										e.printStackTrace();
										out.println("");
									} finally {
										out.flush();
									}
								}
							}
							// 主动关闭socket连接
							socket.close();
							if (mLogEnable) {
								Log.d(TAG, "socket close:" + socket.getInetAddress().toString());
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
