import de.dennisppaul.ciid2015.exquisitdatacorpse.*;
import oscP5.*;
import netP5.*;

NetworkClient mClient;

float mX;

void setup() {
  size(640, 480);
  frameRate(25);
  mClient = new NetworkClient(this, "127.0.0.1", "dennis");
}

void draw() {
  background(mX);
}

void mousePressed() {
  mClient.send("mouse", mouseX, mouseY);
}

void keyPressed() {
  if (key == ',') {
    mClient.disconnect();
  }
  if (key == '.') {
    mClient.connect();
  }
}

void receive(String name, String tag, float x, float y) {
  println("### " + name + " - " + tag + " - " + x + ", " + y);
  if (name.equals("dennis")) {
    mX = x;
  }
}

