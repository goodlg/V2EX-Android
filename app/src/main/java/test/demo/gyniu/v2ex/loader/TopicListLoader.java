package test.demo.gyniu.v2ex.loader;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.network.HttpRequestHelper;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-4-6.
 */
public class TopicListLoader extends AsyncTaskLoader<TopicListLoader.TopicList>
{
    private static final String TAG = "TopicListLoader";
    private static final boolean DEBUG = LogUtil.LOGD;

    private Entity entity;
    private Context context;
    private int mPage;

    public TopicListLoader(Context context, Entity entity) {
        this(context);
        this.entity = entity;
        mPage = 1;
    }

    public TopicListLoader(Context context) {
        super(context);
        this.context = context;
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
    public TopicList loadInBackgroundWithException() throws Exception {
        if (DEBUG) LogUtil.w(TAG, "load topic list...");
        TopicList<Topic> topics = HttpRequestHelper.getInstance().getTopicList(entity, mPage);
        return topics;
    }

    public static class TopicList<Topic> extends ArrayList<Topic> {
        private List<Topic> list;
        private boolean isFav;
        private String onceToken;
        private int mCurPage;
        private int mMaxPage;

        public TopicList(List<Topic> list, int mCurPage, int mMaxPage, boolean isFav, String onceToken){
            this.list = list;
            this.isFav = isFav;
            this.onceToken = onceToken;
            this.mCurPage = mCurPage;
            this.mMaxPage = mMaxPage;
        }

        public int getCurPage() {
            return mCurPage;
        }

        public int getMaxPage() {
            return mMaxPage;
        }

        public Topic get(int location) {
            return list.get(location);
        }

        public List<Topic> getList() {
            return this.list;
        }
    }
}
