package test.demo.gyniu.v2ex;

/**
 * Created by gyniu on 17-3-14.
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {
    private static final String TAG = "TopicListAdapter";
    private static final boolean DEBUG = LogUtil.LOGD;

    private final TopicListView.OnTopicActionListener mListener;

    private List<Topic> mData;

    public TopicListAdapter(@NonNull TopicListView.OnTopicActionListener listener) {
        mListener = listener;
        setHasStableIds(true);
    }

    public void setDataSource(List<Topic> data) {
        mData = data;
        if (mData == null) return;
        if (DEBUG) LogUtil.e(TAG, "set new DataSource, so notify DataSet Changed");
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new TopicListView(parent.getContext()), mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Topic topic = mData.get(position);
        holder.fillData(topic);
    }

    @Override
    public long getItemId(int position) {
        return mData == null ? RecyclerView.NO_ID : mData.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TopicListView mView;

        public ViewHolder(TopicListView view, TopicListView.OnTopicActionListener listener) {
            super(view);
            mView = view;
            mView.setListener(listener);
        }

        public void fillData(Topic topic) {
            mView.buildItem(topic);
        }
    }
}

