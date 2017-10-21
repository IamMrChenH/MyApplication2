package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.administrator.myapplication.db.DatabaseUtil;

import java.util.List;

public class MoneyLogActivity extends Activity {
    GridView gridView;
    private List<MoneyLog> date_list;
    private SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moneylog_activity);
        gridView = (GridView) findViewById(R.id.lzh_vo_grid);
        searchview = (SearchView) findViewById(R.id.searchview);
        date_list = new DatabaseUtil().getAllMoneyLog((this));

        adapter baseAdapter = new adapter(date_list);

        gridView.setAdapter(baseAdapter);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //搜索框内部改变回调，newText就是搜索框里的内容
            @Override
            public boolean onQueryTextChange(String newText) {


                date_list = new DatabaseUtil().getMoneyLog(MoneyLogActivity.this, newText);

                adapter baseAdapter = new adapter(date_list);
                gridView.setAdapter(baseAdapter);


                return true;
            }
        });


    }

    class adapter extends BaseAdapter {
        List<MoneyLog> list;

        public adapter(List<MoneyLog> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(parent.getContext(), R.layout.gplog_item, null);

            MoneyLog moneyLog = list.get(position);
//           // ((TextView) view.findViewById(R.id.gp_cid)).setText(moneyLog.getCid());
//            ((TextView) view.findViewById(R.id.gp_ctype)).setText(String.valueOf(moneyLog.getType()).equals("1") ? "按次计费" : "按时计费");
//
//            ((TextView) view.findViewById(R.id.gp_moneyCost)).setText(moneyLog.getMoney());
//            ((TextView) view.findViewById(R.id.gp_time)).setText(String.valueOf(moneyLog.getTime()));
//            ((TextView) view.findViewById(R.id.gp_Mark)).setText(moneyLog.getMark());
//          //  ((TextView) view.findViewById(R.id.longtime)).setText(String.valueOf(parkLog.getParkTime()));
            TextView carid = (TextView) view.findViewById(R.id.gp_cid1);
            TextView type = (TextView) view.findViewById(R.id.gp_ctype1);
            TextView money = (TextView) view.findViewById(R.id.gp_moneyCost1);
            TextView time = (TextView) view.findViewById(R.id.gp_time1);
            TextView mark = (TextView) view.findViewById(R.id.gp_Mark1);

            carid.setText(moneyLog.getCid() + "");

            type.setText(String.valueOf(moneyLog.getType()) + "");
            money.setText(moneyLog.getMoney() + "");
            time.setText(String.valueOf(moneyLog.getTime()) + "");
            mark.setText(gettype(moneyLog.getMark()) + "");


            return view;
        }
    }

    private String gettype(String str) {//收费方式
        if (str == null || str.equals("") || str.split(",").length < 2) {
            return "现金支付";
        } else {
            String[] a = str.split(",");
            String a1 = a[0];
            String a2 = a[1];
            if (a1.equals("1")) {
                str = "/小时";
            } else {
                str = "/次";
            }
            return a2 + str;
        }
    }


}
