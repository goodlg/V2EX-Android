package test.demo.gyniu.v2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.demo.gyniu.v2ex.Activity.TopicListActivity;
import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.widget.AvatarView;

/**
 * Created by uiprj on 17-7-13.
 */
public class NodeGridAdapter extends RecyclerView.Adapter<NodeGridAdapter.ViewHolder> {
    private static final String TAG = "NodeGridAdapter";
    private final Context mContext;
    List<Node> mNodes = new ArrayList<Node>();

    public NodeGridAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Node node = mNodes.get(position);
        holder.img.setAvatar(node.getAvatar());
        holder.title.setText(node.getTitle());
        holder.topics.setText(mContext.getString(R.string.str_nodes_topics_count,
                node.getTopics()));
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }

    @Override
    public long getItemId(int position) {
        return mNodes.get(position).getId();
    }

    public void setDataSource(List<Node> data) {
        mNodes = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public AvatarView img;
        public TextView title;
        public TextView topics;

        public ViewHolder(View view) {
            super(view);
            img = (AvatarView) view.findViewById(R.id.node_img);
            title = (TextView) view.findViewById(R.id.node_title);
            topics = (TextView) view.findViewById(R.id.node_topics);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "node grid click position: " + position);
            Node node = mNodes.get(position);
            Intent intent = new Intent(mContext, TopicListActivity.class);
            intent.putExtra("node", (Parcelable) node);
            mContext.startActivity(intent);
        }
    }
}
