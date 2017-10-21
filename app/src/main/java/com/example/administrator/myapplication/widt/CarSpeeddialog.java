package com.example.administrator.myapplication.widt;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.administrator.myapplication.Car;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.TrafficR;
import com.example.administrator.myapplication.db.DatabaseUtil;
import com.example.administrator.myapplication.fragment.Fragment_list;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 2017/3/29.
 */

public class CarSpeeddialog extends DialogFragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.car_speed_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getDialog().setTitle("违规拍照");
        initData();
        initViews();
        initSetOnClick();

    }


    private List<String> mCarIDs;
    private List<String> mMsg;

    private void initData()
    {
        mCarIDs = new ArrayList<>();
        mMsg = new ArrayList<>();
        List<Car> cars = Fragment_list.gameView.getCars();
        for (int i = 0; i < cars.size(); i++)
        {
            Car car = cars.get(i);
            mCarIDs.add(car.getName() + "号小车");

        }
        List<TrafficR> trafficRs = DatabaseUtil.getTrafficR(getActivity());
        for (TrafficR trafficR : trafficRs)
        {
            mMsg.add(trafficR.getRule());
        }


    }

    private ImageView mImageView;
    private DrawView tuya;
    private Button mBtnOk;
    private Spinner mSpinner1, mSpinner2;

    private void initViews()
    {
        mImageView = findView(R.id.imager);
        tuya = findView(R.id.tuya);
        mBtnOk = findView(R.id.btn_ok);
        mSpinner1 = findView(R.id.spinner1);
        mSpinner2 = findView(R.id.spinner2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_list_item_1, mCarIDs);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_list_item_1, mMsg);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSpinner1.setAdapter(adapter1);
        mSpinner2.setAdapter(adapter2);
        mImageView.setImageBitmap(Fragment_list.curBitmap);

    }

    private void initSetOnClick()
    {
        mBtnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                int mPosition1 = mSpinner1.getSelectedItemPosition();
//                int mPosition2 = mSpinner2.getSelectedItemPosition();
//                Toast.makeText(getActivity(), "GG", Toast.LENGTH_SHORT).show();
                try
                {
                    FileOutputStream out = new FileOutputStream(new File(Environment
                            .getExternalStorageDirectory(), "Asdd.jpg"));
                    Bitmap doodle = doodle(Fragment_list.curBitmap, tuya.save());
                    doodle.compress(Bitmap.CompressFormat.JPEG,
                            100, out);
//                    Bitmap save = tuya.save();
//                    save.compress(Bitmap.CompressFormat.JPEG,
//                            100, out);
                    out.flush();
                    out.close();
//                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    Log.e("233", "GG ");
                    dismiss();
                } catch (Exception e)
                {

                    e.printStackTrace();
                    Log.e("233", "Fial ");
                }

            }
        });
    }

    /**
     * 组合涂鸦图片和源图片
     *
     * @param src       源图片
     * @param watermark 涂鸦图片
     * @return
     */
    public Bitmap doodle(Bitmap src, Bitmap watermark)
    {
        // 另外创建一张图片
        Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config
                .ARGB_8888);//
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
        canvas.drawBitmap(watermark, 0, 0, null); // 涂鸦图片画到原图片中间位置

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        watermark.recycle();
        watermark = null;

        return newb;
    }

    public <T extends View> T findView(int id)
    {
        return (T) getView().findViewById(id);
    }
}
