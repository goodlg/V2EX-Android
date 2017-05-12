package test.demo.gyniu.v2ex.model;

import android.os.Parcel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by uiprj on 17-3-22.
 */
public class Member extends Entity{
    private static final Pattern PATTERN = Pattern.compile("/member/(.+?)(?:\\W|$)");

    private final String mUserName;
    private Avatar mAvatar;

    public Member(String mUserName, Avatar avatar) {
        this.mUserName = mUserName;
        this.mAvatar = avatar;
    }

    public String getUserName() {
        return mUserName;
    }

    public Avatar getAvatar() {
        return mAvatar;
    }

    public static String getNameFromUrl(String url) {
        final Matcher matcher = PATTERN.matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("match name for member failed: " + url);
        }
        return matcher.group(1);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mUserName);
        dest.writeParcelable(this.mAvatar, 0);
    }

    @Override
    public String getUrl() {
        return null;
    }

    private Member(Parcel in) {
        this.mUserName = in.readString();
        this.mAvatar = in.readParcelable(Avatar.class.getClassLoader());
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public static class Builder {
        private String mUserName;
        private Avatar mAvatar;

        public Builder setUserName(String mUserName) {
            this.mUserName = mUserName;
            return this;
        }

        public Builder setAvatar(Avatar avatar) {
            this.mAvatar = avatar;
            return this;
        }

        public Member createMember(){
            return new Member(mUserName, mAvatar);
        }
    }
}
