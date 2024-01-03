package com.rt.common.util;

/**
 * 裁剪并且圆角的Transformation
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class CropRoundRadiusTransformation extends BitmapTransformation {

    public enum CropType {
        TOP(0),
        CENTER(1),
        BOTTOM(2);

        public int type;

        CropType(int i) {
            type = i;
        }
    }

    private int radius;
    private CropType mCropType;
    private static final String ID = CropRoundRadiusTransformation.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    public CropRoundRadiusTransformation(Context context, int radius) {
        this(context, radius, CropType.CENTER);
    }

    public CropRoundRadiusTransformation(Context context, int radius, CropType mCropType) {
        this.radius = radius;
        this.mCropType = mCropType;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) return null;
        Bitmap toReuse = pool.get(outWidth, outHeight, toTransform.getConfig() != null
                ? toTransform.getConfig() : Bitmap.Config.ARGB_8888);
        // From ImageView/Bitmap.createScaledBitmap.
        final float scale;
        float dx = 0, dy = 0;
        Matrix m = new Matrix();
        if (toTransform.getWidth() * outHeight > outWidth * toTransform.getHeight()) {
            scale = (float) outHeight / (float) toTransform.getHeight();
            dx = (outWidth - toTransform.getWidth() * scale) * 0.5f;
        } else {
            scale = (float) outWidth / (float) toTransform.getWidth();
            if (mCropType == CropType.TOP) {
                dy = 0f;
            } else if (mCropType == CropType.BOTTOM) {
                dy = outHeight - toTransform.getHeight() * scale;
            } else {
                dy = (outHeight - toTransform.getHeight() * scale) * 0.5f;
            }
        }

        m.setScale(scale, scale);
        m.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        final Bitmap result;
        if (toReuse != null) {
            result = toReuse;
        } else {
            result = Bitmap.createBitmap(outWidth, outHeight, toTransform.getConfig() != null ? toTransform.getConfig() : Bitmap.Config.ARGB_8888);
        }

        // We don't add or remove alpha, so keep the alpha setting of the Bitmap we were given.
        result.setHasAlpha(toTransform.hasAlpha());

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, m, paint);

        Bitmap roundB = pool.get(result.getWidth(), result.getHeight(),
                Bitmap.Config.ARGB_8888);
        if (roundB == null) {
            roundB = Bitmap.createBitmap(result.getWidth(), result.getHeight(),
                    Bitmap.Config.ARGB_8888);
        }
        Canvas roundCanvas = new Canvas(roundB);
        Paint shaderPaint = new Paint();
        shaderPaint.setShader(new BitmapShader(result, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP));
        shaderPaint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, canvas.getWidth(), canvas.getHeight());
        roundCanvas.drawRoundRect(rectF, radius, radius, shaderPaint);
        return roundB;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(mCropType.type).array());
        messageDigest.update(ByteBuffer.allocate(4).putInt(radius).array());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CropRoundRadiusTransformation) {
            CropRoundRadiusTransformation other = (CropRoundRadiusTransformation) obj;
            return mCropType.type == other.mCropType.type && radius == other.radius;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (ID + mCropType.type + radius).hashCode();
    }
}
