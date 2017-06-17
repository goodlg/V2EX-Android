package test.demo.gyniu.v2ex;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.LoginResult;
import test.demo.gyniu.v2ex.model.SignInForm;
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
            e.printStackTrace();
            LogUtil.e(TAG, "Exception:" + e);
        }
    }

    public TopicListLoader.TopicList getTopicListByTab(Entity entity) throws Exception {
        if (DEBUG) LogUtil.w(TAG, "get topics use url: " + entity.getUrl());
        final Request request = newRequest().url(entity.getUrl()).build();

        final Response response = sendRequest(request);
        if (response.isRedirect()) {
            LogUtil.e(TAG, "Exception: Topics should be not redirect");
            throw new IllegalStateException("topics should be not redirect");
        }

        final Document doc;
        TopicListLoader.TopicList topics;

        try {
            doc = ParserHelper.toDoc(response.body().string());
            topics = TopicListParser.parseDoc(doc, entity);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception: " + e);
            throw new Exception(e);
        }

        return topics;
    }

    public TopicWithComments getTopicWithComments(Topic topic, int page) throws Exception {
        Preconditions.checkArgument(page > 0, "page must greater than zero");
        if (DEBUG)
            LogUtil.w(TAG, "request topic with comments, id: " + topic.getId()
                + ", title : " + topic.getTitle() + ", url : " + topic.getUrl());
        final Request request = newRequest().url(topic.getUrl() + "?p=" + page).build();
        final Response response = sendRequest(request);
        if (response.isRedirect()) {
            LogUtil.e(TAG, "HAS Exception: topic page shouldn't redirect");
            throw new IllegalStateException("topic page shouldn't redirect");
        }
        final Document doc;
        final TopicWithComments result;
        try {
            LogUtil.w(TAG, "get topic with comments!");
            doc = ParserHelper.toDoc(response.body().string());
            result = TopicParser.parseDoc(doc, topic);
        } catch (IOException e) {
            LogUtil.e(TAG, "Exception: " + e);
            throw new Exception(e);
        }

        if (DEBUG) LogUtil.e(TAG, "result : " + result);

        return result;
    }

    private Request.Builder newRequest() {
        return new Request.Builder().header(HttpHeaders.USER_AGENT, Constant.USER_AGENT);
    }

    private Response sendRequest(Request request) throws Exception{
        final Response rsp;
        try {
            if (DEBUG) LogUtil.d(TAG, "send req to remote");
            rsp = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception: " + e);
            throw new Exception(e);
        }

        if (DEBUG) LogUtil.w(TAG, "rsp: " + rsp);
        return rsp;
    }

    public LoginResult login(String account, String password) throws Exception {
        if (DEBUG) LogUtil.d(TAG, "login user: " + account);

        final SignInForm form = getSignInForm();
        if (DEBUG) LogUtil.d(TAG, "form info: " + form);
        Preconditions.checkNotNull(form);
        final String nextUrl = "/mission";
        final RequestBody requestBody = new FormBody.Builder()
                .add("once", form.getOnceCode())
                .add(form.getAccount(), account)
                .add(form.getPasswd(), password)
                .add("next", nextUrl)
                .build();

        Request request = newRequest().url(Constant.URL_SIGN_IN)
                .header(HttpHeaders.REFERER, Constant.URL_SIGN_IN)
                .post(requestBody).build();

        Response response = sendRequest(request);

        // v2ex will redirect if login success
        if (response.code() != 302) {
            if (DEBUG) LogUtil.d(TAG, "will be redirect if login successfully, resp code: "
                    + response.code());
            return null;
        }

        final String location = response.header(HttpHeaders.LOCATION);
        if (!location.equals(nextUrl)) {
            if (DEBUG) LogUtil.d(TAG, "http header nextUrl: " + nextUrl);
            return null;
        }

        request = newRequest().url(Constant.BASE_URL + location).build();
        response = sendRequest(request);

        try {
            final String html = response.body().string();
            final Document document = ParserHelper.toDoc(html);
            return MyselfParser.parseLoginResult(document);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception: " + e);
            throw new Exception(e);
        }
    }

    private SignInForm getSignInForm() {
        Request request = newRequest()
                .header(HttpHeaders.USER_AGENT, Constant.USER_AGENT_ANDROID)
                .url(Constant.URL_ONCE_TOKEN).build();
        if (DEBUG) LogUtil.d(TAG, "request: " + request);
        try {
            final Response response = sendRequest(request);
            final String html = response.body().string();
            return ParserHelper.parseSignInForm(html);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception: " + e);
        }
        return null;
    }

    public String getOnceCode() throws Exception {
        if (DEBUG) LogUtil.d(TAG, "get once code");
        final Request request = newRequest().url(Constant.URL_ONCE_CODE).build();
        final Response response = sendRequest(request);

        try {
            final String html = response.body().string();
            return ParserHelper.parseOnceCode(html);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception: " + e);
            throw new Exception(e);
        }
    }

}
