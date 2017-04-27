package test.demo.gyniu.v2ex;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by uiprj on 17-4-27.
 */
public class NetworkRequestHelper {
    private static final String TAB = "NetworkRequestHelper";

    private OkHttpClient mClient;
    private PersistentCookieJar mCookieJar;

    {
        mCookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(context));
    }
}
