package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

/**
 * 停车场
 */
public class Fragment_parking extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_etc_parking;
    }

    private Button mSettingBtn, mFindBtn;
    private EditText mEditText;
    private TextView mTextView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSettingBtn = findView(R.id.etc_setting);
        mFindBtn = findView(R.id.etc_find);
        mEditText = findView(R.id.etc_editText);
        mTextView = findView(R.id.etc_find_text);
        ((TextView) findView(R.id.etc_text)).setText("停车场费率：");
        ((TextView) findView(R.id.title)).setText("   二、停车场信息查询");
        initOnClickListener();
    }


    public void initOnClickListener() {
        /**
         *  那个费率设置按钮
         */
        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"费率设置为："+mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        /**
         *  那个查询设置按钮
         */
        mFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"正在查询！",Toast.LENGTH_SHORT).show();
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
