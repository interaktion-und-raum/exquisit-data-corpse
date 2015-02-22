package de.dennisppaul.ciid2015.exquisitdatacorpse.additional.examples;

import de.dennisppaul.ciid2015.exquisitdatacorpse.NetworkClient;
import mouseandkeylogger.Logger;
import processing.core.PApplet;

import static processing.core.PApplet.map;

public class SketchDataStreamMouseLogger extends PApplet {

    private Logger mLogger;

    private float mMouseX;
    private float mMouseY;
    private int mMouseColor;
    private boolean drawCircle = false;
    private NetworkClient mClient;

    public void setup() {
        size(400, 300);

        noFill();
        background(255);

        mLogger = new Logger(this);

        mClient = new NetworkClient(this, "edc.local", "mouselogger");
    }

    public void draw() {
        stroke(mMouseColor);
        point(mMouseX, mMouseY);

        if (drawCircle) {
            ellipse(mMouseX, mMouseY, 20, 20);
            drawCircle = false;
        }
    }

    public void mouseMovedLogger(int x, int y) {
        mClient.send("xyc",
                     (float) x / mLogger.screenWidth(),
                     (float) y / mLogger.screenHeight(),
                     color(mLogger.screenColor(x, y)));

        /* map to window space */
        mMouseX = map(x, 0, mLogger.screenWidth(), 0, width);
        mMouseY = map(y, 0, mLogger.screenHeight(), 0, height);
        mMouseColor = mLogger.screenColor(x, y);
    }

    public void mousePressedLogger(int button) {
        drawCircle = true;
    }

    public static void main(String[] args) {
        PApplet.main(SketchDataStreamMouseLogger.class.getName());
    }
}
