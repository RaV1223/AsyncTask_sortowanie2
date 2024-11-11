package com.example.asynctask_sortowanie2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {
    private int[] data;
    private Paint paint = new Paint();

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.rgb(11,71,39));
    }

    public void setData(int[] data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data != null) {
            float barWidth = (float) getWidth() / data.length;
            for (int i = 0; i < data.length; i++) {
                float barHeight = (float) data[i] / data.length * getHeight();
                canvas.drawRect(i * barWidth, getHeight() - barHeight, (i + 1) * barWidth, getHeight(), paint);
            }
        }
    }
}