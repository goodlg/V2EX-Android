package test.demo.gyniu.v2ex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.common.eventbus.Subscribe;

import test.demo.gyniu.v2ex.eventbus.BaseEvent.ContextInitFinishEvent;

/**
 * Created by uiprj on 17-5-10.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Wrapper wrapper = new Wrapper();
        if (AppCtx.getInstance().isInited()) {
            AppCtx.getEventBus().unregister(wrapper);
            return;
        }
        startActivityForResult(new Intent(this, LoadingActivity.class), 0);
    }

    private class Wrapper {
        @Subscribe
        public void onContextInitFinishEvent(ContextInitFinishEvent event) {
            AppCtx.getEventBus().unregister(this);
            finishActivity(0);
        }
    }
}
