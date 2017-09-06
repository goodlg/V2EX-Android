package test.demo.gyniu.v2ex.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by sdduser on 17-9-6.
 */

public class BottomOptionBar extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "BottomOptionBar";
    private static final boolean DEBUG = LogUtil.LOGD;

    private final View mRootView;
    private final TextView mAllReply;

    private final Context mContext;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.all_reply) {
            Log.d(TAG, "all_reply");
            Toast.makeText(mContext, "all_reply", Toast.LENGTH_SHORT).show();
        }
    }

    public BottomOptionBar(Context context) {
        this(context, null);
    }

    public BottomOptionBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomOptionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mRootView = View.inflate(context, R.layout.topic_option_2_button, this);
        mAllReply = (TextView) mRootView.findViewById(R.id.all_reply);
    }

    public void setListener() {
        setOnClickListener(this);
    }
}
