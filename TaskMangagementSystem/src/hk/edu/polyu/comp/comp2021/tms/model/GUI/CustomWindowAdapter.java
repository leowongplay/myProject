package hk.edu.polyu.comp.comp2021.tms.model.GUI;

import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomWindowAdapter extends WindowAdapter {
	private JFrame frame;
	public CustomWindowAdapter(JFrame frame) {
		this.frame = frame;
	}
	@Override
	public void windowClosing(WindowEvent evt) {
		int confirm = JOptionPane.showOptionDialog(
				null, "Are You Sure to Close Application?",
				"Exit Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (confirm == 0) {
			Controller controller = new Controller();
			controller.quit();
		}
		else {frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);}
	}
}
