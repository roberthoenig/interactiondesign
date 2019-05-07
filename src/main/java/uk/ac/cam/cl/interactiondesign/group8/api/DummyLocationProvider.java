package uk.ac.cam.cl.interactiondesign.group8.api;

public class DummyLocationProvider implements LocationProvider {
    @Override
    public String getCurrentLocation() {
        return "Cambridge,UK";
    }
}
