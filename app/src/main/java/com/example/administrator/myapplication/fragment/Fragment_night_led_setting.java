package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.administrator.myapplication.LzhTool;
import com.example.administrator.myapplication.R;

/**
 * 红绿灯设置
 */
public class Fragment_night_led_setting extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_night_led;
    }

    private Button mNightSetting1, mNightSetting2, mNightSetting3;
    private EditText mEditText;
    private Switch sw_light;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sw_light=(Switch) getView().findViewById(R.id.sw_light);
        if(LzhTool.ludeng.equals("#ffff00"))sw_light.setChecked(true);
//        mNightSetting1 = findView(R.id.night_led_setting1);
//        mNightSetting2 = findView(R.id.night_led_setting2);
//        mNightSetting3 = findView(R.id.night_led_setting3);
//
//        mEditText = findView(R.id.night_led_edittext);
        initOnClickListener();
    }


    public void initOnClickListener() {
//        /**
//         *  设置光照阈值
//         */
//        mNightSetting1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //mEditText
//                Toast.makeText(getActivity(),"阀值设置为："+mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        /**
//         *  设置路灯模式 自动或手动
//         */
//        mNightSetting2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"设置成功！",Toast.LENGTH_SHORT).show();
//            }
//        });
//        /**
//         *  设置路灯  开或关
//         */
//        mNightSetting3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"设置成功！",Toast.LENGTH_SHORT).show();
//            }
//        });
        findView(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content2, new Fragment_list()).commit();
            }
        });

        sw_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LzhTool.ludeng="#ffff00";
                    Toast.makeText(getActivity(),"路灯打开成功！",Toast.LENGTH_SHORT).show();
                }else {
                    LzhTool.ludeng="#00ffff00";
                    Toast.makeText(getActivity(),"路灯关闭成功！",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
