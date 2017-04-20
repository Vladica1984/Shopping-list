package com.example.alen.shoppinglist.Database;

import android.content.ClipData;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.alen.shoppinglist.model.Items;
import com.example.alen.shoppinglist.model.MainList;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Alen on 18-Apr-17.
 */

public class ORMDataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "shopping.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Items, Integer> mItemsDao = null;
    private Dao<MainList, Integer> mMainListDao = null;

    public ORMDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Items.class);
            TableUtils.createTable(connectionSource, MainList.class);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Items.class, true);
            TableUtils.dropTable(connectionSource, MainList.class, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Items, Integer> getmItemsDao() throws SQLException {
        if (mItemsDao == null) {
            mItemsDao = getDao(Items.class);
        }
        return mItemsDao;
    }

    public Dao<MainList, Integer> getmMainListDao() throws SQLException {
        if (mMainListDao == null) {
            mMainListDao = getDao(MainList.class);
        }
        return mMainListDao;
    }

    @Override
    public void close() {
        mItemsDao = null;
        mMainListDao = null;
        super.close();
    }
}
