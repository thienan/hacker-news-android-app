package com.marcelljee.hackernews.screen.user;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.databinding.FragmentUserBinding;
import com.marcelljee.hackernews.event.ItemUpdateEvent;
import com.marcelljee.hackernews.factory.SnackbarFactory;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.loader.HackerNewsResponse;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.loader.UserLoader;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.model.User;
import com.marcelljee.hackernews.utils.CollectionUtils;
import com.marcelljee.hackernews.utils.SettingsUtils;
import com.marcelljee.hackernews.viewmodel.FragmentUserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

public class UserFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<User>>,
        SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    private static final String ARG_USER_ID = "com.marcelljee.hackernews.screen.user.arg.USER_ID";

    private static final String STATE_USER = "com.marcelljee.hackernews.screen.user.state.USER";
    private static final String STATE_CURRENT_PAGE = "com.marcelljee.hackernews.screen.user.state.CURRENT_PAGE";

    private static final int LOADER_ID_USER_ITEM = 800;
    private static final int LOADER_ID_SUBMISSIONS = 900;

    private static final int ITEM_COUNT = 10;

    private String mUserId;

    private User mUser;
    private int mCurrentPage = 1;
    private ItemAdapter mUserSubmissionAdapter;

    private FragmentUserBinding mBinding;

    public static UserFragment newInstance(String userId) {
        UserFragment fragment = new UserFragment();

        Bundle args = createArguments(userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);

        extractArguments();
    }

    @Override
    public void onStop() {
        mUserSubmissionAdapter.closeActionModeMenu();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserBinding.inflate(inflater, container, false);
        mBinding.setViewModel(new FragmentUserViewModel(getToolbarActivity()));
        mBinding.setUser(User.createTempUser(mUserId));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mUserSubmissionAdapter = new ItemAdapter(getToolbarActivity());

        mBinding.rvSubmissionList.setLayoutManager(layoutManager);
        mBinding.rvSubmissionList.setAdapter(mUserSubmissionAdapter);
        mBinding.rvSubmissionList.showDivider();
        mBinding.rvSubmissionList.setOnLoadMoreListener((page, totalItemsCount) -> nextPageSubmissions());

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_USER_ITEM, null, this);

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mUser = Parcels.unwrap(inState.getParcelable(STATE_USER));
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
        mUserSubmissionAdapter.restoreState(inState);

        mBinding.setUser(mUser);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_USER, Parcels.wrap(mUser));
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        mUserSubmissionAdapter.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<HackerNewsResponse<User>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_USER_ITEM:
                return new UserLoader(getContext(), mUserId);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<User>> loader, HackerNewsResponse<User> response) {
        if (response.isSuccessful()) {
            mUser = response.getData();
            mBinding.setUser(mUser);
            // check if this comes after configuration changes and the submissions is not loaded yet.
            if (mUserSubmissionAdapter.getItemCount() == 0 && mBinding.getUser().getSubmitted().size() > 0) {
                refreshSubmissions();
            }
        } else {
            SnackbarFactory
                    .createRetrieveErrorSnackbar(mBinding.getRoot(),
                            UserFragment.this).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<User>> loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.settings_read_indicator_key).equals(key)
                && !SettingsUtils.readIndicatorEnabled(getContext())) {
            mUserSubmissionAdapter.clearReadIndicator();
        }
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_USER_ITEM, null, this);
    }

    @Subscribe()
    @SuppressWarnings({"unused"})
    public void onItemUpdateEvent(ItemUpdateEvent event) {
        mUserSubmissionAdapter.updateItem(event.getItem());
    }

    private static Bundle createArguments(String userId) {
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_USER_ID)) {
            mUserId = args.getString(ARG_USER_ID);
        }
    }

    private void refreshSubmissions() {
        mUserSubmissionAdapter.clearItems();
        mCurrentPage = 1;
        mBinding.rvSubmissionList.restartOnLoadMoreListener();
        mBinding.rvSubmissionList.showProgressBar();
        retrieveSubmissions();
    }

    private void nextPageSubmissions() {
        mCurrentPage++;
        retrieveSubmissions();
    }

    private void retrieveSubmissions() {
        if (mBinding.getUser().getSubmitted() == null) return;
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_SUBMISSIONS, null, getCallback());
    }

    private LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> getCallback() {
        return new LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>>() {
            @Override
            public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
                List<Long> submissions = mBinding.getUser().getSubmitted();

                List<Long> list = CollectionUtils.subList(submissions,
                        (mCurrentPage - 1) * ITEM_COUNT,
                        mCurrentPage * ITEM_COUNT);

                return new ItemListLoader(getContext(), list);
            }

            @Override
            public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader, HackerNewsResponse<List<Item>> data) {
                mUserSubmissionAdapter.addItems(data.getData());
                mBinding.rvSubmissionList.hideProgressBar();
            }

            @Override
            public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

            }
        };
    }
}
