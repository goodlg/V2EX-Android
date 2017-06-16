package test.demo.gyniu.v2ex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.common.eventbus.Subscribe;

import test.demo.gyniu.v2ex.eventbus.BaseEvent.ContextInitFinishEvent;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-10.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private static final boolean DEBUG = LogUtil.LOGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Wrapper wrapper = new Wrapper();
        AppCtx.getEventBus().register(wrapper);

        if (DEBUG) LogUtil.d(TAG, "in create");

        if (AppCtx.getInstance().isInited()) {
            AppCtx.getEventBus().unregister(wrapper);
            if (DEBUG) LogUtil.d(TAG, "init only once, so return");
            return;
        }
        startActivityForResult(new Intent(this, LoadingActivity.class), 0);
    }

    private class Wrapper {
        @Subscribe
        public void onContextInitFinishEvent(ContextInitFinishEvent event) {
            if (DEBUG) LogUtil.d(TAG, "handle done event");
            AppCtx.getEventBus().unregister(this);
            finishActivity(0);
        }
    }
}
