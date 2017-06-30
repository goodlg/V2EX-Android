package test.demo.gyniu.v2ex.common;

import okhttp3.Response;

/**
 * Created by uiprj on 17-6-30.
 */
public class RequestException extends RuntimeException {
    private final Response mResponse;

    public RequestException(Response mResponse) {
        this(null, mResponse);
    }

    public RequestException(String detailMessage, Response mResponse) {
        this(detailMessage, null, mResponse);
    }

    public RequestException(String detailMessage, Throwable throwable, Response mResponse) {
        super(detailMessage, throwable);
        this.mResponse = mResponse;
    }

    public RequestException(Response mResponse, Throwable throwable) {
        this(null, throwable, mResponse);
    }

    public Response getResponse() {
        return mResponse;
    }

    public int getCode() {
        return mResponse.code();
    }
}
