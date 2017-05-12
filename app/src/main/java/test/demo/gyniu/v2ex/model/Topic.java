package test.demo.gyniu.v2ex.model;

import android.os.Parcel;

import com.google.common.base.Objects;

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
    private final int mReplies;
    private final Member mMember;
    private final String mReplyTime;

    public Topic(int mId, String mTitle, String mContent, int mReplies, Member mMember, String mReplyTime) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mReplies = mReplies;
        this.mMember = mMember;
        this.mReplyTime = mReplyTime;
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

    public int getReplies() {
        return mReplies;
    }

    public Member getMember() {
        return mMember;
    }

    public String getReplyTime() {
        return mReplyTime;
    }

    public static int getIdFromUrl(String url) {
        final Matcher matcher = PATTERN.matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("match id for topic failed: " + url);
        }
        final String idStr = matcher.group(1);
        return Integer.parseInt(idStr);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mContent);
        dest.writeInt(this.mReplies);
        dest.writeParcelable(this.mMember, 0);
        dest.writeString(this.mReplyTime);
    }

    @Override
    public String getUrl() {
        return null;
    }

    public Topic(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mContent = in.readString();
        this.mReplies = in.readInt();
        this.mMember = in.readParcelable(Member.class.getClassLoader());
        this.mReplyTime = in.readString();
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
        if (!(o instanceof Topic)) return false;
        Topic topic = (Topic) o;
        return Objects.equal(mId, topic.mId) &&
                Objects.equal(mReplies, topic.mReplies) &&
                Objects.equal(mContent, topic.mContent) &&
                Objects.equal(mReplyTime, topic.mReplyTime);
    }

    @Override
    public String toString() {
        return "id:" + mId
                + ", title:" + mTitle
                + ", content:" + mContent
                + ", reply:" + mReplies
                + ", member:" + mMember.getUserName()
                + ", rtime:" + mReplyTime;
    }

    public static class Builder{
        private int mId;
        private String mTitle;
        private String mContent;
        private int mReplies;
        private Member mMember;
        private String mReplyTime;

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

        public Builder setReplies(int mReplies) {
            this.mReplies = mReplies;
            return this;
        }

        public Builder setMember(Member mMember) {
            this.mMember = mMember;
            return this;
        }

        public Builder setReplyTime(String mReplyTime) {
            this.mReplyTime = mReplyTime;
            return this;
        }

        public Topic createTopic() {
            return new Topic(mId, mTitle, mContent, mReplies, mMember, mReplyTime);
        }
    }
}
