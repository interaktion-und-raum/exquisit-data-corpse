package ciid2015.exquisitdatacorpse.additional.examples;

import ciid2015.exquisitdatacorpse.NetworkClient;
import ciid2015.exquisitdatacorpse.compileatruntime.Pod;
import ciid2015.exquisitdatacorpse.compileatruntime.PodCompiler;
import java.util.concurrent.CopyOnWriteArrayList;
import processing.core.PApplet;

public class SketchCompilePodsFromNetwork extends PApplet {

    private NetworkClient mClient;

    private final CopyOnWriteArrayList<Pod> mPods = new CopyOnWriteArrayList<Pod>();

    public void setup() {
        size(640, 480);
        mClient = new NetworkClient(this, "localhost", "pod-host");
    }

    public void draw() {
        background(255);
        for (Pod mPod : mPods) {
            mPod.draw(this);
        }
    }

    public void receive(String name, String tag, String message) {
        if (tag.equals(tag)) {
            System.out.println("### received pod source code:");
            System.out.println(message);
            Pod mPod = PodCompiler.instance("PodTest", message);
            mPods.add(mPod);
            mPod.setup(this);
        }
    }

    public void keyPressed() {
        if (key == ' ') {
            System.out.println("### sending pod source code.");
            mClient.send("pod", getSourceCode());
        }
    }

    private String getSourceCode() {
        String mPodSrc = "import processing.core.PApplet;\n"
                         + "import ciid2015.exquisitdatacorpse.compileatruntime.Pod;\n"
                         + "\n"
                         + "public class PodTest implements Pod {\n"
                         + "\n"
                         + "    public void setup(PApplet p) {\n"
                         + "    }\n"
                         + "\n"
                         + "    public void draw(PApplet p) {\n"
                         + "        p.stroke(" + random(255) + "f, " + random(255) + "f, " + random(255) + "f);\n"
                         + "        p.line(p.random(p.width), p.random(p.height),\n"
                         + "               p.random(p.width), p.random(p.height));\n"
                         + "    }\n"
                         + "}";
        return mPodSrc;
    }

    public static void main(String[] args) {
        PApplet.main(SketchCompilePodsFromNetwork.class.getName());
    }
}
