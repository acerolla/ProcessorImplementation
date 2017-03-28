package chips;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Evgeniy on 21.05.2015.
 */
public class BlackBox extends Gate {

    byte[][] ansTable;

    public BlackBox(int inCount, int outCount, byte[][] ansTable) {
        super();
        setSize(getWidth(), 50);
        in = new byte[inCount];
        out = new byte[outCount];

        canBeChanged = new boolean[inCount];
        for (int i = 0; i < inCount; i++) {
            canBeChanged[i] = true;
        }

        int space;
        int yPos;
        busesIn = new List[inCount];
        inputWire = new Rectangle[inCount];
        if (inCount == 1) {
            yPos = getHeight() / 2;
            inputWire[0] = new Rectangle(0, yPos - 2, 15, 4);
        } else {
            space = getHeight() / (inCount - 1);
            yPos = 0;
            for (int i = 0; i < inCount; i++) {
                if (i == 0) {
                    inputWire[i] = new Rectangle(0, 0, 15, 3);
                    busesIn[i] = new ArrayList<Connector>();
                    yPos += space;
                } else if (i == inCount - 1) {
                    inputWire[i] = new Rectangle(0, getHeight() - 6, 15, 5);
                    busesIn[i] = new ArrayList<Connector>();
                    yPos += space;
                } else {
                    inputWire[i] = new Rectangle(0, yPos, 15, 3);
                    yPos += space;
                    busesIn[i] = new ArrayList<Connector>();
                }
            }
        }

        busesOut = new List[outCount];
        outputWire = new Rectangle[outCount];
        if (outCount == 1) {
            yPos = getHeight() / 2;
            outputWire[0] = new Rectangle(45, yPos - 2, 15, 4);
        } else {
            space = getHeight() / (outCount - 1);
            yPos = 0;
            for (int i = 0; i < outCount; i++) {
                if (i == 0) {
                    outputWire[i] = new Rectangle(45, 0, 15, 3);
                    busesOut[i] = new ArrayList<Connector>();
                    yPos += space;
                } else if (i == outCount - 1) {
                    outputWire[i] = new Rectangle(45, getHeight() - 6, 15, 5);
                    busesOut[i] = new ArrayList<Connector>();
                    yPos += space;
                } else {
                    outputWire[i] = new Rectangle(45, yPos, 15, 3);
                    yPos += space;
                    busesOut[i] = new ArrayList<Connector>();
                }
            }
        }
        this.ansTable = ansTable;
    }

    @Override
    public void reCompute(int pos) {
        int count = 0;
        for (int i = 0; i < ansTable.length; i++) {
            for (int j = 0; j < getInCount(); j++) {
                if (ansTable[i][j] == in[j]) {
                    count ++ ;
                } else {
                    count = 0;
                }
            }
            if (count == getInCount()) {
                for (int j = 0; j < getOutCount(); j++) {
                    out[j] = ansTable[i][j + getInCount()];
                }
            }
        }
        for (int i = 0; i < getOutCount(); i++) {
            for (Connector bus : busesOut[i]) {
                bus.setBit(out[i], getParent().getGraphics());
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
    public byte[] getOut() {
        return out;
    }

    @Override
    public void reComputeForDecoder(int pos) {
        int count = 0;
        for (int i = 0; i < ansTable.length; i++) {
            for (int j = 0; j < getInCount(); j++) {
                if (ansTable[i][j] == in[j]) {
                    count ++ ;
                } else {
                    count = 0;
                }
            }
            if (count == getInCount()) {
                for (int j = 0; j < getOutCount(); j++) {
                    out[j] = ansTable[i][j + getInCount()];
                }
            }
        }
        for (int i = 0; i < getOutCount(); i++) {
            for (Connector bus : busesOut[i]) {
                bus.setBit(out[i], getParent().getGraphics());
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

    @Override
    public void paint(Graphics g) {
        Color color;

        for (int i = 0; i < getInCount(); i++) {

            color = in[i] == 0 ? Color.RED : Color.GREEN;
            g.setColor(color);
            g.drawLine(0, inputWire[i].y + 1, 15, inputWire[i].y + 1);
        }

        g.setColor(Color.GRAY);
        g.fillRect(15, 0, getWidth() - 30, getHeight() - 2);



        for (int i = 0; i < getOutCount(); i++) {
            color = out[i] == 0 ? Color.RED : Color.GREEN;
            g.setColor(color);
            g.drawLine(45, outputWire[i].y + 1, 60, outputWire[i].y + 1);
        }
    }
}
