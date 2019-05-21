package uk.ac.cam.cl.interactiondesign.group8.ui;

import java.util.Calendar;
import java.util.Date;

public enum ETime implements IChatInterface {
    SUNRISE(2),
    DAY(1),
    SUNSET(2),
    NIGHT(1);

    private double probability;

    ETime(double probability) {
        this.probability = probability;
    }

    public static ETime getTime(Date date) {
        // TODO: Replace this with something in the Weather API (for sunset times)
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

    public static ETime now() {
        return getTime(new Date());
    }

    @Override
    public double getProbability() {
        return probability;
    }

    @Override
    public String getHeader() {
        return "time";
    }
}
