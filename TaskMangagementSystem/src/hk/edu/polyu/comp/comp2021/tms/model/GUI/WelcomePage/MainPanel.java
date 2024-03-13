
package hk.edu.polyu.comp.comp2021.tms.model.GUI.WelcomePage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;

public class MainPanel extends JPanel{
    private GUITMS frame;
    private JButton seachingPrintingButton;
    private JButton pathButton;
    private JButton createTaskCriterionButton;
    private JButton updateDeleteButton;
    private JPanel backgroundPanel;
    private JButton quitButton;
    private JPanel upperPanel;

    public MainPanel(GUITMS frame) {
        this.frame = frame;
        backgroundPanel.setPreferredSize(frame.getSize());
        add(backgroundPanel);

        createTaskCriterionButton.addActionListener(e -> {
            frame.switchToPanel(frame.getCreateTaskAndCriterionPanel());
        });

        seachingPrintingButton.addActionListener(e -> {
            frame.switchToPanel(frame.getSearchAndPrintPanel());
        });

        pathButton.addActionListener(e -> {
            frame.switchToPanel(frame.getPathPanel());
        });

        updateDeleteButton.addActionListener(e -> {
            frame.switchToPanel(frame.getChangeAndDeleteTaskPanel());
        });

        quitButton.addActionListener(e -> {
            int confirm = JOptionPane.showOptionDialog(
                    null, "Are You Sure to Close Application?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
                Controller controller = new Controller();
                controller.quit();
            }
        });
    }

}
