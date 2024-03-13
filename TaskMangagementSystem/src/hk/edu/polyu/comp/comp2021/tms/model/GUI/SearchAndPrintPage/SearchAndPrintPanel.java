package hk.edu.polyu.comp.comp2021.tms.model.GUI.SearchAndPrintPage;

import hk.edu.polyu.comp.comp2021.tms.model.GUITMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchAndPrintPanel extends JPanel{
	private GUITMS frame;
	private JPanel backgroundPanel;
	private JButton backToMainPageButton;
	private JButton printTaskButton;
	private JButton printAllTasksButton;
	private JButton printAllCriteriaButton1;
	private JButton searchByCriteriaButton;
	private JButton reportDurationButton;
	private JButton reportEarliestFinishTimeButton;

	public SearchAndPrintPanel(GUITMS frame) {
		this.frame = frame;
		backgroundPanel.setPreferredSize(frame.getSize());
		add(backgroundPanel);

		backToMainPageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getMainPanel());
			}
		});
		printTaskButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {frame.switchToPanel(frame.getPrintTask());}

		});
		printAllTasksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				controller.printAllTasks();
//				StringBuilder sb = new StringBuilder();
//				for (String s : arrlist) {
//					sb.append(s);
//					sb.append("\n");
//				}
//				JOptionPane.showMessageDialog(
//						null,  sb.toString(),
//						"Print All Tasks",JOptionPane.PLAIN_MESSAGE);
				//
			}
		});
		printAllCriteriaButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				controller.printAllCriteria(); //{name1 duration > 10, name2 duration < 20......}
				}
		});


		searchByCriteriaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getSearchByCriteria());
			}
		});
		reportDurationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.switchToPanel(frame.getReportDuration());
			}
		});
		reportEarliestFinishTimeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { frame.switchToPanel(frame.getReportEarliestFinishTime());
				//
			}
		});
	}
}
