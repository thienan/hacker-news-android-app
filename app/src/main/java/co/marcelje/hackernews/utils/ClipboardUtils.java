package co.marcelje.hackernews.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import co.marcelje.hackernews.R;

@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public final class ClipboardUtils {

    private static final String LABEL_COPIED_LINK = "Copied Link";
    private static final String LABEL_COPIED_DEFAULT = "Copied";

    private ClipboardUtils() {}

    public static void copyLink(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) return;

        copy(context, LABEL_COPIED_LINK, url, context.getString(R.string.message_copy, url));
    }

    public static void copy(Context context, String label, String text, String message) {
        if (context == null || TextUtils.isEmpty(text)) return;
        if (TextUtils.isEmpty(label)) label = LABEL_COPIED_DEFAULT;
        if (TextUtils.isEmpty(message)) message = context.getString(R.string.message_copied_to_clipboard);

        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(label, text);

        clipboardManager.setPrimaryClip(data);

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
