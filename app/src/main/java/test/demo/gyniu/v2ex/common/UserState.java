package test.demo.gyniu.v2ex.common;

import android.widget.Toast;
import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.dao.UserDao;
import test.demo.gyniu.v2ex.eventbus.LoginEvent;
import test.demo.gyniu.v2ex.model.UserProfile;
import test.demo.gyniu.v2ex.utils.ExecutorUtils;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by gyniu on 17-6-16.
 */
public class UserState {
    private static final String TAG = "UserState";
    private static final boolean DEBUG = LogUtil.LOGD;

    private static final UserState instance;

    static {
        instance = new UserState();
    }

    public static UserState getInstance() {
        return instance;
    }

    private UserProfile mProfile = null;

    public void init() {
        AppCtx.getEventBus().register(this);
        mProfile = UserDao.get(UserDao.KEY_USERNAME);
    }

    public void login(UserProfile profile) {
        UserDao.put(UserDao.KEY_USERNAME, profile);
        mProfile = profile;
        if (DEBUG) LogUtil.d(TAG, "post sign in event");
        AppCtx.getEventBus().post(new LoginEvent(profile.getAccount()));
    }

    public void logout() {
        mProfile = null;
        UserDao.remove(UserDao.KEY_USERNAME);

        ExecutorUtils.runInUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AppCtx.getInstance(), R.string.toast_has_sign_out,
                        Toast.LENGTH_LONG).show();
            }
        });
        if (DEBUG) LogUtil.d(TAG, "post logout event");
        AppCtx.getEventBus().post(new LoginEvent());
    }

    public boolean isLoggedIn() {
        return mProfile != null;
    }

    public UserProfile getProfile() {
        return mProfile;
    }
}
