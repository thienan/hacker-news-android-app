package co.marcelje.hackernews.screen.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import co.marcelje.hackernews.R;
import co.marcelje.hackernews.activity.FragmentActivity;
import co.marcelje.hackernews.factory.SpinnerFactory;
import co.marcelje.hackernews.screen.about.AboutActivity;
import co.marcelje.hackernews.screen.settings.SettingsActivity;
import co.marcelje.hackernews.utils.SettingsUtils;

public class NewsActivity extends FragmentActivity<NewsFragment> implements AdapterView.OnItemSelectedListener {

    private static final String EXTRA_NEWS_TYPE = "co.marcelje.hackernews.screen.news.extra.NEWS_TYPE";

    private static final String STATE_NEWS_TYPE = "co.marcelje.hackernews.screen.news.state.NEWS_TYPE";

    private String mNewsType;

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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mNewsType = savedInstanceState.getString(STATE_NEWS_TYPE);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_NEWS_TYPE, mNewsType);
        super.onSaveInstanceState(outState);
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
            case R.id.action_refresh:
                getFragment().refresh();
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
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
        Spinner spinner = SpinnerFactory.createSpinner(this, this);
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
