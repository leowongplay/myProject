package hk.edu.polyu.comp.comp2021.tms.model.GUI.ChangeAndDeleteTaskPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeTask extends JPanel {
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JLabel nameLabel;
	private JLabel propertyLabel;
	private JLabel newValueLabel;
	private JPanel upperRightPanel;
	private JTextField nameTextField;
	private JTextField newValueTextField;
	private JPanel buttonPanel;
	private JButton backButton;
	private JButton resetButton;
	private JButton enterButton;
	private JComboBox<String> propertyComboBox;

	public ChangeTask(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		propertyComboBox.addItem("name");
		propertyComboBox.addItem("description");
		propertyComboBox.addItem("duration");
		propertyComboBox.addItem("prerequisites");
		propertyComboBox.addItem("subtasks");
		propertyComboBox.setSelectedIndex(0);

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameTextField.getText().equals("") || nameTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, "You are required to fill in every elements",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					Controller controller = new Controller();
					controller.changeTasks(nameTextField.getText(),newValueTextField.getText(), (String) propertyComboBox.getSelectedItem());
				}
			}
		});

        resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				nameTextField.setText("");
				propertyComboBox.setSelectedIndex(0);
				newValueTextField.setText("");
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

