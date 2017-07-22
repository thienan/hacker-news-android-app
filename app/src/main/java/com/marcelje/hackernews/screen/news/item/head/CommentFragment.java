package com.marcelje.hackernews.screen.news.item.head;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.FragmentCommentBinding;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.ItemHeadFragment;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

public class CommentFragment extends ItemHeadFragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String ARG_ITEM = "com.marcelje.hackernews.screen.news.item.head.arg.ITEM";
    private static final String ARG_PARENT = "com.marcelje.hackernews.screen.news.item.head.arg.PARENT";
    private static final String ARG_POSTER = "com.marcelje.hackernews.screen.news.item.head.arg.POSTER";

    private static final int LOADER_ID_COMMENT_HEAD = 100;

    private FragmentCommentBinding mBinding;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCommentBinding.inflate(inflater, container, false);
        mBinding.setItem(mItem);
        mBinding.setActivity(getToolbarActivity());
        mBinding.setParent(mParent);
        mBinding.setPoster(mPoster);
        mBinding.setItemUserClickHandlers(new ItemUserClickHandlers(getToolbarActivity()));

        mBinding.tvCommentInfo.setMovementMethod(LinkMovementMethod.getInstance());

        // TODO: find a better way to remove maxLines
        mBinding.commentHead.tvText.setMaxLines(Integer.MAX_VALUE);
        mBinding.commentHead.tvText.setMovementMethod(LinkMovementMethod.getInstance());

        return mBinding.getRoot();
    }

    @Override
    public void refresh() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_COMMENT_HEAD, null, this);
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_COMMENT_HEAD:
                return new ItemListLoader(getActivity(), Collections.singletonList(mItem.getId()));
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader,
                               HackerNewsResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_COMMENT_HEAD:
                    mItem = response.getData().get(0);
                    mBinding.setItem(mItem);
                    break;
                default:
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

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
