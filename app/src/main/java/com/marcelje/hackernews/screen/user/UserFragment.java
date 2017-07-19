package com.marcelje.hackernews.screen.user;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.databinding.FragmentUserBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.listener.EndlessRecyclerViewScrollListener;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.loader.UserLoader;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.model.User;
import com.marcelje.hackernews.utils.CollectionUtils;

import java.util.List;

public class UserFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<User>>, View.OnClickListener {

    private static final String ARG_USER_ID = "com.marcelje.hackernews.screen.user.arg.USER_ID";

    private static final int LOADER_ID_USER_ITEM = 119;
    private static final int LOADER_ID_SUBMISSIONS = 120;

    private static final int ITEM_COUNT = 10;
    private int mCurrentPage = 1;

    private ToolbarActivity mActivity;

    private FragmentUserBinding mBinding;
    private String mUserId;

    private SubmissionAdapter mAdapter;

    public static UserFragment newInstance(String userId) {
        UserFragment fragment = new UserFragment();

        Bundle args = createArguments(userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();

        mActivity = ToolbarActivity.getActivity(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SubmissionAdapter(mActivity);

        mBinding.rvSubmissionList.setLayoutManager(layoutManager);
        mBinding.rvSubmissionList.setAdapter(mAdapter);
        mBinding.rvSubmissionList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                nextPageSubmissions();
            }
        });

        if (savedInstanceState == null) {
            retrieveUser();
        }

        return mBinding.getRoot();
    }

    @Override
    public Loader<HackerNewsResponse<User>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_USER_ITEM:
                return new UserLoader(getActivity(), mUserId);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<User>> loader, HackerNewsResponse<User> response) {
        if (response.isSuccessful()) {
            mBinding.setUser(response.getData());
            refreshSubmissions();
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
    public void onClick(View view) {
        retrieveUser();
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

    private void retrieveUser() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_USER_ITEM, null, this);
    }

    private void refreshSubmissions() {
        mAdapter.clearData();
        mCurrentPage = 1;
        showProgressBar();
        retrieveSubmissions();
    }

    private void nextPageSubmissions() {
        mCurrentPage++;
        retrieveSubmissions();
    }

    private void retrieveSubmissions() {
        if (mBinding.getUser().getSubmitted() == null) return;
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID_SUBMISSIONS);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_SUBMISSIONS, null, getCallback());
    }

    private void showProgressBar() {
        mBinding.pbLoading.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.pbLoading.setVisibility(View.GONE);
    }

    private LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> getCallback() {
        return new LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>>() {
            @Override
            public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
                List<Long> kids = mBinding.getUser().getSubmitted();

                List<Long> list = CollectionUtils.subList(kids,
                        (mCurrentPage - 1) * ITEM_COUNT,
                        mCurrentPage * ITEM_COUNT);

                if (list.size() == 0) return null;

                return new ItemListLoader(getActivity(), list);
            }

            @Override
            public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader, HackerNewsResponse<List<Item>> data) {
                mAdapter.addData(data.getData());
                hideProgressBar();
            }

            @Override
            public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

            }
        };
    }
}
