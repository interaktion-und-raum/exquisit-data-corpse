import oscP5.*;
import netP5.*;
import ciid2015.exquisitedatacorpse.NetworkClient;
import controlP5.ControlP5;
import controlP5.Textarea;
ControlP5 cp5;

NetworkClient mClient;

boolean onTop = true;

Textarea myTextarea;

void setup() {
    size(displayWidth / 2 - 10, 150);

    frameRate(2);
    mClient = new NetworkClient(this, "localhost", "OSChatLurker");
    cp5 = new ControlP5(this);

    cp5.addToggle("onTop")
            .setPosition(displayWidth / 2 - 40, 10)
            .setSize(20, 20);

    myTextarea = cp5.addTextarea("txt")
            .setText("MSGs\n")
            .append("---")
            .setPosition(10, 10)
            .setSize(width - 50, height - 20)
            .setLineHeight(11)
            .showScrollbar()
            .setColor(color(128))
            .setColorBackground(color(255, 100))
            .setColorForeground(color(255, 100));
}

void draw() {
    background(0);

    /* position chat windows */
    frame.setLocation(displayWidth / 2 + 5, displayHeight - 150);
    frame.setAlwaysOnTop(onTop);
}

void receive(String name, String tag, String message) {
    String mText = timestamp() + " " + name + " : " + message;
    myTextarea.append("\n" + mText).scroll(1);
}

String timestamp() {
    return "[" + nf(hour(), 2) + ":" + nf(minute(), 2) + ":" + nf(second(), 2) + "]";
}

void init() {
    /* this removes window borders of applications */
    frame.removeNotify();
    frame.setUndecorated(true);
    frame.setResizable(true);
    super.init();
}
