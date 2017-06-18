package test.demo.gyniu.v2ex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;

import test.demo.gyniu.v2ex.common.UserState;
import test.demo.gyniu.v2ex.eventbus.LoginEvent;
import test.demo.gyniu.v2ex.model.UserProfile;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.ListOptionView.OnDoOptionListener;

/**
 * Created by uiprj on 17-3-17.
 */
public class MeFragment extends Fragment implements OnDoOptionListener, View.OnClickListener{
    private static final String TAG = "MeFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    private ListOptionView mSettings;
    private ListOptionView mUpdate;
    private ListOptionView mAbout;

    private ImageView mAvatar;

    private RelativeLayout mProfileLayout;
    private TextView mUsername;
    private TextView mSilver;
    private TextView mBronze;
    private TextView mRemind;

    //show if user not login
    private TextView mLogin;

    private LinearLayout mLayout2;
    private TextView mNodes;
    private TextView mTopics;
    private TextView mAttentions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) LogUtil.d(TAG, "create me fragment");
        AppCtx.getEventBus().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DEBUG) LogUtil.d(TAG, "create me view");
        View view = inflater.inflate(R.layout.fragment_me, null);
        mSettings = (ListOptionView) view.findViewById(R.id.settings);
        mSettings.setListener(this);
        mUpdate = (ListOptionView) view.findViewById(R.id.update);
        mUpdate.setListener(this);
        mAbout = (ListOptionView) view.findViewById(R.id.about);
        mAbout.setListener(this);

        mAvatar = (ImageView) view.findViewById(R.id.avatar);
        mProfileLayout = (RelativeLayout) view.findViewById(R.id.profileLayout);
        mUsername = (TextView) view.findViewById(R.id.username);
        mSilver = (TextView) view.findViewById(R.id.tv_silver);
        mBronze = (TextView) view.findViewById(R.id.tv_bronze);
        mRemind = (TextView) view.findViewById(R.id.remind);

        mLogin = (TextView) view.findViewById(R.id.login);
        mLogin.setOnClickListener(this);

        mLayout2 = (LinearLayout) view.findViewById(R.id.layout2);
        mNodes = (TextView) view.findViewById(R.id.node_collect_count);
        mTopics = (TextView) view.findViewById(R.id.topic_collect_count);
        mAttentions = (TextView) view.findViewById(R.id.attention_count);

        updateUserStatusUI();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (DEBUG) LogUtil.d(TAG, "activity already created");

        final MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(R.string.str_me);
    }

    @Override
    public void onClickOptionView(View view) {
        int tag = Integer.parseInt((String) view.getTag());
        switch (tag){
            case 0:
                Toast.makeText(getActivity(), "settings", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getActivity(), "update", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            default:
                if (DEBUG) LogUtil.w(TAG, "nothing!!!!");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login) {
            Intent intent = new Intent(getActivity(), SigninActivity.class);
            startActivity(intent);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onLoginEvent(LoginEvent e) {
        if (DEBUG) LogUtil.d(TAG, "login success !!!");
        updateUserStatusUI();
    }

    private void updateUserStatusUI() {
        final UserState us = UserState.getInstance();
        final UserProfile profile = us.getProfile();
        if (profile!=null && us.isLoggedIn()) {
            mProfileLayout.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.GONE);
            mLayout2.setVisibility(View.VISIBLE);
            //username
            mUsername.setText(profile.getAccount());
            mUsername.setVisibility(View.VISIBLE);
            //silver
            mSilver.setText(String.format("%d", profile.getSilver()));
            mSilver.setVisibility(View.VISIBLE);
            //bronze
            mBronze.setText(String.format("%d", profile.getBronze()));
            mSilver.setVisibility(View.VISIBLE);
            //notifications
            mRemind.setText(getString(R.string.str_remind, profile.getRemind()));
            mRemind.setVisibility(View.VISIBLE);
            //nodes
            mNodes.setText(String.format("%d", profile.getNodesCount()));
            mNodes.setVisibility(View.VISIBLE);
            //topics
            mTopics.setText(String.format("%d", profile.getTopicsCount()));
            mTopics.setVisibility(View.VISIBLE);
            //followings
            mAttentions.setText(String.format("%d", profile.getFollowings()));
            mAttentions.setVisibility(View.VISIBLE);
        } else {
            mProfileLayout.setVisibility(View.GONE);
            mLogin.setVisibility(View.VISIBLE);
            mLayout2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppCtx.getEventBus().unregister(this);
    }
}
