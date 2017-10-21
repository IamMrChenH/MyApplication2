package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myapplication.DrawTable;
import com.example.administrator.myapplication.R;


/**
 * 公交巴士的站台查询
 */
public class Fragment_dataTotal extends BaseFragment {

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


        initOnClickListener();
    }


    public void initOnClickListener() {
        /**
         *  查询按钮
         */
        mFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDialog().show(getActivity().getFragmentManager(), null);
                Intent intent = new Intent(getActivity(), DrawTable.class);
//                startActivity(intent);
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
