package ciid2015.exquisitedatacorpse.additional.examples;

import ciid2015.exquisitedatacorpse.compileatruntime.Pod;
import ciid2015.exquisitedatacorpse.compileatruntime.PodCompiler;
import ciid2015.exquisitedatacorpse.compileatruntime.PodDirectoryMonitor;
import ciid2015.exquisitedatacorpse.compileatruntime.PodDirectoryMonitor.PodListener;
import java.util.concurrent.CopyOnWriteArrayList;
import processing.core.PApplet;

public class SketchCompilePodsFromDirectory extends PApplet implements PodListener {

    /**
     * if a file named e.g `TestPod.java` with the following source is dropped
     * in the monitored directory, a new pod will be added.
     *
     * <code>
     * import processing.core.PApplet;
     * import ciid2015.exquisitedatacorpse.compileatruntime.Pod;
     *
     * public class PodTest implements Pod {
     *
     *     public void setup(PApplet p) {}
     *
     *     public void draw(PApplet p) {
     *         p.stroke(255, 0, 0);
     *         p.line(p.random(p.width), p.random(p.height),
     *         p.random(p.width), p.random(p.height));
     *     }
     * }
     * </code>
     *
     */
    private PodDirectoryMonitor mMonitor;

    private final CopyOnWriteArrayList<Pod> mPods = new CopyOnWriteArrayList<Pod>();

    public void setup() {
        size(640, 480);
        mMonitor = PodDirectoryMonitor.monitor(this, sketchPath + "/data", ".java");
    }

    public void draw() {
        background(255);
        for (Pod mPod : mPods) {
            mPod.draw(this);
        }
    }

    public void pod_created(String mFile) {
        System.out.println("### pod_created: " + mMonitor.path() + mFile);
        String mSource = mMonitor.loadString(mFile);
        System.out.println("### loading source:\n" + mSource);
        Pod mPod = PodCompiler.instance("PodTest", mSource);
        mPods.add(mPod);
        mPod.setup(this);
    }

    public void pod_deleted(String mFile) {
        System.out.println("### pod_deleted: " + mMonitor.path() + mFile);
    }

    public void pod_modified(String mFile) {
        System.out.println("### pod_modified: " + mMonitor.path() + mFile);
    }

    public static void main(String[] args) {
        System.out.println("### monitoring: " + System.getProperty("user.dir"));
        PApplet.main(new String[]{"--sketch-path=" + System.getProperty("user.dir"),
                                  SketchCompilePodsFromDirectory.class.getName()});
    }
}
