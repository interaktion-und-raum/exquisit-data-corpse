package de.dennisppaul.ciid2015.exquisitdatacorpse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;

import static processing.core.PApplet.println;

public class NetworkClient {

    private final OscP5 oscP5;

    private final NetAddress myBroadcastLocation;

    private final String mAddrPattern;

    private final Object p;

    private static final String mReceiveMethod = "receive";

    public NetworkClient(Object pPAppletClient, String pServer, String pAddrPattern) {
        p = pPAppletClient;
        oscP5 = new OscP5(this, 12000);
        myBroadcastLocation = new NetAddress(pServer, 32000);
        mAddrPattern = pAddrPattern;
        connect();
    }

    public final void connect() {
        OscMessage m = new OscMessage("/server/connect");
        oscP5.send(m, myBroadcastLocation);
    }

    public final void disconnect() {
        OscMessage m = new OscMessage("/server/disconnect");
        oscP5.send(m, myBroadcastLocation);
    }

    public void send(String tag, float x) {
        OscMessage m = new OscMessage(mAddrPattern);
        m.add(tag);
        m.add(x);
        oscP5.send(m, myBroadcastLocation);
    }

    public void send(String tag, float x, float y) {
        OscMessage m = new OscMessage(mAddrPattern);
        m.add(tag);
        m.add(x);
        m.add(y);
        oscP5.send(m, myBroadcastLocation);
    }

    public void send(String tag, float x, float y, float z) {
        OscMessage m = new OscMessage(mAddrPattern);
        m.add(tag);
        m.add(x);
        m.add(y);
        m.add(z);
        oscP5.send(m, myBroadcastLocation);
    }

    private void receive(String name, String tag, float x) {
        try {
            Method method = p.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE});
            method.invoke(p, name, tag, x);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    void receive(String name, String tag, float x, float y) {
        try {
            Method method = p.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE, Float.TYPE});
            method.invoke(p, name, tag, x, y);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    private void receive(String name, String tag, float x, float y, float z) {
        try {
            Method method = p.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE, Float.TYPE, Float.TYPE});
            method.invoke(p, name, tag, x, y, z);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
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
