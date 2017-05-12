package test.demo.gyniu.v2ex;

import android.app.Application;

/**
 * Created by uiprj on 17-3-21.
 */
public class AppCtx extends Application {
    private static AppCtx mInstance;
    private volatile boolean mIsInited;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init(){
        mIsInited = true;
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
}
