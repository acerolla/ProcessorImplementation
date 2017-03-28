import chips.BlackBox;
import chips.Connector;
import chips.Gate;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeniy on 21.05.2015.
 */
public class Decoder {

    private JPanel parentPanel;
    private byte[][] ansTable;
    private List<Gate> listOfGates;
    private BlackBox blackBox;

    private List<Gate> inGates;
    private List<Integer> forInGates;
    private List<Gate> outGates;
    private List<Integer> forOutGates;

    private BlackBox createObject() {
        int inCount = 0;
        int outCount = 0;

        inGates = new ArrayList<Gate>();
        forInGates = new ArrayList<Integer>();
        outGates = new ArrayList<Gate>();
        forOutGates = new ArrayList<Integer>();

        for (Component component : parentPanel.getComponents()) {
            if (component.getClass().getSuperclass() == Gate.class) {
                listOfGates.add((Gate) component);
                for (int i = 0; i < ((Gate) component).getInCount(); i++) {
                    if (((Gate) component).canBeChanged[i]) {
                        inCount++;
                        inGates.add((Gate)component);
                        forInGates.add(i);
                    }
                }
                for (int i = 0; i < ((Gate)component).getOutCount(); i++) {
                    if (((Gate)component).busesOut[i].size() == 0) {
                        outCount ++;
                        outGates.add((Gate)component);
                        forOutGates.add(i);
                    }
                }
            }
        }

        ansTable = new byte[(int)Math.pow(2, inCount)][];
        for (int i = 0; i < ansTable.length; i++) {
            ansTable[i] = new byte[inCount + outCount];
        }
        fillTable(inCount);

        blackBox = new BlackBox(inCount, outCount, ansTable);
        return blackBox;
    }

    public Decoder(JPanel panel) {
        parentPanel = panel;
        listOfGates = new ArrayList<Gate>();
    }



    public void save() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(parentPanel);
        File file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        createObject();
        FileOutputStream fos = new FileOutputStream(file.getPath());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(blackBox);
        oos.flush();
        oos.close();
    }

    private void fillTable(int inCount) {
        int pos = inCount - 1;
        for (int i = 1; i < ansTable.length; i++) {
            for (int j = 0; j <= pos; j++) {
                ansTable[i][j] = ansTable[i - 1][j];
            }
            ansTable[i][pos] += 1;
            checkLine(ansTable[i], pos);
        }

        for (int i = 0; i < ansTable.length; i++) {
            for (int j = 0; j < inCount; j++) {
                inGates.get(j).setBitForDecoder(forInGates.get(j), ansTable[i][j]);
            }

            for (int j = inCount; j < ansTable[i].length; j++) {
                ansTable[i][j] = outGates.get(j - inCount).getOut()[forOutGates.get(j - inCount)];
            }
        }
    }

    private void checkLine(byte[] line, int pos) {
        for (int j = pos; j >= 0; j--) {
            if (line[j] == 2) {
                line[j] = 0;
                line[j - 1] += 1;
            }
        }
    }
}
