package test.demo.gyniu.v2ex.model;

/**
 * Created by gyniu on 17-6-17.
 */
public class LoginResult {
    public final String mAccount;
    public final Avatar mAvatar;

    public LoginResult(String username, Avatar avatar) {
        mAccount = username;
        mAvatar = avatar;
    }
}
