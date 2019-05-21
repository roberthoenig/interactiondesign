package uk.ac.cam.cl.interactiondesign.group8;

import java.util.ArrayList;

import uk.ac.cam.cl.interactiondesign.group8.utils.Temperatureable;
import uk.ac.cam.cl.interactiondesign.group8.utils.Timeable;

/* Stores settings configuration for temperature and time units. Use the observable pattern
to register hooks that get called whenever the settings change. */
public class Settings {
    static public ArrayList<Timeable> timeables = new ArrayList<Timeable>();
    static public ArrayList<Temperatureable> temperatureables = new ArrayList<Temperatureable>();
    
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

    public static void setTemperature(Temperature t) {
        temperature = t;
        for (Temperatureable temperatureable: temperatureables) {
            temperatureable.updateTemperature(temperature); 
        }
    }
} 