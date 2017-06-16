package test.demo.gyniu.v2ex.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by uiprj on 17-6-16.
 */
public abstract class SqlOperation<T> {
    public abstract T execute(SQLiteDatabase db);
}

