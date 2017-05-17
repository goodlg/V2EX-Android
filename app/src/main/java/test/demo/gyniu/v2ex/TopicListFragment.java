package test.demo.gyniu.v2ex;

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

import java.util.ArrayList;
import java.util.List;

import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.Tab;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-3-14.
 */
public class TopicListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<AsyncTaskLoader.LoaderResult<TopicListLoader.TopicList>>,
        TopicListView.OnTopicActionListener {
    private static final String TAG = "TopicListFragment";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final String TAB = "tab";
    private String mTitle;
    private Entity mEntry;

    private List<Topic> list = new ArrayList<>();

    private  SwipeRefreshLayout mLayout;
    private RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;

    private final int LOADER_ID = 0;

    public TopicListFragment(){}

    public static Fragment newInstance(Tab tab){
        Fragment fragment = new TopicListFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAB, tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null){
            mEntry = args.getParcelable(TAB);
        }

        if (DEBUG) LogUtil.d(TAG, "mEntry=" + mEntry);

        if (mEntry == null){
            throw new RuntimeException("tab can't be null");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        mRecyclerView.setAdapter(mAdapter);
        mLayout.setRefreshing(true);

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
        mLayout.setRefreshing(false);
        if (data.hasException()) {
            LogUtil.e(TAG, "HAS Exception: " + data.mException);
            return;
        }

        if (data != null && data.mResult != null){
            if (DEBUG)
            if (data.mResult != null && data.mResult.getList().size() == 0) {
                LogUtil.w(TAG, "Warning: load data size 0 !!!");
            } else {
                LogUtil.w(TAG, "OK, load done");
            }
            mAdapter.setDataSource(data.mResult.getList());
        }
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
}
