import oscP5.*;
import netP5.*;
import ciid2015.exquisitedatacorpse.NetworkClient;
/**
 * `
 * this sketch illustrates how to mash up two data streams into one new one. in
 * this example `mouselogger/xyc` and `time/local` are combined into:
 *
 * mouse x-coordinates + mouse y-coordinates + time in seconds
 *
*/
NetworkClient mClient;
float mMouseX;
float mMouseY;
float mSeconds;

void setup() {
    size(15, 15);
    frameRate(1);

    mClient = new NetworkClient(this, "edc.local", "mouse-time");
}

void draw() {
}

void receive(String name, String tag, float x, float y, float z) {
    /* only consider messages from `mouselogger` with tag `xyc` */
    if (name.equals("mouselogger")) {
        if (tag.equals("xyc")) {
            mMouseX = x;
            mMouseY = y;
        }
    }
    /* only consider messages from `time` with tag `local` */
    if (name.equals("time")) {
        if (tag.equals("local")) {
            /* relay messages only every 10 seconds */
            if (mSeconds % 10 == 0) {
                /* convert time to seconds: hours * 60 * 60 + minutes * 60 + seconds */
                float t = x * 60 * 60 + y * 60 + z;
                mClient.send("xyt", mMouseX, mMouseY, t);
            }
            mSeconds = z;
        }
    }
}
