package test.demo.gyniu.v2ex.model;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by uiprj on 17-3-22.
 */
public class Topic extends Entity{
    private static final Pattern PATTERN = Pattern.compile("/t/(\\d+?)(?:\\W|$)");

    private final int mId;
    private final String mTitle;
    private final String mContent;
    private final int mCount;
    private final Member mMember;
    private final String mTime;
    private final boolean mHasInfo;
    @Nullable
    private final List<Postscript> mPostscripts;

    public Topic(int mId, String mTitle, String mContent, int mCount, Member mMember,
                 String mTime,@Nullable List<Postscript> postscripts) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mCount = mCount;
        this.mMember = mMember;
        this.mTime = mTime;
        this.mPostscripts = postscripts;

        mHasInfo = mMember != null;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public int getCount() {
        return mCount;
    }

    public Member getMember() {
        return mMember;
    }

    public String getTime() {
        return mTime;
    }

    public boolean hasInfo() {
        return mHasInfo;
    }

    public static int getIdFromUrl(String url) {
        final Matcher matcher = PATTERN.matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("match id for topic failed: " + url);
        }
        final String idStr = matcher.group(1);
        return Integer.parseInt(idStr);
    }

    @Nullable
    public List<Postscript> getPostscripts() {
        return mPostscripts;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mId);
        dest.writeByte(mHasInfo ? (byte) 1 : (byte) 0);
        dest.writeString(this.mTitle);
        dest.writeString(this.mContent);
        dest.writeInt(this.mCount);
        dest.writeParcelable(this.mMember, 0);
        dest.writeString(this.mTime);
    }

    @Override
    public String getUrl() {
        return null;
    }

    public Topic(Parcel in) {
        this.mId = in.readInt();
        this.mHasInfo = in.readByte() != 0;
        this.mTitle = in.readString();
        this.mContent = in.readString();
        this.mCount = in.readInt();
        this.mMember = in.readParcelable(Member.class.getClassLoader());
        this.mTime = in.readString();
        this.mPostscripts = null;
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return mId == topic.mId &&
                mCount == topic.mCount &&
                Objects.equal(mTitle, topic.mTitle) &&
                Objects.equal(mContent, topic.mContent) &&
                Objects.equal(mMember, topic.mMember) &&
                Objects.equal(mTime, topic.mTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mContent, mCount, mMember, mTime);
    }

    @Override
    public String toString() {
        return "id:" + mId
                + ", title:" + mTitle
                + ", content:" + mContent
                + ", count:" + mCount
                + ", member:" + mMember.getUserName()
                + ", time:" + mTime;
    }

    public static class Builder{
        private int mId;
        private String mTitle;
        private String mContent;
        private int mCount;
        private Member mMember;
        private String mTime;
        private List<Postscript> mPostscripts;

        public Builder setId(int mId) {
            this.mId = mId;
            return this;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setContent(String mContent) {
            this.mContent = mContent;
            return this;
        }

        public Builder setCount(int mCount) {
            this.mCount = mCount;
            return this;
        }

        public Builder setMember(Member mMember) {
            this.mMember = mMember;
            return this;
        }

        public Builder setTime(String mTime) {
            this.mTime = mTime;
            return this;
        }

        public Builder setPostscripts(List<Postscript> postscripts) {
            mPostscripts = postscripts;
            return this;
        }

        public Topic createTopic() {
            return new Topic(mId, mTitle, mContent, mCount, mMember, mTime, mPostscripts);
        }
    }
}
