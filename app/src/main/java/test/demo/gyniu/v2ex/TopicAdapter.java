package test.demo.gyniu.v2ex;

/**
 * Created by gyniu on 17-3-14.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    private static final String TAG = "TopicAdapter";
    private static final boolean DEBUG = LogUtil.LOGD;

    private List<Topic> mData;

    public TopicAdapter() {
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
        return new ViewHolder(new TopicItemView(parent.getContext()));
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
        private final TopicItemView mView;

        public ViewHolder(TopicItemView view) {
            super(view);
            mView = view;
        }

        public void fillData(Topic topic) {
            mView.buildItem(topic);
        }
    }
}

