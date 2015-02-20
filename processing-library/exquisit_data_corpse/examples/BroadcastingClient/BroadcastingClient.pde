import de.dennisppaul.ciid2015.exquisitdatacorpse.*;
import oscP5.*;
import netP5.*;

NetworkClient mClient;
float mBackgroundColor;
void setup() {
    size(640, 480);
    frameRate(25);
    mClient = new NetworkClient(this, "127.0.0.1", "dennis");
}
void draw() {
    background(mBackgroundColor);
}
void mousePressed() {
    mClient.send("mouse", mouseX, mouseY);
    mClient.send("random", random(255));
}
void keyPressed() {
    if (key == ',') {
        mClient.disconnect();
    }
    if (key == '.') {
        mClient.connect();
    }
}
void receive(String name, String tag, float x) {
    println("### received: " + name + " - " + tag + " - " + x);
    if (name.equals("dennis") && tag.equals("random")) {
        mBackgroundColor = x;
    }
}
void receive(String name, String tag, float x, float y) {
    println("### received: " + name + " - " + tag + " - " + x + ", " + y);
}
void receive(String name, String tag, float x, float y, float z) {
    println("### received: " + name + " - " + tag + " - " + x + ", " + y + ", " + z);
}
