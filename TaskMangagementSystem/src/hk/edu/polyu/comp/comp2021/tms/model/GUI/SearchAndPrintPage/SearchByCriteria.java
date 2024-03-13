package hk.edu.polyu.comp.comp2021.tms.model.GUI.SearchAndPrintPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchByCriteria extends JPanel {
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JPanel upperPanel;
	private JPanel upperLeftPanel;
	private JLabel nameLabel;
	private JPanel upperRightPanel;
	private JTextField nameTextField;
	private JPanel buttonPanel;
	private JButton backButton;
	private JButton resetButton;
	private JButton enterButton;

	public SearchByCriteria(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				controller.search(nameTextField.getText());
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
				frame.switchToPanel(frame.getSearchAndPrintPanel());
			}
		});
	}
}
