package com.example.administrator.myapplication.http;

import android.os.Environment;
import android.util.Log;

import com.example.administrator.myapplication.LzhTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.Map;

public class VideoServer extends NanoHTTPD {

    public static final int DEFAULT_SERVER_PORT = 8080;
    public static final String TAG = VideoServer.class.getSimpleName();

    private static final String REQUEST_ROOT = "/go/";
    private static final String REQUEST_UP = "/up";
    private String mVideoFilePath;
    private String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/DCIM/";


    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    public VideoServer() {
        super(DEFAULT_SERVER_PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.e("233", "OnRequest:1 " + session.getUri());

        //上传

        Log.e("233", "OnRequest:1 " + session.getUri());
        if (REQUEST_UP.equals(session.getUri())) {
            if (!NanoHTTPD.Method.POST.equals(session.getMethod()))
                return new NanoHTTPD.Response("fail");
            Log.e("233", "上传");
            return new Response(newJasonObj(parseBodyImager(session)));

        } else {

            Log.e("233", "0");
            //下載
            if (REQUEST_ROOT.equals(session.getUri().substring(0, 4))) {
                Log.e("233", "1");
                mVideoFilePath = sdCard + session.getUri().substring(4, session.getUri().length());
//                return responseRootPage(session);
                return responseVideoStream(session);
//            } else if (mVideoFilePath.equals(session.getUri()))
//            {
//                return responseVideoStream(session);
            }
            return response404(session, session.getUri());
        }
    }

    public Response responseRootPage(IHTTPSession session) {
        File file = new File(mVideoFilePath);
        if (!file.exists()) {
            return response404(session, mVideoFilePath);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("<input type=\"image\"  src=" + getQuotaStr(mVideoFilePath) +
                "width=\"1000\" height=\"500\">");
        builder.append("</body></html>\n");
        return new Response(builder.toString());
    }

    public Response responseVideoStream(IHTTPSession session) {
        try {
            FileInputStream fis = new FileInputStream(mVideoFilePath);
            String type = LzhTool.getMIMEType(mVideoFilePath.substring(mVideoFilePath.lastIndexOf
                    ("/") + 1, mVideoFilePath.length()));

            Log.e("233", type);
            return new NanoHTTPD.Response(Response.Status.OK, type, fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return response404(session, mVideoFilePath);
        }
    }

    public Response response404(IHTTPSession session, String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("Sorry, Can't Found " + url + " !");
        builder.append("</body></html>\n");
        return new Response(builder.toString());
    }


    protected String getQuotaStr(String text) {
        return "\"" + text + "\"";
    }

    /**
     * CH  上传图片 文件
     *
     * @param session
     * @throws Exception
     */
    public String parseBodyImager(NanoHTTPD.IHTTPSession session) {
        try {
            Map<String, String> headers = session.getHeaders();
            System.out.println("通过Map.keySet遍历key和value：");
            for (String key : headers.keySet()) {
                Log.e("233", "key= " + key + " and value= " + headers.get(key));
            }

            String fileName = headers.get("filename");

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/DCIM/" + fileName);
            FileOutputStream fileOutputStream = null;

            fileOutputStream = new FileOutputStream(file);


            InputStream inputStream = session.getInputStream();
            int read = 0;
            int lenght = 0;
            byte[] buff = new byte[1024 * 4];
            while (0 != (read = inputStream.read(buff))) {
                fileOutputStream.write(buff);
                lenght += read;
                if (read < 10)
                    break;

            }


            return "ok";
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "ok";
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
            return "fail";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public String newJasonObj(String msg) {
        JSONObject object = new JSONObject();
        try {
            object.put("result", msg);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

}
