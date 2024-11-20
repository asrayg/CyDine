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
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);

        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(5);

        // Draw axes
        canvas.drawLine(50, 250, 50, 50, axisPaint); // Y-axis
        canvas.drawLine(50, 250, 350, 250, axisPaint); // X-axis

        // Draw line chart
        float gap = 40;
        float startX = 70;
        float previousX = startX;
        float previousY = 250 - weightData.get(0) * 2; // Scale factor

        for (int i = 1; i < weightData.size(); i++) {
            float currentX = startX + (i * gap);
            float currentY = 250 - weightData.get(i) * 2; // Scale factor
            canvas.drawLine(previousX, previousY, currentX, currentY, paint);
            previousX = currentX;
            previousY = currentY;
        }
    }
}
