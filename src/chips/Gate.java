package chips;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Evgeniy on 07.05.2015.
 */
public abstract class Gate extends Canvas implements Cloneable{


    protected byte[] in;
    protected byte[] out;
    public java.util.List<Connector>[] busesIn;
    public java.util.List<Connector>[] busesOut;
    public Rectangle[] inputWire;
    public Rectangle[] outputWire;
    public boolean[] canBeChanged;



    public Gate() {
        setBounds(0, 0, 60, 30);

    }

    public int getInCount() {
        return in.length;
    }

    public int getOutCount() {
        return out.length;
    }

    public void setBit(int pos, byte bit) {
        in[pos] = bit;
        reCompute(pos);
    }

    public void setBitForDecoder(int pos, byte bit) {

        in[pos] = bit;
        reComputeForDecoder(pos);
    }

    public byte getBit(int pos) {
        return in[pos];
    }
/*
    public void myNotify(int pos, byte bit) {
            for (Connector bus : busesIn[pos]) {
                bus.setBit(bit, getParent().getGraphics());
            }
    }*/

    abstract public void reCompute(int pos);
    abstract public byte[] getOut();
    abstract public void reComputeForDecoder(int pos);


    public void preRemove(java.util.List<Connector> buses) {
        java.util.List<Connector> forRemove = new ArrayList<Connector>();

        for (int i = 0; i < busesIn.length; i++) {
            for (Connector bus : busesIn[i]) {
                bus.dispose(buses, forRemove);
            }
        }

        for (int i = 0; i < busesOut.length; i++) {
            for (Connector bus : busesOut[i]) {
                bus.dispose(buses, forRemove);
            }
        }
        buses.removeAll(forRemove);
    }

    @Override
    public Gate clone(){

        Gate clone = null;
        try {
            clone = (Gate) super.clone();
            clone.canBeChanged = canBeChanged.clone();
            clone.in = in.clone();
            clone.out = out.clone();
            clone.busesIn = busesIn.clone();
            clone.busesOut = busesOut.clone();
            clone.inputWire = inputWire.clone();
            clone.outputWire = outputWire.clone();
        }
        catch (CloneNotSupportedException ex) {
            System.out.println(ex.getMessage());
        }
        for (int i = 0; i < canBeChanged.length; i++) {
            clone.canBeChanged[i] = true;
            clone.busesIn[i] = new ArrayList<Connector>();
            clone.in[i] = 0;
        }
        for (int i = 0; i < busesOut.length; i++) {
            clone.busesOut[i] = new ArrayList<Connector>();
        }

        clone.reCompute(-1);

        return clone;
    }

}
