package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.myapplication.GameView;
import com.example.administrator.myapplication.Light;
import com.example.administrator.myapplication.R;

/**
 * 红绿灯设置
 */
public class Fragment_red_led extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_red_led;
    }

    private Button mSetting1, mSetting2;
    private Spinner spinner1, spinner2, spinner3;
    private Spinner spinner11, spinner22, spinner33;
    private GameView gameView;
    private Light light, lightTemp;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSetting1 = findView(R.id.red_setting1);
        mSetting2 = findView(R.id.red_setting2);

        spinner1 = findView(R.id.spinner1);
        spinner2 = findView(R.id.spinner2);
        spinner3 = findView(R.id.spinner3);

        spinner11 = findView(R.id.spinner11);
        spinner22 = findView(R.id.spinner22);
        spinner33 = findView(R.id.spinner33);
        gameView = (GameView) getActivity().findViewById(R.id.lzh_view);

        initOnClickListener();
    }


    public void initOnClickListener() {
        /**
         *  设置红绿灯的状态
         */
        mSetting1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spinner1
                //设定绿色
                light = gameView.getLights().get(spinner1.getSelectedItemPosition() * 2 + spinner2.getSelectedItemPosition());
                lightTemp = gameView.getLights().get(spinner1.getSelectedItemPosition() * 2 + (spinner2.getSelectedItemPosition() + 1) % 2);
                light.setNowTime((spinner3.getSelectedItemPosition() == 1 ? 0 : light.getGreedTime()));
                lightTemp.setNowTime((spinner3.getSelectedItemPosition() != 1 ? 0 : light.getGreedTime()));
                Toast.makeText(getActivity(), "" + spinner2.getSelectedItem() + spinner1.getSelectedItem() + "设置成功,当前状态为" + spinner3.getSelectedItem() + "灯", Toast.LENGTH_LONG).show();

//                light.setNowTime(0);

                //spinner2
                //spinner3
            }
        });

        /**
         *  设置红绿灯的时间
         */
        mSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spinner11
                //spinner22
                //spinner33
                light = gameView.getLights().get(spinner11.getSelectedItemPosition() * 2);
                lightTemp = gameView.getLights().get(spinner11.getSelectedItemPosition() * 2 + 1);

                String time = (String) spinner33.getSelectedItem();
                if (spinner22.getSelectedItemPosition() == 0) {
                    light.setRedTime(Integer.valueOf(time.substring(0, (time.length() - 1))) * 25);
                    lightTemp.setGreedTime(Integer.valueOf(time.substring(0, (time.length() - 1))) * 25);
                } else {
                    light.setGreedTime(Integer.valueOf(time.substring(0, (time.length() - 1))) * 25);
                    lightTemp.setRedTime(Integer.valueOf(time.substring(0, (time.length() - 1))) * 25);
                }
                Toast.makeText(getActivity(), "" + spinner11.getSelectedItem() + spinner22.getSelectedItem() + "灯时间设置成功,设置时间为" + spinner33.getSelectedItem(), Toast.LENGTH_LONG).show();

            }
        });

        findView(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content2, new Fragment_list()).commit();
            }
        });
    }
}
