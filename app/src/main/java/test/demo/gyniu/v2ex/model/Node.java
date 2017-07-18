package test.demo.gyniu.v2ex.model;

import android.os.Parcel;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.text.Collator;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.utils.Constant;
import test.demo.gyniu.v2ex.adapter.ExArrayAdapter;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by gyniu on 17-5-29.
 * node bean
 */
public class Node extends Entity implements Comparable<Node>, ExArrayAdapter.Filterable {
    private static final String TAG = "Node";

    private static final Pattern PATTERN = Pattern.compile("/go/(.+?)(?:\\W|$)");
    private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);

    private final int mId;
    private final String mTitle;
    private final String mName;
    private final String mHeader;
    private final String mFooter;
    private final String mTitleAlternative;
    private final int mTopics;
    private final Avatar mAvatar;
    private final boolean mHasInfo;

    public Node(int mId, String mTitle, String mName, String mHeader, String mFooter, String mTitleAlternative,
                int mTopics, Avatar mAvatar) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(mName));
        this.mId = mId;
        this.mTitle = mTitle;
        this.mName = mName;
        this.mHeader = mHeader;
        this.mFooter = mFooter;
        this.mTitleAlternative = mTitleAlternative;
        this.mTopics = mTopics;
        this.mAvatar = mAvatar;
        this.mHasInfo = !Strings.isNullOrEmpty(mTitle);
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getName() {
        return mName;
    }

    public String getHeader() {
        return mHeader;
    }

    public String getFooter() {
        return mFooter;
    }

    public String getTitleAlternative() {
        return mTitleAlternative;
    }

    public int getTopics() {
        return mTopics;
    }

    public Avatar getAvatar() {
        return mAvatar;
    }

    public boolean hasInfo() {
        return mHasInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return mId == node.mId &&
                mTopics == node.mTopics &&
                mHasInfo == node.mHasInfo &&
                Objects.equal(mTitle, node.mTitle) &&
                Objects.equal(mName, node.mName) &&
                Objects.equal(mHeader, node.mHeader) &&
                Objects.equal(mFooter, node.mFooter) &&
                Objects.equal(mTitleAlternative, node.mTitleAlternative) &&
                Objects.equal(mAvatar, node.mAvatar);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mName, mHeader, mFooter, mTitleAlternative, mTopics, mAvatar, mHasInfo);
    }

    @Override
    public String toString() {
        return "Node{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mName='" + mName + '\'' +
                ", mHeader='" + mHeader + '\'' +
                ", mFooter='" + mFooter + '\'' +
                ", mTitleAlternative='" + mTitleAlternative + '\'' +
                ", mTopics=" + mTopics +
                ", mAvatar=" + mAvatar +
                ", mHasInfo=" + mHasInfo +
                ", hashCode:" + hashCode() +
                '}';
    }

    public static String buildUrlByName(String name) {
        return Constant.BASE_URL + "/go/" + name;
    }

    public static String getNameFromUrl(String url) {
        final Matcher matcher = PATTERN.matcher(url);
        Preconditions.checkState(matcher.find(), "match name for node failed: " + url);
        return matcher.group(1);
    }

    @Override
    public String getUrl() {
        return buildUrlByName(getName());
    }

    @Override
    public int compareTo(Node another) {
        return COLLATOR.compare(getTitle(), another.getTitle());
    }

    @Override
    public boolean filter(String query) {
        if (Strings.isNullOrEmpty(query)) {
            return true;
        }
        if (mName.contains(query) || mTitle.contains(query)) {
            return true;
        }
        if (mTitleAlternative != null && mTitleAlternative.contains(query)) {
            return true;
        }
        return false;
    }

    public Builder toBuilder() {
        return new Builder()
                .setId(mId)
                .setTitle(mTitle)
                .setAvatar(mAvatar)
                .setName(mName)
                .setHeader(mHeader)
                .setFooter(mFooter)
                .setTitleAlternative(mTitleAlternative)
                .setTopics(mTopics);
    }

    public static class Builder {
        private static final Cache<String, Node> CACHE;
        static {
            CACHE = CacheBuilder.newBuilder()
                    .softValues()
                    .initialCapacity(32)
                    .maximumSize(128)
                    .build();
        }

        private int mId;
        private String mTitle;
        private Avatar mAvatar;
        private String mName;
        private String mHeader;
        private String mFooter;
        private String mTitleAlternative;
        private int mTopics;

        public Builder setId(int mId) {
            this.mId = mId;
            return this;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "set mTitle for node: " + this.mTitle);
            return this;
        }

        public Builder setAvatar(Avatar mAvatar) {
            this.mAvatar = mAvatar;
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "set mAvatar for node: " + this.mAvatar);
            return this;
        }

        public Builder setName(String mName) {
            this.mName = mName;
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "set mName for node: " + this.mName);
            return this;
        }

        public Builder setHeader(String mHeader) {
            this.mHeader = mHeader;
            return this;
        }

        public Builder setFooter(String mFooter) {
            this.mFooter = mFooter;
            return this;
        }

        public Builder setTitleAlternative(String mTitleAlternative) {
            this.mTitleAlternative = mTitleAlternative;
            return this;
        }

        public Builder setTopics(int mTopics) {
            this.mTopics = mTopics;
            if (BuildConfig.DEBUG)
                LogUtil.d(TAG, "set mTopics for node " + this.mTopics);
            return this;
        }

        public Node createNode(){
            if (mTitle == null) {
                return new Node(mId, null, mName, mHeader, mFooter, mTitleAlternative, mTopics, mAvatar);
            }
            try {
                if (BuildConfig.DEBUG)
                    LogUtil.d(TAG, "GET node " + mTitle + " from cache, before size: " + CACHE.size());

                final Node tmp = new Node(mId, mTitle, mName, mHeader, mFooter, mTitleAlternative, mTopics, mAvatar);
                Node tmp1 = CACHE.get(mName, new Callable<Node>() {
                    @Override
                    public Node call() throws Exception {
                        if (BuildConfig.DEBUG)
                            LogUtil.d(TAG, mName + " node is not exist in cache, new one!!!");
                        return tmp;
                    }
                });
                if (!tmp.equals(tmp1)) {
                    CACHE.put(mName, tmp);
                    if (BuildConfig.DEBUG)
                        LogUtil.d(TAG, "GET node " + mTitle + " from cache, after size: " + CACHE.size() + ", this tmp node:" + tmp);
                    return tmp;
                } else {
                    if (BuildConfig.DEBUG)
                        LogUtil.d(TAG, "GET node " + mTitle + " from cache, after size: " + CACHE.size() + ", this tmp1 node:" + tmp1);
                    return tmp1;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
                LogUtil.e(TAG, "Exception:" + e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mName);
        dest.writeString(this.mHeader);
        dest.writeString(this.mFooter);
        dest.writeString(this.mTitleAlternative);
        dest.writeInt(this.mTopics);
        dest.writeParcelable(this.mAvatar, 0);
        dest.writeByte(mHasInfo ? (byte) 1 : (byte) 0);
    }

    protected Node(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mName = in.readString();
        this.mHeader = in.readString();
        this.mFooter = in.readString();
        this.mTitleAlternative = in.readString();
        this.mTopics = in.readInt();
        this.mAvatar = in.readParcelable(Avatar.class.getClassLoader());
        this.mHasInfo = in.readByte() != 0;
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        public Node createFromParcel(Parcel source) {
            return new Node(source);
        }

        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}
