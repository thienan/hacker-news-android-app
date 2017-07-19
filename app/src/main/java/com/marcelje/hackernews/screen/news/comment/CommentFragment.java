package com.marcelje.hackernews.screen.news.comment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.listener.EndlessRecyclerViewScrollListener;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.databinding.FragmentCommentBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.utils.CollectionUtils;

import org.parceler.Parcels;

import java.util.List;

public class CommentFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>>, View.OnClickListener {

    private static final String ARG_ITEM = "com.marcelje.hackernews.screen.news.comment.arg.ITEM";
    private static final String ARG_PARENT = "com.marcelje.hackernews.screen.news.comment.arg.PARENT";
    private static final String ARG_POSTER = "com.marcelje.hackernews.screen.news.comment.arg.POSTER";

    private static final int LOADER_ID_COMMENT_ITEM = 118;

    private static final int ITEM_COUNT = 10;
    private int mCurrentPage = 1;

    private ToolbarActivity mActivity;

    private FragmentCommentBinding mBinding;
    private CommentAdapter mAdapter;

    private Item mItem;
    private String mParent;
    private String mPoster;

    public static CommentFragment newInstance(Item item, String parent, String poster) {
        CommentFragment fragment = new CommentFragment();

        Bundle args = createArguments(item, parent, poster);
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
        mBinding = FragmentCommentBinding.inflate(inflater, container, false);
        mBinding.setItem(mItem);
        mBinding.setActivity(mActivity);
        mBinding.setParent(mParent);
        mBinding.setPoster(mPoster);
        mBinding.setItemUserClickHandlers(new ItemUserClickHandlers(mActivity));

        mBinding.tvCommentInfo.setMovementMethod(LinkMovementMethod.getInstance());

        // TODO: find a better way to remove maxLines
        mBinding.sectionCommentMain.tvText.setMaxLines(Integer.MAX_VALUE);
        mBinding.sectionCommentMain.tvText.setMovementMethod(LinkMovementMethod.getInstance());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CommentAdapter(mActivity, mItem.getBy(), mPoster);

        mBinding.sectionCommentList.rvCommentList.setLayoutManager(layoutManager);
        mBinding.sectionCommentList.rvCommentList.setAdapter(mAdapter);
        mBinding.sectionCommentList.rvCommentList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mBinding.sectionCommentList.rvCommentList
                .addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        nextPageComments();
                    }
                });

        refreshComments();

        return mBinding.getRoot();
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_COMMENT_ITEM:
                List<Long> kids = mItem.getKids();

                List<Long> list = CollectionUtils.subList(kids,
                        (mCurrentPage - 1) * ITEM_COUNT,
                        mCurrentPage * ITEM_COUNT);

                if (list.size() == 0) return null;

                return new ItemListLoader(getActivity(), list);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader, HackerNewsResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            mAdapter.addData(response.getData());
        } else {
            SnackbarFactory
                    .createRetrieveErrorSnackbar(mBinding.sectionCommentList.getRoot(),
                            CommentFragment.this).show();
        }

        hideProgressBar();
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    @Override
    public void onClick(View view) {
        retrieveComments();
    }

    private static Bundle createArguments(Item item, String parent, String poster) {
        Bundle args = new Bundle();
        if (item != null) args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) args.putString(ARG_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) args.putString(ARG_POSTER, poster);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_ITEM)) {
            mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        }

        if (args.containsKey(ARG_PARENT)) {
            mParent = args.getString(ARG_PARENT);
        }

        if (args.containsKey(ARG_POSTER)) {
            mPoster = args.getString(ARG_POSTER);
        }
    }

    public void refreshComments() {
        mAdapter.clearData();
        mCurrentPage = 1;
        showProgressBar();
        retrieveComments();
    }

    private void nextPageComments() {
        mCurrentPage++;
        retrieveComments();
    }

    private void retrieveComments() {
        if (mItem.getKids() == null) return;
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID_COMMENT_ITEM);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_COMMENT_ITEM, null, this);
    }

    private void showProgressBar() {
        mBinding.sectionCommentList.pbLoading.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.sectionCommentList.pbLoading.setVisibility(View.GONE);
    }
}
