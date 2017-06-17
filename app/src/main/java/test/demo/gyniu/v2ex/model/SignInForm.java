package test.demo.gyniu.v2ex.model;

/**
 * Created by gyniu on 17-6-17.
 */
public class SignInForm {
    private final String mAccount;
    private final String mPasswd;
    private final String mOnceCode;

    public SignInForm(String mAccount, String mPasswd, String mOnceCode) {
        this.mAccount = mAccount;
        this.mPasswd = mPasswd;
        this.mOnceCode = mOnceCode;
    }

    public String getAccount() {
        return mAccount;
    }

    public String getPasswd() {
        return mPasswd;
    }

    public String getOnceCode() {
        return mOnceCode;
    }

    @Override
    public String toString() {
        return "SignInForm{" +
                "mAccount='" + mAccount + '\'' +
                ", mPasswd='" + mPasswd + '\'' +
                ", mOnceCode='" + mOnceCode + '\'' +
                '}';
    }
}
