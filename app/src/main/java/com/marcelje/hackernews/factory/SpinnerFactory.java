package com.marcelje.hackernews.factory;

import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.screen.news.NewsActivityFragment;

import java.util.ArrayList;
import java.util.List;

public class SpinnerFactory {

    public static Spinner createSpinner(AppCompatActivity activity,
                                        AdapterView.OnItemSelectedListener listener) {

        Spinner spinner = new Spinner(activity.getSupportActionBar().getThemedContext());

        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<>(activity.getApplicationContext(),
                        R.layout.item_spinner, createList());

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);

        return spinner;
    }

    private static List<CharSequence> createList() {
        List<CharSequence> list = new ArrayList<>();
        list.add(NewsActivityFragment.TYPE_TOP);
        list.add(NewsActivityFragment.TYPE_BEST);
        list.add(NewsActivityFragment.TYPE_NEW);
        list.add(NewsActivityFragment.TYPE_SHOW);
        list.add(NewsActivityFragment.TYPE_ASK);
        list.add(NewsActivityFragment.TYPE_JOB);

        return list;
    }
}
