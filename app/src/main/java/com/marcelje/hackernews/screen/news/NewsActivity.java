package com.marcelje.hackernews.screen.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.factory.SpinnerFactory;

public class NewsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.addView(SpinnerFactory.createSpinner(this, this));
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
