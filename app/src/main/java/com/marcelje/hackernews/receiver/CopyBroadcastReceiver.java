package com.marcelje.hackernews.receiver;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.marcelje.hackernews.R;

public class CopyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getDataString();

        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Link url", url);

        clipboardManager.setPrimaryClip(data);

        Toast.makeText(context, context.getString(R.string.message_copy, url), Toast.LENGTH_SHORT).show();
    }
}
