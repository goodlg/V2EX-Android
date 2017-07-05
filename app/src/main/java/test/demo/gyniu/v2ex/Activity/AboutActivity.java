package test.demo.gyniu.v2ex.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.R;

/**
 * Created by uiprj on 17-5-10.
 */
public class AboutActivity extends BaseActivity {

    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mVersion = (TextView) findViewById(R.id.version);
        mVersion.setText(getString(R.string.str_version, BuildConfig.VERSION_NAME));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
