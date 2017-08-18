package com.marcelljee.hackernews.screen.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.database.DatabaseDao;

public class SettingsFragment extends PreferenceFragmentCompat
        implements OnSharedPreferenceChangeListener {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);
        setPreferenceSummary(getPreferenceScreen().getSharedPreferences(),
                getString(R.string.settings_type_key));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setPreferenceSummary(sharedPreferences, key);

        if (getString(R.string.settings_read_indicator_key).equals(key)) {
            eraseReadIndicator(sharedPreferences, key);
        }

        if (getString(R.string.settings_history_key).equals(key)) {
            eraseHistory(sharedPreferences, key);
        }
    }

    private void setPreferenceSummary(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.settings_type_key).equals(key)) {
            ListPreference newsTypePreference = (ListPreference) findPreference(key);
            if (newsTypePreference == null) return;

            String value = sharedPreferences.getString(key, getString(R.string.news_type_top));

            int prefIndex = newsTypePreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                newsTypePreference.setSummary(newsTypePreference.getEntries()[prefIndex]);
            }
        }
    }

    private void eraseReadIndicator(SharedPreferences sharedPreferences, String key) {
        boolean readIndicatorEnabled = sharedPreferences.getBoolean(key, true);

        if (!readIndicatorEnabled) {
            DatabaseDao.deleteAllReadIndicatorItem(getContext());
        }
    }

    private void eraseHistory(SharedPreferences sharedPreferences, String key) {
        boolean enableHistory = sharedPreferences.getBoolean(key, true);

        if (!enableHistory) {
            DatabaseDao.deleteAllHistoryItem(getContext());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}