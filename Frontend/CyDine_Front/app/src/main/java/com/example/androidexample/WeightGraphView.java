package com.example.androidexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class WeightGraphView extends View {

    private List<Float> weightData;

    public WeightGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWeightData(List<Float> weightData) {
        this.weightData = weightData;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (weightData == null || weightData.isEmpty()) return;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
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

        for (int i = 0; i < weightData.size(); i++) {
            float height = weightData.get(i) * 2; // Scale factor
            canvas.drawRect(startX, 250 - height, startX + barWidth, 250, paint);
            startX += barWidth + gap;
        }
    }
}
