package test.demo.gyniu.v2ex;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.Topic;
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

    public TopicListLoader(Context context, Entity entity) {
        this(context);
        this.context = context;
        this.entity = entity;
    }

    public TopicListLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public TopicList loadInBackgroundWithException() throws Exception {
        if (DEBUG) LogUtil.d(TAG, "load...");
        TopicList<Topic> topics = HttpRequestHelper.getInstance().getTopicsByTab(entity);
        return topics;
    }

    public static class TopicList<Topic> extends ArrayList<Topic> {
        private List<Topic> list;
        private boolean isFav;
        private String onceToken;

        public TopicList(List<Topic> list, boolean isFav, String onceToken){
            this.list = list;
            this.isFav = isFav;
            this.onceToken = onceToken;
        }

        public Topic get(int location) {
            return list.get(location);
        }
    }
}
