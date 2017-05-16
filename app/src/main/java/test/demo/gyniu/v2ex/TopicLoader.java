package test.demo.gyniu.v2ex;

import android.content.Context;

import test.demo.gyniu.v2ex.model.Topic;

/**
 * Created by uiprj on 17-5-15.
 */
public class TopicLoader extends AsyncTaskLoader<TopicWithComments> {
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
        final TopicWithComments topicWithComments = HttpRequestHelper.getInstance().getTopicWithComments(mTopic, mPage);
        return topicWithComments;
    }
}
