package com.marcelljee.hackernews.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public final class IntentUtils {

    private IntentUtils() {}

    public static void openBrowser(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) return;

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}
