package ciid2015.exquisitdatacorpse.additional.examples;

import ciid2015.exquisitdatacorpse.compileatruntime.Pod;
import ciid2015.exquisitdatacorpse.compileatruntime.PodCompiler;
import processing.core.PApplet;

public class SketchCompilePodsFromString extends PApplet {

    private Pod mPod;

    private final String mPodSrc = "import processing.core.PApplet;\n"
                                   + "import ciid2015.exquisitdatacorpse.compileatruntime.Pod;\n"
                                   + "\n"
                                   + "public class PodTest implements Pod {\n"
                                   + "\n"
                                   + "    public void setup(PApplet p) {\n"
                                   + "    }\n"
                                   + "\n"
                                   + "    public void draw(PApplet p) {\n"
                                   + "        p.line(p.random(p.width), p.random(p.height),\n"
                                   + "               p.random(p.width), p.random(p.height));\n"
                                   + "    }\n"
                                   + "}";

    public void setup() {
        size(640, 480);
    }

    public void draw() {
        background(255);
        if (mPod != null) {
            mPod.draw(this);
        }
    }

    public void keyPressed() {
        System.out.println("### compiling the following source code: \n");
        System.out.println(mPodSrc);
        mPod = PodCompiler.instance("PodTest", mPodSrc);
        mPod.setup(this);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--sketch-path=" + System.getProperty("user.dir"),
                                  SketchCompilePodsFromString.class.getName()});
    }
}
