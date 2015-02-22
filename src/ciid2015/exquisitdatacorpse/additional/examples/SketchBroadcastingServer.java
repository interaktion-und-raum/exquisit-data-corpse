package ciid2015.exquisitdatacorpse.additional.examples;

import ciid2015.exquisitdatacorpse.AppBroadcastingServer;
import processing.core.PApplet;

public class SketchBroadcastingServer extends PApplet {

    public void setup() {
        size(1, 1);
        main(AppBroadcastingServer.class.getName());
    }

    public static void main(String[] args) {
        PApplet.main(AppBroadcastingServer.class.getName());
    }
}
