package test.demo.gyniu.v2ex.utils;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import test.demo.gyniu.v2ex.Tab;

/**
 * Created by uiprj on 17-3-21.
 */
public class SharedPreferencesHelper implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static SharedPreferencesHelper instance;
    private static Context mContext;
    private final SharedPreferences mPreferences;

    public static final String PREF_TABS_TO_SHOW = "tabs_to_show";

    public static SharedPreferencesHelper getInstance(Context context) {
        if (instance == null){
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    SharedPreferencesHelper(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public List<Tab> getTabsToShow() {
        final String string = mPreferences.getString(PREF_TABS_TO_SHOW, null);
        return Tab.getTabsToShow(string);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        requestBackup();
    }

    public static void requestBackup() {
        final BackupManager manager = new BackupManager(mContext);
        manager.dataChanged();
    }
}
