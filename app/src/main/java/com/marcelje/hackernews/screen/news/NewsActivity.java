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
import com.marcelje.hackernews.factory.SpinnerFactory;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.screen.settings.SettingsActivity;
import com.marcelje.hackernews.utils.SettingsUtils;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
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

    private void attachSpinner() {
        String defaultNews = SettingsUtils.getDefaultNews(this);
        Spinner spinner = SpinnerFactory.createSpinner(this, this);
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        spinner.setSelection(adapter.getPosition(defaultNews));

        getToolbar().addView(spinner);
    }

    private void attachFragment() {
        mFragment = NewsActivityFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragment)
                .commit();
    }
}
