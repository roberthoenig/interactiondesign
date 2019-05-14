package uk.ac.cam.cl.interactiondesign.group8.utils;

import java.text.*;

public class UnitConverter {
    public static enum Temp {K, C, F}

    ;

    public static enum Velocity {MPH, KMH, MS}

    ;

    // Converts a temperature between two units
    public static float convertTemp(Temp from, Temp to, float value) {
        float kelvin;
        switch (from) {
            case K:
                kelvin = value;
                break;
            case C:
                kelvin = value + 273.15f;
                break;
            case F:
                kelvin = (value + 459.67f) * 0.555555556f;
                break;
            default:
                throw new RuntimeException("Invalid unit");
        }

        switch (to) {
            case K:
                return kelvin;
            case C:
                return value - 273.15f;
            case F:
                return (kelvin / 0.555555556f) - 459.67f;
            default:
                throw new RuntimeException("Invalid unit");
        }
    }

    // Converts temperature to a printable string
    public static String tempToString(Temp units, float value) {
        DecimalFormat format = new DecimalFormat("#");
        String result = format.format(value);

        switch (units) {
            case K:
                return result + "K";
            case C:
                return result + "\u00B0C";
            case F:
                return result + "\u00B0F";
            default:
                throw new RuntimeException("Invalid unit");
        }
    }

    // Converts a velocity between two units
    public static float convertVel(Velocity from, Velocity to, float value) {
        float ms;
        switch (from) {
            case MPH:
                ms = value / 2.23694f;
                break;
            case KMH:
                ms = value / 3.6f;
                break;
            case MS:
                ms = value;
                break;
            default:
                throw new RuntimeException("Invalid unit");
        }

        switch (to) {
            case MPH:
                return ms * 2.23694f;
            case KMH:
                return ms * 3.6f;
            case MS:
                return ms;
            default:
                throw new RuntimeException("Invalid unit");
        }
    }

    // Converts velocity to a printable string
    public static String velToString(Velocity units, float value) {
        DecimalFormat format = new DecimalFormat("#");
        String result = format.format(value);

        switch (units) {
            case MPH:
                return result + "mph";
            case KMH:
                return result + "km/h";
            case MS:
                return result + "m/s";
            default:
                throw new RuntimeException("Invalid unit");
        }
    }
}