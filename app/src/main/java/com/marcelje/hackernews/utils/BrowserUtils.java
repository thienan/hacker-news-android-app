package com.marcelje.hackernews.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.receiver.CopyReceiver;
import com.marcelje.hackernews.screen.web.WebActivity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public final class BrowserUtils {

    private BrowserUtils() {}

    private static final String ACTION_CUSTOM_TABS_CONNECTION =
            "android.support.customtabs.action.CustomTabsService";

    private static final String STABLE_PACKAGE = "com.android.chrome";
    private static final String BETA_PACKAGE = "com.chrome.beta";
    private static final String DEV_PACKAGE = "com.chrome.dev";
    private static final String LOCAL_PACKAGE = "com.google.android.apps.chrome";

    public static void openTab(Activity activity, String url) {
        if (activity == null || TextUtils.isEmpty(url)) return;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        setShowTitle(builder);
        setToolbarColor(activity, builder);
        setAnimation(activity, builder);
        addCopyMenuItem(activity, builder);
        addShareMenuItem(builder);

        CustomTabsIntent customTabsIntent = builder.build();

        String packageName = getPackageNameToUse(activity);

        if (packageName == null) {
            WebActivity.startActivity(activity, url);
        } else {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, Uri.parse(url));
        }
    }

    private static void setShowTitle(CustomTabsIntent.Builder builder) {
        if (builder == null) return;

        builder.setShowTitle(true);
    }

    private static void setToolbarColor(Context context, CustomTabsIntent.Builder builder) {
        if (context == null || builder == null) return;

        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    private static void setAnimation(Context context, CustomTabsIntent.Builder builder) {
        if (context == null || builder == null) return;

        builder.setStartAnimations(context, R.anim.slide_up, R.anim.no_change);
        builder.setExitAnimations(context, R.anim.no_change, R.anim.slide_down);
    }

    private static void addCopyMenuItem(Context context, CustomTabsIntent.Builder builder) {
        if (context == null || builder == null) return;

        String label = context.getString(R.string.title_copy);

        Intent intent = new Intent(context, CopyReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addMenuItem(label, pendingIntent);
    }

    private static void addShareMenuItem(CustomTabsIntent.Builder builder) {
        if (builder == null) return;

        builder.addDefaultShareMenuItem();
    }

    private static String getPackageNameToUse(Context context) {
        if (context == null) return null;

        String packageNameToUse = null;

        PackageManager pm = context.getPackageManager();

        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));

        ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);

        String defaultViewHandlerPackageName = null;
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
        }

        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);

        List<String> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        if (packagesSupportingCustomTabs.isEmpty()) {
            packageNameToUse = null;
        } else if (packagesSupportingCustomTabs.size() == 1) {
            packageNameToUse = packagesSupportingCustomTabs.get(0);
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                && !hasSpecializedHandlerIntents(context, activityIntent)
                && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
            packageNameToUse = defaultViewHandlerPackageName;
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            packageNameToUse = STABLE_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
            packageNameToUse = BETA_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
            packageNameToUse = DEV_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            packageNameToUse = LOCAL_PACKAGE;
        }
        return packageNameToUse;
    }

    private static boolean hasSpecializedHandlerIntents(Context context, Intent intent) {
        if (context == null || intent == null) return false;

        try {
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> handlers = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);

            if (handlers == null || handlers.size() == 0) {
                return false;
            }

            for (ResolveInfo resolveInfo : handlers) {
                IntentFilter filter = resolveInfo.filter;
                if (filter == null) continue;
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue;
                if (resolveInfo.activityInfo == null) continue;
                return true;
            }

        } catch (RuntimeException e) {
            Timber.e("Runtime exception while getting specialized handlers");
        }

        return false;
    }
}