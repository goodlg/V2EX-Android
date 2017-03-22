package test.demo.gyniu.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by uiprj on 17-3-21.
 */
public abstract class Entity implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
