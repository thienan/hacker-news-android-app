package com.marcelljee.hackernews.screen.web;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.WebToolbarActivity;
import com.marcelljee.hackernews.utils.ClipboardUtils;
import com.marcelljee.hackernews.utils.IntentUtils;
import com.marcelljee.hackernews.utils.MenuUtils;

public class WebActivity extends WebToolbarActivity {

    private static final String TAG_FRAGMENT = "com.marcelljee.hackernews.screen.web.tag.FRAGMENT";

    public static final String EXTRA_URL = "com.marcelljee.hackernews.screen.web.extra.URL";

    public static void startActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, WebActivity.class);

        Bundle extras = createExtras(url);
        intent.putExtras(extras);

        WebActivity.startActivity(activity, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(NO_LAYOUT);
        setDisplayHomeAsCloseEnabled(true);

        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_URL);
        setTitle(url);

        if (savedInstanceState == null) {
            setFragment(WebFragment.newInstance(url));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy:
                ClipboardUtils.copyLink(this, getFragment().getWebView().getUrl());
                return true;
            case R.id.action_share:
                MenuUtils.openShareTextChooser(this, getFragment().getWebView().getUrl());
                return true;
            case R.id.action_open_in_browser:
                IntentUtils.openBrowser(this, getFragment().getWebView().getUrl());
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && getFragment().getWebView().canGoBack()) {
            getFragment().getWebView().goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private static Bundle createExtras(String url) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_URL, url);

        return extras;
    }

    private void setFragment(WebFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_container, fragment, TAG_FRAGMENT)
                .commit();
    }

    private WebFragment getFragment() {
        return (WebFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
