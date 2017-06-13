package test.demo.gyniu.v2ex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.ListOptionView.OnDoOptionListener;

/**
 * Created by uiprj on 17-3-17.
 */
public class MeFragment extends Fragment implements OnDoOptionListener{
    private static final String TAG = "MeFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    private ListOptionView mSettings;
    private ListOptionView mUpdate;
    private ListOptionView mAbout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) LogUtil.d(TAG, "create me fragment");
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
}
