package hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public class CreateCompositeTask extends JPanel{
	private GUITMS frame;
	private JButton resetButton;
	private JButton backButton;
	private JButton enterButton;
	private JTextField nameTextField;
	private JTextField descriptionTextField;
	private JTextField subtasksTextField;
	private JPanel backgroundPanel;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JLabel nameLabel;
	private JLabel propertyLabel;
	private JLabel subtasksLabel;
	private JPanel upperRightPanel;
	private JPanel buttonPanel;

	public CreateCompositeTask(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);
		resetButton.doClick();

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				if (nameTextField.getText().equals("") || descriptionTextField.getText().equals("") ||
						    subtasksTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, "You are required to fill in every elements",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					controller.createCompositeTask(nameTextField.getText(), descriptionTextField.getText(),
							Collections.singletonList(subtasksTextField.getText()));
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameTextField.setText("");
				descriptionTextField.setText("");
				subtasksTextField.setText("");
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getCreateTaskAndCriterionPanel());
			}
		});
	}
}
