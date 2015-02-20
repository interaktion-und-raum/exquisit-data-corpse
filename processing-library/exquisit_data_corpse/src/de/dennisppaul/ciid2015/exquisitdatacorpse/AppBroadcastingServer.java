package de.dennisppaul.ciid2015.exquisitdatacorpse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import netP5.NetAddress;
import netP5.NetAddressList;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class AppBroadcastingServer extends PApplet {

    private OscP5 oscP5;

    private final NetAddressList myNetAddressList = new NetAddressList();

    private final int myListeningPort = 32000; /* incoming */

    private final String myConnectPattern = "/server/connect";

    private final String myDisconnectPattern = "/server/disconnect";

    private final float mFontSize = 9;

    private final List<OscMessage> mLastMessages = Collections.synchronizedList(new ArrayList<OscMessage>());

    private int mMaxMessages;

    public static boolean SHOW_LOG = true;

    private final HashMap<String, String> mAddressMap = new HashMap<String, String>();

    public void setup() {
        size(400, 300);

        mMaxMessages = 20;

        oscP5 = new OscP5(this, myListeningPort);
        frameRate(15);
        textFont(createFont("Courier", mFontSize));
        fill(0);
        noStroke();
    }

    public synchronized void draw() {
        background(255);

        final float mX = 10;
        float mY = mFontSize * 3;
        text("... CONNECTED CLIENTS ... ", mX, mY);

        mY += mFontSize * 2;
        for (int i = 0; i < myNetAddressList.size(); i++) {
            NetAddress mNetAddress = myNetAddressList.get(i);
            text(mNetAddress.address() + ":" + mNetAddress.port(), mX, mY);
            mY += mFontSize;
        }

        mY += mFontSize * 1;
        text("... LAST MESSAGES ....... ", mX, mY);
        mY += mFontSize * 2;
        for (OscMessage m : mLastMessages) {
            text("| " + m.toString() + " | " + getAsString(m.arguments()), mX, mY);
            mY += mFontSize;
        }
    }

    public static String getAsString(Object[] theObject) {
        StringBuilder s = new StringBuilder();
        for (Object o : theObject) {
            if (o instanceof Float) {
                String str = nfc((Float) o, 2);
                s.append(str).append(" | ");
            } else if (o instanceof Integer) {
                String str = ((Integer) o).toString();
                s.append(str).append(" | ");
            }
        }
        return s.toString();
    }

    public synchronized void oscEvent(OscMessage pOscMessage) {
        /* check if the address pattern fits any of our patterns */
        if (pOscMessage.addrPattern().equals(myConnectPattern)) {
            if (pOscMessage.checkTypetag("i")) {
                /* specifies a port, probably desktop client */
                connect(pOscMessage.netAddress().address(), pOscMessage.get(0).intValue());
            } else if (pOscMessage.checkTypetag("f")) {
                connect(pOscMessage.netAddress().address(), (int) pOscMessage.get(0).floatValue());
            }
        } else if (pOscMessage.addrPattern().equals(myDisconnectPattern)) {
            if (pOscMessage.checkTypetag("i")) {
                /* specifies a port, probably desktop client */
                disconnect(pOscMessage.netAddress().address(), pOscMessage.get(0).intValue());
            } else {
                disconnect(pOscMessage.netAddress().address(), 12000);
            }
        } else {
            /*
             * if pattern matching was not successful, then broadcast the incoming
             * message to all addresses in the netAddresList.
             */
            oscP5.send(pOscMessage, myNetAddressList);

            /* try to connect name and IP:port */
            System.out.println("### try to connect name and IP:port.");

//            /* check if the message has a name+tag. if yes check the the 'name' matches with an IP in the address map */
//            final String mAddress = pOscMessage.netAddress().address() + ":" + pOscMessage.netAddress().port();
//            String mValue = mAddressMap.get(mAddress);
//            if (mValue == null) {
//                System.out.println("### address not yet in address map");
//                mAddressMap.put(mAddress, "foobar");
//            } else {
//                System.out.println("### found " + mValue + "in address map.");
//            }
        }

        /* store messages */
        mLastMessages.add(pOscMessage);
        while (mLastMessages.size() > mMaxMessages) {
            mLastMessages.remove(0);
        }
    }

    private void connect(String theIPaddress, int pBroadcastPort) {
        if (!myNetAddressList.contains(theIPaddress, pBroadcastPort)) {
            myNetAddressList.add(new NetAddress(theIPaddress, pBroadcastPort));
            log("###", "adding " + theIPaddress + " to the list.");
        } else {
            log("###", theIPaddress + " is already connected.");
        }
        log("###", "currently there are " + myNetAddressList.list().size() + " remote locations connected.");
        log("###", myNetAddressList.list() + "");
    }

    private void disconnect(String theIPaddress, int pBroadcastPort) {
        if (myNetAddressList.contains(theIPaddress, pBroadcastPort)) {
            myNetAddressList.remove(theIPaddress, pBroadcastPort);
            log("###", "removing " + theIPaddress + " from the list.");
        } else {
            log("###", theIPaddress + " is not connected.");
        }
        log("###", "currently there are " + myNetAddressList.list().size());
    }

    public void keyPressed() {
        if (key == ' ') {
            myNetAddressList.list().clear();
            mLastMessages.clear();
            mAddressMap.clear();
        }
    }

    private void log(String p, String m) {
        if (SHOW_LOG) {
            System.err.println(p + "\t" + m);
        }
    }

    public static void main(String[] args) {
        PApplet.main(AppBroadcastingServer.class.getName());
    }
}
