import chips.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by Evgeniy on 07.05.2015.
 */
public class Main extends JFrame{

    private String text = "";
    java.awt.List gatesName;
    java.util.List<Gate> gatesGUI;
    Point startForChip;

    boolean clicked;
    Point forDel;
    Connector bus;
    long lastClick;


    public java.util.List<Connector> listOfBuses;

    final JPanel panel;

    public Main() {
        super("Processor implementation");
        setVisible(true);

        lastClick = 0;


        JButton but = new JButton();
        but.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(panel, text);
                for (Component comp : panel.getComponents()) {
                    System.out.println(comp);

                }
                for (Connector bus : listOfBuses) {
                    System.out.println(bus);
                }
                System.out.println(text);
            }
        });

        listOfBuses = new ArrayList<Connector>();
        panel = new JPanel(null);

        but.setText("LastAction");

        //list of possible chips
        gatesName = new java.awt.List();
        gatesName.add("NAND");
        gatesName.add("XOR");
        gatesName.add("OR");
        gatesName.add("AND");
        gatesName.add("NOT");
        //gatesName.add("MUX");

        final JTextField console = new JTextField();
        console.setEditable(false);


        final JSplitPane splitPane = new JSplitPane(

                        JSplitPane.HORIZONTAL_SPLIT,
                        new JSplitPane(
                                JSplitPane.VERTICAL_SPLIT,
                                but,
                                gatesName
                                ),
                        panel
                );
        but.setSize(but.getWidth(), 40);

        add(splitPane);


        //list of
        gatesGUI = new java.util.ArrayList<Gate>();
        gatesGUI.add(new NAND());
        gatesGUI.add(new XOR());
        gatesGUI.add(new OR());
        gatesGUI.add(new AND());
        gatesGUI.add(new NOT());
        //gatesGUI.add(new MUX());


        //
        gatesName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                Gate prototype = gatesGUI.get(Integer.parseInt(e.getItem().toString()));
                gatesName.deselect(Integer.parseInt(e.getItem().toString()));
                final Gate chip = prototype.clone();
                chip.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);

                        //when input bus is focused
                        for (int i = 0; i < chip.inputWire.length; i++) {
                            if (chip.inputWire[i].contains(chip.getMousePosition())) {
                                //инвертирование бита
                                if (e.getButton() == 3) {
                                    if (!chip.canBeChanged[i]) {
                                        return;
                                    }

                                    if (chip.getBit(i) == 0) {
                                        chip.setBit(i, (byte) 1);
                                        //chip.myNotify(i, (byte)1 );
                                    } else {
                                        chip.setBit(i, (byte) 0);
                                        //chip.myNotify(i, (byte)0 );
                                    }

                                    return;
                                }//end of right button
                                if (clicked) {
                                    bus.end = chip.inputWire[i].getLocation();
                                    bus.end.x += chip.getX();
                                    bus.end.y += chip.getY() + 2;
                                    chip.setBit(i, bus.getBit());
                                    chip.reCompute(i);
                                    bus.paint(panel.getGraphics());
                                    listOfBuses.add(bus);
                                    chip.canBeChanged[i] = false;
                                    bus.toGates.add(chip);
                                    bus.forGates.add(i);
                                    clicked = false;
                                    System.out.println(i + " input wire has been touched + draw bus");
                                    text += i + " input wire has been touched + draw bus\r\n";
                                    return;
                                }
                                if (!clicked){
                                    clicked = true;
                                    bus = new Connector();
                                    bus.setBit(chip.getBit(i), panel.getGraphics());
                                    forDel = bus.start = chip.inputWire[i].getLocation();
                                    bus.start.x += chip.getX();
                                    bus.start.y += chip.getY() + 2;
                                    chip.busesIn[i].add(bus);
                                    System.out.println(i + " input wire has been touched + start for bus");
                                    text += i + " input wire has been touched + start for bus\r\n";
                                    return;
                                }
                                System.out.println(i + " input wire has been touched + another action");
                                text += i + " input wire has been touched + another action\r\n";
                            }//case of touched input wire
                        }
                        //when output bus is focused
                        for (int i = 0; i < chip.outputWire.length; i++) {
                            if (chip.outputWire[i].contains(chip.getMousePosition())) {
                                if (e.getButton() == 1) {
                                    if (!clicked) {
                                        clicked = true;
                                        bus = new Connector();
                                        bus.setBit(chip.getOut()[i], panel.getGraphics());
                                        forDel = bus.start = chip.outputWire[i].getLocation();
                                        bus.start.x += chip.getX() + chip.outputWire[i].width;
                                        bus.start.y += chip.getY() + chip.outputWire[i].height / 2;
                                        chip.busesOut[i].add(bus);
                                        System.out.println(i + " output wire has been touched + start for bus");
                                        text += i + " output wire has been touched + start for bus\r\n";
                                        return;
                                    }
                                    System.out.println(i + " output wire has been touched + another action");
                                    text += i + " output wire has been touched + another action\r\n";
                                    return;
                                }
                                System.out.println(i + " output wire + another action");
                                text += i + " output wire + another action\r\n";
                            }
                        }

                        //need for changes in location
                        startForChip = new Point(
                                getMousePosition().x - chip.getX(),
                                getMousePosition().y - chip.getY()
                        );
                        System.out.println("chip was touched");
                        text += "chip was touched\r\n";
                    }
                });
                chip.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        //when chip is moved
                        chip.setLocation(
                                getMousePosition().x - startForChip.x,
                                getMousePosition().y - startForChip.y
                        );
                        clicked = false;
                    }
                    @Override
                    public void mouseMoved(MouseEvent e) {
                    }
                });

                //delete chip
                chip.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if (chip.hasFocus() && e.getKeyCode() == KeyEvent.VK_DELETE) {
                            chip.preRemove(listOfBuses);
                            panel.remove(chip);
                            repaint();
                        }
                        clicked = false;
                    }
                });

                //bounds for first adding
                chip.setBounds(
                        panel.getWidth() - chip.getWidth(),
                        panel.getHeight() - chip.getHeight(),
                        chip.getWidth(), chip.getHeight()
                );
                panel.add(chip);
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
               /* if (System.currentTimeMillis() - lastClick < 100) {
                    return;
                }
                lastClick = System.currentTimeMillis();*/
                if (!clicked){
                    final Point center = panel.getMousePosition();
                    for (Connector buffer : listOfBuses) {
                        Rectangle area = buffer.line.getBounds();
                        area.setLocation(area.x - 3, area.y - 3);
                        area.setSize((int)area.getWidth() + 6, (int)area.getHeight() + 6);
                        if (area.contains(center)) {
                            bus = new Connector();
                            bus.setBit(buffer.getBit(), panel.getGraphics());
                            forDel = bus.start = center;
                            buffer.toBuses.add(bus);
                            clicked = true;
                            System.out.println("Click on panel + start bus");
                            text += "Click on panel + start bus\r\n";
                            return;
                        }
                    }
                }
                if (clicked) {
                    bus.end = panel.getMousePosition();
                    bus.paint(panel.getGraphics());
                    listOfBuses.add(bus);
                    clicked = false;
                    System.out.println("click on panel + draw bus");
                    text += "click on panel + draw bus\r\n";
                    return;
                }
                System.out.println("click on panel another action");
                text += "click on panel another action\r\n";
            }
        });//end of MouseListener

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                for (Component chip : panel.getComponents()) {
                    if (chip.getClass() == Gate.class) {
                        /*
                        *
                        * Д
                        * О
                        * Д
                        * Е
                        * Л
                        * А
                        * Т
                        * Ь
                        *
                        *
                        */
                    }
                }
                if (clicked && e.getKeyCode() == KeyEvent.VK_DELETE) {
                    for (Connector bus : listOfBuses) {
                        Rectangle area = bus.line.getBounds();
                        area.setLocation(area.x - 3, area.y - 3);
                        area.setSize((int)area.getWidth() + 6, (int)area.getHeight() + 6);
                        if (area.contains(forDel)) {
                            java.util.List<Connector> forRemove = new ArrayList<Connector>();
                            bus.dispose(listOfBuses, forRemove);
                            listOfBuses.removeAll(forRemove);
                            repaint();
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    clicked = false;
                }
                clicked = false;
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem saveItem = new JMenuItem("Save");
        menu.add(saveItem);
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Decoder save = new Decoder(panel);
                try {
                    save.save();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem loadItem = new JMenuItem("Load");
        menu.add(loadItem);
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parser load = new Parser(panel);
                try {
                    load.load();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                String name = load.getName();
                if (name.equals("default_name")) {
                    return;
                }
                BlackBox obj = load.getBlackBox();
                gatesName.add(name);
                gatesGUI.add(obj);
            }
        });
        JMenuItem refreshItem = new JMenuItem("Refresh");
        menu.add(refreshItem);
        refreshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component component : panel.getComponents()) {
                    if (component.getClass().getSuperclass() == Gate.class) {
                        panel.remove(component);
                    }
                }
                listOfBuses = new ArrayList<Connector>();
                repaint();
            }
        });
        menu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuBar.add(menu);
        setJMenuBar(menuBar);



    }


    @Override
    public void repaint() {
        super.repaint();
        for (Connector bus : listOfBuses) {
            bus.paint(panel.getGraphics());
        }
    }



    public static void main(String[] args) {
        Main m = new Main();
        m.setSize(800, 500);
        m.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        m.setResizable(false);
    }

}
