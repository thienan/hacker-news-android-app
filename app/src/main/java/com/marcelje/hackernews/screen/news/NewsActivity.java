package com.marcelje.hackernews.screen.news;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.factory.SpinnerFactory;
import com.marcelje.hackernews.activity.ToolbarActivity;

public class NewsActivity extends ToolbarActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setDisplayShowTitleEnabled(false);
        getToolbar().addView(SpinnerFactory.createSpinner(this, this));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        NewsActivityFragment fragment =
                (NewsActivityFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.news_fragment);

        String type = (String) parent.getItemAtPosition(pos);
        fragment.retrieveNews(type);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
}
