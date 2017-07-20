package com.marcelje.hackernews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.marcelje.hackernews.activity.ToolbarActivity;

public class ToolbarFragment extends Fragment {

    private ToolbarActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = ToolbarActivity.getActivity(getActivity());
    }

    protected ToolbarActivity getToolbarActivity() {
        return mActivity;
    }
}
