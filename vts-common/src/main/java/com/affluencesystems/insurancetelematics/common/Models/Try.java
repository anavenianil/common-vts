package com.affluencesystems.insurancetelematics.common.Models;

public class Try {

    private byte[] demo1;
    private byte demo;

    public Try(byte[] demo1, byte demo) {
        this.demo1 = demo1;
        this.demo = demo;
    }

    public byte[] getDemo1() {
        return demo1;
    }

    public void setDemo1(byte[] demo1) {
        this.demo1 = demo1;
    }

    public byte getDemo() {
        return demo;
    }

    public void setDemo(byte demo) {
        this.demo = demo;
    }
}
