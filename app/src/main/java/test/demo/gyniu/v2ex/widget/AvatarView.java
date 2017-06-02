package test.demo.gyniu.v2ex.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.Member;

/**
 * Created by uiprj on 17-5-10.
 */
public class AvatarView extends ImageView {
    private int mRealSize;
    private boolean mHasRealSize;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int getRealSize() {
        if (!mHasRealSize) {
            mRealSize = getLayoutParams().width - getPaddingTop() * 2 ;
            mHasRealSize = true;
        }
        return mRealSize;
    }

    public void setAvatar(Avatar avatar) {
        final int size = getRealSize();
        final String url = avatar.getUrlByPx(size);
        Glide.with(getContext()).load(url).placeholder(R.drawable.ic_default_avatar)
                .override(size, size).crossFade().fitCenter().into(this);
    }

    public interface OnAvatarActionListener {
        void onMemberClick(Member member);
    }

}
