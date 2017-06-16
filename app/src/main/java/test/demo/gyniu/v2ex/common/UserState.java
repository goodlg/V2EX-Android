package test.demo.gyniu.v2ex.common;

import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.dao.ConfigDao;
import test.demo.gyniu.v2ex.eventbus.LoginEvent;
import test.demo.gyniu.v2ex.model.Avatar;

/**
 * Created by gyniu on 17-6-16.
 */
public class UserState {
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
        AppCtx.getEventBus().post(new LoginEvent(username));
    }

    public boolean isLoggedIn() {
        return mUsername != null;
    }

    public String getUsername() {
        return mUsername;
    }
}
