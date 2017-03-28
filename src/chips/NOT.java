package chips;

import java.awt.*;
import java.util.*;

/**
 * Created by Evgeniy on 12.05.2015.
 */
public class NOT extends Gate {

    public NOT() {
        super();
        in = new byte[1];
        out = new byte[1];
        in[0] = 0;

        busesIn = new java.util.List[1];
        busesIn[0] = new ArrayList<Connector>();
        busesOut = new java.util.List[1];
        busesOut[0] = new ArrayList<Connector>();

        canBeChanged = new boolean[1];
        canBeChanged[0] = true;

        reCompute(-1);

        inputWire = new Rectangle[1];
        inputWire[0] = new Rectangle(0, getHeight() / 2 - 2, getWidth() / 2 - 4, 4);
        outputWire = new Rectangle[1];
        outputWire[0] = new Rectangle(getWidth() / 2 + 4, getHeight() / 2 - 2, getWidth() - getWidth() / 2 - 4, 4);
    }

    @Override
    public void reCompute(int pos) {
        out[0] = (byte)Math.abs(in[0] - 1);
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
        out[0] = (byte)Math.abs(in[0] - 1);
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
        g.drawLine(0, getHeight() / 2, getWidth() / 2 - 4, getHeight() / 2);

        //chip
        g.setColor(Color.BLACK);
        g.drawOval(26, 11, 8, 8);

        //output
        color = out[0] == 0 ? Color.RED : Color.GREEN;
        g.setColor(color);
        g.drawLine(getWidth() / 2 + 4, getHeight() / 2, getWidth(), getHeight() / 2);

    }
}
