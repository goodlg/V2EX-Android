package test.demo.gyniu.v2ex;

import android.app.Application;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import test.demo.gyniu.v2ex.dao.DbHelper;
import test.demo.gyniu.v2ex.eventbus.BaseEvent;
import test.demo.gyniu.v2ex.eventbus.executor.HandlerExecutor;
import test.demo.gyniu.v2ex.utils.ExecutorUtils;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-3-21.
 */
public class AppCtx extends Application {
    private static final String TAG = "AppCtx";
    private static final boolean DEBUG = LogUtil.LOGD;

    private static AppCtx mInstance;
    private volatile boolean mIsInited;
    private EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init(){
        // event bus is the first
        mEventBus = new AsyncEventBus(new HandlerExecutor());
        mEventBus.register(this);

        ExecutorUtils.execute(new AsyncInitTask());
    }

    public boolean isInited() {
        return mIsInited;
    }

    public static AppCtx getInstance(){
        if(mInstance == null){
            mInstance = new AppCtx();
        }
        return mInstance;
    }

    public static EventBus getEventBus() {
        return mInstance.mEventBus;
    }

    private class AsyncInitTask implements Runnable {
        @Override
        public void run() {
            DbHelper.getInstance().init();

            mIsInited = true;
            if (DEBUG) LogUtil.d(TAG, "init done ,post done event");
            mEventBus.post(new BaseEvent.ContextInitFinishEvent());
        }
    }
}
