package com.example.administrator.myapplication.baseData;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BaseData
{
    /**
     * 共用数据类
     */
    //公用变量
    public static String[] current_name = {"co2", "airTemperature", "airHumidity",
            "soilTemperature", "soilHumidity", "light"};
    public static int current_data[] = new int[6];
    public static String[] control_name = {"Blower", "Roadlamp", "WaterPump", "Buzzer"};
    public static int control_data[] = {0, 0, 0, 0};//  风扇 LED  水泵  警报
    public static int maxdata[] = {100, 100, 100, 100, 100, 9999};
    public static int mindata[] = {0, 0, 0, 0, 0, 0};
    public static String ip = "";
    public static String ip2 = "192.168.191.7 ";
    public static String login_name = "";
    public static Boolean auto = false;


    //非

    /**
     * 初始化数据库
     * 获取初始阈值   自动控制状态   开关状态
     *
     * @param context
     */
    public static void init(Context context)
    {
        httppost_getdata();
    }

    public static void stopall(Context context)
    {
        BaseData.getdata = false;


    }

    /**
     * 每秒获取传感器数据、开关状态    并每秒插入数据库
     */
    public static boolean getdata = false;

    public static void httppost_getdata()
    {
        getdata = true;

        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                while (getdata)
                {
                    BufferedReader reader = null;
                    try
                    {

                        URL url = new URL("http://" + ip2 + ":8890" +
                                "/type/jason/action/getSensor");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setConnectTimeout(2000);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.connect();
                        conn.getOutputStream().write(JsonUtils.getname().toString().getBytes());

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String read = new String();
                        StringBuffer sb = new StringBuffer();
                        while ((read = reader.readLine()) != null)
                        {
                            sb.append(read);
                        }

                        JSONObject jsonObject = new JSONObject(sb.toString());
                        Log.e("456", "run: " + jsonObject.toString());
                        for (int i = 0; i < 6; i++)
                        {
                            current_data[i] = Integer.parseInt(jsonObject.getString
                                    (current_name[i]));
                            //System.out.println(current_data[i]);
                        }
                    } catch (Exception e)
                    {
                        // TODO: handle exception
                    }

                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public static void post(String content, Handler handler)
    {

        BufferedWriter writer = null;
        BufferedReader reader = null;
        try
        {
            URL url = new URL("http://" + ip2 + ":8890" +
                    "/type/jason/action/getSensor");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(2000);
            conn.setDoInput(true);
            conn.setDoOutput(true);


//            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            writer.write(content);
            conn.connect();
            if (!TextUtils.isEmpty(content))
            {
                conn.getOutputStream().write(content.getBytes());
            }


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String read = new String();
                StringBuffer sb = new StringBuffer();
                while ((read = reader.readLine()) != null)
                {
                    sb.append(read);
                }
                read = sb.toString();
                Log.e("456", "result " + read);

                Message message = new Message();
                message.obj = read;
                handler.sendMessage(message);
            }


        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            handler.sendEmptyMessage(404);
        } catch (IOException e)
        {
            e.printStackTrace();
            handler.sendEmptyMessage(404);
        } finally
        {
            try
            {
                if (writer != null)
                    writer.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (reader != null)
                    reader.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
