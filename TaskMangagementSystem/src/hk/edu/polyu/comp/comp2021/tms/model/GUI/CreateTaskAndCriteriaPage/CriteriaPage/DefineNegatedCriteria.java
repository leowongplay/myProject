package hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefineNegatedCriteria extends JPanel{
	private GUITMS frame;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JLabel nameLabel;
	private JLabel name2Label;
	private JPanel upperRightPanel;
	private JTextField nameTextField;
	private JTextField name1TextField;
	private JPanel buttonPanel;
	private JButton backButton;
	private JButton resetButton;
	private JButton enterButton;
	private JPanel backgroundPanel;

	public DefineNegatedCriteria(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				if (nameTextField.getText().equals("") || name1TextField.getText().equals("")){
					JOptionPane.showMessageDialog(frame, "You are required to fill in every elements",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					controller.defineNegatedCriterion(nameTextField.getText(), name1TextField.getText());
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameTextField.setText("");
				name1TextField.setText("");
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getChangeAndDeleteTaskPanel());
			}
		});

	}
}