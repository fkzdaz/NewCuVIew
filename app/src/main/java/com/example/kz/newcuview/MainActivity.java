package com.example.kz.newcuview;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mView = new DrawingView(this);
        super.onCreate(savedInstanceState);
        setContentView(mView);

        Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.love);
        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.a);

        CustomBitmap c1 = new CustomBitmap(b1);
        CustomBitmap c2 = new CustomBitmap(b2);

        c1.setId(1);
        c2.setId(2);

        if (getSaveMatrix(1) != null) {
            c1.setMatrix(getSaveMatrix(1));
        }


        if (getSaveMatrix(2) != null) {
            c2.setMatrix(getSaveMatrix(2));
        }

        mView.addBitmap(c1);
        mView.addBitmap(c2);


    }

    private void saveMatrix(CustomBitmap cusBit) {

        SharedPreferences.Editor editor = getSharedPreferences("matrix", 1).edit();
        Matrix matrix = cusBit.matrix;
        float[] values = new float[9];
        matrix.getValues(values);
        JSONArray array = new JSONArray();
        for (float value : values) {
            try {
                array.put(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(String.valueOf(cusBit.getId()), array.toString());
        editor.commit();
    }

    private Matrix getSaveMatrix(int id) {
        SharedPreferences sp = getSharedPreferences("matrix", 1);
        String result = sp.getString(String.valueOf(id), null);
        if (result != null) {
            float[] values = new float[9];
            Matrix matrix = new Matrix();
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    values[i] = Float.valueOf(String.valueOf(array.getDouble(i)));
                }
                matrix.setValues(values);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return matrix;
        }
        return null;
    }

    @Override
    public void finish() {
        List<CustomBitmap> list = mView.getView();
        for (CustomBitmap cus : list) {
            saveMatrix(cus);
        }
        super.finish();
    }
}
