package test.demo.gyniu.v2ex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import java.util.List;

import test.demo.gyniu.v2ex.model.Tab;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.SharedPreferencesHelper;

/**
 * Created by uiprj on 17-3-14.
 */
public class HomeFragment extends BaseNavFragment {
    private static final String TAG = "HomeFragment";
    private static final boolean DEBUG = LogUtil.LOGD;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) LogUtil.d(TAG, "create home fragment");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (DEBUG) LogUtil.d(TAG, "create home view");
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.nav_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        final View tabStrip = mTabLayout.getChildAt(0);
        Preconditions.checkNotNull(tabStrip, "tabStrip shouldn't be null");
        ViewCompat.setPaddingRelative(tabStrip, 8, 0, 8, 0);

        return view;
    }

    @Override
    protected FragmentPagerAdapter getAdapter(FragmentManager manager) {
        return new TopNavFragmentAdapter(manager);
    }

    private class TopNavFragmentAdapter extends FragmentPagerAdapter {
        private final List<Tab> mNavs;
        public TopNavFragmentAdapter(FragmentManager manager) {
            super(manager);
            mNavs = SharedPreferencesHelper.getInstance(getContext()).getTabsToShow();
        }

        public Fragment getItem(int position) {
            if (DEBUG) LogUtil.d(TAG, "TopNavFragmentAdapter getItem");
            return TopicListFragment.newInstance(mNavs.get(position));
        }

        @Override
        public int getCount() {
            return mNavs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getContext().getResources().
                    getString(mNavs.get(position).getTitleResId());
        }
    }

}
