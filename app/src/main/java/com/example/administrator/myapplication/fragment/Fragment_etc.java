package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

public class Fragment_etc extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_etc_parking;
    }

    private Button mSettingBtn, mFindBtn;
    private AutoCompleteTextView mEditText;
    private TextView mTextView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSettingBtn = findView(R.id.etc_setting);
        mFindBtn = findView(R.id.etc_find);
        mEditText = findView(R.id.etc_editText);
        mTextView = findView(R.id.etc_find_text);

        String[] strings=new String[]{"1","5","10","15","20","25","30","35","40","45","50","55","60","65","70","75","80","85","90","95","100","200","300","400",
                "500","600","700","800","900","1000"};
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,strings);
        mEditText.setThreshold(1);
        mEditText.setAdapter(adapter1);
        initOnClickListener();
    }


    public void initOnClickListener() {
        /**
         *  那个费率设置按钮
         */
        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"设置为："+mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        /**
         *  那个ETC查询设置按钮
         */
        mFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"暂无数据！",Toast.LENGTH_SHORT).show();
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
