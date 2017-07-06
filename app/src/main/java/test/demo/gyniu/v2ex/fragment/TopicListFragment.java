package test.demo.gyniu.v2ex.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.demo.gyniu.v2ex.Activity.TopicActivity;
import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.loader.AsyncTaskLoader;
import test.demo.gyniu.v2ex.loader.TopicLoader;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.model.Tab;
import test.demo.gyniu.v2ex.utils.MultiList;
import test.demo.gyniu.v2ex.widget.CustomDecoration;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.loader.TopicListLoader;
import test.demo.gyniu.v2ex.widget.TopicListView;
import test.demo.gyniu.v2ex.adapter.TopicListAdapter;
import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.Constant.TopicListType;

/**
 * Created by uiprj on 17-3-14.
 * list of topics
 */
public class TopicListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<AsyncTaskLoader.LoaderResult<TopicListLoader.TopicList>>,
        TopicListView.OnTopicActionListener,
        RecyclerView.OnChildAttachStateChangeListener{
    private static final String TAG = "TopicListFragment";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final String ENTITY = "entity";
    private Entity mEntry;

    private  SwipeRefreshLayout mLayout;
    private RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;

    private final int LOADER_ID = 0;

    private MultiList<Topic> mTopics;
    private TopicListType listType;
    private boolean mIsLoading;
    private boolean mLastIsFailed;
    private boolean mIsLoaded;
    private int mCurPage;
    private int mMaxPage;

    public TopicListFragment(){}

    public static Fragment newInstance(Entity entity){
        Fragment fragment = new TopicListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ENTITY, entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            mEntry = args.getParcelable(ENTITY);
            if (BuildConfig.DEBUG) LogUtil.d(TAG, "mEntry=" + mEntry);
        }

        if (mEntry == null){
            throw new RuntimeException("entity can't be null");
        }

        if (mEntry instanceof Tab) {
            listType = TopicListType.tab;
        } else if (mEntry instanceof Node) {
            listType = TopicListType.node;
        } else {
            listType = TopicListType.none;
        }
        mTopics = new MultiList<>();
        mMaxPage = 1;
        mCurPage = 1;
        mIsLoaded = false;
        //setRetainInstance(true);
    }

    private void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
        mLayout.setRefreshing(isLoading);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setIsLoading(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        LoaderManager lm = getLoaderManager();
        if (lm.getLoader(LOADER_ID) != null){
            if (DEBUG) LogUtil.d(TAG, "already loaded");
            return;
        }

        //init loader
        lm.initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_topic_list, container, false);
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.recycle_view);

        mLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mLayout.getContext()));
        mRecyclerView.addItemDecoration(new CustomDecoration(getContext(), CustomDecoration.VERTICAL_LIST, R.drawable.divider));

        mAdapter = new TopicListAdapter(this);
        mAdapter.setDataSource(mTopics);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnChildAttachStateChangeListener(this);

        return mLayout;
    }

    @Override
    public void onRefresh() {
        if (DEBUG) LogUtil.d(TAG, "Refresh!!!");
        final Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
        if (loader == null){
            return ;
        }

        if (DEBUG) LogUtil.d(TAG, "force Load!!!");
        loader.forceLoad();
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public Loader<AsyncTaskLoader.LoaderResult<TopicListLoader.TopicList>> onCreateLoader(int id, Bundle args) {
        return new TopicListLoader(getActivity(), mEntry);
    }

    @Override
    public void onLoadFinished(Loader<AsyncTaskLoader.LoaderResult<TopicListLoader.TopicList>> loader, AsyncTaskLoader.LoaderResult<TopicListLoader.TopicList> data) {
        if (data.hasException()) {
            LogUtil.e(TAG, "Exception: " + data.mException);
            return;
        }
        mIsLoaded = true;
        mLastIsFailed = false;
        mCurPage = data.mResult.getCurPage();
        mMaxPage = data.mResult.getMaxPage();
        LogUtil.d(TAG, "!!!mCurPage: " + mCurPage + ", mMaxPage=" + mMaxPage);
        final int oldSize = mTopics.listSize();
        if (mCurPage > oldSize) {
            // new page
            mTopics.addList(data.mResult.getList());
        } else {
            mTopics.setList(mCurPage - 1, data.mResult.getList());
        }
        mAdapter.notifyDataSetChanged();
        setIsLoading(false);
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskLoader.LoaderResult<TopicListLoader.TopicList>> loader) {
        if (DEBUG) LogUtil.d(TAG, "load reset");
        mAdapter.setDataSource(null);
    }

    @Override
    public void onTopicOpen(View view, Topic topic) {
        if (DEBUG) LogUtil.e(TAG, "view topic");
        final Intent intent = new Intent(getContext(), TopicActivity.class);
        intent.putExtra(TopicActivity.KEY_TOPIC, topic);
        startActivity(intent);
    }

    @Override
    public void onTopicStartPreview(View view, Topic topic) {

    }

    @Override
    public void onTopicStopPreview(View view, Topic topic) {

    }

    private TopicListLoader getLoader() {
        return (TopicListLoader) getLoaderManager().<AsyncTaskLoader.LoaderResult<TopicListLoader.TopicList>>getLoader(LOADER_ID);
    }

    private void loadNextPageIfNeed(int totalItemCount, int lastVisibleItem) {
        if (BuildConfig.DEBUG) LogUtil.w(TAG, "totalItemCount:" + totalItemCount
                + ", lastVisibleItem=" + lastVisibleItem
                + ", mIsLoading=" + mIsLoading
                + ", mLastIsFailed=" + mLastIsFailed
                + ", mCurPage=" + mCurPage
                + ", mMaxPage=" + mMaxPage);
        //do not load next list by tab
        if (listType == TopicListType.tab) {
            LogUtil.d(TAG, "do not load next list by tab.");
            return;
        }

        if (mIsLoading || mLastIsFailed || (mCurPage >= mMaxPage)) {
            return;
        }

        if ((totalItemCount - lastVisibleItem) > 20) {
            return;
        }

        final TopicListLoader loader = getLoader();
        setIsLoading(true);
        loader.setPage(mCurPage + 1);

        if (BuildConfig.DEBUG)
            LogUtil.d(TAG, "start loading if need.");
        loader.startLoading();
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        loadNextPageIfNeed(mAdapter.getItemCount(), mRecyclerView.getChildAdapterPosition(view));
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {

    }
}
