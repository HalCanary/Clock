package org.skia.clock;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
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
    private Paint fSweepPaint;
    private Path fHourShadePath;
    private Path fMinuteShadePath;
    private Path fSecondShadePath;
    private Paint fBackgroundPaint;
    private Paint fDialPaint;
    private int fScale;
    private long fDateStamp;
    private String fDateString;

    private void setScale(int scale) {
        fScale = scale;
        fSecondPath = new Path();
        fSecondPath.lineTo(0.0f, scale * -0.45f);

        fHourPath = new Path();
        fHourPath.lineTo(0.0f, scale * -0.25f);

        fMinutePath = new Path();
        fMinutePath.lineTo(0.0f, scale * -0.40f);

        fMarks = new Path();
        float r1 = scale * 0.225f;
        float r2 = scale * 0.250f;
        float r3 = scale * 0.450f;
        float r4 = scale * 0.475f;
        float r5 = scale * 0.500f;
        for (int i = 0; i < 12; ++i) {
            double angleRad = 0.52359877559829887307 * i;
            double c = Math.cos(angleRad);
            double s = Math.sin(angleRad);
            fMarks.moveTo((float)(r1 * c), (float)(r1 * s));
            fMarks.lineTo((float)(r2 * c), (float)(r2 * s));
            fMarks.moveTo((float)(r3 * c), (float)(r3 * s));
            fMarks.lineTo((float)(r5 * c), (float)(r5 * s));
            for (int j = 0; j < 5; ++j) {
                angleRad += .10471975511965977461;
                c = Math.cos(angleRad);
                s = Math.sin(angleRad);
                fMarks.moveTo((float)(r3 * c), (float)(r3 * s));
                fMarks.lineTo((float)(r4 * c), (float)(r4 * s));
            }
        }

        fHourShadePath = CoolClock.MakeShade(scale * 0.225f, scale * 0.025f);
        fMinuteShadePath = CoolClock.MakeShade(scale * 0.45f, scale * 0.25f);
        fSecondShadePath = CoolClock.MakeShade(scale * 0.5f, scale * 0.475f);

        fBackgroundPaint = new Paint();
        fBackgroundPaint.setColor(0xFF000000);
    }

    public CoolClock(Context ctx) {
        super(ctx);
        fPaint = new Paint();
        fPaint.setAntiAlias(true);
        fPaint.setTextSize(120);
        fPaint.setColor(0xFFE8EAF6);

        fDate = new Date();
        fCalendar = Calendar.getInstance();

        fDialPaint = new Paint();
        fDialPaint.setColor(0xFF3F51B5);

        fStroke = new Paint();
        fStroke.setAntiAlias(true);
        fStroke.setStyle(Paint.Style.STROKE);
        fStroke.setStrokeWidth(10.0f);
        fStroke.setColor(0xFFE8EAF6);

        int[] colors = {0xFFFFFFFF,
                        0x003F51B5,
                        0x003F51B5,
                        0xFFFFFFFF};
        SweepGradient sweepGradient = new SweepGradient(
                0.0f, 0.0f, colors, null);
        fSweepPaint = new Paint();
        fSweepPaint.setShader(sweepGradient);
    }

    static private Path MakeShade(float u, float v) {
        assert u > 0.0f;
        assert v > 0.0f;
        assert u > v;
        RectF uRect = new RectF(-u, -u, u, u);
        RectF vRect = new RectF(-v, -v, v, v);
        Path h = new Path();
        h.moveTo(v, 0.0f);
        h.arcTo(vRect , 0.0f, -180.0f, false);
        h.lineTo(-u, 0.0f);
        h.arcTo(uRect, -180.0f, 180.0f, false);
        h.close();
        return h;
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
        boolean is_pm = Calendar.PM == fCalendar.get(Calendar.AM_PM);

        // =====================================================================
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        int scale = height < width ? height : width;

        if (scale != fScale) {
            this.setScale(scale);
        }
        canvas.save();
        canvas.translate(0.5f * width, 0.5f * height);

        canvas.drawPaint(fBackgroundPaint);
        canvas.drawCircle(0.0f, 0.0f, scale * 0.5f, fDialPaint);
        canvas.drawPath(fMarks, fStroke);
        canvas.drawCircle(0.0f, 0.0f, scale * 0.025f,
                          is_pm ? fBackgroundPaint : fPaint);

        canvas.save();
        canvas.rotate(hour * 30.0f + minute * 0.5f - 90.0f);
        canvas.drawPath(fHourShadePath, fSweepPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(minute * 6.0f + second * 0.1f - 90.0f);
        canvas.drawPath(fMinuteShadePath, fSweepPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(second * 6.0f + millisecond * 0.006f - 90.0f);
        canvas.drawPath(fSecondShadePath, fSweepPaint);
        canvas.restore();

        canvas.restore();

        long dateStamp = (year << 9) | (month << 5) | day;
        if (fDateStamp != dateStamp) {
            fDateStamp = dateStamp;
            fDateString = String.format("%4d-%02d-%02d", year, month, day);
        }
        canvas.drawText(fDateString, 10, 130, fPaint);
        // =====================================================================
        //int DELAY_MILLISECONDS = 250;
        fDate.setTime(System.currentTimeMillis());
        fCalendar.setTime(fDate);
        int period = 500;
        millisecond = period - fCalendar.get(Calendar.MILLISECOND) % period;
        this.postInvalidateDelayed(millisecond);
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
