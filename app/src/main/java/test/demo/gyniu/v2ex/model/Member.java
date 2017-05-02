package test.demo.gyniu.v2ex.model;

import android.os.Parcel;

/**
 * Created by uiprj on 17-3-22.
 */
public class Member extends Entity{
    private String mUserName;

    public Member(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mUserName);
    }

    @Override
    public String getUrl() {
        return null;
    }

    private Member(Parcel in) {
        this.mUserName = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
