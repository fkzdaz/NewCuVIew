package com.example.kz.newcuview;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;

/**
 * Created by 方坤镇 on 17-6-6.
 * 定制一个Bitmap
 */

public class CustomBitmap {

    private int id;
    public float startDis;
    public PointF midPoint;
    public float oldRotation = 0;
    public float rotation = 0;
    public PointF startPoint = new PointF();
    public Matrix matrix = new Matrix();
    public Bitmap bitmap;

    public CustomBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getStartDis() {
        return startDis;
    }

    public void setStartDis(float startDis) {
        this.startDis = startDis;
    }

    public PointF getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(PointF midPoint) {
        this.midPoint = midPoint;
    }

    public float getOldRotation() {
        return oldRotation;
    }

    public void setOldRotation(float oldRotation) {
        this.oldRotation = oldRotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public PointF getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(PointF startPoint) {
        this.startPoint = startPoint;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
