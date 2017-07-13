package test.demo.gyniu.v2ex.utils;

import android.os.Build;

import test.demo.gyniu.v2ex.BuildConfig;

/**
 * Created by uiprj on 17-4-27.
 */
public class Constant {
    public static enum orientation {
        left,
        center,
        right
    }

    public static enum TopicListType {
        none,
        tab,
        node
    }
    public static String BASE_URL = "https://www.v2ex.com";
    public static long HTTP_CONNECT_TIMEOUT = 10;
    public static long HTTP_WRITE_TIMEOUT = 10;
    public static long HTTP_READ_TIMEOUT = 30;

    public static String USER_AGENT = "V2EX+/" + BuildConfig.VERSION_NAME;
    public static String USER_AGENT_ANDROID = String.format("%s (Android %s)",
            USER_AGENT, Build.VERSION.RELEASE);

    public static final String URL_SIGN_IN = BASE_URL + "/signin";
    public static final String URL_ONCE_CODE = URL_SIGN_IN;

    public static final String URL_ONCE_TOKEN = URL_SIGN_IN;
    public static final String API_GET_ALL_NODES = BASE_URL + "/api/nodes/all.json";

    public static final String API_GET_USER_FAV_NODES = BASE_URL + "/my/nodes";
}
