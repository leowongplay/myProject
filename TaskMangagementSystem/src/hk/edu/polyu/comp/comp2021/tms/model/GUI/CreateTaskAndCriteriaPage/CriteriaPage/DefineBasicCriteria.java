package hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefineBasicCriteria extends JPanel{
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JTextField nameTextField;
	private JTextField valueTextField;
	private JPanel buttonPanel;
	private JButton backButton;
	private JButton resetButton;
	private JButton enterButton;
	private JLabel propertyLabel;
	private JLabel nameLabel;
	private JLabel opLabel;
	private JLabel valueLabel;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JPanel upperRightPanel;
	private JComboBox<String> propertyComboBox;
	private JComboBox<String> opComboBox;

	public DefineBasicCriteria(GUITMS frame) {

		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);


		propertyComboBox.addItem("name");
		propertyComboBox.addItem("description");
		propertyComboBox.addItem("duration");
		propertyComboBox.addItem("prerequisites");
		propertyComboBox.addItem("subtasks");
		opComboBox.addItem("contains");
		propertyComboBox.setSelectedIndex(0);
		opComboBox.setSelectedIndex(0);

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				if (nameTextField.getText().equals("") || valueTextField.getText().equals("")){
					JOptionPane.showMessageDialog(frame, "You are required to fill in every elements",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					controller.defineBasicCriterion(nameTextField.getText(), (String) propertyComboBox.getSelectedItem(),
							(String) opComboBox.getSelectedItem(), valueTextField.getText());
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameTextField.setText("");
				valueTextField.setText("");
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getCreateTaskAndCriterionPanel());
			}
		});
		propertyComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (propertyComboBox.getSelectedItem().equals("duration")){
					opComboBox.removeAllItems();
					opComboBox.addItem(">");
					opComboBox.addItem("<");
					opComboBox.addItem(">=");
					opComboBox.addItem("<=");
					opComboBox.addItem("==");
					opComboBox.addItem("!=");
				}
				else if (propertyComboBox.getSelectedItem().equals("name") || propertyComboBox.getSelectedItem().equals("description") ||
						         propertyComboBox.getSelectedItem().equals("prerequisites")||
						         propertyComboBox.getSelectedItem().equals("subtasks")){
					opComboBox.removeAllItems();
					opComboBox.addItem("contains");
				}
			}
		});

	}
}
