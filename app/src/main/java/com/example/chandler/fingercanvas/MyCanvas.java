package com.example.chandler.fingercanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Chandler on 7/22/2017.
 */

public class MyCanvas extends View {

    HashMap<Integer, Path> activePaths;

    Paint pathPaint;

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        activePaths = new HashMap<Integer, Path>();
        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(Color.BLACK);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(70);
    }

    public void addPath(int key, float x, float y) {

        Path path = new Path();
        path.moveTo(x, y);
        activePaths.put(key, path);
        invalidate();
    }

    public void updatePath(int key, float x, float y) {

        Path path = activePaths.get(key);
        if (path != null) {

            path.lineTo(x, y);
        }
        invalidate();
    }

    public void removePath(int key) {
        if (activePaths.containsKey(key)) {
            activePaths.remove(key);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Path path: activePaths.values()) {
            canvas.drawPath(path, pathPaint);

        }
    }
}
