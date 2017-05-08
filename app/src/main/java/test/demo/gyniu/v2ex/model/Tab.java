package test.demo.gyniu.v2ex.model;

import android.os.Parcel;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.Constant;
import test.demo.gyniu.v2ex.R;

/**
 * Created by uiprj on 17-3-21.
 */
public class Tab extends Entity {
    private final int mTitleResId;
    private final int mKey;
    public static final ImmutableMap<Integer, Tab> ALL_TABS;
    private static final String SEPARATOR = ",";

    private static final int[][] TAB_DATA = {
            {0, R.string.str_tech},
            {1, R.string.str_creative},
            {2, R.string.str_play},
            {3, R.string.str_apple},
            {4, R.string.str_jobs},
            {5, R.string.str_deals},
            {6, R.string.str_city},
            {7, R.string.str_qna},
            {8, R.string.str_hot},
            {9, R.string.str_all},
            {10, R.string.str_r2}
    };

    static {
        final ImmutableMap.Builder<Integer, Tab> builder = ImmutableMap.builder();
        for (int[] data : TAB_DATA) {
            final int key = data[0];
            final int titleResId = data[1];
            final Tab tab = new Tab(key, titleResId);
            builder.put(key, tab);
        }
        ALL_TABS = builder.build();
    }

    public Tab(int key, int titleResId) {
        mTitleResId = titleResId;
        mKey = key;
    }

    private Tab(Parcel in) {
        mKey = in.readInt();
        mTitleResId = in.readInt();
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getKey() {
        return mKey;
    }

    public String getRealKey(int resId){
        switch(resId){
            case R.string.str_tech: return "tech";
            case R.string.str_creative: return "creative";
            case R.string.str_play: return "play";
            case R.string.str_apple: return "apple";
            case R.string.str_jobs: return "jobs";
            case R.string.str_deals: return "deals";
            case R.string.str_city: return "city";
            case R.string.str_qna: return "qna";
            case R.string.str_hot: return "hot";
            case R.string.str_all: return "all";
            case R.string.str_r2: return "r2";
            default:return null;
        }
    }

    public static List<Tab> getTabsToShow(String prefStr) {
        if (Strings.isNullOrEmpty(prefStr)) {
            return Lists.newArrayList(ALL_TABS.values());
        }

        final String[] keys = prefStr.split(SEPARATOR);
        final ArrayList<Tab> result = Lists.newArrayList();
        for (String key : keys) {
            result.add(ALL_TABS.get(Integer.getInteger(key)));
        }

        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mKey);
        dest.writeInt(mTitleResId);
    }

    @Override
    public String getUrl() {
        return Constant.BASE_URL + "/?tab=" + getRealKey(mTitleResId);
    }

    public static final Creator<Tab> CREATOR = new Creator<Tab>() {
        public Tab createFromParcel(Parcel source) {
            return new Tab(source);
        }

        public Tab[] newArray(int size) {
            return new Tab[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tab)) return false;
        Tab tab = (Tab) o;
        return (mTitleResId == tab.mTitleResId) && (mKey == tab.mKey);
    }

}
