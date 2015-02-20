import de.dennisppaul.ciid2015.exquisitdatacorpse.*;
import oscP5.*;
import netP5.*;

PVector mScreenSize;
PVector mPreviousPostion;
java.awt.Robot mRobot;
NetworkClient mClient;
void setup() {
    size(400, 300);
    frameRate(30);
    background(255);
    try {
        mRobot = new java.awt.Robot();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    mScreenSize = new PVector(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width,
                              java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
    mPreviousPostion = new PVector();
    mClient = new NetworkClient(this, "127.0.0.1", "auto-mouse");
}
void keyPressed() {
    if (key == ',') {
        mClient.disconnect();
    }
    if (key == '.') {
        mClient.connect();
    }
}
void draw() {
    /* position */
    PVector p = new PVector(java.awt.MouseInfo.getPointerInfo().getLocation().x / mScreenSize.x,
                            java.awt.MouseInfo.getPointerInfo().getLocation().y / mScreenSize.y);
    /* color */
    java.awt.Color mColor = mRobot.getPixelColor(java.awt.MouseInfo.getPointerInfo().getLocation().x,
                                                 java.awt.MouseInfo.getPointerInfo().getLocation().y);
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
