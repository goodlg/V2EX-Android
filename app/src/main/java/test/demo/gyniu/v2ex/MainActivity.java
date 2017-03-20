package test.demo.gyniu.v2ex;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by uiprj on 17-3-13.
 */
public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{
    private static final String TAG = "MainActivity";

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
    }

    private View getTabItemView(int i) {
        View view = layoutInflater.inflate(R.layout.bottom_tab_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        TextView title = (TextView) view.findViewById(R.id.tab_title);
        icon.setBackgroundResource(mBottomTabIconNormal[i]);
        title.setText(mBottomTabTitle[i]);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d(TAG, "@@@@@ onTabChanged, tabId=" + tabId);
        setTitle(tabId);
        //resetOtherTabs();
        View view = mTabHost.getCurrentTabView();
        Log.d(TAG, " @@@@@ tag:" + mTabHost.getTag() + ", view=" + view);
        if (view != null){
            ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
            TextView title = (TextView) view.findViewById(R.id.tab_title);
            icon.setBackgroundResource(R.drawable.selector_tab_img_background);
            title.setTextColor(Color.parseColor("#f2497c"));
        }
    }

    private void resetOtherTabs() {
        for (int i=0; i<MAX_BOTTOM_TAB_COUNT; i++){
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.selector_tab_background);
        }
    }
}
