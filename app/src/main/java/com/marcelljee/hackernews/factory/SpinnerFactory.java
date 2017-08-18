package com.marcelljee.hackernews.factory;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.utils.SettingsUtils;

public class SpinnerFactory {

    public static Spinner createNewsTypeSpinner(AppCompatActivity activity,
                                                AdapterView.OnItemSelectedListener listener) {

        Context context;

        if (activity.getSupportActionBar() != null) {
            context = activity.getSupportActionBar().getThemedContext();
        } else {
            context = activity.getApplicationContext();
        }

        Spinner spinner = new Spinner(context);

        List<CharSequence> newsTypeList =
                new ArrayList<>(Arrays.asList(context.getResources().getTextArray(R.array.settings_type_options)));
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<>(context, R.layout.item_spinner, newsTypeList);

        if (SettingsUtils.historyEnabled(context)) {
            adapter.add(context.getString(R.string.news_type_history));
        }

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);

        return spinner;
    }
}
