package hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefineCriteriaPanel extends JPanel{
	private GUITMS frame;
	private JButton basicButton;
	private JButton negatedButton;
	private JButton binaryButton;
	private JButton backButton;
	private JPanel backgroundPanel;

	public DefineCriteriaPanel(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		basicButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getDefineBasicCriteria());
			}
		});
		negatedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getDefineNegatedCriteria());
			}
		});
		binaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getDefineBinaryCriteria());
			}
		});
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getMainPanel());
			}
		});
	}
}
