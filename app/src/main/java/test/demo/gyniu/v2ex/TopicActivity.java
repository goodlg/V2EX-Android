package test.demo.gyniu.v2ex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import com.google.common.base.Strings;

import test.demo.gyniu.v2ex.model.Topic;

/**
 * Created by uiprj on 17-5-10.
 */
public class TopicActivity extends BaseActivity {
    public static final String KEY_TOPIC = "topic";
    public static final String KEY_TOPIC_ID = "topic_id";
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mAppBarLayout = ((AppBarLayout) findViewById(R.id.appBar));
        if (savedInstanceState == null) {
            Topic topic = getTopicFromIntent();
            if (topic == null) {
                finish();
                return;
            }

            addFragmentToView(topic);
        }
    }

    private Topic getTopicFromIntent() {
        final Intent it = getIntent();
        if (it == null) return null;

        if (it.hasExtra(KEY_TOPIC)) {
            return it.getParcelableExtra(KEY_TOPIC);
        }
        if (it.hasExtra(KEY_TOPIC_ID)) {
            final int id = it.getIntExtra(KEY_TOPIC_ID, 0);
            return new Topic.Builder().setId(id).createTopic();
        }

        if(it.getAction() != null && it.getAction().equals(Intent.ACTION_VIEW)) {
            final String url = it.getDataString();
            if (!Strings.isNullOrEmpty(url)) {
                final int id = Topic.getIdFromUrl(url);
                return new Topic.Builder().setId(id).createTopic();
            }
        }

        return null;
    }

    private void addFragmentToView(Topic topic) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, TopicContentFragment.newInstance(topic))
                .commit();
    }

    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }
}
