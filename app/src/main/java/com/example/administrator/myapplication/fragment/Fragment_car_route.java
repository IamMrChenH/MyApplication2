package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.myapplication.Car;
import com.example.administrator.myapplication.GameView;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 小车路线设置
 */
public class Fragment_car_route extends BaseFragment {

    private GameView gameView;
    private SharedPreferences sp;
    AutoCompleteTextView spend;
    Button setSpend;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_car_route;
    }

    private Button mCarRouteSetting, mCarNumberSeting;
    Spinner spinner1, spinner2, spinner3;
    private AutoCompleteTextView mEditText;
    int[] luxian = {0, 678, 1100};

    @Override
    public void onActivityCreated(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCarNumberSeting = findView(R.id.car_route_number_setting);
        mCarRouteSetting = findView(R.id.car_route_setting);

        spend = (AutoCompleteTextView) getActivity().findViewById(R.id.lzh_spend);
        setSpend = findView(R.id.lzh_setspend);

        spinner1 = findView(R.id.spinner1);
        spinner2 = findView(R.id.spinner2);
        spinner3 = findView(R.id.spinner3);

        mEditText = (AutoCompleteTextView) getActivity().findViewById(R.id.car_route_edittext);
        gameView = (GameView) getActivity().findViewById(R.id.lzh_view);
        sp = getContext().getSharedPreferences("config", getActivity().MODE_APPEND);


        String[] strings = new String[]{"1", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100", "200", "300", "400",
                "500", "600", "700", "800", "900", "1000"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, strings);
        mEditText.setThreshold(1);
        mEditText.setAdapter(adapter1);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, strings);
        spend.setThreshold(1);
        spend.setAdapter(adapter1);

        setSpinnerAdapter();
        initOnClickListener();

        setSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spend.getText().toString().equals("")){
                Toast.makeText(getActivity(), "设置" + (spinner3.getSelectedItemPosition() + 1) + "号小车速度成功！当前速度为" + Integer.valueOf(spend.getText().toString()) + "米/秒", Toast.LENGTH_SHORT).show();
                gameView.getCars().get(spinner3.getSelectedItemPosition()).setSpeed(Integer.valueOf(spend.getText().toString()));}else{

                    Toast.makeText(getActivity(), "速度输入错误，请重新输入", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void setSpinnerAdapter() {
        gameView = (GameView) getActivity().findViewById(R.id.lzh_view);
        List<Car> cars = gameView.getCars();
        List<String> obj = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            obj.add((i + 1) + "号小车");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, obj);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter);
        spinner3.setAdapter(adapter);
    }


    public void initOnClickListener() {

        /**
         *小车数量设置
         */
        mCarNumberSeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mEditText.getText().toString().trim().equals("")) {
                    Integer num = Integer.valueOf(mEditText.getText().toString().trim());
                    sp.edit().putInt("carNum", num).commit();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    Toast.makeText(getActivity(), "设置小车数量成功！数量为" + num + "辆", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "数量设置错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /**
         *小车路线设置
         */
        mCarRouteSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gameView.getCars().get(spinner1.getSelectedItemPosition()).setLuxian(luxian[spinner2.getSelectedItemPosition()]);
                Toast.makeText(getActivity(), spinner1.getSelectedItem() + "路线设置成功,路线为" + spinner2.getSelectedItem(), Toast.LENGTH_LONG).show();
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
