package com.marcelljee.hackernews.screen.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.marcelljee.hackernews.R;

class WebActivityClient extends WebViewClient {

    private final Activity mActivity;

    WebActivityClient(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mActivity.findViewById(R.id.layout_web_title).setVisibility(View.GONE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        TextView tvWebTitle = (TextView) mActivity.findViewById(R.id.tv_web_title);
        tvWebTitle.setText(view.getTitle());

        TextView tvWebUrl = (TextView) mActivity.findViewById(R.id.tv_web_url);
        tvWebUrl.setText(url);

        mActivity.findViewById(R.id.layout_web_title).setVisibility(View.VISIBLE);
    }
}
