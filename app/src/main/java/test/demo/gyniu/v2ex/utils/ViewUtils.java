package test.demo.gyniu.v2ex.utils;

import android.support.annotation.DimenRes;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.AsyncImageGetter;

/**
 * Created by uiprj on 17-5-15.
 */
public class ViewUtils {
    private static final String TAG = "ViewUtils";
    private static final boolean DEBUG = LogUtil.LOGD;
    public static final float density;
    static {
        final AppCtx context = AppCtx.getInstance();
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        density = displayMetrics.density;
    }

    public static void setHtmlIntoTextView(TextView view, String html, int maxWidthPixels, boolean isTopic) {
        setHtmlIntoTextView(view, html, new AsyncImageGetter(view, maxWidthPixels), isTopic);
    }

    private static void setHtmlIntoTextView(TextView view, String html, AsyncImageGetter imageGetter, boolean isTopic) {
        final Spanned spanned = Html.fromHtml(html, imageGetter);
        final SpannableStringBuilder builder = (SpannableStringBuilder) spanned;

        if (isTopic) {
            final int length = builder.length();
            if (length > 2) {
                final CharSequence subSequence = builder.subSequence(length - 2, length);
                if (TextUtils.equals(subSequence, "\n\n")) {
                    builder.delete(length - 2, length);
                }
            }
        }
        view.setText(builder);
    }

    public static float dp2Pixel(float dp) {
        return density * dp;
    }

    public static int getExactlyWidth(View view, int maxWidth) {
        int width = view.getWidth();
        if (width <= 0) {
            view.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.UNSPECIFIED);
            width = view.getMeasuredWidth();

            if (width <= 0) {
                width = maxWidth;
            }
        }

        if (DEBUG)
            LogUtil.d(TAG, "get exactly width, result:" + width + "view=" + view);
        return width;
    }

    public static int getWidthPixels() {
        return AppCtx.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDimensionPixelSize(@DimenRes int id) {
        return AppCtx.getInstance().getResources().getDimensionPixelSize(id);
    }
}
