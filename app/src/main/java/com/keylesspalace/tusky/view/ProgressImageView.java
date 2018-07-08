/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Tusky.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tusky is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tusky; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.tusky.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.keylesspalace.tusky.R;
import at.connyduck.sparkbutton.helpers.Utils;

public final class ProgressImageView extends AppCompatImageView {

    private int progress = -1;
    private RectF progressRect = new RectF();
    private RectF biggerRect = new RectF();
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint markBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ProgressImageView(Context context) {
        super(context);
        init();
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        circlePaint.setStrokeWidth(Utils.dpToPx(getContext(), 4));
        circlePaint.setStyle(Paint.Style.STROKE);

        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        markPaint.setColor(ContextCompat.getColor(getContext(), R.color.md_white_1000));
        markPaint.setStyle(Paint.Style.STROKE);
        markPaint.setTextSize(Utils.dpToPx(getContext(), 16));
        markPaint.setStrokeCap(Paint.Cap.ROUND);

        markBgPaint.setStyle(Paint.Style.FILL);
        markBgPaint.setColor(ContextCompat.getColor(getContext(),
                R.color.description_marker_unselected));
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress != -1) {
            setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
        } else {
            clearColorFilter();
        }
        invalidate();
    }

    public void setChecked(boolean checked) {
        this.markBgPaint.setColor(ContextCompat.getColor(getContext(),
                checked ? R.color.colorPrimary : R.color.description_marker_unselected));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float angle = (progress / 100f) * 360 - 90;
        float halfWidth = canvas.getWidth() / 2;
        float halfHeight = canvas.getHeight() / 2;
        progressRect.set(halfWidth * 0.75f, halfHeight * 0.75f, halfWidth * 1.25f, halfHeight * 1.25f);
        biggerRect.set(progressRect);
        int margin = 8;
        biggerRect.set(progressRect.left - margin, progressRect.top - margin, progressRect.right + margin, progressRect.bottom + margin);
        canvas.saveLayer(biggerRect, null, Canvas.ALL_SAVE_FLAG);
        if (progress != -1) {
            canvas.drawOval(progressRect, circlePaint);
            canvas.drawArc(biggerRect, angle, 360 - angle - 90, true, clearPaint);
        }
        canvas.restore();

        int markWidth = Utils.dpToPx(getContext(), 14);
        int circleMargin = Utils.dpToPx(getContext(), 14);

        int circleY = canvas.getHeight() - circleMargin - markWidth / 2;
        int circleX = canvas.getWidth() - circleMargin - markWidth / 2;

        canvas.drawCircle(circleX, circleY, markWidth, markBgPaint);

        canvas.drawText("A"
                , circleX - markPaint.measureText("A") / 2,
                circleY + markPaint.getTextSize() / 2.6f,
                markPaint);
    }
}
