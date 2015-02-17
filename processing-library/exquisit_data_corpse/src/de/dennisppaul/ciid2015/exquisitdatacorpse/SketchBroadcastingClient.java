package de.dennisppaul.ciid2015.exquisitdatacorpse;

import processing.core.PApplet;

public class SketchBroadcastingClient extends PApplet {

    public NetworkClient mClient;

    public float mX;

    public void setup() {
        size(640, 480);
        frameRate(25);
        mClient = new NetworkClient(this, "127.0.0.1", "dennis");
    }

    public void draw() {
        background(mX);
    }

    public void mousePressed() {
        mClient.send("mouse", mouseX, mouseY);
        mClient.send("random", random(width));
    }

    public void keyPressed() {
        if (key == ',') {
            mClient.disconnect();
        }
        if (key == '.') {
            mClient.connect();
        }
    }

    public void receive(String name, String tag, float x, float y) {
        println("### " + name + " - " + tag + " - " + x + ", " + y);
        if (name.equals("dennis")) {
            mX = x;
        }
    }

    public static void main(String[] args) {
        PApplet.main(SketchBroadcastingClient.class.getName());
    }
}
