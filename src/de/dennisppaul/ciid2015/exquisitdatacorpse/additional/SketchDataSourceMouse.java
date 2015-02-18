package de.dennisppaul.ciid2015.exquisitdatacorpse.additional;

import de.dennisppaul.ciid2015.exquisitdatacorpse.NetworkClient;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import processing.core.PApplet;
import processing.core.PVector;

public class SketchDataSourceMouse extends PApplet {

    private PVector mScreenSize;
    private PVector mPreviousPostion;
    private Robot mRobot;

    private NetworkClient mClient;

    public void setup() {
        size(400, 300);
        frameRate(30);
        background(255);

        try {
            mRobot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
        mScreenSize = new PVector(Toolkit.getDefaultToolkit().getScreenSize().width,
                                  Toolkit.getDefaultToolkit().getScreenSize().height);
        mPreviousPostion = new PVector();

        mClient = new NetworkClient(this, "127.0.0.1", "auto-mouse");
    }

    public void keyPressed() {
        if (key == ',') {
            mClient.disconnect();
        }
        if (key == '.') {
            mClient.connect();
        }
    }

    public void draw() {
        /* position */
        PVector p = new PVector(MouseInfo.getPointerInfo().getLocation().x / mScreenSize.x,
                                MouseInfo.getPointerInfo().getLocation().y / mScreenSize.y);

        /* color */
        Color mColor = mRobot.getPixelColor(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
        stroke(mColor.getRed(), mColor.getGreen(), mColor.getBlue());

        /* visualize */
        line(p.x * width,
             p.y * height,
             mPreviousPostion.x * width,
             mPreviousPostion.y * height);

        /* send values */
        mClient.send("position", p.x, p.y);
        mClient.send("color", color(mColor.getRed(), mColor.getGreen(), mColor.getBlue()));

        mPreviousPostion.set(p.x, p.y);
    }

    public static void main(String[] args) {
        PApplet.main(SketchDataSourceMouse.class.getName());
    }
}
