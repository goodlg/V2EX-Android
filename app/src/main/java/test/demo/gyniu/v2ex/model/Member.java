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

    public Member(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserName() {
        return mUserName;
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

    public static class Builder {
        private String mUserName;

        public void setUserName(String mUserName) {
            this.mUserName = mUserName;
        }

        public Member createMember(){
            return new Member(mUserName);
        }
    }
}
