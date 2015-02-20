package de.dennisppaul.ciid2015.exquisitdatacorpse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class NetworkClient {

    private final OscP5 mOSC;

    private final NetAddress mBroadcastLocation;

    private final String mAddrPattern;

    private final Object mClientParent;

    private static final String mReceiveMethod = "receive";

    private static final int MIN_PORT_NUMBER = 12000;

    private int mPort;

    private static final String DELIMITER = "/";

    public static boolean SHOW_LOG = true;

    private static final String ANONYM = "anonymous";

    private Method mMethodReceive3f;
    private Method mMethodReceive2f;
    private Method mMethodReceive1f;

    public NetworkClient(Object pClientParent, String pServer, String pAddrPattern) {
        mClientParent = pClientParent;

        mPort = MIN_PORT_NUMBER;
        while (!available(mPort)) {
            mPort++;
        }

        log("###", "client using port " + mPort);

        mOSC = new OscP5(this, mPort);
        mBroadcastLocation = new NetAddress(pServer, 32000);
        mAddrPattern = pAddrPattern;

        try {
            mMethodReceive1f = mClientParent.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE});
        } catch (NoSuchMethodException ex) {
        }
        try {
            mMethodReceive2f = mClientParent.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE, Float.TYPE});
        } catch (NoSuchMethodException ex) {
        }
        try {
            mMethodReceive3f = mClientParent.getClass().getDeclaredMethod(mReceiveMethod, new Class[]{String.class, String.class, Float.TYPE, Float.TYPE, Float.TYPE});
        } catch (NoSuchMethodException ex) {
        }

        prepareExitHandler();
        if (pClientParent instanceof PApplet) {
            PApplet p = (PApplet) pClientParent;
            p.registerMethod("dispose", this);
        }

        connect();
    }

    private static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public void dispose() {
        log("###", "disconnecting client*");
        disconnect();
    }

    private void prepareExitHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                log("###", "disconnecting client*");
                disconnect();
            }
        }));
    }

    public final void connect() {
        OscMessage m = new OscMessage("/server/connect");
        m.add(mPort);
        System.out.println("### also connect with a name `m.add(mAddrPattern`); so that IPs can be mapped to names.");
        mOSC.send(m, mBroadcastLocation);
    }

    public final void disconnect() {
        OscMessage m = new OscMessage("/server/disconnect");
        m.add(mPort);
        mOSC.send(m, mBroadcastLocation);
    }

    public void spoof(String sender, String tag, float x) {
        OscMessage m = new OscMessage(getAddressPattern(sender, tag));
        m.add(x);
        mOSC.send(m, mBroadcastLocation);
    }

    public void spoof(String sender, String tag, float x, float y) {
        OscMessage m = new OscMessage(getAddressPattern(sender, tag));
        m.add(x);
        m.add(y);
        mOSC.send(m, mBroadcastLocation);
    }

    public void spoof(String sender, String tag, float x, float y, float z) {
        OscMessage m = new OscMessage(getAddressPattern(sender, tag));
        m.add(x);
        m.add(y);
        m.add(z);
        mOSC.send(m, mBroadcastLocation);
    }

    public void send(String tag, float x) {
        OscMessage m = new OscMessage(getAddressPattern(tag));
        m.add(x);
        mOSC.send(m, mBroadcastLocation);
    }

    public void send(String tag, float x, float y) {
        OscMessage m = new OscMessage(getAddressPattern(tag));
        m.add(x);
        m.add(y);
        mOSC.send(m, mBroadcastLocation);
    }

    public void send(String tag, float x, float y, float z) {
        OscMessage m = new OscMessage(getAddressPattern(tag));
        m.add(x);
        m.add(y);
        m.add(z);
        mOSC.send(m, mBroadcastLocation);
    }

    private String getAddressPattern(String pAddrPattern, String pTag) {
        String mAddrPatternP = DELIMITER + pAddrPattern + DELIMITER + pTag;
        return mAddrPatternP;
    }

    private String getAddressPattern(String pTag) {
        return getAddressPattern(mAddrPattern, pTag);
    }

    private void receive(String name, String tag, float x) {
        try {
            mMethodReceive1f.invoke(mClientParent, name, tag, x);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    private void receive(String name, String tag, float x, float y) {
        try {
            mMethodReceive2f.invoke(mClientParent, name, tag, x, y);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    private void receive(String name, String tag, float x, float y, float z) {
        try {
            mMethodReceive3f.invoke(mClientParent, name, tag, x, y, z);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    public void oscEvent(OscMessage theOscMessage) {
        if (theOscMessage.typetag().equalsIgnoreCase("f") && mMethodReceive1f != null) {
            receive(getName(theOscMessage.addrPattern()),
                    getTag(theOscMessage.addrPattern()),
                    theOscMessage.get(0).floatValue());
        } else if (theOscMessage.typetag().equalsIgnoreCase("ff") && mMethodReceive2f != null) {
            receive(getName(theOscMessage.addrPattern()),
                    getTag(theOscMessage.addrPattern()),
                    theOscMessage.get(0).floatValue(),
                    theOscMessage.get(1).floatValue());
        } else if (theOscMessage.typetag().equalsIgnoreCase("fff") && mMethodReceive3f != null) {
            receive(getName(theOscMessage.addrPattern()),
                    getTag(theOscMessage.addrPattern()),
                    theOscMessage.get(0).floatValue(),
                    theOscMessage.get(1).floatValue(),
                    theOscMessage.get(2).floatValue());
        } else {
            log("### ", "client couldn t parse message:");
            log("### ", theOscMessage.toString());
        }
    }

    private String getTag(String pAddrPattern) {
        String[] mStrings = PApplet.split(pAddrPattern, DELIMITER);
        if (mStrings.length == 3) {
            return mStrings[2];
        } else if (mStrings.length == 2) {
            return mStrings[1];
        } else {
            log("### ", "ERROR-MALFORMED-NAME-TAG: " + pAddrPattern);
            return "ERROR-MALFORMED-NAME-TAG";
        }
    }

    private String getName(String pAddrPattern) {
        String[] mStrings = PApplet.split(pAddrPattern, DELIMITER);
        if (mStrings.length == 3) {
            return mStrings[1];
        } else if (mStrings.length == 2) {
            return ANONYM;
        } else {
            log("### ", "ERROR-MALFORMED-NAME-TAG: " + pAddrPattern);
            return "ERROR-MALFORMED-NAME-TAG";
        }
    }

    private void log(String p, String m) {
        if (SHOW_LOG) {
            System.err.println(p + "\t" + m);
        }
    }
}
