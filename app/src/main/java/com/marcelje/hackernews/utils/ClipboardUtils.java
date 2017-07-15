package com.marcelje.hackernews.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.marcelje.hackernews.R;

@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public final class ClipboardUtils {

    private ClipboardUtils() {}

    public static void copyLink(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) return;

        copy(context, "Link", url, context.getString(R.string.message_copy, url));
    }

    public static void copy(Context context, String label, String text, String message) {
        if (context == null || TextUtils.isEmpty(text)) return;
        if (TextUtils.isEmpty(label)) label = "Copied label";
        if (TextUtils.isEmpty(message)) message = text + " is copied";

        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(label, text);

        clipboardManager.setPrimaryClip(data);

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
