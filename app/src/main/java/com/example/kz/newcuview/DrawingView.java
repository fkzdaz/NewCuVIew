package com.example.kz.newcuview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 方坤镇 on 17-6-6.
 */

public class DrawingView extends View {

    private List<CustomBitmap> mbitmap;
    private Context mContext;
    private CustomBitmap mCustomBitmap;

    private Matrix currentMatrix = new Matrix();


    private enum MODE {
        NONE, DRAG, ZOOM
        //模式   NONE：无 DRAG：拖拽. ZOOM:缩放
    }

    private MODE mode = MODE.NONE;//默认模式

    public DrawingView(Context context) {
        super(context);
        this.mContext = context;
        mbitmap = new ArrayList<>();
    }

    public void addBitmap(CustomBitmap bitmap) {
        mbitmap.add(bitmap);
    }

    public List<CustomBitmap> getView() {
        return mbitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        for (CustomBitmap bitmap : mbitmap) {
            canvas.drawBitmap(bitmap.getBitmap(), bitmap.matrix, paint);
        }
    }

    //计算两点之间的距离

    public float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getX(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    //计算两点之间的中间点

    public PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    //计算旋转点

    public float rotation(MotionEvent event) {
        double deX = (event.getX(0) - event.getX(1));
        double deY = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(deX, deY);
        return (float) Math.toDegrees(radians);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = MODE.DRAG;
                if (mCustomBitmap == null && mbitmap.size() > 0) {
                    mCustomBitmap = mbitmap.get(mbitmap.size() - 1);
                }
                boolean isChange = false;
                //判断当前的bitmap是否改变

                for (CustomBitmap bitmap : mbitmap) {
                    float[] value = new float[9];
                    bitmap.matrix.getValues(value);
                    float globalX = value[Matrix.MTRANS_X];
                    float globalY = value[Matrix.MTRANS_Y];
                    float width = value[Matrix.MSCALE_X] * bitmap.getBitmap().getWidth();
                    float height = value[Matrix.MSCALE_Y] * bitmap.getBitmap().getHeight();

                    Rect rect = new Rect((int) globalX, (int) globalY, (int) (globalX + width), (int) (globalY + height));
                    if (rect.contains((int) event.getX(), (int) event.getY())) {
                        mCustomBitmap = bitmap;
                        isChange = true;
                    }
                }

                //切换操作对象，只要把这个对象添加到栈底就行
                if (isChange) {
                    mbitmap.remove(mCustomBitmap);
                    mbitmap.add(mCustomBitmap);
                }

                // 记录ImageView当前的移动位置
                currentMatrix.set(mCustomBitmap.matrix);
                mCustomBitmap.matrix.set(currentMatrix);
                mCustomBitmap.startPoint.set(event.getX(), event.getY());
                postInvalidate();
                break;


            // 当屏幕上还有触点（手指），再有一个手指压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE.ZOOM;
                mCustomBitmap.oldRotation = rotation(event);
                mCustomBitmap.startDis = distance(event);
                if (mCustomBitmap.startDis > 10f) {
                    mCustomBitmap.midPoint = mid(event);

                    // 记录ImageView当前的缩放倍数
                    currentMatrix.set(mCustomBitmap.matrix);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == MODE.DRAG) {

                    // 得到在x轴的移动距离
                    float dx = event.getX() - mCustomBitmap.startPoint.x;
                    // 得到在y轴的移动距离
                    float dy = event.getY() - mCustomBitmap.startPoint.y;
                    // 在没有进行移动之前的位置基础上进行移动
                    mCustomBitmap.matrix.set(currentMatrix);
                    mCustomBitmap.matrix.postTranslate(dx, dy);
                } else if (mode == MODE.ZOOM) {// 缩放与旋转
                    float endDis = distance(event);// 结束距离
                    mCustomBitmap.rotation = rotation(event) - mCustomBitmap.oldRotation;
                    if (endDis > 10f) {
                        float scale = endDis / mCustomBitmap.startDis;// 得到缩放倍数
                        mCustomBitmap.matrix.set(currentMatrix);
                        mCustomBitmap.matrix.postScale(scale, scale, mCustomBitmap.midPoint.x, mCustomBitmap.midPoint.y);
                        mCustomBitmap.matrix.postRotate(mCustomBitmap.rotation, mCustomBitmap.midPoint.x, mCustomBitmap.midPoint.y);

                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

            // 有手指离开屏幕,但屏幕还有触点（手指）
            case MotionEvent.ACTION_POINTER_UP:
                mode = MODE.NONE;
                break;

        }
        invalidate();
        return true;
    }
}
