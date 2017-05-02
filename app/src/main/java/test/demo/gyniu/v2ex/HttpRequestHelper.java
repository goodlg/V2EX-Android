package test.demo.gyniu.v2ex;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-4-27.
 */
public class HttpRequestHelper {
    private static final String TAG = "HttpRequestHelper";
    private static final boolean DEBUG = LogUtil.LOGD;

    private OkHttpClient mClient;
    private PersistentCookieJar mCookieJar;

    private HttpRequestHelper(){
        initOkhttp();
    }

    public static HttpRequestHelper getInstance(){
        return OkHttpSingle.sHttpHelper;
    }

    private static class OkHttpSingle{
        private static HttpRequestHelper sHttpHelper = new HttpRequestHelper();
    }

    private void initOkhttp(){
        if (DEBUG) LogUtil.e(TAG, "init okhttp3");
        //config cache size
        try {
            File cacheDirectory = AppCtx.getInstance().getExternalCacheDir();
            int cacheSize = 16 * 1024 * 1024;
            Cache cache = new Cache(cacheDirectory, cacheSize);

            //config Cookie
            mCookieJar = new PersistentCookieJar(new SetCookieCache(),
                    new SharedPrefsCookiePersistor(AppCtx.getInstance()));

            mClient = new OkHttpClient().newBuilder()
                    .connectTimeout(Constant.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constant.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constant.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                    .cache(cache)
                    .followRedirects(false)
                    .cookieJar(mCookieJar)
                    .build();
        } catch (Exception e){
            LogUtil.e(TAG, "init Okhttp Excepion:" + e);
        }
    }

    public TopicListLoader.TopicList getTopicsByTab(Entity e){
        if (DEBUG) LogUtil.e(TAG, "want to get top list");
        return null;
    }

}
