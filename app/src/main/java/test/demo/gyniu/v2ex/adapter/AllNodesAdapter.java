package test.demo.gyniu.v2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import test.demo.gyniu.v2ex.Activity.NodeListActivity;
import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.PinyinAlpha;
import test.demo.gyniu.v2ex.utils.PinyinComparator;

/**
 * Created by uiprj on 17-7-5.
 */
public class AllNodesAdapter extends RecyclerView.Adapter<AllNodesAdapter.ViewHolder>
        implements SectionIndexer {
    private static final String TAG = "AllNodesAdapter";

    final Context mContext;
    List<Node> mNodes = new ArrayList<Node>();
    List<Node> mAllNodes = new ArrayList<Node>();
    HashMap<String, Integer> mAlphaPosition = new HashMap<String, Integer>();
    String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public AllNodesAdapter(Context context) {
        mContext = context;
    }

    public HashMap<String, Integer> getAlphaPosition() {
        return mAlphaPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_node, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Node node = mNodes.get(position);
        viewHolder.title.setText(node.getTitle());
        if (node.getHeader() != null) {
            viewHolder.summary.setVisibility(View.VISIBLE);
            viewHolder.summary.setText(Html.fromHtml(node.getHeader()));
        } else {
            viewHolder.summary.setVisibility(View.GONE);
        }
        viewHolder.topics.setText(mContext.getString(R.string.str_nodes_topics_count,
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
        //for debug
        long startTime = 0L;
        if (BuildConfig.DEBUG) startTime = System.nanoTime();
        //////////////////////
        TreeMap<String, List<Node>> lists = new TreeMap<String, List<Node>>();
        for (int i = 0; i < data.size(); i++) {
            Node node = data.get(i);
            String alpha = PinyinAlpha.getFirstChar(node.getTitle());
            if (!lists.containsKey(alpha)) {
                List<Node> list = new ArrayList<Node>();
                list.add(node);
                lists.put(alpha, list);
            } else {
                lists.get(alpha).add(node);
            }
        }

        PinyinComparator comparator = new PinyinComparator();
        mNodes.clear();
        Iterator iter = lists.entrySet().iterator();
        int offset = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            List<Node> val = (List<Node>) entry.getValue();
            Collections.sort(val, comparator);
            mNodes.addAll(val);
            mAlphaPosition.put(key, offset);
            offset += val.size();
        }

        mAllNodes = mNodes;
        //for debug
        if (BuildConfig.DEBUG) {
            long consumingTime = System.nanoTime() - startTime;
            LogUtil.d(TAG, "!!!consuming time : " + consumingTime/1000 + "μs");
        }
        //////////////////////////
        notifyDataSetChanged();
    }

    @Override
    public int getPositionForSection(int i) {
        return mAlphaPosition.get(mSections.substring(i, i + 1));
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] chars = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++) {
            chars[i] = String.valueOf(mSections.charAt(i));
        }
        return chars;
    }

    public void filterText(CharSequence query) {
        if (TextUtils.isEmpty(query)) {
            mNodes = mAllNodes;
            notifyDataSetChanged();
            return;
        }

        List<Node> result = new ArrayList<Node>();
        for (Node node : mAllNodes) {
            if (node.getName().contains(query) ||
                    node.getTitle().contains(query) ||
                    (node.getTitleAlternative() != null
                            && node.getTitleAlternative().contains(query)))
                result.add(node);
        }

        mNodes = result;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public TextView summary;
        public TextView topics;
        public CardView card;

        public ViewHolder(View view) {
            super(view);
            card = (CardView) view.findViewById(R.id.card_container);
            title = (TextView) view.findViewById(R.id.node_title);
            summary = (TextView) view.findViewById(R.id.node_summary);
            topics = (TextView) view.findViewById(R.id.node_topics);

            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "node click position: " + position);
            Node node = mNodes.get(position);
            Intent intent = new Intent(mContext, NodeListActivity.class);
            intent.putExtra("node", (Parcelable) node);
            mContext.startActivity(intent);
        }
    }
}

