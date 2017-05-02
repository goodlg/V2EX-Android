package test.demo.gyniu.v2ex.model;

import android.os.Parcel;

import com.google.common.base.Objects;

/**
 * Created by uiprj on 17-3-22.
 */
public class Topic extends Entity{
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

    public int getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public int getmReplies() {
        return mReplies;
    }

    public Member getmMember() {
        return mMember;
    }

    public String getmReplyTime() {
        return mReplyTime;
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
        return "id:" + mId + ", title:" + mTitle
                + ", content:" + mContent + ", reply:" + mReplies
                + ", rtime:" + mReplyTime;
    }
}
