package test.demo.gyniu.v2ex;

import android.app.Application;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.sql.BatchUpdateException;
import java.util.List;

import test.demo.gyniu.v2ex.common.UserState;
import test.demo.gyniu.v2ex.dao.ConfigDao;
import test.demo.gyniu.v2ex.dao.DbHelper;
import test.demo.gyniu.v2ex.dao.NodeDao;
import test.demo.gyniu.v2ex.eventbus.BaseEvent;
import test.demo.gyniu.v2ex.eventbus.executor.HandlerExecutor;
import test.demo.gyniu.v2ex.model.Etag;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.network.HttpRequestHelper;
import test.demo.gyniu.v2ex.utils.ExecutorUtils;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-3-21.
 */
public class AppCtx extends Application {
    private static final String TAG = "AppCtx";

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
            UserState.getInstance().init();

            mIsInited = true;
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "init done ,post done event");
            mEventBus.post(new BaseEvent.ContextInitFinishEvent());

            //load all nodes
            loadAllNodes();
        }

        private void loadAllNodes() {
            final String etagStr = ConfigDao.get(ConfigDao.KEY_NODE_ETAG, null);
            Etag etag = new Etag(etagStr);
            List<Node> result;
            //for debug
            long startTime = 0L;
            if (BuildConfig.DEBUG) startTime = System.nanoTime();
            //////////////////
            try {
                result = HttpRequestHelper.getInstance().getAllNodes(etag);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "Exception: " + e);
                return;
            }
            for (Node node :  result) {
                LogUtil.d(TAG, "node: " + node);
            }
            //for debug
            if (BuildConfig.DEBUG) {
                long consumingTime = System.nanoTime() - startTime;
                LogUtil.d(TAG, "!!!consuming time : " + consumingTime/1000 + "μs");
            }
            ////////////////////
            if (etag.isModified()) {
                NodeDao.saveAllNodes(result);
                ConfigDao.put(ConfigDao.KEY_NODE_ETAG, etag.getNewEtag());
            }
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "load nodes finish! etagStr: " + etagStr);
        }
    }
}
