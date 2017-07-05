package test.demo.gyniu.v2ex.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-6-16.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";
    private static final boolean DEBUG = LogUtil.LOGD;
    //DataBase
    public static final String DB_NAME = "v2ex.db";
    public static final int DB_VERSION = 1;//DB version

    private static final DbHelper INSTANCE;
    static {
        if (DEBUG) LogUtil.d(TAG, "INIT DB");
        INSTANCE = new DbHelper(AppCtx.getInstance());
    }

    public DbHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DbHelper getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) LogUtil.d(TAG, "Create db table");
        ConfigDao.createTable(db);
        UserDao.createTable(db);
        NodeDao.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     * may be take a lot time
     * @return
     */
    public boolean init() {
        try {
            getWritableDatabase();
            LogUtil.d(TAG, "DB exits");
            return true;
        } catch (Exception e) {
            LogUtil.e(TAG, "HAS Exception: " + e);
            e.printStackTrace();
            return false;
        }
    }
}
