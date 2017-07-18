package com.marcelje.hackernews.screen.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;

public class SettingsActivity extends ToolbarActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setDisplayHomeAsUpEnabled(true);
        attachFragment();
    }

    private void attachFragment() {
        Fragment fragment = SettingsFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }
}