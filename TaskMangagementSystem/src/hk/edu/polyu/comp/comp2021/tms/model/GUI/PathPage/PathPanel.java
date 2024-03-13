package hk.edu.polyu.comp.comp2021.tms.model.GUI.PathPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathPanel extends JPanel{
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JButton loadPathButton;
	private JButton storePathButton;
	private JButton backToMainPageButton;

	public PathPanel(GUITMS frame){
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		loadPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getLoadPath());
			}
		});
		storePathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getStorePath());
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
