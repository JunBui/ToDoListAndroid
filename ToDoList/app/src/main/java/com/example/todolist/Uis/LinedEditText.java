package com.example.todolist.Uis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

public class LinedEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Paint linePaint;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#000000"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2.5f);  // Line thickness
        int lineSpacingExtra = 20; // Additional spacing between lines
        setLineSpacing(lineSpacingExtra, 1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int lineHeight = getLineHeight();
        int totalLines = height / lineHeight;
        int leftMargin = 45;
        int rightMargin = getWidth() - 30;

        // Draw the lines
        for (int i = 0; i < totalLines; i++) {
            int y = (i + 1) * lineHeight;
            canvas.drawLine(leftMargin, y, rightMargin, y, linePaint);
        }

        super.onDraw(canvas); // Draw text and other elements
    }
}
