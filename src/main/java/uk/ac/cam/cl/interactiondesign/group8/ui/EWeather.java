package uk.ac.cam.cl.interactiondesign.group8.ui;

public enum EWeather implements IChatInterface {
    THUNDERSTORM(3),    // codes 200-299
    DRIZZLE(1),             // codes 300-399
    RAIN(2),                   // codes 500-599
    SNOW(3),                   // codes 600-699

    FOG(2),                     // code 741

    OVERCAST(1),           // code 804
    CLEAR(2);                  // any codes not already mentioned

    private double probability;

    EWeather(double probability) {
        this.probability = probability;
    }

    @Override
    public double getProbability() {
        return probability;
    }

    @Override
    public String getHeader() {
        return "weather";
    }
}
