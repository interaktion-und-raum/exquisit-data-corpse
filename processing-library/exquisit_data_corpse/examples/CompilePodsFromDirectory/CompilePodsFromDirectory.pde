import oscP5.*;
import netP5.*;
import ciid2015.exquisitdatacorpse.compileatruntime.Pod;
import ciid2015.exquisitdatacorpse.compileatruntime.PodCompiler;
import ciid2015.exquisitdatacorpse.compileatruntime.PodDirectoryMonitor;
import ciid2015.exquisitdatacorpse.compileatruntime.PodDirectoryMonitor.PodListener;
import java.util.concurrent.CopyOnWriteArrayList;
class SketchCompilePodsFromDirectory extends PApplet implements PodListener {

/**
 * if a file named e.g `TestPod.java` with the following source is dropped
 * in the monitored directory, a new pod will be added.
 *
 * <code>
 *
 * import ciid2015.exquisitdatacorpse.compileatruntime.Pod;
 *
 * class PodTest implements Pod {
 *
 *     void setup(PApplet p) {}
 *
 *     void draw(PApplet p) {
 *         p.stroke(255, 0, 0);
 *         p.line(p.random(p.width), p.random(p.height),
 *         p.random(p.width), p.random(p.height));
 *     }
 * }
 * </code>
 *
 */
PodDirectoryMonitor mMonitor;

final CopyOnWriteArrayList<Pod> mPods = new CopyOnWriteArrayList<Pod>();

void setup() {
    size(640, 480);
    mMonitor = PodDirectoryMonitor.monitor(this, sketchPath + "/data", ".java");
}

void draw() {
    background(255);
    for (Pod mPod : mPods) {
        mPod.draw(this);
    }
}

void pod_created(String mFile) {
    System.out.println("### pod_created: " + mMonitor.path() + mFile);
    String mSource = mMonitor.loadString(mFile);
    System.out.println("### loading source:\n" + mSource);
    Pod mPod = PodCompiler.instance("PodTest", mSource);
    mPods.add(mPod);
    mPod.setup(this);
}

void pod_deleted(String mFile) {
    System.out.println("### pod_deleted: " + mMonitor.path() + mFile);
}

void pod_modified(String mFile) {
    System.out.println("### pod_modified: " + mMonitor.path() + mFile);
}
