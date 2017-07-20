package com.marcelje.hackernews.screen.news;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.FragmentActivity;
import com.marcelje.hackernews.factory.SpinnerFactory;
import com.marcelje.hackernews.screen.about.AboutActivity;
import com.marcelje.hackernews.screen.settings.SettingsActivity;
import com.marcelje.hackernews.utils.SettingsUtils;

public class NewsActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    private NewsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayShowTitleEnabled(false);

        attachSpinner(savedInstanceState);

        mFragment = NewsFragment.newInstance();
        setFragment(mFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_about:
                AboutActivity.startActivity(this);
                return true;
            case R.id.action_settings:
                SettingsActivity.startActivity(this);
                return true;
            default:
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String type = (String) parent.getItemAtPosition(pos);
        mFragment.changeNewsType(type);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    @Override
    public void onBackPressed() {
        finish(); // no animation
    }

    private void attachSpinner(Bundle savedInstanceState) {
        Spinner spinner = SpinnerFactory.createSpinner(this, this);
        spinner.setId(R.id.spinner_news_type);

        if (savedInstanceState == null) {
            String defaultNews = SettingsUtils.getDefaultNews(this);
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            spinner.setSelection(adapter.getPosition(defaultNews));
        }

        getToolbar().addView(spinner);
    }
}
