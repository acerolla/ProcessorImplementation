package chips;

import java.awt.*;
import java.util.*;

/**
 * Created by Evgeniy on 17.05.2015.
 */
public class NAND extends Gate {

    public NAND() {
        super();
        in = new byte[2];
        out = new byte[1];
        in[0] = 0;
        in[1] = 0;


        busesIn = new java.util.List[2];
        busesIn[0] = new ArrayList<Connector>();
        busesIn[1] = new ArrayList<Connector>();
        busesOut = new java.util.List[1];
        busesOut[0] = new ArrayList<Connector>();

        canBeChanged = new boolean[2];
        canBeChanged[0] = true;
        canBeChanged[1] = true;

        reCompute(-1);

        inputWire = new Rectangle[2];
        inputWire[0] = new Rectangle(0, 3, 15, 4);
        inputWire[1] = new Rectangle(0, getHeight() - 8, 15, 4);
        outputWire = new Rectangle[1];
        outputWire[0] = new Rectangle(13 + getWidth() / 2, getHeight() / 2 - 2, getWidth() - (13 + getWidth() / 2), 4);
    }

    @Override
    public void reCompute(int pos) {
        out[0] = (byte)Math.abs((byte)(in[0] & in[1]) - 1);
        for (int i = 0; i < busesOut.length; i++) {
            for (Connector bus : busesOut[i]) {
                bus.setBit(out[0], getParent().getGraphics());
            }
        }
        if (pos == -1) {
            for (int i = 0; i < getInCount(); i++) {
                for (Connector bus : busesIn[i]) {
                    bus.setBit(in[i], getParent().getGraphics());
                }
            }
        } else {
            for (Connector bus : busesIn[pos]) {
                bus.setBit(in[pos], getParent().getGraphics());
            }
        }
        repaint();
    }

    @Override
    public void reComputeForDecoder(int pos) {
        out[0] = (byte)Math.abs((byte)(in[0] & in[1]) - 1);
        for (int i = 0; i < busesOut.length; i++) {
            for (Connector bus : busesOut[i]) {
                bus.setBitForDecoder(out[0]);
            }
        }
        if (pos == -1) {
            for (int i = 0; i < getInCount(); i++) {
                for (Connector bus : busesIn[i]) {
                    bus.setBitForDecoder(in[i]);
                }
            }
        } else {
            for (Connector bus : busesIn[pos]) {
                bus.setBitForDecoder(in[pos]);
            }
        }
    }

    public byte[] getOut() {
        return out;
    }

    @Override
    public void paint(Graphics g) {

        //input
        Color color;
        color = in[0] == 0 ? Color.RED : Color.GREEN;
        g.setColor(color);
        g.drawLine(0, 5, 15, 5);
        color = in[1] == 0 ? Color.RED : Color.GREEN;
        g.setColor(color);
        g.drawLine(0, getHeight() - 6, 15, getHeight() - 6);

        //chip
        g.setColor(Color.BLACK);
        g.drawLine(15, 0, 15, getHeight() - 1);
        g.drawLine(15, 0, 15 + getWidth() / 4, 0);
        g.drawLine(15, getHeight() - 1, 15 + getWidth() / 4, getHeight() - 1);
        g.drawArc(12, 0, getWidth() / 2, getHeight() - 1, -90, 180);
        g.drawOval(13 + getWidth() / 2, getHeight() / 2 - 4, 8, 8);

        //output
        color = out[0] == 0 ? Color.RED : Color.GREEN;
        g.setColor(color);
        g.drawLine(21 + getWidth() / 2, getHeight() / 2, getWidth(), getHeight() / 2);
    }
}
