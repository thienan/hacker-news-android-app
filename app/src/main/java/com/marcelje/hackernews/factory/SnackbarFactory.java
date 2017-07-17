package com.marcelje.hackernews.factory;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.marcelje.hackernews.R;

@SuppressWarnings("WeakerAccess")
public class SnackbarFactory {

    public static Snackbar createRetrieveErrorSnackbar(View view, View.OnClickListener listener) {
        return createSnackBar(view, R.string.error_retrieve_data, R.string.action_retry, listener);
    }

    public static Snackbar createBookmarkedSuccessSnackBar(View view) {
        return createSnackBar(view, R.string.bookmarked, -1, null);
    }

    public static Snackbar createUnbookmarkedSuccessSnackBar(View view) {
        return createSnackBar(view, R.string.unbookmarked, -1, null);
    }

    public static Snackbar createSnackBar(View view, @StringRes
            int messageResId, @StringRes int actionResId, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, messageResId, Snackbar.LENGTH_LONG);
        if (actionResId > 0 && listener != null) snackbar.setAction(actionResId, listener);

        return snackbar;
    }

}
