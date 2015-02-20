import de.dennisppaul.ciid2015.exquisitdatacorpse.*;
import oscP5.*;
import netP5.*;

final StringBuffer mString = new StringBuffer();
void setup() {
    size(400, 300);
    noStroke();
    fill(0);
    new Logger(this);
    textFont(createFont("Courier", 10));
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
    }
}
