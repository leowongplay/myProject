package hk.edu.polyu.comp.comp2021.tms.model.GUI.ChangeAndDeleteTaskPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeAndDeleteTaskPanel extends JPanel{
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JButton changeButton;
	private JButton deleteButton;
	private JButton backToMainPageButton;

	public ChangeAndDeleteTaskPanel(GUITMS frame){
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		changeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getChangeTask());
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getDeleteTask());
			}
		});

		backToMainPageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getMainPanel());
			}
		});
	}
}
