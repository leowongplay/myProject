package hk.edu.polyu.comp.comp2021.tms.model.Main;

import hk.edu.polyu.comp.comp2021.tms.model.Criteria.Criterion;
import hk.edu.polyu.comp.comp2021.tms.model.IOManipulate.FileProcessor;
import hk.edu.polyu.comp.comp2021.tms.model.Task.CompositeTask;
import hk.edu.polyu.comp.comp2021.tms.model.Task.SimpleTask;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static hk.edu.polyu.comp.comp2021.tms.model.Task.Task.*;

/**
 * The Controller of TMS system.
 */
public class Controller {

	/**
	 *Load a txt file containing the data into TMS system
	 * @param pathName the path Name
	 */
	public void loadPath(String pathName) {
		FileProcessor fp = new FileProcessor();
		try {
			fp.CreateFile(pathName);
			fp.loadFileData(pathName, getSimpleTaskList(), getCompositeTaskList(), getCriterionList());
			System.out.println("All changes defined before this command are discarded.\t");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Store the TMS system data into a txt file
	 * @param name filename or pathname
	 * @throws IOException IOException
	 */
	public void storePath(String name) throws IOException {
		FileProcessor fp = new FileProcessor();
		try{
			fp.writeIntoFile(name, getSimpleTaskList(), getCompositeTaskList(), getCriterionList());
		} catch (IOException e){
			throw new RuntimeException(e);
		}

	}

	/**
	 * Create a Primitive Task.
	 * @param name task name
	 * @param description task description
	 * @param duration task duration
	 * @param prerequisites task prerequisites
	 */
	public void createSimpleTask(String name, String description, double duration, List<String> prerequisites) {
		SimpleTask simp = new SimpleTask(name, description, duration, prerequisites);
		simp.createSimpleTask();
	}

	/**
	 * Create a Composite Task.
	 * @param name Composite task name
	 * @param description Task description
	 * @param subtasks The subtasks of the composite task
	 */
	public void createCompositeTask(String name, String description, List<String> subtasks) {
		CompositeTask comp = new CompositeTask(name, description, subtasks);
		comp.createCompositeTask();
	}

	/**
	 * Define a basic criterion. The property is name, description, duration or prerequiites. The operator is <,>,>=,<=,== or !=
	 * @param name criterion name
	 * @param property property
	 * @param op operator
	 * @param value value
	 */
	public void defineBasicCriterion(String name, String property, String op, String value) {
		Criterion cri = new Criterion(name, property, op, value);
		cri.defineBasicCriterion();
	}

	/**
	 * Define a BinaryCriterion.
	 * @param name1 Criterion name
	 * @param name2 Existing criterion name
	 * @param logOp Operator is && or ||
	 * @param name3 Existing criterion name
	 */
	public void defineBinaryCriterion(String name1, String name2, String logOp, String name3) {
		Criterion cri = new Criterion(name1, name2, logOp, name3);
		cri.defineBinaryCriterion();
	}

	/**
	 * Define a Criterion that is a reverse of current existing criteria
	 * @param name1 criteria name
	 * @param name2 existing criteria name
	 */
	public void defineNegatedCriterion(String name1, String name2) {
		Criterion cri = new Criterion(name1, name2, null, null);
		cri.defineNegatedCriterion();

	}

	/**
	 * Print all the existing criteria
	 */
	public void printAllCriteria() {
		Criterion cri = new Criterion(null);
		ArrayList<String> arrList = cri.printAllCriteria();
		if (!arrList.isEmpty()) {
			for (String str : arrList){
				System.out.println(str);
			}
			System.out.println("\t");
		}
		else System.out.println("No criteria defined!\t");
	}

	/**
	 * Search on Tasks that match the criteria
	 * @param name existing criteria
	 */
	public void search(String name) {
		Criterion cri = new Criterion(name);
		Set<String> set1 = cri.searchCriteria(name);
		if (set1.isEmpty()) {
			System.out.println("NO task fulfill this criterion!\t");
		} else {
			for (String s : set1) {
				System.out.println(s);
			}
			System.out.println("\t");
		}
	}

	/**
	 * Change the property of a task
	 * @param name Name of a existing task
	 * @param property name, description, duration, subtask or prerequite of primitive task or composite task
	 * @param newValue New Value
	 */
	public void changeTasks(String name, String property, String newValue) {
		SimpleTask resultS = SimpleTask.returnLineIfSthEqualSimpleTaskName(name);
		CompositeTask resultC = CompositeTask.returnLineIfSthEqualCompositeTaskName(name);
		if (resultS != null) {
			switch (property) {
				case "name":
					System.out.println("Changing the " + property + " of " + name + " to " + newValue +"\t");
					resultS.setName(newValue);
					break;
				case "description":
					System.out.println("Changing the " + property + " of " + name + " to " + newValue +"\t");
					resultS.setDescription(newValue);
					break;
				case "duration":
					System.out.println("Changing the " + property + " of " + name + " to " + newValue +"\t");
					try {
						resultS.setDuration(Double.parseDouble(newValue));
					} catch (NumberFormatException e) {
						System.out.println("Number only!\t");
					}
					break;
				case "prerequisites":
					System.out.println("Changing the " + property + " of " + name + " to " + newValue +"\t");
					resultS.setPrerequisite(Collections.singletonList(newValue));
					break;
				default:
					System.out.println("Invalid property\t");
			}
		} else if (resultC != null) {
			switch (property) {
				case "name":
					System.out.println("Changing the " + property + " of " + name + " to " + newValue +"\t");
					resultC.setName(newValue);
					break;
				case "description":
					System.out.println("Changing the " + property + " of " + name + " to " + newValue +"\t");
					resultC.setDescription(newValue);
					break;
				case "subtasks":
					System.out.println("Changing the " + property + " of " + name + " to " + newValue +"\t");
					resultC.setSubtask(Collections.singletonList(newValue));
					break;
				default:
					System.out.println("Invalid property"+"\t");
			}
		} else {
			System.out.println("No record named "+name+"\t");
		}
	}

	/**
	 * Quit the TMS system
	 */
	public void quit() {
		try {
			System.out.println("Looking forward to your next use :)"+"\t");
			System.exit(0);
		} catch (HeadlessException e) {
			throw new RuntimeException(e);}
	}

	/**
	 * Print the name of all existing Tasks.
	 */
	public void printAllTasks() {
		ArrayList<String> allTasks = new ArrayList<>();
		allTasks.addAll(SimpleTask.printAllSimpleTasks());
		allTasks.addAll(CompositeTask.printAllCompositeTasks());
		if (!allTasks.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String s : allTasks) {
				sb.append(s);
				sb.append("\n");
			}
			System.out.println(sb + "\t");
		}
		else System.out.println("No tasks found!\t");
	}

	/**
	 * Print details of a task
	 * @param str a existing task
	 */
	public void printTask(String str) {
		SimpleTask simp = SimpleTask.returnLineIfSthEqualSimpleTaskName(str);
		CompositeTask comp = CompositeTask.returnLineIfSthEqualCompositeTaskName(str);
		if (simp != null) {
			System.out.println(simp + "\t");
		} else if  (comp != null) {
				System.out.println(comp + "\t");
		}
		else System.out.println("No such task.\t");
	}

	/**
	 * delete a existing task
	 * @param str Name of a existing task
	 */
	public void deleteTask(String str) {
//		SimpleTask simp = SimpleTask.returnLineIfSthEqualSimpleTaskName(str);
		if (SimpleTask.returnLineIfSthEqualSimpleTaskName(str) != null) {
			if (SimpleTask.returnPrerequisite(str) || CompositeTask.returnSubtask(str)) {
				System.out.println("Task exist in prerequisite or subtask!"+"\t");
			}
			else {
				SimpleTask.deleteSimpleTask(str);
				System.out.println("Successfully deleted "+ str + "\t");
			}
		}
		else if (CompositeTask.returnLineIfSthEqualCompositeTaskName(str) != null) {
			if (SimpleTask.returnPrerequisite(str) || CompositeTask.returnSubtask(str)) {
				System.out.println("Task exist in prerequisite or subtask!"+"\t");
			}
			else {
				CompositeTask.deleteCompositeTask(str);
				System.out.println("Successfully deleted "+ str + "\t");
			}
		}
		else {
			System.out.println("No Task Found :("+"\t");
		}
	}

	/**
	 * Print the Duration needed to finish the composite task
	 * @param str A existing composite task name
	 */
	public void reportDuration(String str) {
		CompositeTask comp = CompositeTask.returnLineIfSthEqualCompositeTaskName(str);
		if (comp != null) {
			System.out.println(comp.returnFinalDuration(str) +"\t");
		} else {
			System.out.println("No such task.\t"); // Assume that a task can't have 0 duration
		}
	}

	/**
	 * The earliest finish time of a primitive task. The sum of the duration of the prerequiste of the primitive task.
	 * @param str Name of a existing primitive task
	 */
	public void reportEarliestFinishTime(String str) {
		SimpleTask simp = SimpleTask.returnLineIfSthEqualSimpleTaskName(str);
		if (simp != null) {
			System.out.println(simp.returnEarliestFinishTime(str) +"\t");
		} else {
			System.out.println("No such task.\t"); // Assume that a task can't have 0 duration
		}
	}
}




