package test.demo.gyniu.v2ex.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Preconditions;

import test.demo.gyniu.v2ex.Activity.TopicActivity;
import test.demo.gyniu.v2ex.loader.AsyncTaskLoader;
import test.demo.gyniu.v2ex.widget.CustomDecoration;
import test.demo.gyniu.v2ex.utils.MultiList;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.loader.TopicLoader;
import test.demo.gyniu.v2ex.model.TopicWithComments;
import test.demo.gyniu.v2ex.adapter.TopicViewAdapter;
import test.demo.gyniu.v2ex.common.ExceptionHelper;
import test.demo.gyniu.v2ex.common.RequestException;
import test.demo.gyniu.v2ex.model.Comment;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.network.HttpStatus;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.ViewUtils;

/**
 * Created by uiprj on 17-5-12.
 */
public class TopicViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
    LoaderManager.LoaderCallbacks<AsyncTaskLoader.LoaderResult<TopicWithComments>> {
    private static final String TAG = "TopicViewFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    private static final String ARG_TOPIC = "topic";
    private Topic mTopic;
    private MultiList<Comment> mComments;
    private boolean mIsLoaded;
    private int mCurPage;
    private int mMaxPage;

    private AppBarLayout mAppBarLayout;
    private SwipeRefreshLayout mLayout;
    private RecyclerView mTopicAndCommentsView;
    private LinearLayoutManager mLayoutManager;
    private TopicViewAdapter mTopicViewAdapter;

    private boolean mIsLoading;
    private boolean mLastIsFailed;

    private final int LOADER_ID = 1;

    public TopicViewFragment(){}

    public static TopicViewFragment newInstance(Topic topic) {
        if (DEBUG) LogUtil.w(TAG, "new a instance , topic: " + topic);
        TopicViewFragment fragment = new TopicViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TOPIC, topic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTopic = getArguments().getParcelable(ARG_TOPIC);
        }
        mComments = new MultiList<>();
        mMaxPage = 1;
        mCurPage = 1;
        mIsLoaded = false;
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_topic_view, container, false);

        mLayout = ((SwipeRefreshLayout) rootView.findViewById(R.id.layout));
        mLayout.setOnRefreshListener(this);

        mTopicAndCommentsView = (RecyclerView) mLayout.findViewById(R.id.topic_and_comments);

        if (!mTopic.hasInfo()) {
            mTopicAndCommentsView.setVisibility(View.INVISIBLE);
        }

        if (DEBUG) LogUtil.w(TAG, "create view fragment");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TopicActivity activity = (TopicActivity) getActivity();
        activity.setTitle(null);

        final ActionBar actionBar = activity.getSupportActionBar();
        Preconditions.checkNotNull(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAppBarLayout = activity.getAppBarLayout();

        initLayoutView(activity);
        setIsLoading(true);

        if (DEBUG) LogUtil.w(TAG, "attached activity !!!");

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void initLayoutView(TopicActivity activity){
        if (DEBUG) LogUtil.w(TAG, "init layout view start");
        mLayoutManager = new LinearLayoutManager(activity);
        mTopicAndCommentsView.setLayoutManager(mLayoutManager);
        int offsets = ViewUtils.getDimensionPixelSize(R.dimen.decoration_offsets);
        mTopicAndCommentsView.addItemDecoration(new CustomDecoration(activity, CustomDecoration.VERTICAL_LIST, R.drawable.divider, offsets));
        mTopicViewAdapter = new TopicViewAdapter();
        mTopicViewAdapter.setTopic(mTopic);
        mTopicViewAdapter.setDataSource(mComments);
        mTopicAndCommentsView.setAdapter(mTopicViewAdapter);
        mTopicAndCommentsView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (DEBUG) LogUtil.w(TAG, "load Next Page If Need");
                loadNextPageIfNeed(mTopicViewAdapter.getItemCount(), mTopicAndCommentsView.getChildAdapterPosition(view));
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {}
        });
        if (DEBUG) LogUtil.w(TAG, "init layout view done.");
    }

    private void loadNextPageIfNeed(int totalItemCount, int lastVisibleItem) {
        if (DEBUG) LogUtil.w(TAG, "totalItemCount:" + totalItemCount
                + ", lastVisibleItem=" + lastVisibleItem
                + ", mIsLoading=" + mIsLoading
                + ", mLastIsFailed=" + mLastIsFailed
                + ", mCurPage=" + mCurPage
                + ", mMaxPage=" + mMaxPage);

        if (mIsLoading || mLastIsFailed || (mCurPage >= mMaxPage)) {
            return;
        }
        if ((totalItemCount - lastVisibleItem) > 20) {
            return;
        }

        final TopicLoader loader = getLoader();

        setIsLoading(true);
        loader.setPage(mCurPage + 1);

        if (DEBUG) LogUtil.w(TAG, "start loading if need.");
        loader.startLoading();
    }

    private void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
        mLayout.setRefreshing(isLoading);
    }

    @Override
    public void onRefresh() {
        final TopicLoader loader = getLoader();
        if (loader == null) {
            return;
        }
        loader.forceLoad();
    }

    private TopicLoader getLoader() {
        return (TopicLoader) getLoaderManager().<AsyncTaskLoader.LoaderResult<TopicWithComments>>getLoader(LOADER_ID);
    }

    @Override
    public Loader<AsyncTaskLoader.LoaderResult<TopicWithComments>> onCreateLoader(int id, Bundle args) {
        if (DEBUG) LogUtil.w(TAG, "create topic loader");
        return new TopicLoader(getActivity(), mTopic);
    }

    @Override
    public void onLoadFinished(Loader<AsyncTaskLoader.LoaderResult<TopicWithComments>> loader, AsyncTaskLoader.LoaderResult<TopicWithComments> result) {
        if (DEBUG) LogUtil.w(TAG, "load topic done");
        if (result.hasException()) {
            LogUtil.e(TAG, "HAS Exception: " + result.getException());
            handleLoadException(result);
            return;
        }

        final TopicWithComments data = result.mResult;
        if (!mIsLoaded) {
            if (!mTopic.hasInfo()) {
                mTopicAndCommentsView.setVisibility(View.VISIBLE);
            }
        }

        mIsLoaded = true;
        mLastIsFailed = false;
        mTopicViewAdapter.setTopic(data.mTopic);
        mTopic = data.mTopic;
        mCurPage = data.mCurPage;
        mMaxPage = data.mMaxPage;
        LogUtil.d(TAG, "!!!mCurPage: " + mCurPage + ", mMaxPage=" + mMaxPage);
        final int oldSize = mComments.listSize();
        if (mCurPage > oldSize) {
            // new page
            mComments.addList(data.mComments);
        } else {
            mComments.setList(mCurPage - 1, data.mComments);
        }

        mTopicViewAdapter.notifyDataSetChanged();

        setIsLoading(false);
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskLoader.LoaderResult<TopicWithComments>> loader) {
        if (DEBUG) LogUtil.w(TAG, "reset topic loader");
        mTopicViewAdapter.setDataSource(null);
        mComments.clear();
    }

    private void handleLoadException(AsyncTaskLoader.LoaderResult<TopicWithComments> result) {
        boolean finishActivity = false;
        try {
            ExceptionHelper.handleExceptionNoCatch(this, result.mException);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof RequestException) {
                final RequestException ex = (RequestException) e.getCause();
                int strId;
                switch (ex.getCode()) {
                    case HttpStatus.SC_NOT_FOUND:
                        strId = R.string.toast_topic_not_found;
                        break;
                    default:
                        throw e;
                }

                if (getUserVisibleHint()) {
                    Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT).show();
                }
                finishActivity = true;
            } else {
                throw e;
            }
        }

        if (DEBUG) LogUtil.w(TAG, "need finish activity ? " + finishActivity);

        if (finishActivity) {
            if (DEBUG) LogUtil.w(TAG, "finish this activity");
            getActivity().finish();
        }
    }
}
