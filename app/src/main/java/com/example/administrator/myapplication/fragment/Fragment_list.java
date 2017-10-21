package com.example.administrator.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.GameView;
import com.example.administrator.myapplication.NetWorkUtils;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widt.CarSpeeddialog;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by chenhao on 17-3-6.
 */

public class Fragment_list extends BaseFragment
{

    public static GameView gameView;
    private LinearLayout mLayout;
    public static Bitmap curBitmap;

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_list;
    }

    private MyListview mListView1, mListView2;
    Button camera;

    private Handler mUIHandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 200)
            {

                curBitmap = CutScreem((View) msg.obj);
                gameView.isShowCarSpeed = false;
//                showDialog(curBitmap);

                new CarSpeeddialog().show(getChildFragmentManager(), "");

            }


        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mListView1 = findView(R.id.listView1);
        mListView2 = findView(R.id.listView2);
        Log.e("chenhao", "run: ");

        List<Item> items = new ArrayList<>();
        items.add(new Item(R.mipmap.ic_launcher, "ETC信息查询", "查询ETC费率和各个时间点小车消费记录"));
        items.add(new Item(R.mipmap.ic_launcher, "停车场信息查询", "查询停车场和各个时间点小车消费记录"));
        items.add(new Item(R.mipmap.ic_launcher, "数据统计", "小车记录的数据统计"));
        items.add(new Item(R.mipmap.ic_launcher, "小车行驶路线控制", "设置小车行驶路径"));
        items.add(new Item(R.mipmap.ic_launcher, "环境监控", "查询各个传感器"));
        items.add(new Item(R.mipmap.ic_launcher, "红绿灯控制", "设置每个红绿灯状态和时间"));
        items.add(new Item(R.mipmap.ic_launcher, "路灯控制", "设置每个路灯的开光和自动/手动模式"));
        mListView1.setAdapter(new ListViewAdapter(getActivity(), items));


        items = new ArrayList<>();
        items.add(new Item(R.mipmap.ic_launcher, "对接互联", "同步物理沙盘的各种信息"));
        items.add(new Item(R.mipmap.ic_launcher, "打开服务器", "打开服务器选项，便于终端获取本地信息"));
        items.add(new Item(R.mipmap.ic_launcher, "网络设置", "选择设置网络"));
        mListView2.setAdapter(new ListViewAdapter(getActivity(), items));

        setListViewOnItemListene();

        mLayout = (LinearLayout) getActivity().findViewById(R.id.lzh_line);
        camera = (Button) getView().findViewById(R.id.paizhao);
        gameView = (GameView) getActivity().findViewById(R.id.lzh_view);
        camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {

//                gameView.setIsShowCarSpeed();
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            gameView.isShowCarSpeed = true;
                            sleep(500);
                            mUIHandle.obtainMessage(200, v).sendToTarget();
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }.start();


            }
        });
        String ipStr = NetWorkUtils.getLocalIpAddress(getActivity());
        TextView ipText = (TextView) getView().findViewById(R.id.lzh_ip);
        ipText.setText(ipStr);
    }

    private void setListViewOnItemListene()
    {
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_etc()).commit();
                        break;
                    case 1:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_partCar()).commit();
                        break;
                    case 2:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_dataTotal()).commit();

                        break;
                    case 3:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_car_route()).commit();

                        break;
                    case 4:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_envir()).commit();

                        break;
                    case 5:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_red_led()).commit();

                        break;
                    case 6:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_night_led_setting()).commit();

                        break;
                }
            }
        });


        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_intnet()).commit();
                        break;
                    case 1:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content2, new Fragment_open_server()).commit();

                        break;
                    case 2:

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        break;
                }
            }
        });

    }

    public class ListViewAdapter extends BaseAdapter
    {
        private final LayoutInflater from;
        private List<Item> mItems;

        public ListViewAdapter(Context context, List<Item> mItems)
        {
            this.mItems = mItems;
            from = LayoutInflater.from(context);
        }

        @Override
        public int getCount()
        {
            return mItems.size();
        }

        @Override
        public Item getItem(int position)
        {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
                convertView = from.inflate(R.layout.item_1, null);
            ImageView img1 = (ImageView) convertView.findViewById(R.id.img1);
            TextView t1 = (TextView) convertView.findViewById(R.id.t1);
            TextView t2 = (TextView) convertView.findViewById(R.id.t2);
            Item item = getItem(position);
            img1.setImageResource(item.mIcon);
            t1.setText(item.mText1);
            t2.setText(item.mText2);
            return convertView;
        }


    }

    class Item
    {
        public Item(int mIcon, String mText1, String mText2)
        {
            this.mIcon = mIcon;
            this.mText1 = mText1;
            this.mText2 = mText2;
        }

        int mIcon;
        String mText1;
        String mText2;
    }


    @SuppressLint("SdCardPath")
    private Bitmap CutScreem(View views)
    {//截屏


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        String fname = "/sdcard/" + sdf.format(new Date()) + ".png";
        View view = views.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null)
        {
            System.out.println("bitmap got!");
            try
            {


                FileOutputStream out = new FileOutputStream(fname);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                System.out.println("file" + fname + "output done.");
                Toast.makeText(getActivity(), "抓拍成功！", Toast.LENGTH_LONG).show();
            } catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), "抓拍保存失败！", Toast.LENGTH_LONG).show();
            }
        } else
        {
            System.out.println("bitmap is NULL!");
            Toast.makeText(getActivity(), "抓拍失败！", Toast.LENGTH_LONG).show();
        }
        return bitmap;
    }
}
