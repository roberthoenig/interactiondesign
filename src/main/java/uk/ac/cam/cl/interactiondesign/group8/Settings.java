package uk.ac.cam.cl.interactiondesign.group8;

import java.util.ArrayList;

import uk.ac.cam.cl.interactiondesign.group8.utils.Timeable;

public class Settings {
    static public ArrayList<Timeable> timeables = new ArrayList<Timeable>();
    
    public enum Temperature {
        CELSIUS, FAHRENHEIT
    }
    public enum Time {
        TWELVE_HOURS, TWENTYFOUR_HOURS;
    }
    static public Temperature temperature = Temperature.CELSIUS;
    static public Time time = Time.TWELVE_HOURS;

    public static void setTime(Time t) {
        time = t;
        for (Timeable timeable: timeables) {
            timeable.updateTime(time); 
        }
    }
} 