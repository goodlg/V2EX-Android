package test.demo.gyniu.v2ex.model;

/**
 * Created by gyniu on 17-6-17.
 */
public class UserProfile {
    private final String mAccount;
    private final Avatar mAvatar;
    private final int mNodesCount;
    private final int mTopicsCount;
    private final int mFollowings;
    private final int mRemind;
    private final int mSilver;
    private final int mBronze;

    public UserProfile(String mAccount, Avatar mAvatar, int mNodesCount, int mTopicsCount,
                       int mFollowings, int mRemind, int mSilver, int mBronze) {
        this.mAccount = mAccount;
        this.mAvatar = mAvatar;
        this.mNodesCount = mNodesCount;
        this.mTopicsCount = mTopicsCount;
        this.mFollowings = mFollowings;
        this.mRemind = mRemind;
        this.mSilver = mSilver;
        this.mBronze = mBronze;
    }

    public String getAccount() {
        return mAccount;
    }

    public Avatar getAvatar() {
        return mAvatar;
    }

    public int getNodesCount() {
        return mNodesCount;
    }

    public int getTopicsCount() {
        return mTopicsCount;
    }

    public int getFollowings() {
        return mFollowings;
    }

    public int getRemind() {
        return mRemind;
    }

    public int getSilver() {
        return mSilver;
    }

    public int getBronze() {
        return mBronze;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "mAccount='" + mAccount + '\'' +
                ", mAvatar=" + mAvatar +
                ", mNodesCount=" + mNodesCount +
                ", mTopicsCount=" + mTopicsCount +
                ", mFollowings=" + mFollowings +
                ", mRemind=" + mRemind +
                ", mSilver=" + mSilver +
                ", mBronze=" + mBronze +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .setAccount(mAccount)
                .setAvatar(mAvatar)
                .setNodesCount(mNodesCount)
                .setTopicsCount(mTopicsCount)
                .setFollowings(mFollowings)
                .setRemind(mRemind)
                .setSilver(mSilver)
                .setBronze(mBronze);
    }

    public static class Builder{
        private String mAccount;
        private Avatar mAvatar;
        private int mNodesCount;
        private int mTopicsCount;
        private int mFollowings;
        private int mRemind;
        private int mSilver;
        private int mBronze;

        public Builder setAccount(String mAccount) {
            this.mAccount = mAccount;
            return this;
        }

        public Builder setAvatar(Avatar mAvatar) {
            this.mAvatar = mAvatar;
            return this;
        }

        public Builder setNodesCount(int mNodesCount) {
            this.mNodesCount = mNodesCount;
            return this;
        }

        public Builder setTopicsCount(int mTopicsCount) {
            this.mTopicsCount = mTopicsCount;
            return this;
        }

        public Builder setFollowings(int mFollowings) {
            this.mFollowings = mFollowings;
            return this;
        }

        public Builder setRemind(int mRemind) {
            this.mRemind = mRemind;
            return this;
        }

        public Builder setSilver(int mSilver) {
            this.mSilver = mSilver;
            return this;
        }

        public Builder setBronze(int mBronze) {
            this.mBronze = mBronze;
            return this;
        }

        public UserProfile createUserProfile() {
            return new UserProfile(mAccount, mAvatar, mNodesCount, mTopicsCount, mFollowings,
                    mRemind, mSilver, mBronze);
        }
    }
}
