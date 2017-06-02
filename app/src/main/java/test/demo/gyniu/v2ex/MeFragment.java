package test.demo.gyniu.v2ex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-3-17.
 */
public class MeFragment extends Fragment{
    private static final String TAG = "MeFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) LogUtil.d(TAG, "create me fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        if (DEBUG) LogUtil.d(TAG, "create me view");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (DEBUG) LogUtil.d(TAG, "activity already created");

        final MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(R.string.str_me);
    }
}
