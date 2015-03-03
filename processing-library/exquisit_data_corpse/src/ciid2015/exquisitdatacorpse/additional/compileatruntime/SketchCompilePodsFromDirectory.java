package ciid2015.exquisitdatacorpse.additional.compileatruntime;

import ciid2015.exquisitdatacorpse.additional.compileatruntime.PodDirectoryMonitor.PodListener;
import processing.core.PApplet;

public class SketchCompilePodsFromDirectory extends PApplet implements PodListener {

    private PodDirectoryMonitor mMonitor;

    public void setup() {
        mMonitor = PodDirectoryMonitor.monitor(this, sketchPath + "/data", ".java");
    }

    public void draw() {
    }

    public void pod_created(String mFile) {
        System.out.println("### pod_created: " + mMonitor.path() + mFile);
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
