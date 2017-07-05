package test.demo.gyniu.v2ex.loader;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-4-6.
 */
public abstract class AsyncTaskLoader<T> extends android.support.v4.content.AsyncTaskLoader {
    private static final String TAG = "AsyncTaskLoader";
    private static final boolean DEBUG = LogUtil.LOGD;

    protected LoaderResult<T> mResult;
    private static final long DEFAULT_UPDATE_THROTTLE = TimeUnit.SECONDS.toMillis(3);

//    protected boolean mRefresh = false;

    public AsyncTaskLoader(Context context) {
        super(context);
        setUpdateThrottle(DEFAULT_UPDATE_THROTTLE);
    }

//    public boolean isRefresh() {
//        return mRefresh;
//    }
//
//    public void setRefresh(boolean mRefresh) {
//        this.mRefresh = mRefresh;
//    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (DEBUG) LogUtil.d(TAG, "start loading...");
        if (mResult != null && isStarted()) {
            if (DEBUG) LogUtil.d(TAG, "start loading 2...");
            deliverResult(mResult);
        }

        if (DEBUG) LogUtil.d(TAG, "start loading 3...");
        if (takeContentChanged() || mResult == null) {
            if (DEBUG) LogUtil.d(TAG, "start loading 4...");
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        if (DEBUG) LogUtil.d(TAG, "cancel load.");
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (DEBUG) LogUtil.d(TAG, "need reset load.");
        stopLoading();
        mResult = null;
    }

    @Override
    public LoaderResult<T> loadInBackground() {
        LoaderResult<T> loaderResult;
        try {
            if (DEBUG) LogUtil.w(TAG, "Load in background...");
            T result = loadInBackgroundWithException();
            loaderResult = new LoaderResult<>(result);
        } catch (Exception e) {
            LogUtil.e(TAG, "HAS Exception: " +e);
            loaderResult = new LoaderResult<>(e);
        }

        if (DEBUG) LogUtil.w(TAG, "Load done.");
        mResult = loaderResult;
        return mResult;
    }

    public abstract T loadInBackgroundWithException() throws Exception;

    public static class LoaderResult<T> {
        public final Exception mException;
        public final T mResult;

        public LoaderResult(Exception exception) {
            mException = exception;
            mResult = null;
        }

        public LoaderResult(T result) {
            mResult = result;
            mException = null;
        }

        public boolean hasException() {
            return mException != null;
        }

        public Exception getException(){
            return mException;
        }
    }
}
