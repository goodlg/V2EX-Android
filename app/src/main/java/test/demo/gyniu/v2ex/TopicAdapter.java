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

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    private List<String> mData;

    public TopicAdapter(List<String> data) {
        this.mData = data;
        setHasStableIds(true);
    }

    public void setDataSource(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String str = mData.get(position);
        holder.fillData(str);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mView;

        public ViewHolder(View view) {
            super(view);
            mView = (TextView) view.findViewById(R.id.tv_title);
        }

        public void fillData(String str) {
            mView.setText(str);
        }
    }
}

