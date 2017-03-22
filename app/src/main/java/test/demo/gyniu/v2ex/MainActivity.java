package test.demo.gyniu.v2ex;

import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-3-13.
 */
public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{
    private static final String TAG = "MainActivity";

    private static final boolean DEBUG = LogUtil.LOGD;

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolBar;

    private LayoutInflater layoutInflater;

    private FragmentTabHost mTabHost;
    private String[] mBottomTabTitle;
    private int[] mBottomTabIconNormal;
    private int[] mBottomTabIconSelect;

    private Class fragmentArray[] = { TopNavFragment.class, FavFragment.class, MeFragment.class };

    private static final int MAX_BOTTOM_TAB_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutInflater = LayoutInflater.from(this);
        getRes();
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null){
            mToolBar.setTitle(R.string.app_name);
            setSupportActionBar(mToolBar);
        }
        initBottomTabHost();
    }

    private void getRes(){
        //string
        mBottomTabTitle = getResources().getStringArray(R.array.bootom_tab_title);
        //normal icon
        TypedArray ar = getResources().obtainTypedArray(R.array.bootom_tab_normal_icon);
        int len = ar.length();
        mBottomTabIconNormal = new int[len];
        for (int i=0; i<len; i++){
            mBottomTabIconNormal[i] = ar.getResourceId(i, 0);
        }
        //select icon
        ar = getResources().obtainTypedArray(R.array.bootom_tab_icon_select);
        len = ar.length();
        mBottomTabIconSelect = new int[len];
        for (int i=0; i<len; i++){
            mBottomTabIconSelect[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
    }

    private void initBottomTabHost() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fragment);
        mTabHost.getTabWidget().setDividerDrawable(null);
        //build each tab
        for(int i=0; i<MAX_BOTTOM_TAB_COUNT; i++){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mBottomTabTitle[i])
                    .setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.selector_tab_background);
        }
        //set listener
        mTabHost.setOnTabChangedListener(this);
        //select first tab
        mTabHost.setCurrentTab(0);
        onTabChanged(mTabHost.getCurrentTabTag());
    }

    private View getTabItemView(int i) {
        View view = layoutInflater.inflate(R.layout.bottom_tab_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        TextView title = (TextView) view.findViewById(R.id.tab_title);
        icon.setImageResource(mBottomTabIconNormal[i]);
        title.setText(mBottomTabTitle[i]);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        if (DEBUG) LogUtil.d(TAG, "tabId=" + tabId);
        setTitle(tabId);
        resetOtherTabs();
        View view = mTabHost.getCurrentTabView();
        if (view != null){
            int index = getIndexByTabId(tabId);
            ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
            TextView title = (TextView) view.findViewById(R.id.tab_title);
            icon.setImageResource(mBottomTabIconSelect[index]);
            title.setTextColor(getResources().getColor(R.color.colorBottomTabTextSelect));
        }
    }

    private int getIndexByTabId(String tabId){
        int index = 0;
        for (int i=0; i<MAX_BOTTOM_TAB_COUNT; i++){
            if (mBottomTabTitle[i].equals(tabId)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void resetOtherTabs() {
        for (int i=0; i<MAX_BOTTOM_TAB_COUNT; i++){
            View view = mTabHost.getTabWidget().getChildAt(i);
            if (view != null){
                ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
                TextView title = (TextView) view.findViewById(R.id.tab_title);
                icon.setImageResource(mBottomTabIconNormal[i]);
                title.setTextColor(getResources().getColor(R.color.colorBottomTabText));
                view.setBackgroundResource(R.drawable.selector_tab_background);
            }
        }
    }
}
