import ciid2015.exquisitdatacorpse.*;
import oscP5.*;
import netP5.*;

NetworkClient mClient;

PFont font;

char lastKey;

void setup() {
  size(300, 300);
  mClient = new NetworkClient(this, "edc.local", "keyPressor");
  font = createFont("courier", 200);
  textAlign(CENTER, CENTER);
  textFont(font);
}

void draw() {
  background(23, 68, 250);
  text(lastKey, width/2, height/2-15);
}

void keyPressed() {
  // send keyPresses as ASCII values
  // www.asciitable.com
  println("asciivalue: "+int(key));
  mClient.send("keyPress", int(key));
}

void receive(String name, String tag, float x) {
  //println("### received: " + name + " - " + tag + " - " + x);
  if (tag.equals("keyPress")) {
    lastKey = char(int(x));
    if (x == 'a') {
      println(name+" sent keyPress 'a'");
    }
  }
}
