package com.marcelje.hackernews.screen.user;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.FragmentUserBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.fragment.ToolbarFragment;
import com.marcelje.hackernews.listener.EndlessRecyclerViewScrollListener;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.loader.UserLoader;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.model.User;
import com.marcelje.hackernews.utils.CollectionUtils;

import org.parceler.Parcels;

import java.util.List;

public class UserFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<User>>, View.OnClickListener {

    private static final String ARG_USER_ID = "com.marcelje.hackernews.screen.user.arg.USER_ID";

    private static final String STATE_USER = "com.marcelje.hackernews.screen.user.state.USER";
    private static final String STATE_SUBMISSION_DATA = "com.marcelje.hackernews.screen.user.state.SUBMISSION_DATA";
    private static final String STATE_CURRENT_PAGE = "com.marcelje.hackernews.screen.user.state.CURRENT_PAGE";

    private static final int LOADER_ID_USER_ITEM = 800;
    private static final int LOADER_ID_SUBMISSIONS = 900;

    private static final int ITEM_COUNT = 10;

    private String mUserId;

    private FragmentUserBinding mBinding;
    private SubmissionAdapter mAdapter;

    private int mCurrentPage = 1;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SubmissionAdapter(getToolbarActivity());

        mBinding.rvSubmissionList.setLayoutManager(layoutManager);
        mBinding.rvSubmissionList.setAdapter(mAdapter);
        mBinding.rvSubmissionList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                nextPageSubmissions();
            }
        });

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_USER_ITEM, null, this);

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mBinding.setUser((User) Parcels.unwrap(inState.getParcelable(STATE_USER)));
        mAdapter.swapData((List<Item>) Parcels.unwrap(inState.getParcelable(STATE_SUBMISSION_DATA)));
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_USER, Parcels.wrap(mBinding.getUser()));
        outState.putParcelable(STATE_SUBMISSION_DATA, Parcels.wrap(mAdapter.getData()));
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        super.onSaveInstanceState(outState);
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
            // check if this comes after configuration changes and the submissions is not loaded yet.
            if (mAdapter.getData().size() == 0 && mBinding.getUser().getSubmitted().size() > 0) {
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
    public void onClick(View view) {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_USER_ITEM, null, this);
        refreshSubmissions();
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
