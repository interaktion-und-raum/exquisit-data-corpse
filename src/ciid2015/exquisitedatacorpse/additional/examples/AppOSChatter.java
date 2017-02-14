package ciid2015.exquisitedatacorpse.additional.examples;

import ciid2015.exquisitedatacorpse.NetworkClient;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Textfield;
import processing.core.PApplet;

public class AppOSChatter extends PApplet {

    private ControlP5 cp5;

    private NetworkClient mClient;

    private boolean onTop = true;

    public void setup() {
        size(displayWidth / 2 - 10, 50);

        frameRate(15);
        mClient = new NetworkClient(this, "localhost", "OSChat");

        cp5 = new ControlP5(this);

        cp5.addTextfield("msg")
                .setPosition(10, 10)
                .setAutoClear(false)
                .setSize(displayWidth / 2 - 55, 20);

        // create a toggle
        cp5.addToggle("onTop")
                .setPosition(displayWidth / 2 - 35, 10)
                .setSize(20, 20);

    }

    public void draw() {
        background(0);

        // position chat windows
        frame.setLocation(0, displayHeight - 50);
        frame.setSize(displayWidth / 2 - 5, 50);
        frame.setAlwaysOnTop(onTop);

    }

    public void clear() {
        cp5.get(Textfield.class, "msg").clear();
    }

    public void controlEvent(ControlEvent theEvent) {
        if (theEvent.isAssignableFrom(Textfield.class)) {
            if (theEvent.getName().equals("msg")) {
                sendMsg(theEvent.getStringValue());
            }
            clear();
        }
    }

    public void sendMsg(String theMsg) {
        mClient.send("msg", theMsg);
    }

    public void init() {
        /* this removes window borders of applications */
        frame.removeNotify();
        frame.setUndecorated(true);
        frame.setResizable(true);
//        super.init();
    }

    public static void main(String[] args) {
        PApplet.main(AppOSChatter.class.getName());
    }
}
