package com.example.administrator.myapplication.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;

import com.example.administrator.myapplication.AppConfig;
import com.example.administrator.myapplication.http.JasonSoapHttpServer;
import com.example.administrator.myapplication.http.VideoServer;
import com.example.administrator.myapplication.socket.NotificationServerThread;
import com.example.administrator.myapplication.socket.SocketServerThread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;


/**
 * Net server控制器类，其同时控制一个http服务器对象和一个socket服务器对象
 * 
 * @author asus
 * 
 */
public class NetServerService extends Service {
	// http服务器对象
	private JasonSoapHttpServer jsonHttpd;
	private VideoServer videoServer;
	// socket服务器对象
	private SocketServerThread mServerThread;
	// notification服务器对象
	private NotificationServerThread mNotifiThread;

	private final static long LOG_FILE_MAX_SIZE = 1024 * 1024;
	private String mPreLogStr = "";
	private File mLogFile;
	private LogServiceBroadcast mBroadcast;

	/**
	 * 绑定Net server
	 * 
	 * @see Service#onBind(Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 创建Net server
	 * 
	 * @see Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();


		// 在创建该服务时，要立刻注册相关广播接收器
		mBroadcast = new LogServiceBroadcast();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction("com.lenovo.agriculture.server.broadcat.saveLog");
		registerReceiver(mBroadcast, intentFilter);
	}

	// Net server运行标记，防止重复启动
	private static boolean ifrun = false;

	/**
	 * 开始启动Net server
	 * 
	 * @see Service#onStart(Intent, int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (!ifrun) {
			// 创建http服务器
			try {
				jsonHttpd = new JasonSoapHttpServer(this.getLocalIpAddress(),
						AppConfig.HTTP_SERVER_PORT, getApplicationContext(),
						false);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			videoServer = new VideoServer();
			// 创建socket服务器
			mServerThread = new SocketServerThread(getApplicationContext(),
                    AppConfig.SOCKET_SERVER_PORT, false);
			// 创建notification服务器
//				mNotifiThread = new NotificationServerThread(
//						getApplicationContext(), AppConfig.NOTIFI_SERVER_PORT,
//						false);
			// 启动http服务
			try {
				// 启动http服务器
				jsonHttpd.start();
				videoServer.start();
				System.out.println("JasonHttpd Started. ");
				// 启动socket服务器
				mServerThread.start();
				System.out.println("Socket server Started. ");
				// 启动notification服务器
//				mNotifiThread.start();
				System.out.println("Notification server Started. ");
			} catch (Exception ioe) {
				System.err.println("Couldn't start JasonHttpd:\n" + ioe);
				ioe.printStackTrace();
				System.exit(-1);
			}

			ifrun = true;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 注销Net server
	 * 
	 * @see Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			// 停止http服务器
			jsonHttpd.stop();
			videoServer.stop();
			// 停止socket服务器
			mServerThread.stopServer();
			// 停止notification服务器
//			mNotifiThread.stopServer();
			ifrun = false;
		} catch (Exception e) {
		}
		if (mBroadcast != null) {
			unregisterReceiver(mBroadcast);
		}
	}

	/**
	 * 接收广播存储日志
	 * 
	 * @author asus
	 *
	 */
	class LogServiceBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 用来接收log message
			String message = intent.getStringExtra("log_message");
			if (message == null
					|| (message.equals(mPreLogStr) && mPreLogStr
							.indexOf("[getSensor]") >= 0)) {
				return;
			}
			mPreLogStr = message;
			FileOutputStream outStream;
			try {
				outStream = new FileOutputStream(mLogFile, true);
				outStream.write(message.getBytes());
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取本地IP地址
	 * 
	 * @return IP地址
	 * @throws UnknownHostException
	 */
	private String getLocalIpAddress() throws UnknownHostException {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String IP;
		int ipAddress = wifiInfo.getIpAddress();
		IP = intToIp(ipAddress);
//		Log.e("ip===================================", Formatter.formatIpAddress(ipAddress)+"dasdasdsa");
		return IP;
	}

	/**
	 * 将ip地址转成字符串
	 * 
	 * @param i
	 *            未转化的IP
	 * @return 转化后的IP
	 */
	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF);
	}
}