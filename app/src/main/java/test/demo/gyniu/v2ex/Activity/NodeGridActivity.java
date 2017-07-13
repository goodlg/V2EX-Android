package test.demo.gyniu.v2ex.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.fragment.NodeGridFragment;
import test.demo.gyniu.v2ex.fragment.TopicListFragment;

/**
 * Created by uiprj on 17-7-13.
 */
public class NodeGridActivity extends BaseActivity{
    private static final String TAG = "NodeGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NodeGridFragment fragment = NodeGridFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
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
