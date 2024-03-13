package hk.edu.polyu.comp.comp2021.tms.model.GUI.WelcomePage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanel extends JPanel{
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JButton pressToContinue;

	public WelcomePanel(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		pressToContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getMainPanel());
			}
		});
	}
}
