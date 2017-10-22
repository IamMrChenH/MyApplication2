package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenhao on 17-3-6.
 */

public abstract class BaseFragment extends Fragment {
    public abstract int getLayoutId();

    public Handler mBaseFragmentHandle = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    public <T extends View> T findView(int id) {
        return (T) getView().findViewById(id);

    }

    public <T extends View> T findView(View view, int id) {
        return (T) getView().findViewById(id);

    }

}
