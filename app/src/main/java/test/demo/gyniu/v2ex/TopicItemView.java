package test.demo.gyniu.v2ex;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-3-22.
 */
public class TopicItemView extends FrameLayout {
    private static final String TAG = "TopicItemView";
    private static final boolean DEBUG = LogUtil.LOGD;

    private View mRootView;
    private ImageView mUserAvatar;
    private TextView mTopicTitle;
    private TextView mTopicNode;
    private TextView mUserName;
    private TextView mReplyTime;

    private TextView mReplyCount;

    private Topic mTopic;

    public TopicItemView(Context context) {
        this(context, null);
        init(context);
    }

    public TopicItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public TopicItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mRootView = View.inflate(context, R.layout.topic_item, this);
        mUserAvatar = (ImageView) mRootView.findViewById(R.id.user_avatar);
        mTopicTitle = (TextView) mRootView.findViewById(R.id.topic_title);
        mTopicNode = (TextView) mRootView.findViewById(R.id.topic_node);
        mUserName = (TextView) mRootView.findViewById(R.id.username);
        mReplyTime = (TextView) mRootView.findViewById(R.id.reply_time);
        mReplyCount = (TextView) mRootView.findViewById(R.id.reply_count);
    }

    public void buildItem(Topic topic){
        if (DEBUG) LogUtil.e(TAG, "topic:" + topic);
        mTopic = topic;
        mTopicTitle.setText(topic.getTitle());
        mTopicNode.setText("teach");
        mUserName.setText(topic.getMember().getUserName());
        mReplyTime.setText(topic.getReplyTime());
        mReplyCount.setText(topic.getReplies() + "");
    }
}
