package org.usfirst.frc.team4999.lights.animations;

import java.util.ArrayList;

import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class ClippedAnimation implements Animation {

    private Animation animation;
    private int startidx;
    private int totallength;

    private ArrayList<Packet> packetbuffer;

    public ClippedAnimation(Animation animation, int startidx, int totallength) {
        this.animation = animation;
        packetbuffer = new ArrayList<Packet>();

        this.startidx = startidx;
        this.totallength = totallength;
    }

    @Override
    public Packet[] getNextFrame() {
        packetbuffer.clear();
        
        Packet[] animationPackets = animation.getNextFrame();
        for(int i = 0; i < animationPackets.length; i++) {
            Packet curr = animationPackets[i];
            curr = Commands.clipPacketRange(curr, startidx, totallength);
            if(curr != null)
                packetbuffer.add(curr);
        }

        Packet[] outpackets = new Packet[packetbuffer.size()];
        for(int i = 0; i < packetbuffer.size(); i++) {
            outpackets[i] = packetbuffer.get(i);
        }
        return outpackets;
    }

    @Override
    public int getFrameDelayMilliseconds() {
        return animation.getFrameDelayMilliseconds();
    }

}
