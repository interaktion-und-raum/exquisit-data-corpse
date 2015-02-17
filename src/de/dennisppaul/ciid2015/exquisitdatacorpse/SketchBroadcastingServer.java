package de.dennisppaul.ciid2015.exquisitdatacorpse;

import java.util.Iterator;
import netP5.NetAddress;
import netP5.NetAddressList;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class SketchBroadcastingServer extends PApplet {

    private OscP5 oscP5;
    private final NetAddressList myNetAddressList = new NetAddressList();

    /* listeningPort is the port the server is listening for incoming messages */
    private final int myListeningPort = 32000;

    private final String myConnectPattern = "/server/connect";

    private final String myDisconnectPattern = "/server/disconnect";

    private final float mFontSize = 10;

    public void setup() {
        size(400, 300);
        oscP5 = new OscP5(this, myListeningPort);
        frameRate(15);
        textFont(createFont("Courier", mFontSize));
        fill(0);
        noStroke();
    }

    public void draw() {
        background(255);

        final float mX = 10;
        float mY = mFontSize * 3;
        text("CONNECTED CLIENTS /// ", mX, mY);
        mY += mFontSize * 2;
        for (int i = 0; i < myNetAddressList.size(); i++) {
            NetAddress mNetAddress = myNetAddressList.get(i);
            text(mNetAddress.address() + ":" + mNetAddress.port(), mX, mY);
            mY += mFontSize;
        }
    }

    public void oscEvent(OscMessage theOscMessage) {
        /* check if the address pattern fits any of our patterns */
        if (theOscMessage.addrPattern().equals(myConnectPattern) && theOscMessage.checkTypetag("i")) {
            connect(theOscMessage.netAddress().address(), theOscMessage.get(0).intValue());
        } else if (theOscMessage.addrPattern().equals(myDisconnectPattern) && theOscMessage.checkTypetag("i")) {
            disconnect(theOscMessage.netAddress().address(), theOscMessage.get(0).intValue());
        } /**
         * if pattern matching was not successful, then broadcast the incoming
         * message to all addresses in the netAddresList.
         */
        else {
            oscP5.send(theOscMessage, myNetAddressList);
        }
    }

    private void connect(String theIPaddress, int pBroadcastPort) {
        if (!myNetAddressList.contains(theIPaddress, pBroadcastPort)) {
            myNetAddressList.add(new NetAddress(theIPaddress, pBroadcastPort));
            println("### adding " + theIPaddress + " to the list.");
        } else {
            println("### " + theIPaddress + " is already connected.");
        }
        println("### currently there are " + myNetAddressList.list().size() + " remote locations connected.");
        println("### " + myNetAddressList.list() + "");
    }

    private void disconnect(String theIPaddress, int pBroadcastPort) {
        if (myNetAddressList.contains(theIPaddress, pBroadcastPort)) {
            myNetAddressList.remove(theIPaddress, pBroadcastPort);
            println("### removing " + theIPaddress + " from the list.");
        } else {
            println("### " + theIPaddress + " is not connected.");
        }
        println("### currently there are " + myNetAddressList.list().size());
    }

    public void keyPressed() {
        if (key == ' ') {
            myNetAddressList.list().clear();
        }
    }

    public static void main(String[] args) {
        PApplet.main(SketchBroadcastingServer.class.getName());
    }
}
