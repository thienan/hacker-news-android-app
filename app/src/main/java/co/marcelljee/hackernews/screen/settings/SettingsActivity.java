package com.marcelljee.hackernews.screen.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.marcelljee.hackernews.activity.FragmentActivity;

public class SettingsActivity extends FragmentActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            setFragment(SettingsFragment.newInstance());
        }
    }
}