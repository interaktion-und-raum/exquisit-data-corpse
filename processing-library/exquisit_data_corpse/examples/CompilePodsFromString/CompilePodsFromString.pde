import oscP5.*;
import netP5.*;
import ciid2015.exquisitdatacorpse.compileatruntime.Pod;
import ciid2015.exquisitdatacorpse.compileatruntime.PodCompiler;
Pod mPod;

final String mPodSrc = "
                               + "import ciid2015.exquisitdatacorpse.compileatruntime.Pod;\n"
                               + "\n"
                               + "class PodTest implements Pod {\n"
                               + "\n"
                               + "    void setup(PApplet p) {\n"
                               + "    }\n"
                               + "\n"
                               + "    void draw(PApplet p) {\n"
                               + "        p.line(p.random(p.width), p.random(p.height),\n"
                               + "               p.random(p.width), p.random(p.height));\n"
                               + "    }\n"
                               + "}";

void setup() {
    size(640, 480);
}

void draw() {
    background(255);
    if (mPod != null) {
        mPod.draw(this);
    }
}

void keyPressed() {
    System.out.println("### compiling the following source code: \n");
    System.out.println(mPodSrc);
    mPod = PodCompiler.instance("PodTest", mPodSrc);
    mPod.setup(this);
}
