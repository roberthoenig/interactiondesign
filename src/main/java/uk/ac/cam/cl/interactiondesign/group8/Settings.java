package uk.ac.cam.cl.interactiondesign.group8;

public class Settings {
    public enum Temperature {
        CELSIUS, FAHRENHEIT
    }
    public enum Time {
        TWELVE_HOURS, TWENTYFOUR_HOURS;
    }
    static public Temperature temperature = Temperature.CELSIUS;
    static public Time time = Time.TWENTYFOUR_HOURS;
} 