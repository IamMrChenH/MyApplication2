package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

/**
 * 对接互联
 */
public class Fragment_open_server extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_open_server;
    }

    private Button mBtn;
    private TextView ip;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBtn = findView(R.id.service_open);
        ip = findView(R.id.service_ip);

        initOnClickListener();
    }


    public void initOnClickListener() {
        /**
         *  打开服务器 获取IP
         */
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "获取成功！", Toast.LENGTH_SHORT).show();
                //ip
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
