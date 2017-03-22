package test.demo.gyniu.v2ex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-3-14.
 */
public class TopicListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "TopicListFragment";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final String TAB = "tab";
    private String mTitle;
    private Entity mEntry;

    private List<String> list = new ArrayList<>();

    private  SwipeRefreshLayout mLayout;
    private RecyclerView mRecyclerView;
    private TopicAdapter mAdapter;

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
            Entity entry = args.getParcelable(TAB);
            if (entry == null){
                throw new RuntimeException("tab can't be null");
            }
            mEntry = entry;
            if (DEBUG) LogUtil.d(TAG, "mEntry=" + mEntry);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_topic_list, container, false);
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.recycle_view);

        mLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mLayout.getContext()));

        initData();

        mAdapter = new TopicAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mLayout.setRefreshing(true);

        return mLayout;
    }

    private void initData() {
        if (list.size() != 0){
            list.clear();
        }
        for (int i = 0; i < 20; i++) {
            list.add("hello " + i);
        }
    }

    @Override
    public void onRefresh() {
        if (list.size() != 0){
            list.clear();
        }
        for (int i = 0; i < 30; i++) {
            list.add("world " + i);
        }
        mAdapter.setDataSource(list);

        mRecyclerView.smoothScrollToPosition(0);
    }
}
