package ciid2015.exquisitedatacorpse.additional.logo;

import processing.core.PApplet;

public class SketchCircleLogo extends PApplet {

    private float mCounter;
    private float mPX;
    private float mPY;
    private boolean mFirstFrame = true;

    public void setup() {
        size(1024, 768);
        smooth();
        background(255);
        stroke(0);

        beginRecord(PDF, "logo" + nf(hour(), 2) + nf(minute(), 2) + nf(second(), 2) + ".pdf");
    }

    public void draw() {
        final float mStepSize = 1.0f / 60.0f;
        final int mSteps = 20;
        for (int i = 0; i < mSteps; i++) {
            mCounter += mStepSize;
            drawSegment(mCounter);
        }
    }

    public void drawSegment(float pCounter) {
        final float mMedianRadius = 200;
        final float mMaxRadiusOffset = 175;
        float r = pCounter * 2.5f;
        float mRadiusOffset = noise(pCounter) * mMaxRadiusOffset;
        float mRadius = mMedianRadius + mRadiusOffset;
        float x = sin(r) * mRadius + width / 2;
        float y = cos(r) * mRadius + height / 2;
        float mStrokeWidth = 0.01f + pow(1.0f - (abs(mRadius - mMedianRadius) / mMaxRadiusOffset), 4) * 4.0f;

        if (!mFirstFrame) {
            strokeWeight(mStrokeWidth);
            line(x, y, mPX, mPY);
        } else {
            mFirstFrame = false;
        }

        mPX = x;
        mPY = y;
    }

    public void keyPressed() {
        if (keyCode == ESC) {
            endRecord();
            System.out.println("### saved PDF to folder " + sketchPath);
            exit();
        }
    }

    public static void main(String[] args) {
        PApplet.main(SketchCircleLogo.class.getName(), new String[]{"--sketch-path=" + System.getProperty("user.home") + "/Desktop/logo/"});
    }
}
