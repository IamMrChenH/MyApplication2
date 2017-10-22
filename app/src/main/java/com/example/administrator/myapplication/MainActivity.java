package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.baseData.BaseData;
import com.example.administrator.myapplication.db.DatabaseUtil;
import com.example.administrator.myapplication.fragment.Fragment_list;
import com.example.administrator.myapplication.service.LinghtsService;
import com.example.administrator.myapplication.service.NetServerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{

    private static GameView gameView;
    private LinearLayout lzh_line;
    Button camera;
    SharedPreferences sp;

    public static GameView getGameView()
    {
        return gameView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DatabaseUtil.getDatabase(getApplicationContext());
        setContentView(R.layout.activity_main);
        BaseData.httppost_getdata();
        sp = getSharedPreferences("chhhhh", MODE_PRIVATE);
        if (sp.getBoolean("init", true))
        {
            saveDrawableById(R.drawable.a1, "a1.jpg", Bitmap.CompressFormat.JPEG);
            saveDrawableById(R.drawable.a2, "a2.jpg", Bitmap.CompressFormat.JPEG);
            saveDrawableById(R.drawable.a3, "a3.jpg", Bitmap.CompressFormat.JPEG);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("init", false);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content2, new Fragment_list()
        ).commit();
        gameView = (GameView) findViewById(R.id.lzh_view);
        Intent sev = new Intent(MainActivity.this,
                NetServerService.class);
        startService(sev);// 打开Net服务
        startService(new Intent(MainActivity.this, LinghtsService.class));




        gameView.getCars().get(0).setPark(true);
        gameView.getCars().get(0).setParkTime(10*1000);
//        gameView.getCars().get(0).setPartType();



    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("233", "onTouchEvent: "  + ev.getX() );
        Log.e("233", "onTouchEvent: "  + ev.getY() );
        return super.dispatchTouchEvent(ev);
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Intent sev = new Intent(MainActivity.this,
                NetServerService.class);
        stopService(sev);// 关闭Net服务
        stopService(new Intent(MainActivity.this, LinghtsService.class));
        BaseData.stopall(this);
    }


    /**
     * 存储资源为ID的图片
     *
     * @param id
     * @param name
     */
    public void saveDrawableById(int id, String name, Bitmap.CompressFormat format)
    {
        Drawable drawable = idToDrawable(id);
        Bitmap bitmap = drawableToBitmap(drawable);
        saveBitmap(bitmap, name, format);
    }

    /**
     * 将资源ID转化为Drawable
     *
     * @param id
     * @return
     */
    public Drawable idToDrawable(int id)
    {
        return this.getResources().getDrawable(id);
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable == null)
            return null;
        return ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * 将Bitmap以指定格式保存到指定路径
     * @param bitmap
     * @param name
     * @param format
     */
    public void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format)
    {
        // 创建一个位于SD卡上的文件
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/DCIM/",
                name);
        FileOutputStream out = null;
        try
        {
            // 打开指定文件输出流
            out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(format, 100,
                    out);
            out.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
