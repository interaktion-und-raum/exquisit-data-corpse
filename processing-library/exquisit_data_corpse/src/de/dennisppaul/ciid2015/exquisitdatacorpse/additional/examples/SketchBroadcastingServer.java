package de.dennisppaul.ciid2015.exquisitdatacorpse.additional.examples;

import de.dennisppaul.ciid2015.exquisitdatacorpse.AppBroadcastingServer;
import processing.core.PApplet;

public class SketchBroadcastingServer extends PApplet {

    public void setup() {
        main(AppBroadcastingServer.class.getName());
    }

    public static void main(String[] args) {
        PApplet.main(AppBroadcastingServer.class.getName());
    }
}
