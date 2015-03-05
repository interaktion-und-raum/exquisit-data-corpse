import oscP5.*;
import netP5.*;
import ciid2015.exquisitedatacorpse.NetworkClient;
import ciid2015.exquisitedatacorpse.compileatruntime.Pod;
import ciid2015.exquisitedatacorpse.compileatruntime.PodCompiler;
import java.util.concurrent.CopyOnWriteArrayList;
NetworkClient mClient;

final CopyOnWriteArrayList<Pod> mPods = new CopyOnWriteArrayList<Pod>();

void setup() {
    size(640, 480);
    mClient = new NetworkClient(this, "localhost", "pod-host");
}

void draw() {
    background(255);
    for (Pod mPod : mPods) {
        mPod.draw(this);
    }
}

void receive(String name, String tag, String message) {
    if (tag.equals(tag)) {
        System.out.println("### received pod source code:");
        System.out.println(message);
        Pod mPod = PodCompiler.instance("PodTest", message);
        mPods.add(mPod);
        mPod.setup(this);
    }
}

void keyPressed() {
    if (key == ' ') {
        System.out.println("### sending pod source code.");
        mClient.send("pod", getSourceCode());
    }
}

String getSourceCode() {
    String mPodSrc = "
                     + "import ciid2015.exquisitedatacorpse.compileatruntime.Pod;\n"
                     + "\n"
                     + "class PodTest implements Pod {\n"
                     + "\n"
                     + "    void setup(PApplet p) {\n"
                     + "    }\n"
                     + "\n"
                     + "    void draw(PApplet p) {\n"
                     + "        p.stroke(" + random(255) + "f, " + random(255) + "f, " + random(255) + "f);\n"
                     + "        p.line(p.random(p.width), p.random(p.height),\n"
                     + "               p.random(p.width), p.random(p.height));\n"
                     + "    }\n"
                     + "}";
    return mPodSrc;
}
