package uk.ac.cam.cl.interactiondesign.group8.ui;

public enum ETemperature implements IChatInterface {
    HOT(3),
    MILD(1),
    COLD(3);

    private double probability;

    ETemperature(double probability) {
        this.probability = probability;
    }

    @Override
    public double getProbability() {
        return probability;
    }

    @Override
    public String getHeader() {
        return "temperature";
    }

    public static ETemperature getTempFromKelvin(double temp) {
        if (temp < 3) {
            return COLD;
        } else if (temp > 16) {
            return HOT;
        } else {
            return MILD;
        }
    }
}
