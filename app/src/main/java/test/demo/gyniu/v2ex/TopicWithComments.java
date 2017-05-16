package test.demo.gyniu.v2ex;

/**
 * Created by uiprj on 17-5-15.
 */
import android.support.annotation.NonNull;

import java.util.List;

import test.demo.gyniu.v2ex.model.Comment;
import test.demo.gyniu.v2ex.model.Topic;

public class TopicWithComments {
    public final Topic mTopic;
    public final List<Comment> mComments;
    public final int mCurPage;
    public final int mMaxPage;
    public final String mCsrfToken;
    public final String mOnceToken;
    public int mLastReadPos;

    public TopicWithComments(@NonNull Topic topic, @NonNull List<Comment> comments,
                             int curPage, int maxPage,
                             String csrfToken, String onceToken) {
        mTopic = topic;
        mComments = comments;
        mCurPage = curPage;
        mMaxPage = maxPage;
        mCsrfToken = csrfToken;
        mOnceToken = onceToken;
    }

    @Override
    public String toString() {
        return "TopicWithComments{" +
                "mTopic=" + mTopic +
                ", mComments=" + mComments +
                ", mCurPage=" + mCurPage +
                ", mMaxPage=" + mMaxPage +
                ", mCsrfToken='" + mCsrfToken + '\'' +
                ", mOnceToken='" + mOnceToken + '\'' +
                ", mLastReadPos=" + mLastReadPos +
                '}';
    }
}