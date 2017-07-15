package com.marcelje.hackernews.screen.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.utils.ClipboardUtils;
import com.marcelje.hackernews.utils.IntentUtils;
import com.marcelje.hackernews.utils.MenuUtils;

public class WebActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "com.marcelje.hackernews.screen.web.extra.URL";

    private WebView wvWebPage;

    public static void startActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Drawable close = ContextCompat.getDrawable(this, R.drawable.ic_close);
            close.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            getSupportActionBar().setHomeAsUpIndicator(close);
        }

        wvWebPage = (WebView) findViewById(R.id.wv_web_page);

        wvWebPage.getSettings().setJavaScriptEnabled(true);
        wvWebPage.getSettings().setLoadWithOverviewMode(true);
        wvWebPage.getSettings().setUseWideViewPort(true);
        wvWebPage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                findViewById(R.id.layout_web_title).setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                TextView tvWebTitle = (TextView) findViewById(R.id.tv_web_title);
                tvWebTitle.setText(view.getTitle());

                TextView tvWebUrl = (TextView) findViewById(R.id.tv_web_url);
                tvWebUrl.setText(url);

                findViewById(R.id.layout_web_title).setVisibility(View.VISIBLE);
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_URL)) {
            String url = intent.getStringExtra(EXTRA_URL);
            wvWebPage.loadUrl(url);
            setTitle(url);
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
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_change, R.anim.slide_down);
                return true;
            case R.id.action_copy:
                ClipboardUtils.copyLink(this, wvWebPage.getUrl());
                return true;
            case R.id.action_share:
                MenuUtils.openShareChooser(this, wvWebPage.getUrl());
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
}
