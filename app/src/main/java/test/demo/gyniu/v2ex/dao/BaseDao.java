package test.demo.gyniu.v2ex.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by uiprj on 17-6-16.
 */
public abstract class BaseDao {
    protected static <T> T execute(SqlOperation<T> operation) {
        return execute(operation, false);
    }

    protected static synchronized <T> T execute(SqlOperation<T> operation, boolean isWrite) {
        SQLiteDatabase db = null;
        try {
            final DbHelper instance = DbHelper.getInstance();
            db = isWrite ? instance.getWritableDatabase() : instance.getReadableDatabase();
            return operation.execute(db);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}

