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
import com.marcelljee.hackernews.databinding.component.AppDataBindingComponent;
import com.marcelljee.hackernews.event.ItemUpdateEvent;
import com.marcelljee.hackernews.factory.SnackbarFactory;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.loader.AppResponse;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.loader.UserLoader;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.model.User;
import com.marcelljee.hackernews.utils.PagingUtils;
import com.marcelljee.hackernews.utils.SettingsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

public class UserFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    private static final String ARG_USER_ID = "com.marcelljee.hackernews.screen.user.arg.USER_ID";

    private static final String STATE_USER = "com.marcelljee.hackernews.screen.user.state.USER";
    private static final String STATE_CURRENT_PAGE = "com.marcelljee.hackernews.screen.user.state.CURRENT_PAGE";

    private static final int LOADER_ID_USER_ITEM = 8000;
    private static final int LOADER_ID_SUBMISSIONS = 9000;

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

        Bundle args = getArguments();
        String userId = args.getString(ARG_USER_ID);
        mUser = User.createTempUser(userId);

        mUserSubmissionAdapter = new ItemAdapter(getToolbarActivity());

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        mUserSubmissionAdapter.closeActionModeMenu();
        EventBus.getDefault().unregister(this);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserBinding.inflate(inflater, container, false,
                new AppDataBindingComponent(getToolbarActivity()));
        mBinding.setUser(mUser);

        mBinding.rvSubmissionList.setAdapter(mUserSubmissionAdapter);
        mBinding.rvSubmissionList.setOnLoadMoreListener((page, totalItemsCount) -> nextPageSubmissions());

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_USER_ITEM, null, this);

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mUser.update(Parcels.unwrap(inState.getParcelable(STATE_USER)));
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
        mUserSubmissionAdapter.restoreState(inState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_USER, Parcels.wrap(mUser));
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        mUserSubmissionAdapter.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_USER_ITEM:
                return new UserLoader(getContext(), mUser.getId());
            case LOADER_ID_SUBMISSIONS:
                List<Long> items = PagingUtils.getItems(mUser.getSubmitted(), mCurrentPage);
                return new ItemListLoader(getContext(), items);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        AppResponse response = (AppResponse) data;

        if (response != null && response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_USER_ITEM:
                    mUser.update((User) response.getData());
                    // check if this comes after configuration changes and the submissions is not loaded yet.
                    if (mUserSubmissionAdapter.getItemCount() == 0 && mUser.getSubmitted().size() > 0) {
                        refreshSubmissions();
                    }
                    break;
                case LOADER_ID_SUBMISSIONS:
                    mUserSubmissionAdapter.addItems((List<Item>) response.getData());
                    mBinding.rvSubmissionList.hideProgressBar();
                    break;
                default:
                    //do nothing
            }

        } else {
            switch (loader.getId()) {
                case LOADER_ID_SUBMISSIONS:
                    mCurrentPage--;
                    break;
                default:
                    //do nothing
            }

            SnackbarFactory.createRetrieveErrorSnackbar(mBinding.getRoot()).show();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

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
        if (mUser.getSubmitted() == null) return;
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_SUBMISSIONS, null, this);
    }
}
