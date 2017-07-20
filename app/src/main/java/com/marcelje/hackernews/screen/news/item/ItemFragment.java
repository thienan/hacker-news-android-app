package com.marcelje.hackernews.screen.news.item;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.databinding.FragmentItemBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.listener.EndlessRecyclerViewScrollListener;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.utils.BrowserUtils;
import com.marcelje.hackernews.utils.CollectionUtils;
import com.marcelje.hackernews.utils.HackerNewsUtils;
import com.marcelje.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

import java.util.List;

public class ItemFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>>, View.OnClickListener {

    private static final String ARG_ITEM = "com.marcelje.hackernews.screen.news.item.arg.ITEM";
    private static final String ARG_PARENT = "com.marcelje.hackernews.screen.news.item.arg.PARENT";
    private static final String ARG_POSTER = "com.marcelje.hackernews.screen.news.item.arg.POSTER";

    private static final String ITEM_TYPE_COMMENT = "comment";
    private static final String ITEM_TYPE_STORY = "story";
    private static final String ITEM_TYPE_POLL = "poll";
    private static final String ITEM_TYPE_JOB = "job";

    private static final int LOADER_ID_COMMENT_ITEM = 118;

    private static final int ITEM_COUNT = 10;
    private int mCurrentPage = 1;

    private ToolbarActivity mActivity;

    private FragmentItemBinding mBinding;
    private CommentAdapter mAdapter;

    private Item mItem;
    private String mParent;
    private String mPoster;

    public static ItemFragment newInstance(Item item, String parent, String poster) {
        ItemFragment fragment = new ItemFragment();

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
        mBinding = FragmentItemBinding.inflate(inflater, container, false);
        mBinding.setItem(mItem);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        if (TextUtils.isEmpty(mParent) && TextUtils.isEmpty(mPoster)) {
            mAdapter = new CommentAdapter(mActivity, null, mItem.getBy());
        } else {
            mAdapter = new CommentAdapter(mActivity, mItem.getBy(), mPoster);
        }

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

        Fragment fragment;

        switch (mItem.getType()) {
            case ITEM_TYPE_COMMENT:
                fragment = CommentFragment.newInstance(mItem, mParent, mPoster);
                break;
            case ITEM_TYPE_STORY:
                //fall through
            case ITEM_TYPE_JOB:
                //fall through
            case ITEM_TYPE_POLL:
                //fall through
            default:
                fragment = StoryFragment.newInstance(mItem);
        }

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.item_head_container, fragment)
                .commit();

        refreshPage();

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
            switch (loader.getId()) {
                case LOADER_ID_COMMENT_ITEM:
                    mAdapter.addData(response.getData());
                    hideProgressBar();
                    break;
                default:
            }
        } else {
            SnackbarFactory.createRetrieveErrorSnackbar(mBinding.sectionCommentList.getRoot(), this).show();
            hideProgressBar();
        }

    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    @Override
    public void onClick(View view) {
        retrieveComments();
    }

    public void share() {
        MenuUtils.openShareHackerNewsLinkChooser(getContext(), mItem);
    }

    public void bookmark(MenuItem menuItem) {
        if (HackerNewsDao.isItemAvailable(getContext(), mItem.getId())) {
            HackerNewsDao.deleteItem(getContext(), mItem.getId());
            SnackbarFactory.createUnbookmarkedSuccessSnackBar(mBinding.getRoot()).show();
            menuItem.setTitle(R.string.menu_item_bookmark);
        } else {
            HackerNewsDao.insertItem(getContext(), mItem);
            SnackbarFactory.createBookmarkedSuccessSnackBar(mBinding.getRoot()).show();
            menuItem.setTitle(R.string.menu_item_unbookmark);
        }
    }

    public void openPage() {
        BrowserUtils.openTab(getActivity(), HackerNewsUtils.geItemUrl(mItem.getId()));
    }

    public void refreshPage() {
        mAdapter.clearData();
        mCurrentPage = 1;
        showProgressBar();

        //loadNews();
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
}
