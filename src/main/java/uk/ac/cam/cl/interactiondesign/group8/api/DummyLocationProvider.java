package uk.ac.cam.cl.interactiondesign.group8.api;

public class DummyLocationProvider implements LocationProvider {
    private final String location;

    @Override
    public String getCurrentLocation() {
        return location;
    }

    public DummyLocationProvider() {
        location = "Cambridge,UK";
    }

    public DummyLocationProvider(String location) {
        this.location = location;
    }
}
