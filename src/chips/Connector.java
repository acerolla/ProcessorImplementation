package chips;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Evgeniy on 17.05.2015.
 */
public class Connector implements Serializable{

    private byte bit;

    public Point start;
    public Point end;

    public Line2D line;

    public java.util.List<Gate> toGates;
    public java.util.List<Integer> forGates;
    public java.util.List<Connector> toBuses;


    public Connector() {
        super();
        toGates = new ArrayList<Gate>();
        forGates = new ArrayList<Integer>();
        toBuses = new ArrayList<Connector>();

    }


    public void setBit(byte bit, Graphics g) {
        this.bit = bit;
        for (int i = 0; i < toGates.size(); i++) {
            toGates.get(i).setBit(forGates.get(i), bit);
        }
        for (Connector bus : toBuses) {
            bus.setBit(bit, g);
        }
        paint(g);
    }

    public void setBitForDecoder(byte bit) {
        this.bit = bit;
        for (int i = 0; i < toGates.size(); i++) {
            toGates.get(i).setBitForDecoder(forGates.get(i), bit);
        }
        for (Connector bus : toBuses) {
            bus.setBitForDecoder(bit);
        }
    }

    public byte getBit() {
        return bit;
    }


    public void paint(Graphics g) {
        if (start == null || end == null) {
            return;
        }

        line = new Line2D() {
            @Override
            public double getX1() {
                return start.x;
            }

            @Override
            public double getY1() {
                return start.y;
            }

            @Override
            public Point2D getP1() {
                return new Point((int)getX1(), (int)getY1());
            }

            @Override
            public double getX2() {
                return end.x;
            }

            @Override
            public double getY2() {
                return end.y;
            }

            @Override
            public Point2D getP2() {
                return new Point((int)getX2(), (int)getY2());
            }

            @Override
            public void setLine(double x1, double y1, double x2, double y2) {

            }

            @Override
            public Rectangle2D getBounds2D() {
                int height = (int)Math.abs(getY1() - getY2());
                int width = (int)Math.abs(getX1() - getX2());
                int x = (int)Math.min(getX1(), getX2());
                int y = (int)Math.min(getY1(), getY2());
                return new Rectangle(x, y, width, height);
            }
        };

        Color color;
        color = bit == 0 ? Color.RED : Color.GREEN;
        g.setColor(color);
        g.fillOval(start.x - 2, start.y - 2, 4, 4);
        g.drawLine(start.x, start.y, end.x, end.y);
        g.fillOval(end.x - 2, end.y - 2, 4, 4);
    }

    public void dispose(java.util.List<Connector> buses, java.util.List<Connector> forRemove) {

        for (Connector bus : buses) {
            if (bus == this) {
                for (Connector subBus : toBuses) {
                    subBus.dispose(buses, forRemove);
                }
            }
        }
        for (Gate chip : toGates) {
            chip.canBeChanged[forGates.get(toGates.indexOf(chip))] = true;
        }
        if (!forRemove.contains(this))
            forRemove.add(this);
    }

}
