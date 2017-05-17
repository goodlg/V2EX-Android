package test.demo.gyniu.v2ex;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import test.demo.gyniu.v2ex.model.Comment;
import test.demo.gyniu.v2ex.model.Postscript;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.ViewUtils;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by uiprj on 17-5-15.
 */
public class TopicViewAdapter extends RecyclerView.Adapter<ViewHolder>{
    private static final String TAG = "TopicViewAdapter";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final int TYPE_TOPIC = 0;
    private static final int TYPE_COMMENT = 1;

    private Topic mTopic;
    private List<Comment> mCommentList;

    public TopicViewAdapter() {
        setHasStableIds(true);
    }

    public void setTopic(Topic topic) {
        if (mTopic != null && mTopic.hasInfo()) {
            notifyItemChanged(0);
        } else {
            notifyItemInserted(0);
        }
        mTopic = topic;
    }

    public void setDataSource(List<Comment> comments) {
        mCommentList = comments;
        if (mCommentList == null) return;
        if(DEBUG) LogUtil.w(TAG, "data has changed need notify adapter");
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        Context context = parent.getContext();
        switch (viewType) {
            case TYPE_TOPIC:
                if(DEBUG) LogUtil.w(TAG, "make TopicViewHolder");
                viewHolder = TopicViewHolder.makeHolder(parent);
                break;
            case TYPE_COMMENT:
                if(DEBUG) LogUtil.w(TAG, "make CommentViewHolder");
                viewHolder = new CommentViewHolder(new CommentView(context));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            if(DEBUG) LogUtil.w(TAG, "TopicViewHolder fill Data");
            ((TopicViewHolder) holder).fillData(mTopic);
        } else {
            Comment comment = mCommentList.get(position);
            if(DEBUG) LogUtil.w(TAG, "CommentViewHolder fill Data");
            ((CommentViewHolder) holder).fillData(comment);
        }
    }

    @Override
    public long getItemId(int position) {
        if(DEBUG) LogUtil.w(TAG, "get item id by position : " + position);
        if (position == 0) {
            return mTopic.getId();
        }
        return mCommentList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        int commentNum = mCommentList == null ? 0 : mCommentList.size();
        int topicNum = mTopic.hasInfo() ? 1 : 0;

        if(DEBUG) LogUtil.w(TAG, "topicNum : " + topicNum + ", commentNum: " + commentNum
            + ", mCommentList=" + mCommentList);
        return topicNum + commentNum;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TOPIC : TYPE_COMMENT;
    }

    static class CommentViewHolder extends ViewHolder {

        public CommentViewHolder(CommentView view) {
            super(view);
        }

        public void fillData(Comment comment) {
            ((CommentView) itemView).fillData(comment, getAdapterPosition());
        }
    }

    static class TopicViewHolder extends ViewHolder {
        private static final int TOPIC_PICTURE_OTHER_WIDTH = ViewUtils.getDimensionPixelSize(R.dimen.topic_picture_other_width);

        private final LinearLayout mTopicLayout;
        private final TopicView mTopicView;

        private TopicViewHolder(LinearLayout layout, TopicView view) {
            super(layout);
            mTopicLayout = layout;
            mTopicView = view;
        }

        public static TopicViewHolder makeHolder(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.header_topic, parent, false);
            TopicView view = (TopicView) layout.findViewById(R.id.topic);
            return new TopicViewHolder(layout, view);
        }

        public void fillData(Topic data) {
            mTopicView.buildItem(data);
            fillPostscript(data.getPostscripts());
        }

        private void fillPostscript(List<Postscript> postscripts) {
            if (postscripts == null) {
                return;
            }

            final int childCount = mTopicLayout.getChildCount();
            if (childCount > 1) {
                mTopicLayout.removeViews(1, childCount - 1);
            }

            Context context = mTopicLayout.getContext();
            final LayoutInflater inflater = LayoutInflater.from(context);

            for (int i = 0, size = postscripts.size(); i < size; i++) {
                Postscript postscript = postscripts.get(i);
                final View view = inflater.inflate(R.layout.view_postscript, mTopicLayout, false);
                ((TextView) view.findViewById(R.id.title)).setText(context.getString(R.string.title_postscript, i + 1));
                ((TextView) view.findViewById(R.id.time)).setText(postscript.mTime);
                final TextView contentView = (TextView) view.findViewById(R.id.content);
                ViewUtils.setHtmlIntoTextView(contentView, postscript.mContent,
                        ViewUtils.getWidthPixels() - TOPIC_PICTURE_OTHER_WIDTH, true);
                mTopicLayout.addView(view);
            }
        }
    }
}
