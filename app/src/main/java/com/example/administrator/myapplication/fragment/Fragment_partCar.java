package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Car;
import com.example.administrator.myapplication.GameView;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.db.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2017/3/9.
 */

public class Fragment_partCar extends BaseFragment {

    private Button stopCar;
    private Spinner mSpinner;
    private TextView allmoney;
    private RadioButton xs, cs;
    private Button exit_parkfragment;
    //    private Switch sw_light;
    private AutoCompleteTextView input_money;
    private Button input_money_keep;
    private GameView gameView;


    @Nullable
    @Override
    public int getLayoutId() {
        return R.layout.partlist;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(getView());
    }

    private void initData(View v) {
        initView(v);
    }

    private volatile boolean isRun = true;

    private void initView(View v) {
        //加载所有小车的数量
        mSpinner = (Spinner) v.findViewById(R.id.Spinner);
        //指定某量小车停车
        stopCar = (Button) v.findViewById(R.id.stopCar);
        //按小时
        xs = (RadioButton) getView().findViewById(R.id.radio_xs);
        //按次数
        cs = (RadioButton) getView().findViewById(R.id.radio_cs);
        //停车费
        allmoney = (TextView) getView().findViewById(R.id.allmoney);
        //突出设置
        exit_parkfragment = (Button) getView().findViewById(R.id.exit_parkfragment);
        //路灯设置
//        sw_light=(Switch) getView().findViewById(R.id.sw_light);
        //获取金额
        input_money = (AutoCompleteTextView) getView().findViewById(R.id.input_money);
        input_money.setThreshold(1);
        String[] strings = new String[]{"1", "5", "10", "15", "20", "25", "30", "35", "40", "45",
                "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100", "200", "300",
                "400",
                "500", "600", "700", "800", "900", "1000"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_dropdown_item_1line, strings);
        input_money.setAdapter(adapter1);
        //保存
        input_money_keep = (Button) getView().findViewById(R.id.input_money_keep);
        gameView = (GameView) getActivity().findViewById(R.id.lzh_view);
        initSpinnerData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_list_item_1, obj);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(adapter);
        initData();
        initSpinnerData();
        new Thread() {
            @Override
            public void run() {
                while (isRun) {
                    mUIHandler.obtainMessage(200).sendToTarget();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }

    List<String> obj;

    private void initSpinnerData() {
        List<Car> cars = gameView.getCars();
        obj = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            obj.add((i + 1) + "号小车");
        }


    }

    private int mPosition = 0;

    private void initData() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        stopCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = gameView.getCars().get(mSpinner.getSelectedItemPosition());
                car.setPark(true);
                car.setParkTime(3000);
                Toast.makeText(getActivity(), "停车成功！", Toast.LENGTH_SHORT).show();
            }
        });
        xs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getActivity(), "设置成功！", Toast.LENGTH_SHORT).show();
            }
        });
        cs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        allmoney.setText("");
        exit_parkfragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                        .content2, new Fragment_list()).commit();
            }
        });

        input_money_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = input_money.getText().toString();
                if (!money.equals("")) {
                    if (xs.isChecked()) {
                        DatabaseUtil.updateParkMoney(getContext(), "2", money);
                    } else if (cs.isChecked()) {
                        DatabaseUtil.updateParkMoney(getContext(), "1", money);
                    } else {
                        Toast.makeText(getActivity(), "请选择收费模式！", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getActivity(), "停车场收费设置成功！当前收费标准为" + (xs.isChecked() ? "一小时" :
                            "") + (cs.isChecked() ? "一次" : "") + money + "元", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), "请输入单位金额！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 200:
                    List<Car> cars = gameView.getCars();
                    List<Car> carsT = gameView.getPartCars();
                    if (cars.get(mPosition).isPark()) {
                        stopCar.setText("取消预约");
                        stopCar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Car car = gameView.getCars().get(mSpinner.getSelectedItemPosition());
                                car.setPark(false);
                                Toast.makeText(getActivity(), "取消预约成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (carsT.indexOf(cars.get(mPosition)) != -1) {
                            stopCar.setText("出库");
                            stopCar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Car car = gameView.getCars().get(mSpinner.getSelectedItemPosition());
                                    car.setPark(false);
                                    Toast.makeText(getActivity(), "出库成功！", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    } else {
                        stopCar.setText("停车");
                        stopCar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Car car = gameView.getCars().get(mSpinner.getSelectedItemPosition());
                                car.setPark(true);
                                car.setParkTime(3000);
                                Toast.makeText(getActivity(), "停车成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    break;
            }

        }
    };

    @Override
    public void onDestroyView() {
        isRun = false;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}
