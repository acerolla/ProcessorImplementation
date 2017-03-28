package chips;

import java.awt.*;

/**
 * Created by Evgeniy on 09.05.2015.
 */
public class MUX extends Gate {

    public MUX() {
        super();
        in = new byte[3];
        out = new byte[1];
        in[0] = 0;
        in[1] = 0;
        in[2] = 0;
        reCompute(-1);

        inputWire = new Rectangle[3];
        inputWire[0] = new Rectangle(0, 3, 15, 4);
        inputWire[1] = new Rectangle(0, getHeight() - 8, 15, 4);
        inputWire[2] = new Rectangle(getWidth() / 2 - 2, getHeight() - 4, 4, 4);
        outputWire = new Rectangle[1];
        outputWire[0] = new Rectangle(12 + getWidth() / 2, getHeight() / 2 - 2, getWidth() - (12 + getWidth() / 2), 4);
    }

    @Override
    public void reCompute(int pos) {
        if (in[2] == 0) {
            out[0] = in[0];
            return;
        }
        out[0] = in[1];
    }

    @Override
    public void reComputeForDecoder(int pos) {

    }

    public byte[] getOut() {
        return out;
    }

    @Override
    public void paint(Graphics g) {

        //input
        g.drawLine(0, 5, 15, 5);
        g.drawLine(0, getHeight() - 6, 15, getHeight() - 6);
        g.drawLine(getWidth() / 2, getHeight() - 3, getWidth() / 2, getHeight());

        //chip
        g.drawLine(15, 0, 15, getHeight() - 1);
        g.drawLine(15, 0, 12 + getWidth() / 2, 3);
        g.drawLine(15, getHeight() - 1, 12 + getWidth() / 2, getHeight() - 4);
        g.drawLine(12 + getWidth() / 2, 3, 12 + getWidth() / 2, getHeight() - 4);

        //output
        g.drawLine(12 + getWidth() / 2, getHeight() / 2, getWidth(), getHeight() / 2);
    }
}
