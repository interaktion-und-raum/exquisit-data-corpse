package ciid2015.exquisitdatacorpse.additional.examples;

import ciid2015.exquisitdatacorpse.NetworkClient;
import processing.core.PApplet;

public class AppOSChatter extends PApplet {

    private controlP5.ControlP5 cp5;

    private NetworkClient mClient;

    private boolean onTop = true;

    public void setup() {
        size(displayWidth / 2 - 10, 50);

        frameRate(15);
        mClient = new NetworkClient(this, "edc.local", "OSChat");

        cp5 = new controlP5.ControlP5(this);

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
        cp5.get(controlP5.Textfield.class, "msg").clear();
    }

    public void controlEvent(controlP5.ControlEvent theEvent) {
        if (theEvent.isAssignableFrom(controlP5.Textfield.class)) {
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
        super.init();
    }

    public static void main(String[] args) {
        PApplet.main(AppOSChatter.class.getName());
    }
}
