package test.demo.gyniu.v2ex.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by gyniu on 17-6-6.
 */
public class ListOptionView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "ListOptionView";
    private static final boolean DEBUG = LogUtil.LOGD;

    private final ImageView mLeftIcon;
    private final TextView mMidTitle;
    private final ImageView mRightIcon;

    private OnDoOptionListener mListener;

    public ListOptionView(Context context) {
        this(context, null);
    }

    public ListOptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListOptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.list_option_view, this);

        mLeftIcon = (ImageView) findViewById(R.id.left_icon);
        mMidTitle = (TextView) findViewById(R.id.mid_title);
        mRightIcon = (ImageView) findViewById(R.id.right_icon);

        TypedArray array = null;
        try {
            //type arrays
            array = context.obtainStyledAttributes(attrs, R.styleable.OptionView);
            //res
            Drawable left = array.getDrawable(R.styleable.OptionView_leftIcon);
            String title = array.getString(R.styleable.OptionView_text);
            Drawable right = array.getDrawable(R.styleable.OptionView_rightIcon);
            //set each view ui
            mLeftIcon.setImageDrawable(left);
            mMidTitle.setText(title);
            mRightIcon.setImageDrawable(right);
            if (DEBUG) LogUtil.d(TAG, "ListOptionView, title=" + title);
        } catch (Resources.NotFoundException e) {
            LogUtil.e(TAG, "HAS Exception: " + e);
            e.printStackTrace();
        } finally {
            if(array != null){
                array.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (DEBUG) LogUtil.d(TAG, "onFinishInflate");
    }

    public void setListener(@NonNull OnDoOptionListener listener) {
        mListener = listener;
        setOnClickListener(this);
    }

    public void initUI(Drawable icon1, CharSequence title, Drawable icon2) {
        mLeftIcon.setImageDrawable(icon1);
        mMidTitle.setText(title);
        mRightIcon.setImageDrawable(icon2);
    }

    @Override
    public void onClick(View v) {
        mListener.onClickOptionView(v);
    }

    public interface OnDoOptionListener {
        void onClickOptionView(View view);
    }
}
