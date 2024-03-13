package hk.edu.polyu.comp.comp2021.tms.model.GUI;

import javax.swing.*;
import java.io.OutputStream;

public class GuiOutputStream extends OutputStream {
    private StringBuilder stringBuilder;
    public GuiOutputStream() {
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public void write(int data) {
        if (data == '\t') {
            // Show the text in a JOptionPane
            JOptionPane.showMessageDialog(null, stringBuilder.toString(),"Group12 GUI TMS",
                    JOptionPane.PLAIN_MESSAGE);
            // Reset the StringBuilder for the next sentence
            stringBuilder.setLength(0);
        } else {
            // Append the character to the StringBuilder
            stringBuilder.append((char) data);
        }
    }
}
