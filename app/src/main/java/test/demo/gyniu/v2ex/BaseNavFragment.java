package test.demo.gyniu.v2ex;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by uiprj on 17-3-14.
 */
public abstract class BaseNavFragment extends Fragment {
    private static final String TAG = "BaseNavFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    protected TabLayout mTabLayout;

    private Context mContxt;

    public BaseNavFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.nav_layout, container, false);
        if (DEBUG) LogUtil.d(TAG, " create base nav fragment");

        ViewPager viewPager = ((ViewPager) view.findViewById(R.id.view_pager));
        FragmentPagerAdapter adapter = getAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin));

        mTabLayout = (TabLayout) view.findViewById(R.id.nav_layout);
        ViewCompat.setElevation(mTabLayout, getResources().getDimension(R.dimen.appbar_elevation));
        mTabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContxt = getActivity();
    }

    public Context getContext(){
        if(mContxt == null){
            return AppCtx.getInstance();
        }
        return mContxt;
    }

    protected abstract FragmentPagerAdapter getAdapter(FragmentManager manager);
}
