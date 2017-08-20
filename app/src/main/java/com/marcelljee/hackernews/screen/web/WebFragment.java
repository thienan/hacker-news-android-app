package com.marcelljee.hackernews.screen.web;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.fragment.ToolbarFragment;

public class WebFragment extends ToolbarFragment {

    private static final String ARG_URL = "com.marcelljee.hackernews.screen.web.arg.URL";

    private WebView wvWebPage;

    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();

        Bundle args = createArguments(url);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);

        Bundle args = getArguments();
        String url = args.getString(ARG_URL);
        setUpWebView(rootView, url, savedInstanceState);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        wvWebPage.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    private static Bundle createArguments(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);

        return args;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView(View rootView, String url, Bundle savedInstanceState) {
        wvWebPage = (WebView) rootView.findViewById(R.id.wv_web_page);

        WebSettings webSettings = wvWebPage.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        wvWebPage.setWebViewClient(new WebActivityClient());

        if (savedInstanceState == null) {
            wvWebPage.loadUrl(url);
        } else {
            wvWebPage.restoreState(savedInstanceState);
        }
    }

    public WebView getWebView() {
        return wvWebPage;
    }

    private class WebActivityClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            getActivity().findViewById(R.id.layout_web_title).setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            TextView tvWebTitle = (TextView) getActivity().findViewById(R.id.tv_web_title);
            tvWebTitle.setText(view.getTitle());

            TextView tvWebUrl = (TextView) getActivity().findViewById(R.id.tv_web_url);
            tvWebUrl.setText(url);

            getActivity().findViewById(R.id.layout_web_title).setVisibility(View.VISIBLE);
        }
    }
}
