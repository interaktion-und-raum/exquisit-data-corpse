package de.dennisppaul.ciid2015.exquisitdatacorpse.additional.examples;

import de.dennisppaul.ciid2015.exquisitdatacorpse.NetworkClient;
import processing.core.PApplet;

public class SketchBroadcastingClient extends PApplet {

    private NetworkClient mClient;

    private float mBackgroundColor;

    public void setup() {
        size(640, 480);
        frameRate(25);
        mClient = new NetworkClient(this, "edc.local", "dennis");
    }

    public void draw() {
        background(mBackgroundColor);
    }

    public void mousePressed() {
        mClient.send("mouse", mouseX, mouseY);
        mClient.send("random", random(255));
    }

    public void keyPressed() {
        if (key == ',') {
            mClient.disconnect();
        }
        if (key == '.') {
            mClient.connect();
        }
    }

    public void receive(String name, String tag, float x) {
        println("### received: " + name + " - " + tag + " - " + x);
        if (name.equals("dennis") && tag.equals("random")) {
            mBackgroundColor = x;
        }
    }

    public void receive(String name, String tag, float x, float y) {
        println("### received: " + name + " - " + tag + " - " + x + ", " + y);
    }

    public void receive(String name, String tag, float x, float y, float z) {
        println("### received: " + name + " - " + tag + " - " + x + ", " + y + ", " + z);
    }

    public static void main(String[] args) {
        PApplet.main(SketchBroadcastingClient.class.getName());
    }
}
