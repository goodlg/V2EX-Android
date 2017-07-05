package test.demo.gyniu.v2ex.model;

import android.os.Parcel;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.text.Collator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.demo.gyniu.v2ex.Constant;
import test.demo.gyniu.v2ex.ExArrayAdapter;

/**
 * Created by gyniu on 17-5-29.
 */
public class Node extends Entity implements Comparable<Node>, ExArrayAdapter.Filterable {
    private static final Pattern PATTERN = Pattern.compile("/go/(.+?)(?:\\W|$)");
    private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);

    private final int mId;
    private final String mTitle;
    private final String mName;
    private final String mTitleAlternative;
    private final int mTopics;
    private final Avatar mAvatar;
    private final boolean mHasInfo;

    public Node(int mId, String mTitle, String mName, String mTitleAlternative, int mTopics, Avatar mAvatar) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(mName));
        this.mId = mId;
        this.mTitle = mTitle;
        this.mName = mName;
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
                Objects.equal(mTitle, node.mTitle) &&
                Objects.equal(mName, node.mName) &&
                Objects.equal(mTitleAlternative, node.mTitleAlternative) &&
                Objects.equal(mAvatar, node.mAvatar);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mName, mTitleAlternative, mTopics, mAvatar);
    }

    @Override
    public String toString() {
        return "Node{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mName='" + mName + '\'' +
                ", mTitleAlternative='" + mTitleAlternative + '\'' +
                ", mTopics=" + mTopics +
                ", mAvatar=" + mAvatar +
                ", mHasInfo=" + mHasInfo +
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
        private String mTitleAlternative;
        private int mTopics;

        public Builder setId(int mId) {
            this.mId = mId;
            return this;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setAvatar(Avatar mAvatar) {
            this.mAvatar = mAvatar;
            return this;
        }

        public Builder setName(String mName) {
            this.mName = mName;
            return this;
        }

        public Builder setTitleAlternative(String mTitleAlternative) {
            this.mTitleAlternative = mTitleAlternative;
            return this;
        }

        public Builder setTopics(int mTopics) {
            this.mTopics = mTopics;
            return this;
        }

        public Node createNode(){
            if (mTitle == null) {
                return new Node(mId, null, mName, mTitleAlternative, mTopics, mAvatar);
            }
            try {
                return CACHE.get(mName, () ->new Node(mId, mTitle, mName, mTitleAlternative, mTopics, mAvatar));
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mName);
        dest.writeString(this.mTitleAlternative);
        dest.writeInt(this.mTopics);
        dest.writeParcelable(this.mAvatar, 0);
        dest.writeByte(mHasInfo ? (byte) 1 : (byte) 0);
    }

    protected Node(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mName = in.readString();
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
