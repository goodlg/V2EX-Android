package test.demo.gyniu.v2ex.common;

import android.widget.Toast;

import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.dao.ConfigDao;
import test.demo.gyniu.v2ex.eventbus.LoginEvent;
import test.demo.gyniu.v2ex.model.Avatar;
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

    private String mUsername;

    public void init() {
        AppCtx.getEventBus().register(this);
        mUsername = ConfigDao.get(ConfigDao.KEY_USERNAME, null);
    }

    public void login(String username, Avatar avatar) {
        ConfigDao.put(ConfigDao.KEY_AVATAR, avatar.getBaseUrl());
        ConfigDao.put(ConfigDao.KEY_USERNAME, username);

        mUsername = username;
        if (DEBUG) LogUtil.d(TAG, "post sign in event");
        AppCtx.getEventBus().post(new LoginEvent(username));
    }

    public void logout() {
        mUsername = null;
        ConfigDao.remove(ConfigDao.KEY_USERNAME);
        ConfigDao.remove(ConfigDao.KEY_AVATAR);

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
        return mUsername != null;
    }

    public String getUsername() {
        return mUsername;
    }
}
