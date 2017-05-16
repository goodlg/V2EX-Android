package test.demo.gyniu.v2ex;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;

import org.jsoup.nodes.Document;

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
import test.demo.gyniu.v2ex.model.Topic;
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
        if (DEBUG) LogUtil.w(TAG, "init okhttp3");
        //config cache size
        try {
            File cacheDirectory = AppCtx.getInstance().getExternalCacheDir();
            int cacheSize = 64 * 1024 * 1024;
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

    public TopicListLoader.TopicList getTopicListByTab(Entity entity) throws Exception {
        if (DEBUG) LogUtil.w(TAG, "get topics use url: " + entity.getUrl());
        final Request request = newRequest().url(entity.getUrl()).build();

        final Response response = sendRequest(request);
        if (response.isRedirect()) {
            LogUtil.e(TAG, "Has Exception: topics should be not redirect");
            throw new IllegalStateException("topics should be not redirect");
        }

        final Document doc;
        TopicListLoader.TopicList topics;

        try {
            doc = ParserHelper.toDoc(response.body().string());
            topics = TopicListParser.parseDoc(doc, entity);
        } catch (IOException e) {
            LogUtil.e(TAG, "HAS Exception: \n parse doc failed!");
            throw new Exception(e);
        }

        return topics;
    }

    public TopicWithComments getTopicWithComments(Topic topic, int page) throws Exception {
        Preconditions.checkArgument(page > 0, "page must greater than zero");
        if (DEBUG)
            LogUtil.w(TAG, "request topic with comments, id: " + topic.getId()
                + ", title : " + topic.getTitle() + ", url : " + topic.getUrl());

        if (DEBUG) LogUtil.w(TAG, "getTopicWithComments 1");

        final Request request = newRequest().url(topic.getUrl() + "?p=" + page).build();
        if (DEBUG) LogUtil.w(TAG, "getTopicWithComments 2");
        final Response response = sendRequest(request);
        if (DEBUG) LogUtil.w(TAG, "getTopicWithComments 3");
        if (response.isRedirect()) {
            throw new IllegalStateException("topic page shouldn't redirect");
        }
        final Document doc;
        final TopicWithComments result;
        try {
            doc = ParserHelper.toDoc(response.body().string());
            result = TopicParser.parseDoc(doc, topic);
        } catch (IOException e) {
            LogUtil.e(TAG, "HAS Exception: \n" + e);
            throw new Exception(e);
        }

        if (DEBUG) LogUtil.e(TAG, "result : " + result);

        return result;
    }

    private Request.Builder newRequest() {
        return new Request.Builder().header(HttpHeaders.USER_AGENT, Constant.USER_AGENT);
    }

    private Response sendRequest(Request request) throws Exception{
        final Response response;
        try {
            if (DEBUG) LogUtil.w(TAG, "send a request to remote");
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            LogUtil.e(TAG, "HAS Exception: " + e);
            throw new Exception(e);
        }
        if (DEBUG) LogUtil.w(TAG, "response: " + response);
        return response;
    }

}
