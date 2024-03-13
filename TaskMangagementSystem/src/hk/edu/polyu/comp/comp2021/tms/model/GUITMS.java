package hk.edu.polyu.comp.comp2021.tms.model;

import hk.edu.polyu.comp.comp2021.tms.model.GUI.ChangeAndDeleteTaskPage.ChangeAndDeleteTaskPanel;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.ChangeAndDeleteTaskPage.ChangeTask;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.ChangeAndDeleteTaskPage.DeleteTask;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CreateCompositeTask;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CreateSimpleTask;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CreateTaskAndCriterionPanel;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage.DefineBasicCriteria;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage.DefineBinaryCriteria;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage.DefineCriteriaPanel;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CreateTaskAndCriteriaPage.CriteriaPage.DefineNegatedCriteria;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.CustomWindowAdapter;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.GuiOutputStream;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.PathPage.LoadPath;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.PathPage.PathPanel;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.PathPage.StorePath;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.SearchAndPrintPage.*;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.WelcomePage.MainPanel;
import hk.edu.polyu.comp.comp2021.tms.model.GUI.WelcomePage.WelcomePanel;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * Main of GUI
 */
public class GUITMS extends JFrame {
    private JPanel LoadPath;
	private JPanel currentPanel;
	private JPanel WelcomePanel;
	private JPanel MainPanel;
	private JPanel CreateTaskAndCriterionPanel;
	private JPanel SearchAndPrintPanel;
	private JPanel ChangeAndDeleteTaskPanel;
	private JPanel ChangeTask;
	private JPanel DeleteTask;
	private JPanel CreateSimpleTask;
	private JPanel DefineCriteriaPanel;
	private JPanel CreateCompositeTask;
	private JPanel DefineBasicCriteria;
	private JPanel DefineBinaryCriteria;
	private JPanel DefineNegatedCriteria;
	private JPanel PrintTask;
	private JPanel SearchByCriteria;
	private JPanel ReportDuration;
	private JPanel ReportEarliestFinishTime;
	private JPanel PathPanel;
	private JPanel StorePath;

	/**
	 * Set up of the GUI frame
	 */
	public GUITMS() {
		setTitle("Group12 TMS");
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		// 設定視窗大小佔螢幕四分之一
		setSize(dimension.width / 2, dimension.height / 2);
		addWindowListener(new CustomWindowAdapter(this));
		WelcomePanel = new WelcomePanel(this);
		MainPanel = new MainPanel(this);

		CreateTaskAndCriterionPanel = new CreateTaskAndCriterionPanel(this);
		CreateSimpleTask = new CreateSimpleTask(this);
		CreateCompositeTask = new CreateCompositeTask(this);
		DefineBasicCriteria = new DefineBasicCriteria(this);
		DefineBinaryCriteria = new DefineBinaryCriteria(this);
		DefineNegatedCriteria = new DefineNegatedCriteria(this);
		DefineCriteriaPanel = new DefineCriteriaPanel(this);

		SearchAndPrintPanel = new SearchAndPrintPanel(this);
		PrintTask = new PrintTask(this);
		SearchByCriteria = new SearchByCriteria(this);
		ReportDuration = new ReportDuration(this);
		ReportEarliestFinishTime = new ReportEarliestFinishTime(this);

		ChangeAndDeleteTaskPanel = new ChangeAndDeleteTaskPanel(this);
		DeleteTask = new DeleteTask(this);
		ChangeTask = new ChangeTask(this);


		PathPanel = new PathPanel(this);
		LoadPath = new LoadPath(this);
		StorePath = new StorePath(this);

		currentPanel = WelcomePanel;
		setContentPane(currentPanel);
		pack();
		setLocationRelativeTo(null);            //設定視窗顯示在螢幕畫面中間位置
		setVisible(true);
	}

	public JPanel getLoadPath() {
		return LoadPath;
	}

	public JPanel getCurrentPanel() {
		return currentPanel;
	}

	public JPanel getWelcomePanel() {
		return WelcomePanel;
	}

	public JPanel getMainPanel() {
		return MainPanel;
	}

	public JPanel getCreateTaskAndCriterionPanel() {
		return CreateTaskAndCriterionPanel;
	}

	public JPanel getSearchAndPrintPanel() {
		return SearchAndPrintPanel;
	}

	public JPanel getChangeAndDeleteTaskPanel() {
		return ChangeAndDeleteTaskPanel;
	}

	public JPanel getChangeTask() {
		return ChangeTask;
	}

	public JPanel getDeleteTask() {
		return DeleteTask;
	}

	public JPanel getCreateSimpleTask() {
		return CreateSimpleTask;
	}

	public JPanel getDefineCriteriaPanel() {
		return DefineCriteriaPanel;
	}

	public JPanel getCreateCompositeTask() {
		return CreateCompositeTask;
	}

	public JPanel getDefineBasicCriteria() {
		return DefineBasicCriteria;
	}

	public JPanel getDefineBinaryCriteria() {
		return DefineBinaryCriteria;
	}

	public JPanel getDefineNegatedCriteria() {
		return DefineNegatedCriteria;
	}

	public JPanel getPrintTask() {
		return PrintTask;
	}

	public JPanel getSearchByCriteria() {
		return SearchByCriteria;
	}

	public JPanel getReportDuration() {
		return ReportDuration;
	}

	public JPanel getReportEarliestFinishTime() {
		return ReportEarliestFinishTime;
	}

	public JPanel getPathPanel() {
		return PathPanel;
	}

	public JPanel getStorePath() {
		return StorePath;
	}

	public void switchToPanel(JPanel panel) {
		try {
			setContentPane(panel);
			currentPanel = panel;
			revalidate();
			repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,"Page can't change!","Error!",JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(() -> {
				System.setOut(new PrintStream(new GuiOutputStream(), true));
				new GUITMS();
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
