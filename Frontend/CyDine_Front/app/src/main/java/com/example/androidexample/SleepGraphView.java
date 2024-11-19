package com.example.androidexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class SleepGraphView extends View {

    private List<Float> sleepData;

    public SleepGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSleepData(List<Float> sleepData) {
        this.sleepData = sleepData;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (sleepData == null || sleepData.isEmpty()) return;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(5);

        // Draw axes
        canvas.drawLine(50, 250, 50, 50, axisPaint); // Y-axis
        canvas.drawLine(50, 250, 350, 250, axisPaint); // X-axis

        float barWidth = 30;
        float gap = 20;
        float startX = 70;

        for (int i = 0; i < sleepData.size(); i++) {
            float height = sleepData.get(i) * 20; // Scale factor
            canvas.drawRect(startX, 250 - height, startX + barWidth, 250, paint);
            startX += barWidth + gap;
        }
    }
}
