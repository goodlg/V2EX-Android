package test.demo.gyniu.v2ex;

import android.app.Application;

/**
 * Created by uiprj on 17-3-21.
 */
public class AppCtx extends Application {
    private static AppCtx mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Application getInstance(){
        if(mInstance == null){
            mInstance = new AppCtx();
        }
        return mInstance;
    }
}
