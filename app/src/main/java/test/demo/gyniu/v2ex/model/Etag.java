package test.demo.gyniu.v2ex.model;

import com.google.common.base.Preconditions;

/**
 * Created by uiprj on 17-7-5.
 */
public class Etag {
    public final String mOldEtag;
    private String mNewEtag;

    public Etag(String etag) {
        mOldEtag = etag;
    }

    /**
     * @return etag is modified
     */
    public boolean setNewEtag(String etag) {
        Preconditions.checkNotNull(etag);
        mNewEtag = etag;
        return isModified();
    }

    public boolean isModified() {
        return !mNewEtag.equals(mOldEtag);
    }

    public String getNewEtag() {
        return mNewEtag;
    }
}