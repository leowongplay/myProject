package hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public class CreateSimpleTask extends JPanel{
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JButton resetButton;
	private JButton backButton;
	private JButton enterButton;
	private JTextField nameTextField;
	private JTextField descriptionTextField;
	private JTextField durationTextField;
	private JTextField prerequisitesTextField;
	private JPanel buttonPanel;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JLabel nameLabel;
	private JLabel propertyLabel;
	private JLabel opLabel;
	private JLabel valueLabel;
	private JPanel upperRightPanel;

	public CreateSimpleTask(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);
		resetButton.doClick();

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					double d = Double.parseDouble(durationTextField.getText());
					Controller controller = new Controller();
					if (nameTextField.getText().equals("") || descriptionTextField.getText().equals("") ||
							    durationTextField.getText().equals("")) {
						JOptionPane.showMessageDialog(frame, "Name, description and duration are required",
								"Warning", JOptionPane.WARNING_MESSAGE);
					} else if (prerequisitesTextField.getText().equals("")) {
						controller.createSimpleTask(nameTextField.getText(), descriptionTextField.getText(),
								Double.parseDouble(durationTextField.getText()), null);
					} else {
						controller.createSimpleTask(nameTextField.getText(), descriptionTextField.getText(),
								Double.parseDouble(durationTextField.getText()),
								Collections.singletonList(prerequisitesTextField.getText()));
					}
				}
				catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(
							null, "Duration can only be a real number.",
							"WARNING!!!",JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameTextField.setText("");
				descriptionTextField.setText("");
				durationTextField.setText("");
				prerequisitesTextField.setText("");
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