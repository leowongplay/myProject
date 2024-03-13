package hk.edu.polyu.comp.comp2021.tms.model.GUI.SearchAndPrintPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrintTask extends JPanel {
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JLabel inputBox;
	private JButton enterButton;
	private JButton resetButton;
	private JTextField getTaskName;
	private JPanel upperPanel;
	private JLabel NameLabel;
	private JPanel lowerPanel;
	private JButton backButton;

	public PrintTask(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller ct = new Controller();
				ct.printTask(getTaskName.getText());
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getSearchAndPrintPanel());
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inputBox.setText("");
			}
		});
	}
}