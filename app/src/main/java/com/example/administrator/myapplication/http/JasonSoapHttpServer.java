package com.example.administrator.myapplication.http;

import android.content.Context;
import android.util.Log;

import com.example.administrator.myapplication.http.action.BaseAction;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Map;


/**
 * @author zongbingwu Http服务器，支持jason和soap
 */
public class JasonSoapHttpServer extends NanoHTTPD {
    private static String TAG = "JasonHttpServer";
    private Context context;
    private boolean logEnable = true;// 日志开关

    public JasonSoapHttpServer(String ip, int port, Context context,
                               boolean logEnable) throws UnknownHostException {
        super(ip, port);
        this.context = context;
        this.logEnable = logEnable;
    }

    @Override
    public Response serve(IHTTPSession session) {

        session.getHeaders();
        String httpResp = "Can't find action";
        String strBody = "";
        if (session.getMethod() == Method.POST) {
            strBody = parsebody(session);
        }

        // 读取请求 url
        String strUrl = session.getUri();
        if (logEnable) {
            Log.i(TAG, "Http url:" + strUrl + ", body:" + strBody);
        }

        // 解析出请求名称和协议类型
        String actionName = "";
        String actionType = "";
        String tmpArray[] = strUrl.split("/");
        for (int i = 0; i < tmpArray.length; i++) {
            String str = tmpArray[i];

            if (str.equals("type")) {
                actionType = tmpArray[i + 1];
            }
            if (str.equals("action")) {
                actionName = tmpArray[i + 1];
            }
        }

        // 如果是soap类型的请求，action名称应该从soapaction参数里获取
        Map<String, String> headers = session.getHeaders();
        if (actionType.equals("soap")) {
            if (headers.containsKey("soapaction")) {
                actionName = headers.get("soapaction").toString();
            }
        }
        if (logEnable) {
            Log.i(TAG, "protocol action:" + actionName + ", type:" + actionType);
        }


        // 根据协议类型和action，解析协议内容并返回相关结果
        httpResp = BaseAction.disposeAction("http", actionType, actionName,
                strBody, context, headers.get("remote-addr").toString());


        httpResp += "\n";
        if (logEnable) {
            Log.i(TAG, "return :" + httpResp);
        }
        //s http 响应消息
        return new NanoHTTPD.Response(httpResp);
    }

    // 解析http body
    private String parsebody(IHTTPSession session) {
        String body = "";
        try {
            // 读取输入流
            InputStream is = session.getInputStream();
            if (is == null) {
                Log.i(TAG, "session.getInputStream() is null!");
                return body;
            }

            long size = 0;
            Map<String, String> headers = session.getHeaders();
            if (headers.containsKey("content-length")) {
                size = Integer.parseInt(headers.get("content-length"));
                Log.e("233", "size: " + size);


            }
            String type = "";
            if (headers.containsKey("filetype")) {
                type = headers.get("filetype").toString();
                Log.e("233", "type: " + type);
                if (type.equals("png"))
                    return type;
            }


            if (size > 0) {
                // 假定消息不超过8192byte
                int bufsize = 8192;
                byte[] buf = new byte[bufsize];
                int rlen = is.read(buf, 0, (int) Math.min(size, bufsize));
                if (rlen <= 0) {
                    Log.i(TAG, "http body read 0 byte!");
                    return null;
                }
                // 读取http body内容
                ByteArrayInputStream hbis = new ByteArrayInputStream(buf, 0,
                        rlen);
                BufferedReader hin = new BufferedReader(new InputStreamReader(
                        hbis));
                body = hin.readLine();
                Log.e("233", "parsebody: " + body);
            }
            return body;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
