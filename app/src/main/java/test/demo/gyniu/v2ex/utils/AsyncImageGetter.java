package test.demo.gyniu.v2ex.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.common.base.Preconditions;

import test.demo.gyniu.v2ex.AppCtx;
import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.utils.Constant;
import test.demo.gyniu.v2ex.utils.Html;
import test.demo.gyniu.v2ex.utils.LogUtil;
import test.demo.gyniu.v2ex.utils.ViewUtils;

/**
 * Created by uiprj on 17-5-15.
 */
public class AsyncImageGetter implements Html.ImageGetter {
    private static final String TAG = "ViewUtils";
    private static final boolean DEBUG = LogUtil.LOGD;
    private final TextView mTextView;
    private final int mPixel;

    public AsyncImageGetter(TextView textView, int pixel) {
        mTextView = textView;
        mPixel = pixel;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (source.startsWith("//")) {
            source = "https:" + source;
        } else if (source.startsWith("/")) {
            source = Constant.BASE_URL + source;
        }
        if (DEBUG)
            LogUtil.d(TAG, "load image for text view:" + source);

        final NetworkDrawable drawable = new NetworkDrawable();
        final int width = ViewUtils.getExactlyWidth(mTextView, mPixel);
        final NetworkDrawableTarget target = new NetworkDrawableTarget(mTextView, drawable, width);
        Glide.with(mTextView.getContext()).load(source).asBitmap().fitCenter().into(target);

        return drawable;
    }

    private static class NetworkDrawable extends BitmapDrawable {
        private static final Drawable DRAWABLE_LOADING;
        private static final Drawable DRAWABLE_PROBLEM;
        private static final Rect DRAWABLE_BOUNDS;
        private static final Paint PAINT;

        private boolean mIsFailed;
        private Drawable mDrawable;

        static {
            DRAWABLE_LOADING = ContextCompat.getDrawable(AppCtx.getInstance(),
                    R.drawable.ic_sync_white_24dp);
            DRAWABLE_PROBLEM = ContextCompat.getDrawable(AppCtx.getInstance(),
                    R.drawable.ic_sync_problem_white_24dp);

            Preconditions.checkNotNull(DRAWABLE_LOADING);
            Preconditions.checkNotNull(DRAWABLE_PROBLEM);

            DRAWABLE_BOUNDS = new Rect(0, 0, DRAWABLE_PROBLEM.getIntrinsicWidth(),
                    DRAWABLE_PROBLEM.getIntrinsicHeight());
            DRAWABLE_LOADING.setBounds(DRAWABLE_BOUNDS);
            DRAWABLE_PROBLEM.setBounds(DRAWABLE_BOUNDS);

            PAINT = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
            PAINT.setColor(Color.LTGRAY);
        }

        @SuppressWarnings("deprecation")
        public NetworkDrawable() {
            super();
            setBounds(DRAWABLE_BOUNDS);
        }

        public void setDrawable(@NonNull Drawable drawable) {
            mDrawable = drawable;
            setBounds(mDrawable.getBounds());
        }

        public void setFailed() {
            mIsFailed = true;
        }

        @Override
        public void draw(Canvas canvas) {
            if (mDrawable != null) {
                mDrawable.draw(canvas);
                return;
            }

            canvas.drawRect(getBounds(), PAINT);
            if (mIsFailed) {
                DRAWABLE_PROBLEM.draw(canvas);
            } else {
                DRAWABLE_LOADING.draw(canvas);
            }
        }
    }

    private static class NetworkDrawableTarget extends SimpleTarget<Bitmap> {
        private final TextView mTextView;
        private final NetworkDrawable mDrawable;
        private final int mMaxWidth;

        private NetworkDrawableTarget(TextView textView, NetworkDrawable drawable, int maxWidth) {
            super(maxWidth, SIZE_ORIGINAL);
            mMaxWidth = maxWidth;
            mTextView = textView;
            mDrawable = drawable;
        }

        @Override
        public void onResourceReady(Bitmap bitmap,
                                    GlideAnimation<? super Bitmap> glideAnimation) {
            final BitmapDrawable bitmapDrawable = new BitmapDrawable(null, bitmap);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width < mMaxWidth) {
                float fitWidth = Math.min(ViewUtils.dp2Pixel(width), mMaxWidth);
                height *= fitWidth / width;
                width = (int) fitWidth;
            }

            bitmapDrawable.setBounds(0, 0, width, height);
            mDrawable.setDrawable(bitmapDrawable);
            mTextView.setText(mTextView.getText());
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            mDrawable.setFailed();
        }
    }
}