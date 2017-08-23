package com.marcelljee.hackernews.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.marcelljee.hackernews.R;

public class LinearRecyclerView extends FrameLayout {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mEmptyView;
    private EndlessRecyclerViewScrollListener mScrollListener;

    public interface OnLoadMoreListener {
        void onLoadMore(int page, int totalItemsCount);
    }

    public LinearRecyclerView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LinearRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LinearRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mRecyclerView.getLayoutManager().onRestoreInstanceState(ss.layoutManagerState);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.layoutManagerState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        return ss;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.simple_recycler_view, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mEmptyView = view.findViewById(R.id.empty_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void showDivider() {
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mScrollListener = new EndlessRecyclerViewScrollListener(
                (LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                listener.onLoadMore(page, totalItemsCount);
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    public void restartOnLoadMoreListener() {
        mScrollListener.restart();
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void hideProgressBar() {
        if (mRecyclerView.getAdapter().getItemCount() > 0) {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(VISIBLE);
        }
    }

    private static class SavedState extends BaseSavedState {
        Parcelable layoutManagerState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            layoutManagerState = in.readParcelable(RecyclerView.LayoutManager.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(layoutManagerState, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private static final int VISIBLE_THRESHOLD = 10;
        // Sets the starting page index
        private static final int STARTING_PAGE_INDEX = 1;

        // The current offset index of data you have loaded
        private int currentPage;
        // The total number of items in the data start after the last load
        private int previousTotalItemCount;
        // True if we are still waiting for the last start of data to load.
        private boolean loading;

        private final LinearLayoutManager mLinearLayoutManager;

        EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.mLinearLayoutManager = layoutManager;
            restart();
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = view.getChildCount();
            int totalItemCount = mLinearLayoutManager.getItemCount();

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = STARTING_PAGE_INDEX;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // If it’s still loading, we check to see if the data start count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the VISIBLE_THRESHOLD and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        @SuppressWarnings("UnusedParameters")
        public abstract void onLoadMore(int page, int totalItemsCount);

        void restart() {
            currentPage = STARTING_PAGE_INDEX;
            previousTotalItemCount = 0;
            loading = true;
        }
    }
}
