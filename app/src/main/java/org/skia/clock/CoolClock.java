package org.skia.clock;

import android.content.Context;
import android.graphics.Paint;
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

    public CoolClock(Context ctx) {
        super(ctx);
        fPaint = new Paint();
        fPaint.setAntiAlias(true);
        fPaint.setTextSize(120);
        fDate = new Date();
        fCalendar = Calendar.getInstance();
    }

    protected void onDraw(android.graphics.Canvas canvas) {
        fDate.setTime(System.currentTimeMillis());
        fCalendar.setTime(fDate);

        // // You may want these values.
        // int day_of_month = fCalendar.get(Calendar.DAY_OF_MONTH);
        // int month = 1 + fCalendar.get(Calendar.MONTH);
        // int year = fCalendar.get(Calendar.YEAR);
        // int millisecond = fCalendar.get(Calendar.MILLISECOND);

        int hour = fCalendar.get(Calendar.HOUR);
        int minute = fCalendar.get(Calendar.MINUTE);
        int second = fCalendar.get(Calendar.SECOND);
        boolean is_pm = Calendar.PM == fCalendar.get(Calendar.AM_PM);

        // ====================================
        // REPLACE THIS CODE WITH A COOL CLOCK!
        String date = String.format("%2d:%02d:%02d %s",
                hour, minute, second, is_pm ? "p.m." : "a.m.");
        canvas.drawRGB(255, 255, 128);
        canvas.drawText(date, 10, 130, fPaint);
        // REPLACE THIS CODE WITH A COOL CLOCK!
        // ====================================

        long DELAY_MILLISECONDS = 300;
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
