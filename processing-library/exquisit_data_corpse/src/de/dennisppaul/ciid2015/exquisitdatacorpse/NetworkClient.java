package de.dennisppaul.ciid2015.exquisitdatacorpse;

import java.lang.reflect.Method;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

import static processing.core.PApplet.println;

public class NetworkClient {

    private final OscP5 mOSC;

    private final NetAddress myBroadcastLocation;

    private final String mAddrPattern;

    private final Object mClientParent;

    private static final String mReceiveMethod = "receive";

    public NetworkClient(Object pClientParent, String pServer, String pAddrPattern) {
        mClientParent = pClientParent;
        mOSC = new OscP5(this, 12000);
        myBroadcastLocation = new NetAddress(pServer, 32000);
        mAddrPattern = pAddrPattern;
        connect();

        prepareExitHandler();
        if (pClientParent instanceof PApplet) {
            PApplet p = (PApplet) pClientParent;
            p.registerMethod("dispose", this);
        }
    }

    public void dispose() {
        System.out.println("### disconnecting client");
        disconnect();
    }

    private void prepareExitHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("### disconnecting client*");
                disconnect();
            }
        }));
    }

    public final void connect() {
        OscMessage m = new OscMessage("/server/connect");
        mOSC.send(m, myBroadcastLocation);
    }

    public final void disconnect() {
        OscMessage m = new OscMessage("/server/disconnect");
        mOSC.send(m, myBroadcastLocation);
    }

    public void send(String tag, float x) {
        OscMessage m = new OscMessage(mAddrPattern);
        m.add(tag);
        m.add(x);
        mOSC.send(m, myBroadcastLocation);
    }

    public void send(String tag, float x, float y) {
        OscMessage m = new OscMessage(mAddrPattern);
        m.add(tag);
        m.add(x);
        m.add(y);
        mOSC.send(m, myBroadcastLocation);
    }

    public void send(String tag, float x, float y, float z) {
        OscMessage m = new OscMessage(mAddrPattern);
        m.add(tag);
        m.add(x);
        m.add(y);
        m.add(z);
        mOSC.send(m, myBroadcastLocation);
    }

    private void receive(String name, String tag, float x) {
        try {
            Method method = mClientParent.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE});
            method.invoke(mClientParent, name, tag, x);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    private void receive(String name, String tag, float x, float y) {
        try {
            Method method = mClientParent.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE, Float.TYPE});
            method.invoke(mClientParent, name, tag, x, y);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    private void receive(String name, String tag, float x, float y, float z) {
        try {
            Method method = mClientParent.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE, Float.TYPE, Float.TYPE});
            method.invoke(mClientParent, name, tag, x, y, z);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    public void oscEvent(OscMessage theOscMessage) {
        println("### client: received an osc message with addrpattern " + theOscMessage.addrPattern() + " and typetag " + theOscMessage.typetag());
        if (theOscMessage.typetag().equalsIgnoreCase("sf")) {
            receive(theOscMessage.addrPattern(),
                    theOscMessage.get(0).stringValue(),
                    theOscMessage.get(1).floatValue());
        } else if (theOscMessage.typetag().equalsIgnoreCase("sff")) {
            receive(theOscMessage.addrPattern(),
                    theOscMessage.get(0).stringValue(),
                    theOscMessage.get(1).floatValue(),
                    theOscMessage.get(2).floatValue());
        } else if (theOscMessage.typetag().equalsIgnoreCase("sfff")) {
            receive(theOscMessage.addrPattern(),
                    theOscMessage.get(0).stringValue(),
                    theOscMessage.get(1).floatValue(),
                    theOscMessage.get(2).floatValue(),
                    theOscMessage.get(3).floatValue());
        } else {
            theOscMessage.print();
        }
    }
}
