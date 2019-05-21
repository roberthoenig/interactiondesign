package uk.ac.cam.cl.interactiondesign.group8.ui;

public enum EWeather {
    THUNDERSTORM("THUNDERSTORM"),    // codes 200-299
    DRIZZLE("DRIZZLE"),             // codes 300-399
    RAIN("RAIN"),                   // codes 500-599
    SNOW("SNOW"),                   // codes 600-699

    FOG("FOG"),                     // code 741

    OVERCAST("OVERCAST"),           // code 804
    CLEAR("CLEAR");                  // any codes not already mentioned

    private String name;

    EWeather(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
