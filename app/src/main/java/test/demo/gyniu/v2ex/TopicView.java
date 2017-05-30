package test.demo.gyniu.v2ex;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.common.base.Strings;

import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.ViewUtils;
import test.demo.gyniu.v2ex.widget.AvatarView;

/**
 * Created by uiprj on 17-5-15.
 */
public class TopicView extends FrameLayout {
    private static final String TAG = "TopicItemView";
    private static final boolean DEBUG = LogUtil.LOGD;

    private static final int TOPIC_PICTURE_OTHER_WIDTH =
            ViewUtils.getDimensionPixelSize(R.dimen.topic_picture_other_width);

    private final View mRootView;
    private final AvatarView mUserAvatar;
    private final TextView mTopicTitle;
    private final TextView mTopicNode;
    private final TextView mUserName;
    private final TextView mPostTime;
    private final TextView mClickRate;
    private final TextView mContent;
    private final View mLine;

    private Topic mTopic;
    private Context mContext;

    public TopicView(Context context) {
        this(context, null);
    }

    public TopicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mRootView = inflate(context, R.layout.topic_view, this);
        mUserAvatar = (AvatarView) findViewById(R.id.user_avatar);
        mTopicTitle = (TextView) mRootView.findViewById(R.id.topic_title);
        mTopicNode = (TextView) mRootView.findViewById(R.id.topic_node);
        mUserName = (TextView) mRootView.findViewById(R.id.username);
        mPostTime = (TextView) mRootView.findViewById(R.id.post_time);
        mClickRate = (TextView) mRootView.findViewById(R.id.click_rate);
        mContent = (TextView) mRootView.findViewById(R.id.content);
        mLine = mRootView.findViewById(R.id.line);
    }

    public void buildItem(Topic topic) {
        if (DEBUG) LogUtil.w(TAG, "topic:" + topic);
        mTopic = topic;
        mTopicTitle.setText(topic.getTitle());
        mTopicNode.setText(topic.getNode().getTitle());
        mUserName.setText(topic.getMember().getUserName());
        mPostTime.setText(topic.getTime());
        final int clickRate = topic.getClickRate();
        mClickRate.setText(String.format("%d", clickRate) + " "
                + mContext.getResources().getString(R.string.str_click_rate));
        mUserAvatar.setAvatar(topic.getMember().getAvatar());
        setContent(topic);
    }

    private void setContent(Topic topic) {
        final String content = topic.getContent();
        if (Strings.isNullOrEmpty(content)) {
            mContent.setVisibility(GONE);
            mLine.setVisibility(GONE);
            return;
        }
        mContent.setVisibility(VISIBLE);
        mLine.setVisibility(VISIBLE);
        ViewUtils.setHtmlIntoTextView(mContent, content, ViewUtils.getWidthPixels() -
                TOPIC_PICTURE_OTHER_WIDTH, true);
    }
}
