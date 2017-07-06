package test.demo.gyniu.v2ex.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.fragment.TopicListFragment;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-7-6.
 */
public class NodeListActivity extends BaseActivity{
    private static final String TAG = "NodeListActivity";
    private Node mNode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //get arguments
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mNode = intent.getParcelableExtra("node");
            if (mNode == null) {
                finish();
                return;
            } else {
                setTitle(mNode.getTitle());
            }
        } else {
            mNode = savedInstanceState.getParcelable("node");
        }
        LogUtil.d(TAG, "node: " + mNode);
        TopicListFragment fragment = (TopicListFragment) TopicListFragment.newInstance(mNode);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("node", mNode);
        super.onSaveInstanceState(outState);
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
