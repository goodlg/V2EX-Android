package test.demo.gyniu.v2ex.eventbus;

/**
 * Created by gyniu on 17-6-16.
 */
public class LoginEvent extends BaseEvent {
    public final String mUsername;

    public LoginEvent() {
        mUsername = null;
    }

    public LoginEvent(String username) {
        mUsername = username;
    }

    public boolean isLogOut() {
        return mUsername == null;
    }
}

