package test.demo.gyniu.v2ex.loader;

import android.content.Context;

import test.demo.gyniu.v2ex.model.TopicWithComments;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.network.HttpRequestHelper;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-15.
 */
public class TopicLoader extends AsyncTaskLoader<TopicWithComments> {
    private static final String TAG = "TopicLoader";
    private static final boolean DEBUG = LogUtil.LOGD;
    private final Topic mTopic;
    private int mPage;

    public TopicLoader(Context context, Topic topic) {
        super(context);
        mTopic = topic;
        mPage = 1;
    }

    public void setPage(int page) {
        if (page == mPage) {
            return;
        }
        mPage = page;
        mResult = null;
        onContentChanged();
    }

    @Override
    public TopicWithComments loadInBackgroundWithException() throws Exception {
        if (DEBUG) LogUtil.w(TAG, "load topic with comments...");
        final TopicWithComments topicWithComments = HttpRequestHelper.getInstance().getTopicWithComments(mTopic, mPage);
        if (DEBUG) LogUtil.e(TAG, "TopicWithComments : " + topicWithComments);
        return topicWithComments;
    }
}
