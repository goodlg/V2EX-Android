package test.demo.gyniu.v2ex;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.common.net.HttpHeaders;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    private HttpRequestHelper() {
        initOkhttp();
    }

    public static HttpRequestHelper getInstance() {
        return OkHttpSingle.sHttpHelper;
    }

    private static class OkHttpSingle {
        private static HttpRequestHelper sHttpHelper = new HttpRequestHelper();
    }

    private void initOkhttp() {
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
        } catch (Exception e) {
            LogUtil.e(TAG, "init Okhttp Excepion:" + e);
        }
    }

    public TopicListLoader.TopicList getTopicsByTab(Entity e) {
        if (DEBUG) LogUtil.e(TAG, "get topics use url: " + e.getUrl());
        Request request = newRequest().url(e.getUrl()).build();
        sendRequest(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>(){

                    @Override
                    public void onCompleted() {
                        if (DEBUG) LogUtil.e(TAG, "subscribe Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (DEBUG) LogUtil.e(TAG, "subscribe Exception:" + e);
                    }

                    @Override
                    public void onNext(String s) {
                        if (DEBUG) LogUtil.e(TAG, "content: " + s);
                    }
                });

        return null;
    }

    private Request.Builder newRequest() {
        return new Request.Builder().header(HttpHeaders.USER_AGENT, Constant.USER_AGENT);
    }


    public Observable<String> sendRequest(Request request){
        return sendRequest(request, true);
    }

    public Observable<String> sendRequest(final Request request, final boolean checkResponse){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    mClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (DEBUG) LogUtil.e(TAG, "send request Exception:" + e);
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String str = response.body().string();
                                subscriber.onNext(str);
                            }
                            subscriber.onCompleted();
                        }

                    });
                }
            }
        });
    }

}
