package test.demo.gyniu.v2ex.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.utils.GsonFactory;

/**
 * Created by uiprj on 17-7-5.
 */
public class NodeDao extends BaseDao {
    private static final String TABLE_NAME = "nodes";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TITLE = "title";
    private static final String KEY_HEADER = "header";
    private static final String KEY_FOOTER = "footer";
    private static final String KEY_TOPICS = "topics";

    // meaning alternative
    private static final String KEY_ALT = "alt";
    private static final String KEY_AVATAR = "avatar";

    private static final String[] SCHEMA = {KEY_ID, KEY_NAME, KEY_TITLE, KEY_HEADER, KEY_FOOTER, KEY_TOPICS, KEY_ALT, KEY_AVATAR};

    private static final String SQL_GET_BY_NAME = SQLiteQueryBuilder.buildQueryString(false,
            TABLE_NAME, SCHEMA, KEY_NAME + " = ?", null, null, null ,null);
    private static final String SQL_GET_ALL = SQLiteQueryBuilder.buildQueryString(false,
            TABLE_NAME, SCHEMA, null, null, null, null, null);

    private static final LruCache<String, Node> CACHE = new LruCache<>(16);

    static void createTable(SQLiteDatabase db) {
        Preconditions.checkState(db.inTransaction(), "create table must be in transaction");

        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT UNIQUE NOT NULL," +
                KEY_TITLE + " TEXT NOT NULL," +
                KEY_HEADER + " TEXT," +
                KEY_FOOTER + " TEXT," +
                KEY_TOPICS + " INTEGER," +
                KEY_ALT + " TEXT," +
                KEY_AVATAR + " TEXT" +
                ")";
        db.execSQL(sql);

        sql = String.format("CREATE UNIQUE INDEX %1$s_%2$s ON %1$s(%2$s)", TABLE_NAME, KEY_NAME);
        db.execSQL(sql);
    }

    private static void insert(SQLiteDatabase db, Node node) {
        final ContentValues values = new ContentValues(5);
        values.put(KEY_ID, node.getId());
        values.put(KEY_NAME, node.getName());
        values.put(KEY_TITLE, node.getTitle());
        values.put(KEY_HEADER, node.getHeader());
        values.put(KEY_FOOTER, node.getFooter());
        values.put(KEY_TOPICS, node.getTopics());
        values.put(KEY_ALT, node.getTitleAlternative());
        final Avatar avatar = node.getAvatar();
        if (avatar != null) {
            values.put(KEY_AVATAR, avatar.getBaseUrl());
        }
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void updateAvatar(final String name, final String url) {
        execute(new SqlOperation<Void>() {
            @Override
            public Void execute(SQLiteDatabase db) {
                final ContentValues values = new ContentValues(1);
                values.put(KEY_AVATAR, url);
                db.update(TABLE_NAME, values, KEY_NAME + " = ?", new String[]{name});
                CACHE.remove(name);
                return null;
            }
        }, true);
    }

    private static Node buildNode(Cursor cursor) {
        Node.Builder builder = new Node.Builder();
        builder.setId(cursor.getInt(0))//id
                .setName(cursor.getString(1))//name
                .setTitle(cursor.getString(2))//title
                .setHeader(cursor.getString(3))//header
                .setFooter(cursor.getString(4))//footer
                .setTopics(cursor.getInt(5));//topics
        String str = cursor.getString(6);//alternative
        if (str != null) {
            builder.setTitleAlternative(str);
        }
        str = cursor.getString(7);//avatar
        if (str != null) {
            final Avatar avatar = new Avatar.Builder().setUrl(str).createAvatar();
            builder.setAvatar(avatar);
        }
        return builder.createNode();
    }

    public static void saveNode(final Node node) {
        execute(new SqlOperation<Void>() {
            @Override
            public Void execute(SQLiteDatabase db) {
                insert(db, node);
                return null;
            }
        }, true);
    }

    @Nullable
    public static Node getNode(final String name) {
        Node node = CACHE.get(name);
        if (node != null) {
            return node;
        }
        return execute(new SqlOperation<Node>() {
            @Override
            public Node execute(SQLiteDatabase db) {
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(SQL_GET_BY_NAME, new String[]{name});
                    if (!cursor.moveToFirst()) {
                        return null;
                    }
                    Node node = buildNode(cursor);
                    CACHE.put(name, node);
                    return node;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }, false);
    }

    public static void saveAllNodes(final Iterable<Node> nodes) {
        execute(new SqlOperation<Void>() {
            @Override
            public Void execute(SQLiteDatabase db) {
                db.beginTransaction();
                try {
                    for (Node node : nodes) {
                        insert(db, node);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return null;
            }
        }, true);
    }

    public static List<Node> getAllNodes() {
        return execute(new SqlOperation<List<Node>>() {
            @Override
            public List<Node> execute(SQLiteDatabase db) {
                List<Node> result = Lists.newArrayList();
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(SQL_GET_ALL, null);
                    while (cursor.moveToNext()) {
                        final Node node = buildNode(cursor);
                        result.add(node);
                    }
                    return result;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }, false);
    }
}
