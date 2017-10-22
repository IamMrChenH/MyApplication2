package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myapplication.MoneyLogActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.baseData.BaseData;

/**
 * 环境监控
 */
public class Fragment_envir extends BaseFragment implements Runnable {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bus_envir;
    }

    private Button mFindBtn;
    private TextView mBusText1, mBusText2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFindBtn = findView(R.id.bus_find);
        mBusText1 = findView(R.id.bus_text1);
        mBusText2 = findView(R.id.bus_text2);

        ((TextView) findView(R.id.title)).setText("   二、环境监控");


        initOnClickListener();


    }


    public void initOnClickListener() {
        /**
         *  查询环境
         */
        mFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoneyLogActivity.class);
                startActivity(intent);

            }
        });


        findView(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content2, new Fragment_list()).commit();
            }
        });
    }

    @Override
    public void run() {
        try {
            mBusText1.setText("C02浓度：" + BaseData.current_data[0]
                    + "\n空气温度：" + BaseData.current_data[1]
                    + "\n空气湿度：" + BaseData.current_data[2]
                    + "\n光照强度：" + BaseData.current_data[3]
                    + "\n土壤温度：" + BaseData.current_data[4]
                    + "\n土壤湿度：" + BaseData.current_data[5]
            );


            mBaseFragmentHandle.postDelayed(this, 1 * 1000);


            Log.e("233", "run: " + BaseData.current_data[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mBaseFragmentHandle.post(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mBaseFragmentHandle.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
