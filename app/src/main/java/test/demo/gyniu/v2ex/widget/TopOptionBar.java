package test.demo.gyniu.v2ex.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by sdduser on 17-9-6.
 */

public class TopOptionBar extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "TopOptionBar";
    private static final boolean DEBUG = LogUtil.LOGD;

    private final View mRootView;
    private final TextView mAddToFav;
    private final TextView mShareToWechat;
    private final TextView mIgnore;
    private final TextView mThank;

    private final Context mContext;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        //Toast.makeText(mContext, "click " + resId, Toast.LENGTH_SHORT).show();
        if (resId == R.id.addToFav) {
            Log.d(TAG, "addToFav");
            Toast.makeText(mContext, "addToFav", Toast.LENGTH_SHORT).show();
        } else if (resId == R.id.shareToWechat) {
            Toast.makeText(mContext, "shareToWechat", Toast.LENGTH_SHORT).show();
        } else if (resId == R.id.ignore) {
            Toast.makeText(mContext, "ignore", Toast.LENGTH_SHORT).show();
        } else if (resId == R.id.thank) {
            Toast.makeText(mContext, "thank", Toast.LENGTH_SHORT).show();
        }
    }

    public TopOptionBar(Context context) {
        this(context, null);
    }

    public TopOptionBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopOptionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mRootView = View.inflate(context, R.layout.topic_option_1_button, this);
        mAddToFav = (TextView) mRootView.findViewById(R.id.addToFav);
        mShareToWechat = (TextView) mRootView.findViewById(R.id.shareToWechat);
        mIgnore = (TextView) mRootView.findViewById(R.id.ignore);
        mThank = (TextView) mRootView.findViewById(R.id.thank);
    }

    public void setListener() {
        //setOnClickListener(this);
        mAddToFav.setOnClickListener(this);
        mShareToWechat.setOnClickListener(this);
        mIgnore.setOnClickListener(this);
        mThank.setOnClickListener(this);
    }
}
