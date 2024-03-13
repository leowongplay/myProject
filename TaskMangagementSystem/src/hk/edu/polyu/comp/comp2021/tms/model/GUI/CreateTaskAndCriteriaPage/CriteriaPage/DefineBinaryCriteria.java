package hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefineBinaryCriteria extends JPanel{
	private GUITMS frame;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JLabel nameLabel;
	private JLabel name2Label;
	private JLabel logopLabel;
	private JLabel name1Label;
	private JPanel upperRightPanel;
	private JTextField nameTextField;
	private JTextField name2TextField;
	private JPanel buttonPanel;
	private JButton backButton;
	private JButton resetButton;
	private JButton enterButton;
	private JTextField name1TextField;
	private JRadioButton orRadioButton;
	private JRadioButton andRadioButton;
	private JPanel backgroundPanel;

	public DefineBinaryCriteria(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				if (name1TextField.getText().equals("") || name2TextField.getText().equals("") ||
						    nameTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, "You are required to fill in every elements",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					if (andRadioButton.isSelected()) {
						controller.defineBinaryCriterion(nameTextField.getText(), name1TextField.getText(),
								"&&", name2TextField.getText());
					} else if (orRadioButton.isSelected()) {
						controller.defineBinaryCriterion(nameTextField.getText(), name1TextField.getText(),
								"||", name2TextField.getText());
					}
				}
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getCreateTaskAndCriterionPanel());
			}
		});
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameTextField.setText("");
				name1TextField.setText("");
				name2TextField.setText("");
			}
		});
	}
}
