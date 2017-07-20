package com.marcelje.hackernews.screen.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.marcelje.hackernews.activity.FragmentActivity;
import com.marcelje.hackernews.activity.ToolbarActivity;

public class AboutActivity extends FragmentActivity {

    public static void startActivity(ToolbarActivity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        Fragment fragment = AboutFragment.newInstance();
        setFragment(fragment);
    }

}
