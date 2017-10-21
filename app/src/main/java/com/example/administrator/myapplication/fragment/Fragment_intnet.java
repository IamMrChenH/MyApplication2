package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.baseData.BaseData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.administrator.myapplication.baseData.BaseData.current_data;
import static com.example.administrator.myapplication.baseData.BaseData.current_name;

/**
 * 对接互联
 */
public class Fragment_intnet extends BaseFragment implements Runnable
{

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_intnet;
    }

    private Button mBtn;
    private EditText mEdtext;
    private TextView mText;
    Handler handler = new Handler();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBtn = findView(R.id.intnet_btn1);
        mEdtext = findView(R.id.intnet_edtext);
        mText = findView(R.id.text);

        initOnClickListener();
        handler.post(this);
    }


    public void initOnClickListener()
    {
        /**
         *  对接互联
         */
        mBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //mEdtext  ip从这里来
                String ip = mEdtext.getText().toString();
                if (TextUtils.isEmpty(ip))
                {
                    Toast.makeText(getActivity(), mEdtext.getText().toString() + "不能为空！", Toast
                            .LENGTH_SHORT).show();
                    return;
                }


                if (!isIP(ip))
                {
                    Toast.makeText(getActivity(), mEdtext.getText().toString() + "是错误的Ip！", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), "Ip设置成功！", Toast
                        .LENGTH_SHORT).show();
                BaseData.ip2 = ip;

            }
        });


        findView(R.id.exit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                        .content2, new Fragment_list()).commit();
            }
        });
    }

    @Override
    public void run()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < current_name.length; i++)
        {
            stringBuilder.append(current_name[i] + ":").append(current_data[i] + "\n");
        }

        mText.setText(stringBuilder.toString());

        handler.postDelayed(this, 1000);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        handler.removeCallbacks(this);
    }

    public boolean isIP(String addr)
    {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\." +
                "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }
}
