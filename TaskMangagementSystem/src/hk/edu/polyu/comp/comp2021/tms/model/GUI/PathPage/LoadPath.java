package hk.edu.polyu.comp.comp2021.tms.model.GUI.PathPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadPath extends JPanel{
    private GUITMS frame;
    private JPanel upperPanel;
    private JPanel upperRightPanel;
    private JTextField nameTextField;
    private JPanel upperLeftPanel;
    private JLabel nameLabel;
    private JPanel buttonPanel;
    private JButton backButton;
    private JButton resetButton;
    private JButton enterButton;
    private JPanel backgroundPanel;

    public LoadPath(GUITMS frame) {
        this.frame = frame;
        backgroundPanel.setPreferredSize(frame.getSize());
        add(backgroundPanel);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller controller = new Controller();

                String str = nameTextField.getText();
                controller.loadPath(str);

            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.switchToPanel(frame.getPathPanel());
            }
        });
    }
}
