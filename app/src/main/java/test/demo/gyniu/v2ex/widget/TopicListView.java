package test.demo.gyniu.v2ex.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.ViewUtils;
import test.demo.gyniu.v2ex.widget.AvatarView;

/**
 * Created by uiprj on 17-3-22.
 */
public class TopicListView extends FrameLayout implements View.OnClickListener{
    private static final String TAG = "TopicItemView";
    private static final boolean DEBUG = LogUtil.LOGD;

    private static final int TOPIC_TITLE_PICTURE_OTHER_WIDTH =
            ViewUtils.getDimensionPixelSize(R.dimen.topic_title_picture_other_width);

    private final View mRootView;
    private final AvatarView mUserAvatar;
    private final TextView mTopicTitle;
    private final TextView mTopicNode;
    private final TextView mUserName;
    private final TextView mReplyTime;
    private final TextView mReplyCount;

    private Topic mTopic;

    private OnTopicActionListener mListener;

    public TopicListView(Context context) {
        this(context, null);
    }

    public TopicListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopicListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = View.inflate(context, R.layout.topic_list_item, this);
        mUserAvatar = (AvatarView) mRootView.findViewById(R.id.user_avatar);
        mTopicTitle = (TextView) mRootView.findViewById(R.id.topic_title);
        mTopicNode = (TextView) mRootView.findViewById(R.id.topic_node);
        mUserName = (TextView) mRootView.findViewById(R.id.username);
        mReplyTime = (TextView) mRootView.findViewById(R.id.reply_time);
        mReplyCount = (TextView) mRootView.findViewById(R.id.reply_count);
    }

    public void setListener(@NonNull OnTopicActionListener listener) {
        mListener = listener;
        setOnClickListener(this);
    }

    public void buildItem(Topic topic) {
        if (DEBUG) LogUtil.d(TAG, "topic:" + topic);
        mTopic = topic;

        /**
         * it throw exception:
         * android.os.NetworkOnMainThreadException
         */
//        String title = topic.getTitle();
//        Spanned sp = Html.fromHtml(title, new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {
//                InputStream is = null;
//                try {
//                    is = (InputStream) new URL(source).getContent();
//                    Drawable d = Drawable.createFromStream(is, "src");
//                    d.setBounds(0, 0, d.getIntrinsicWidth(),
//                            d.getIntrinsicHeight());
//                    if (DEBUG) LogUtil.d(TAG, ".......................topic img w:"
//                            + d.getIntrinsicWidth() + ", h:" +  d.getIntrinsicHeight());
//                    is.close();
//                    return d;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LogUtil.e(TAG, "Exception: " + e);
//                    return null;
//                }
//            }
//        }, null);
//        mTopicTitle.setText(sp);

        ViewUtils.setHtmlIntoTextView(mTopicTitle, topic.getTitle(), ViewUtils.getWidthPixels() -
                TOPIC_TITLE_PICTURE_OTHER_WIDTH, false);
        mTopicNode.setText(topic.getNode().getTitle());
        mUserName.setText(topic.getMember().getUserName());
        mReplyTime.setText(topic.getTime());
        final int replyCount = topic.getReplyCount();
        if (replyCount > 0) {
            mReplyCount.setVisibility(View.VISIBLE);
            mReplyCount.setText(String.format("%d", replyCount));
        } else {
            mReplyCount.setVisibility(View.INVISIBLE);
        }

        mUserAvatar.setAvatar(topic.getMember().getAvatar());
    }

    @Override
    public void onClick(View v) {
        mListener.onTopicOpen(v, mTopic);
    }

    public interface OnTopicActionListener {
        void onTopicOpen(View view, Topic topic);
        void onTopicStartPreview(View view, Topic topic);
        void onTopicStopPreview(View view, Topic topic);
    }
}
