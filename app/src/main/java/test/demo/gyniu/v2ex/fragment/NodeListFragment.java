package test.demo.gyniu.v2ex.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.demo.gyniu.v2ex.Activity.MainActivity;
import test.demo.gyniu.v2ex.utils.Constant.orientation;
import test.demo.gyniu.v2ex.loader.NodeListLoader;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.adapter.AllNodesAdapter;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.widget.NodesIndexRecyclerView;
import test.demo.gyniu.v2ex.loader.AsyncTaskLoader.LoaderResult;

/**
 * Created by uiprj on 17-3-17.
 */
public class NodeListFragment extends Fragment implements LoaderManager.LoaderCallbacks<LoaderResult<List<Node>>>,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NodeListFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    NodesIndexRecyclerView mIndexRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeLayout;
    AllNodesAdapter mNodeAdapter;

    private final int LOADER_ID = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node, null);
        final Context context = getActivity();
        mNodeAdapter = new AllNodesAdapter(context);
        mIndexRecyclerView = (NodesIndexRecyclerView) view.findViewById(R.id.all_nodes);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mIndexRecyclerView.setLayoutManager(mLayoutManager);
        mIndexRecyclerView.setAdapter(mNodeAdapter);
        mIndexRecyclerView.setFastScrollEnabled(true);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
//        mSwipeLayout.setColorScheme(ContextCompat.getColor(context, android.R.color.holo_blue_bright),
//                ContextCompat.getColor(context, android.R.color.holo_green_light),
//                ContextCompat.getColor(context, android.R.color.holo_orange_light),
//                ContextCompat.getColor(context, android.R.color.holo_red_light));
        mSwipeLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity())
                .alignTitle(orientation.left, getString(R.string.str_node));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_nodes, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_nodes_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_nodes_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mNodeAdapter.filterText(newText);
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        LoaderManager lm = getLoaderManager();
        if (lm.getLoader(LOADER_ID) != null){
            if (DEBUG) LogUtil.d(TAG, "nodes loader != null");
            return;
        }

        //init nodes loader
        lm.initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onRefresh() {
        if (DEBUG) LogUtil.d(TAG, "refresh node!!!");
        final Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
        if (loader == null){
            return ;
        }

        if (DEBUG) LogUtil.d(TAG, "force nodes Load!!!");
        loader.forceLoad();
        mIndexRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public Loader<LoaderResult<List<Node>>> onCreateLoader(int id, Bundle args) {
        return new NodeListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult<List<Node>>> loader, LoaderResult<List<Node>> data) {
        mSwipeLayout.setRefreshing(false);
        if (data.hasException()) {
            throw new RuntimeException(data.mException);
        }
        mNodeAdapter.setDataSource(data.mResult);
    }

    @Override
    public void onLoaderReset(Loader<LoaderResult<List<Node>>> loader) {

    }
}
