package test.demo.gyniu.v2ex;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by uiprj on 17-3-13.
 */
public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        mToolBar = (Toolbar) mAppBarLayout.findViewById(R.id.toolbar);
        mToolBar.setTitle(R.string.app_name);
        setSupportActionBar(mToolBar);
    }

}
