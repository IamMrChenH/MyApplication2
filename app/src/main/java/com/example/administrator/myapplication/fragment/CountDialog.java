package com.example.administrator.myapplication.fragment;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.DrawTable;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;


public class CountDialog extends DialogFragment implements OnClickListener {

    LinearLayout li1;
    private double[] d1;//收益
    private double[] d2;//车辆数
    SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.draw_table, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        //得到资源
        li1 = (LinearLayout) getView().findViewById(R.id.lli);
        //初始化柱状图
        initView();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置弹出对话框的宽度
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(
                    (int) 800,//设置宽度为屏幕宽度的比例
                    600//高度用默认高度
            );
        }
    }

    private void initView() {
        //柱状图的两个序列的名字
        String[] titles = new String[]{"车辆 （单位：万辆）", "收益  （单位：万）"};
        //存放柱状图两个序列的值
        ArrayList<double[]> value = new ArrayList<double[]>();
        //停车场数量和收益
        d1 = new double[]{0.1, 0.3, 0.7, 0.8, 0.5, 0.3, 0.7, 0.8, 0.5, 0.7, 0.8, 0.5};
        d2 = new double[]{0.3, 0.4, 0.8, 0.6, 0.1, 0.3, 0.7, 0.8, 0.5, 0.3, 0.7, 0.8};
        value.add(d1);
        value.add(d2);
        //两个状的颜色
        int[] colors = {Color.BLUE, Color.GREEN};

        //为li1添加柱状图
        li1.addView(
                DrawTable.xychar(getActivity(), titles, value, colors, 6, 1, new double[]{0,
                        12, 0, 1}, new int[]{1, 2, 3, 4,
                        5, 6, 7, 8, 9, 10, 11, 12}, "停车场收益（月报表）", true));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View arg0) {
        dismiss();
    }


}
