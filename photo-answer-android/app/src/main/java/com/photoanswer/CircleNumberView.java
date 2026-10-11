package com.photoanswer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/** 圆形题号按钮，选中时填充品牌色。 */
public class CircleNumberView extends View {

    private String text = "1";
    private boolean checked;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleNumberView(Context c, @Nullable AttributeSet a) {
        super(c, a);
        setFocusable(true);
        setClickable(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float size = Math.min(getWidth(), getHeight());
        if (size <= 0) return;
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float r = size / 2f - getResources().getDisplayMetrics().density;

        if (checked) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFF2F6BFF);
            canvas.drawCircle(cx, cy, r, paint);
            textPaint.setColor(0xFFFFFFFF);
            textPaint.setFakeBoldText(true);
        } else {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFFF1F3F8);
            canvas.drawCircle(cx, cy, r, paint);
            textPaint.setColor(0xFF767C88);
            textPaint.setFakeBoldText(false);
        }

        textPaint.setTextSize(size * 0.42f);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float baseline = cy - (fm.ascent + fm.descent) / 2f;
        canvas.drawText(text, cx, baseline, textPaint);
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int size = (int) (38 * getResources().getDisplayMetrics().density + 0.5f);
        setMeasuredDimension(size, size);
    }
}
