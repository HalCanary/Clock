package org.skia.clock;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 *  /class CoolClock displays time (and maybe date).
 *
 *  TODO(you): make this clock cool.
 */
public class CoolClock extends View {
    private Paint fPaint;
    private Date fDate;
    private Calendar fCalendar;
    private Path fSecondPath;
    private Path fHourPath;
    private Path fMinutePath;
    private Path fMarks;
    private Paint fStroke;

    private int fScale;

    private void setScale(int scale) {
        if (scale == fScale) {
            return;
        }
        fScale = scale;
        fSecondPath = new Path();
        fSecondPath.lineTo(0.0f, scale * -0.45f);

        fHourPath = new Path();
        fHourPath.lineTo(0.0f, scale * -0.25f);

        fMinutePath = new Path();
        fMinutePath.lineTo(0.0f, scale * -0.40f);

        fMarks = new Path();
        float r1 = scale * 0.45f;
        float r2 = scale * 0.5f;
        float r3 = scale * 0.47f;
        for (int i = 0; i < 12; ++i) {
            double angleRad = 0.52359877559829887307 * i;
            double c = Math.cos(angleRad);
            double s = Math.sin(angleRad);
            fMarks.moveTo((float) (r1 * c), (float) (r1 * s));
            fMarks.lineTo((float) (r2 * c), (float) (r2 * s));
            for (int j = 0; j < 5; ++j) {
                angleRad += .10471975511965977461;
                c = Math.cos(angleRad);
                s = Math.sin(angleRad);
                fMarks.moveTo((float)(r1 * c), (float)(r1 * s));
                fMarks.lineTo((float)(r3 * c), (float)(r3 * s));
            }
        }
    }
    public CoolClock(Context ctx) {
        super(ctx);
        fPaint = new Paint();
        fPaint.setAntiAlias(true);
        fPaint.setTextSize(120);
        fDate = new Date();
        fCalendar = Calendar.getInstance();

        fStroke = new Paint();
        fStroke.setAntiAlias(true);
        fStroke.setStyle(Paint.Style.STROKE);
        fStroke.setStrokeWidth(10.0f);
        fStroke.setStrokeCap(Paint.Cap.ROUND);
    }

    protected void onDraw(android.graphics.Canvas canvas) {
        fDate.setTime(System.currentTimeMillis());
        fCalendar.setTime(fDate);

        int day = fCalendar.get(Calendar.DAY_OF_MONTH);
        int month = 1 + fCalendar.get(Calendar.MONTH);
        int year = fCalendar.get(Calendar.YEAR);
        int millisecond = fCalendar.get(Calendar.MILLISECOND);

        int hour = fCalendar.get(Calendar.HOUR);
        int minute = fCalendar.get(Calendar.MINUTE);
        int second = fCalendar.get(Calendar.SECOND);
        //boolean is_pm = Calendar.PM == fCalendar.get(Calendar.AM_PM);

        // ====================================
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        int scale = height < width ? height : width;
        this.setScale(scale);
        canvas.save();
        canvas.translate(0.5f * width, 0.5f * height);
        canvas.drawRGB(255, 255, 128);
        fPaint.setColor(0xFFDDDDFF);
        canvas.drawCircle(0.0f, 0.0f, scale * 0.45f, fPaint);
        fPaint.setColor(0xFF000000);

        canvas.save();
        canvas.rotate(hour * 30f + minute * 0.5f);
        canvas.scale(-1.0f, 1.0f);
        fStroke.setStrokeWidth(20.0f);
        canvas.drawPath(fHourPath, fStroke);
        canvas.restore();

        canvas.save();
        canvas.rotate(minute * 6.0f + second * 0.1f);
        canvas.scale(-1.0f, 1.0f);
        fStroke.setStrokeWidth(15.0f);
        canvas.drawPath(fMinutePath, fStroke);
        fStroke.setStrokeWidth(10.0f);
        canvas.restore();


        canvas.save();
        canvas.rotate(second * 6.0f + millisecond * 0.006f);
        canvas.scale(-1.0f, 1.0f);
        canvas.drawPath(fSecondPath, fStroke);
        canvas.restore();

        canvas.drawPath(fMarks, fStroke);

        canvas.restore();
        String date = String.format("%4d-%02d-%02d",
                year, month, day);
        canvas.drawText(date, 10, 130, fPaint);
        // ====================================

        long DELAY_MILLISECONDS = 100;
        this.postInvalidateDelayed(DELAY_MILLISECONDS);
    }

    private static double RadiansFrom60(double angle) {
        return Math.toRadians(angle * 6 - 90);
    }
    private static double RadiansFrom12(double angle) {
        return Math.toRadians(angle * 30 - 90);
    }
    private static float FaceX(double angle, int length, int centerX) {
        return (float) (length * Math.cos(angle) + centerX);
    }
    private static float FaceY(double angle, int length, int centerY) {
        return (float) (length * Math.sin(angle) + centerY);
    }
}
