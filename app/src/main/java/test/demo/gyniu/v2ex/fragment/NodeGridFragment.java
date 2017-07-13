package test.demo.gyniu.v2ex.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.adapter.NodeGridAdapter;
import test.demo.gyniu.v2ex.common.ExceptionHelper;
import test.demo.gyniu.v2ex.loader.AsyncTaskLoader;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.network.HttpRequestHelper;

/**
 * Created by uiprj on 17-7-13.
 */
public class NodeGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncTaskLoader.LoaderResult<List<Node>>>,
        SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeLayout;
    NodeGridAdapter mAdapter;
    RecyclerView mRecycView;
    RecyclerView.LayoutManager mLayoutManager;

    private final int LOADER_ID = 4;

    public static NodeGridFragment newInstance() {
        return new NodeGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_node_grid, null);

        final Context context = getActivity();

        mAdapter = new NodeGridAdapter(context);
        mRecycView = (RecyclerView) root.findViewById(R.id.node_grid);
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecycView.setLayoutManager(mLayoutManager);
        mRecycView.setAdapter(mAdapter);

        mSwipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_layout);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setRefreshing(true);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<AsyncTaskLoader.LoaderResult<List<Node>>> onCreateLoader(int id, Bundle args) {
        return new NodeLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<AsyncTaskLoader.LoaderResult<List<Node>>> loader, AsyncTaskLoader.LoaderResult<List<Node>> data) {
        mSwipeLayout.setRefreshing(false);
        if (data.hasException()) {
            if (ExceptionHelper.handleExceptionNoCatch(this, data.mException)) {
                getActivity().finish();
            }
            return;
        }

        mAdapter.setDataSource(data.mResult);
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskLoader.LoaderResult<List<Node>>> loader) {
        mAdapter.setDataSource(null);
    }

    @Override
    public void onRefresh() {
        final Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
        if (loader == null) {
            return;
        }
        loader.forceLoad();
    }

    private static class NodeLoader extends AsyncTaskLoader<List<Node>> {
        public NodeLoader(Context context) {
            super(context);
        }

        @Override
        public List<Node> loadInBackgroundWithException() throws Exception {
            return HttpRequestHelper.getInstance().getFavNodes();
        }
    }
}
