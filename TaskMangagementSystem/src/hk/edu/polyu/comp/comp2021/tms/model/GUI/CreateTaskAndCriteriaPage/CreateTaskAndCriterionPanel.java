package hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateTaskAndCriterionPanel extends JPanel{
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JButton backToMainPageButton;
	private JButton simpleTaskButton;
	private JButton compositeTaskButton;
	private JButton defindCriteriaButton;
	private JButton quitButton;

	public CreateTaskAndCriterionPanel(GUITMS frame){
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		backToMainPageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getMainPanel());
			}
		});
		simpleTaskButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getCreateSimpleTask());
			}
		});
		compositeTaskButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getCreateCompositeTask());
			}
		});
		defindCriteriaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getDefineCriteriaPanel());
			}
		});
	}
}
