package com.marcelje.hackernews.screen.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.WebToolbarActivity;
import com.marcelje.hackernews.utils.ClipboardUtils;
import com.marcelje.hackernews.utils.IntentUtils;
import com.marcelje.hackernews.utils.MenuUtils;

public class WebActivity extends WebToolbarActivity {

    private static final String EXTRA_URL = "com.marcelje.hackernews.screen.web.extra.URL";

    private WebView wvWebPage;

    private String mUrl;

    public static void startActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, WebActivity.class);

        Bundle extras = createExtras(url);
        intent.putExtras(extras);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setDisplayHomeAsCloseEnabled(true);

        wvWebPage = (WebView) findViewById(R.id.wv_web_page);

        wvWebPage.getSettings().setJavaScriptEnabled(true);
        wvWebPage.getSettings().setLoadWithOverviewMode(true);
        wvWebPage.getSettings().setUseWideViewPort(true);
        wvWebPage.setWebViewClient(new WebActivityClient(this));

        extractExtras();

        wvWebPage.loadUrl(mUrl);
        setTitle(mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_change, R.anim.slide_down);
                return true;
            case R.id.action_copy:
                ClipboardUtils.copyLink(this, wvWebPage.getUrl());
                return true;
            case R.id.action_share:
                MenuUtils.openShareTextChooser(this, wvWebPage.getUrl());
                return true;
            case R.id.action_open_in_browser:
                IntentUtils.openBrowser(this, wvWebPage.getUrl());
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvWebPage.canGoBack()) {
            wvWebPage.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private static Bundle createExtras(String url) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_URL, url);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_URL)) {
            mUrl = intent.getStringExtra(EXTRA_URL);
        }
    }
}
