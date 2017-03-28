import chips.BlackBox;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Evgeniy on 21.05.2015.
 */
public class Parser {

    private JPanel parentPanel;
    private BlackBox blackBox;
    private String name;

    public Parser(JPanel panel) {
        this.parentPanel = panel;
        name = "default_name";
    }

    public void load() throws IOException, ClassNotFoundException {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(parentPanel);
        File file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        name = file.getName();
        try {
            FileInputStream fis = new FileInputStream(file.getPath());
            ObjectInputStream oin = new ObjectInputStream(fis);
            blackBox = (BlackBox) oin.readObject();
        }
        catch (Exception ex) {
            name = "default_name";
        }

    }

    public BlackBox getBlackBox() {
        return blackBox;
    }

    public String getName() {
        return name;
    }
}
