package uk.ac.cam.cl.interactiondesign.group8.ui;

import java.util.Calendar;
import java.util.Date;

public enum ETime {
    SUNRISE,
    DAY,
    SUNSET,
    NIGHT;

    public ETime getTime(Date date) {
        // TODO: Replace this with something in the Weather API
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour < 7) {
            return NIGHT;
        } else if (hour < 8) {
            return SUNRISE;
        } else if (hour < 18) {
            return DAY;
        } else if (hour < 19) {
            return SUNSET;
        } else {
            return NIGHT;
        }
    }
}
