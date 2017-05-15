package test.demo.gyniu.v2ex;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import test.demo.gyniu.v2ex.model.Comment;
import test.demo.gyniu.v2ex.utils.ViewUtils;
import test.demo.gyniu.v2ex.widget.AvatarView;

/**
 * Created by uiprj on 17-5-15.
 */
public class CommentView extends FrameLayout {
    private static final int COMMENT_PICTURE_OTHER_WIDTH =
            ViewUtils.getDimensionPixelSize(R.dimen.comment_picture_other_width);

    private final TextView mContent;
    private final AvatarView mAvatar;
    private final TextView mUsername;
    private final TextView mReplyTime;
    private final TextView mFloor;

    private Comment mComment;

    public CommentView(Context context) {
        this(context, null);
    }

    public CommentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.comment_view, this);

        mAvatar = (AvatarView) findViewById(R.id.avatar_img);
        mContent = (TextView) findViewById(R.id.content);
        mUsername = (TextView) findViewById(R.id.username_tv);
        mReplyTime = (TextView) findViewById(R.id.time_tv);
        mFloor = (TextView) findViewById(R.id.floor);
    }

    public void fillData(Comment comment, int position) {
        if (comment.equals(mComment)) {
            return;
        }
        mComment = comment;

        ViewUtils.setHtmlIntoTextView(mContent, comment.getContent(), ViewUtils.getWidthPixels() -
                COMMENT_PICTURE_OTHER_WIDTH, false);

        mUsername.setText(comment.getMember().getUserName());
        mReplyTime.setText(comment.getReplyTime());
        mFloor.setText(Integer.toString(comment.getFloor()));

        mAvatar.setAvatar(comment.getMember().getAvatar());
    }
}
