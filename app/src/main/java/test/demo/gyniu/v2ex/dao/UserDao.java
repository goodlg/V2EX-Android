package test.demo.gyniu.v2ex.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.LruCache;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.UserProfile;

/**
 * Created by uiprj on 17-6-16.
 */
public class UserDao extends BaseDao {
    private static final String TABLE_NAME = "user";

    private static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_NODES = "nodes";
    private static final String KEY_TOPICS = "topics";
    private static final String KEY_FOLLOWINGS = "followings";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_SILVERS = "silver";
    private static final String KEY_BRONZES = "bronze";

    private static final String[] SCHEMA = {
            KEY_ID,
            KEY_USERNAME,
            KEY_AVATAR,
            KEY_NODES,
            KEY_TOPICS,
            KEY_FOLLOWINGS,
            KEY_NOTIFICATIONS,
            KEY_SILVERS,
            KEY_BRONZES
    };

    private static final String SQL_GET_ALL = SQLiteQueryBuilder.buildQueryString(false,
            TABLE_NAME, SCHEMA, null, null, null, null, null);

    private static final LruCache<String, UserProfile> CACHE = new LruCache<>(1);

    static void createTable(SQLiteDatabase db) {
        Preconditions.checkState(db.inTransaction(), "create table must be in transaction");

        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT UNIQUE NOT NULL,"
                + KEY_AVATAR + " TEXT,"
                + KEY_NODES + " INTEGER,"
                + KEY_TOPICS + " INTEGER,"
                + KEY_FOLLOWINGS + " INTEGER,"
                + KEY_NOTIFICATIONS + " INTEGER,"
                + KEY_SILVERS + " INTEGER,"
                + KEY_BRONZES + " INTEGER" + ")";
        db.execSQL(sql);

        sql = String.format("CREATE UNIQUE INDEX %1$s_%2$s ON %1$s(%2$s)", TABLE_NAME, KEY_USERNAME);
        db.execSQL(sql);
    }

    public static UserProfile get(final String key) {
        UserProfile profile = CACHE.get(key);
        if (profile != null) {
            return profile;
        }
        return execute(new SqlOperation<UserProfile>() {
            @Override
            public UserProfile execute (SQLiteDatabase db){
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(SQL_GET_ALL, null);
                    if (!cursor.moveToFirst()) {return null;}
                    final UserProfile.Builder uProfile = new UserProfile.Builder();
                    String username = cursor.getString(1);
                    String avatar = cursor.getString(2);
                    int nodes = cursor.getInt(3);
                    int topics = cursor.getInt(4);
                    int followings = cursor.getInt(5);
                    int notifications = cursor.getInt(6);
                    int silver = cursor.getInt(7);
                    int bronze = cursor.getInt(8);
                    uProfile.setAccount(username);
                    uProfile.setAvatar(new Avatar.Builder().setUrl(avatar).createAvatar());
                    uProfile.setNodesCount(nodes);
                    uProfile.setTopicsCount(topics);
                    uProfile.setFollowings(followings);
                    uProfile.setRemind(notifications);
                    uProfile.setSilver(silver);
                    uProfile.setBronze(bronze);
                    UserProfile profile = uProfile.createUserProfile();
                    CACHE.put(key, profile);
                    return profile;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }, false);
    }

    public static void put(final String key, final UserProfile profile) {
        execute(new SqlOperation<Void>() {
            @Override
            public Void execute(SQLiteDatabase db) {
                final ContentValues values = new ContentValues(8);
                values.put(KEY_USERNAME, profile.getAccount());
                values.put(KEY_AVATAR, profile.getAvatar().getBaseUrl());
                values.put(KEY_NODES, profile.getNodesCount());
                values.put(KEY_TOPICS, profile.getTopicsCount());
                values.put(KEY_FOLLOWINGS, profile.getFollowings());
                values.put(KEY_NOTIFICATIONS, profile.getRemind());
                values.put(KEY_SILVERS, profile.getSilver());
                values.put(KEY_BRONZES, profile.getBronze());
                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                CACHE.put(key, profile);
                return null;
            }
        }, true);
    }

    public static void remove(final String key) {
        execute(new SqlOperation<Void>() {
            @Override
            public Void execute(SQLiteDatabase db) {
                db.delete(TABLE_NAME, KEY_USERNAME + " = ?", new String[]{key});
                return null;
            }
        }, true);
    }
}
