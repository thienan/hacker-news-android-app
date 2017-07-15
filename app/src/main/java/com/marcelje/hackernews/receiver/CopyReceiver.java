package com.marcelje.hackernews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marcelje.hackernews.utils.ClipboardUtils;

public class CopyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getDataString();
        ClipboardUtils.copyLink(context, url);
    }
}
