package com.marcelljee.hackernews.screen.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.FragmentActivity;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.factory.SpinnerFactory;
import com.marcelljee.hackernews.screen.about.AboutActivity;
import com.marcelljee.hackernews.screen.settings.SettingsActivity;
import com.marcelljee.hackernews.utils.SettingsUtils;

public class NewsActivity extends FragmentActivity<NewsFragment>
        implements AdapterView.OnItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String EXTRA_NEWS_TYPE = "com.marcelljee.hackernews.screen.news.extra.NEWS_TYPE";

    private static final String STATE_NEWS_TYPE = "com.marcelljee.hackernews.screen.news.state.NEWS_TYPE";
    private static final String STATE_NEWS_SHOW_ALL = "com.marcelljee.hackernews.screen.news.state.NEWS_SHOW_ALL";

    private String mNewsType;
    private boolean isShowAll = true;

    private boolean isNewState = false;

    public static Intent createIntent(Context context, String newsType) {
        Intent intent = new Intent(context, NewsActivity.class);

        Bundle extras = createExtras(newsType);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayShowTitleEnabled(false);

        extractExtras();
        attachSpinner(savedInstanceState);

        if (savedInstanceState == null) {
            isNewState = true;
            setFragment(NewsFragment.newInstance());
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mNewsType = savedInstanceState.getString(STATE_NEWS_TYPE);
        isShowAll = savedInstanceState.getBoolean(STATE_NEWS_SHOW_ALL);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_NEWS_TYPE, mNewsType);
        outState.putBoolean(STATE_NEWS_SHOW_ALL, isShowAll);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getString(R.string.news_type_history).equals(mNewsType)) {
            menu.findItem(R.id.action_clear_history).setVisible(true);

            menu.findItem(R.id.action_show_all).setVisible(false);
            menu.findItem(R.id.action_show_unread).setVisible(false);
        } else {
            menu.findItem(R.id.action_clear_history).setVisible(false);

            if (isShowAll) {
                menu.findItem(R.id.action_show_all).setVisible(false);
                menu.findItem(R.id.action_show_unread).setVisible(true);
            } else {
                menu.findItem(R.id.action_show_all).setVisible(true);
                menu.findItem(R.id.action_show_unread).setVisible(false);
            }
        }

        if (!SettingsUtils.readIndicatorEnabled(this)) {
            menu.findItem(R.id.action_show_all).setVisible(false);
            menu.findItem(R.id.action_show_unread).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_refresh:
                getFragment().refresh();
                return true;
            case R.id.action_clear_history:
                DatabaseDao.deleteAllHistoryItem(this);
                getFragment().clearItems();
                return true;
            case R.id.action_show_all:
                isShowAll = true;
                getFragment().showAllItems();
                supportInvalidateOptionsMenu();
                return true;
            case R.id.action_show_unread:
                isShowAll = false;
                getFragment().showUnreadItems();
                supportInvalidateOptionsMenu();
                return true;
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
        // TODO: This is a workaround to make saveInstanceState possible for spinner.
        if (isNewState || !parent.getSelectedItem().toString().equals(mNewsType)) {
            mNewsType = parent.getSelectedItem().toString();
            String type = (String) parent.getItemAtPosition(pos);
            getFragment().changeNewsType(type);
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.settings_history_key).equals(key)) {
            Spinner spinner = (Spinner) findViewById(R.id.spinner_news_type);
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();

            if (SettingsUtils.historyEnabled(this)) {
                if (adapter.getPosition(getString(R.string.news_type_history)) == -1) {
                    adapter.add(getString(R.string.news_type_history));
                }
            } else {
                adapter.remove(getString(R.string.news_type_history));
            }
        } else if (getString(R.string.settings_read_indicator_key).equals(key)) {
            supportInvalidateOptionsMenu();
        }
    }

    private static Bundle createExtras(String newsType) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_NEWS_TYPE, newsType);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_NEWS_TYPE)) {
            mNewsType = intent.getStringExtra(EXTRA_NEWS_TYPE);
        }
    }

    private void attachSpinner(Bundle savedInstanceState) {
        Spinner spinner = SpinnerFactory.createNewsTypeSpinner(this, this);
        spinner.setId(R.id.spinner_news_type);

        if (savedInstanceState == null) {
            if (TextUtils.isEmpty(mNewsType)) {
                mNewsType = SettingsUtils.getDefaultNews(this);
            }

            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            spinner.setSelection(adapter.getPosition(mNewsType));
        }

        getToolbar().addView(spinner);
    }
}
