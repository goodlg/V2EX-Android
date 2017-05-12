package test.demo.gyniu.v2ex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;

import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-12.
 */
public class TopicContentFragment extends Fragment {
    private static final String TAG = "TopicContentFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    private static final String ARG_TOPIC = "topic";
    private Topic mTopic;
    private AppBarLayout mAppBarLayout;

    public TopicContentFragment(){}

    public static TopicContentFragment newInstance(Topic topic) {
        TopicContentFragment fragment = new TopicContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TOPIC, topic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTopic = getArguments().getParcelable(ARG_TOPIC);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_topic_content, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TopicActivity activity = (TopicActivity) getActivity();
        activity.setTitle(null);

        final ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            Preconditions.checkNotNull(actionBar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAppBarLayout = activity.getAppBarLayout();
    }
}
