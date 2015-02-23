import ciid2015.exquisitdatacorpse.*;
import oscP5.*;
import netP5.*;

mouseandkeylogger.Logger mLogger;
float mMouseX;
float mMouseY;
int mMouseColor;
boolean drawCircle = false;
NetworkClient mClient;
void setup() {
    size(400, 300);
    noFill();
    background(255);
    mLogger = new mouseandkeylogger.Logger(this);
    mClient = new NetworkClient(this, "edc.local", "mouselogger");
}
void draw() {
    stroke(mMouseColor);
    point(mMouseX, mMouseY);
    if (drawCircle) {
        ellipse(mMouseX, mMouseY, 20, 20);
        drawCircle = false;
    }
}
void mouseMovedLogger(int x, int y) {
    mClient.send("xyc",
                 (float) x / mLogger.screenWidth(),
                 (float) y / mLogger.screenHeight(),
                 color(mLogger.screenColor(x, y)));
    /* map to window space */
    mMouseX = map(x, 0, mLogger.screenWidth(), 0, width);
    mMouseY = map(y, 0, mLogger.screenHeight(), 0, height);
    mMouseColor = mLogger.screenColor(x, y);
}
void mousePressedLogger(int button) {
    drawCircle = true;
}
