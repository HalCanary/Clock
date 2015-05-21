package org.skia.clock;

import android.app.Activity;
import android.os.Bundle;

public class ClockActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(new CoolClock(this));
    }
}
