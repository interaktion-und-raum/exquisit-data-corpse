import oscP5.*;
import netP5.*;
import ciid2015.exquisitdatacorpse.NetworkClient;
final StringBuffer mString = new StringBuffer();
NetworkClient mClient;

void setup() {
    size(400, 300);

    noStroke();
    fill(0);

    new mouseandkeylogger.Logger(this);
    textFont(createFont("Courier", 10));

    mClient = new NetworkClient(this, "edc.local", "keylogger");
}

void draw() {
    background(255);
    text(mString.toString(), 10, 10, width - 20, height - 20);
}

void keyTypedLogger(char key) {
    final char DELETE_KEY = 8; // this is the int value for the delete key
    if (key == DELETE_KEY) {
        /* delete last char */
        if (mString.length() > 0) {
            mString.deleteCharAt(mString.length() - 1);
        }
    } else {
        /* add key to string */
        mString.append(key);
        mClient.send("key", key);
    }
}
