package test.demo.gyniu.v2ex;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.Topic;

/**
 * Created by uiprj on 17-4-6.
 */
public class TopicListLoader extends AsyncTaskLoader<TopicListLoader.TopicList>
{
    public TopicListLoader(Context context, Entity e) {
        super(context);
    }

    public TopicListLoader(Context context) {
        super(context);
    }

    @Override
    public TopicList loadInBackgroundWithException() throws Exception {
        return null;
    }

    public static class TopicList<Topic> extends ArrayList<Topic> {
        private List<Topic> list;

        public TopicList(List<Topic> list, boolean isFavorited, String onceToken){
            this.list = list;
        }

        public Topic get(int location) {
            return list.get(location);
        }
    }
}
