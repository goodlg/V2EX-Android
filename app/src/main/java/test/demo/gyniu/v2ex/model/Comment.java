package test.demo.gyniu.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

/**
 * Created by uiprj on 17-5-15.
 */
public class Comment implements Parcelable {
    private final int mId;
    private final String mContent;
    private final Member mMember;
    private final String mReplyTime;
    private final int mFloor;

    public Comment(int mId, String mContent, Member mMember, String mReplyTime, int mFloor) {
        this.mId = mId;
        this.mContent = mContent;
        this.mMember = mMember;
        this.mReplyTime = mReplyTime;
        this.mFloor = mFloor;
    }

    public int getId() {
        return mId;
    }

    public String getContent() {
        return mContent;
    }

    public Member getMember() {
        return mMember;
    }

    public String getReplyTime() {
        return mReplyTime;
    }

    public int getFloor() {
        return mFloor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return mId == comment.mId &&
                mFloor == comment.mFloor &&
                Objects.equal(mContent, comment.mContent) &&
                Objects.equal(mMember, comment.mMember) &&
                Objects.equal(mReplyTime, comment.mReplyTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mContent, mMember, mReplyTime, mFloor);
    }

    public static class Builder {
        private int mId;
        private String mContent;
        private Member mMember;
        private String mReplyTime;
        private int mFloor;

        public Builder setId(int mId) {
            this.mId = mId;
            return this;
        }

        public Builder setContent(String mContent) {
            this.mContent = mContent;
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

        public Builder setFloor(int mFloor) {
            this.mFloor = mFloor;
            return this;
        }

        public Comment createComment() {
            return new Comment(mId, mContent, mMember, mReplyTime, mFloor);
        }
    }

    protected Comment(Parcel in) {
        this.mId = in.readInt();
        this.mContent = in.readString();
        this.mMember = in.readParcelable(Member.class.getClassLoader());
        this.mReplyTime = in.readString();
        this.mFloor = in.readInt();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.mId);
        dest.writeString(this.mContent);
        dest.writeParcelable(this.mMember, 0);
        dest.writeString(this.mReplyTime);
        dest.writeInt(this.mFloor);
    }
}
