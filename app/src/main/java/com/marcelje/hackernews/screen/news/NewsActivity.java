package com.marcelje.hackernews.screen.news;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.factory.SpinnerFactory;
import com.marcelje.hackernews.activity.ToolbarActivity;

public class NewsActivity extends ToolbarActivity implements AdapterView.OnItemSelectedListener {

    private NewsActivityFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setDisplayShowTitleEnabled(false);
        attachSpinner();
        attachFragment();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String type = (String) parent.getItemAtPosition(pos);
        mFragment.retrieveNews(type);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    @Override
    public void onBackPressed() {
        finish(); // no animation
    }

    private void attachSpinner() {
        getToolbar().addView(SpinnerFactory.createSpinner(this, this));
    }

    private void attachFragment() {
        mFragment = NewsActivityFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragment)
                .commit();
    }
}
