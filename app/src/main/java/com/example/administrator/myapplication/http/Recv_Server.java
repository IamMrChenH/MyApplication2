package com.example.administrator.myapplication.http;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
/**
 * Created by chenhao on 17-3-13.
 */

public class Recv_Server extends NanoHTTPD{
    public Recv_Server(int port) {
        super(port);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        if (!NanoHTTPD.Method.POST.equals(session.getMethod()))
            return new NanoHTTPD.Response("fail");
        try {
            parseBodyImager(session);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new NanoHTTPD.Response("ok");
    }

    public void parseBodyImager(NanoHTTPD.IHTTPSession session) throws Exception {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/abc.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);


        InputStream inputStream = session.getInputStream();
        int read = 0;
        int lenght = 0;
        byte[] buff = new byte[1024 * 4];
        while (0 != (read = inputStream.read(buff))) {
            fileOutputStream.write(buff);
            lenght += read;
            if (read < 4096)
                break;

        }

    }


}
