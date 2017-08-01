// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package co.marcelje.hackernews.chrome;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import java.util.List;

/**
 * This is a helper class to manage the connection to the Custom Tabs Service.
 */
public class CustomTabsHelper implements CustomTabsConnection.ServiceConnectionCallback {
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mClient = client;
        mClient.warmup(0L);
    }

    @Override
    public void onServiceDisconnected() {
        mClient = null;
        mCustomTabsSession = null;
    }

    /**
     * Binds the Activity to the Custom Tabs Service.
     * @param activity the activity to be binded to the service.
     */
    public void bindCustomTabsService(Activity activity) {
        if (mClient != null) return;

        String packageName = CustomTabsBrowser.getCustomTabPackageName(activity);
        if (packageName == null) return;

        mConnection = new CustomTabsConnection(this);
        CustomTabsClient.bindCustomTabsService(activity, packageName, mConnection);
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service.
     * @param activity the activity that is connected to the service.
     */
    public void unbindCustomTabsService(Activity activity) {
        if (mConnection == null) return;
        activity.unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession.
     *
     * @return a CustomTabsSession.
     */
    public CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(null);
        }
        return mCustomTabsSession;
    }

    /**
     * @see CustomTabsSession#mayLaunchUrl(Uri, Bundle, List).
     * @return true if call to mayLaunchUrl was accepted.
     */
    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    public boolean mayLaunchUrl(Uri uri, Bundle extras, List<Bundle> otherLikelyBundles) {
        if (mClient == null) return false;

        CustomTabsSession session = getSession();
        return session != null && session.mayLaunchUrl(uri, extras, otherLikelyBundles);

    }
}
