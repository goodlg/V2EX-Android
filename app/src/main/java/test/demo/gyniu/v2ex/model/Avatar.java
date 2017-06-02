package test.demo.gyniu.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import test.demo.gyniu.v2ex.AppCtx;

/**
 * Created by uiprj on 17-5-10.
 */
public class    Avatar implements Parcelable {
    public static final float DENSITY;
    public static final int SIZE_NORMAL = 48;
    public static final int SIZE_MINI = 24;

    private static final Pattern PATTERN = Pattern.compile("(mini|normal|large)");
    private static final String LARGE = "large";
    private static final String NORMAL = "normal";
    private static final String MINI = "mini";

    static {
        DENSITY = AppCtx.getInstance().getResources().getDisplayMetrics().density;
    }

    private final String mBaseUrl;

    Avatar(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    protected Avatar(Parcel in) {
        this.mBaseUrl = in.readString();
    }

    public String getLarge() {
        return getUrl(mBaseUrl, LARGE);
    }

    public String getNormal() {
        return getUrl(mBaseUrl, NORMAL);
    }

    public String getMini() {
        return getUrl(mBaseUrl, MINI);
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public String getUrlByDp(float dp) {
        final float size = dp * DENSITY;
        return getUrlByPx(size);
    }

    public String getUrlByPx(float size) {
        if (size <= SIZE_MINI) {
            return getMini();
        } else if (size <= SIZE_NORMAL) {
            return getNormal();
        } else {
            return getLarge();
        }
    }

    public static String getUrl(String baseUrl, String size) {
        return "https:" + String.format(baseUrl, size);
    }

    private static String getBaseUrl(String url) {
        return PATTERN.matcher(url).replaceFirst("%s");
    }

    public static final Creator<Avatar> CREATOR = new Creator<Avatar>() {
        @Override
        public Avatar createFromParcel(Parcel in) {
            return new Avatar(in);
        }

        @Override
        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };

    public static class Builder {
        private static final LoadingCache<String, Avatar> CACHE;
        private String mBaseUrl;

        static {
            CACHE = CacheBuilder.newBuilder()
                    .softValues()
                    .initialCapacity(32)
                    .maximumSize(128)
                    .build(new CacheLoader<String, Avatar>() {
                        @Override
                        public Avatar load(@NonNull String key) throws Exception {
                            return new Avatar(key);
                        }
                    });
        }

        public Builder setUrl(String url) {
            mBaseUrl = getBaseUrl(url);
            return this;
        }

        public Builder setBaseUrl(String url) {
            mBaseUrl = url;
            return this;
        }

        public Avatar createAvatar() {
            try {
                return CACHE.get(mBaseUrl);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBaseUrl);
    }
}
