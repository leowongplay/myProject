package hk.edu.polyu.comp.comp2021.tms.model.GUI.ChangeAndDeleteTaskPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteTask extends JPanel {
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JLabel nameLabel;
	private JPanel upperRightPanel;
	private JTextField nameTextField;
	private JTextField newValueTextField;
	private JPanel buttonPanel;
	private JButton backButton;
	private JButton resetButton;
	private JButton enterButton;

	public DeleteTask(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

//		this.frame = frame;
//		backgroundPanel.setPreferredSize(frame.getSize());
//		add(backgroundPanel);

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, "You are required to fill in every elements",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					String str = nameTextField.getText();
					Controller controller = new Controller();
					// main.deleteTask(nameTextField.getText());
					controller.deleteTask(str);
//					JOptionPane.showMessageDialog(
//							null, str + " Deleted",
//							"Delete",JOptionPane.PLAIN_MESSAGE);
				}
//				JOptionPane.showMessageDialog(
//                null, "No hk.edu.polyu.comp.comp2021.tms.model.Task Found :(",
//                "No hk.edu.polyu.comp.comp2021.tms.model.Task Found",JOptionPane.PLAIN_MESSAGE);
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				nameTextField.setText("");
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				frame.switchToPanel(frame.getChangeAndDeleteTaskPanel());
			}
		});
	}
}

