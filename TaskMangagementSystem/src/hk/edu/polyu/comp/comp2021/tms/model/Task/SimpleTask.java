package hk.edu.polyu.comp.comp2021.tms.model.Task;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The `SimpleTask` class represents a simple task, which is a basic unit of work with a name, description,
 * duration, and optional prerequisites. It extends the `Task` class and provides functionality for managing
 * and processing simple tasks.
 */
public class SimpleTask extends Task {
	private String description;
	private List<String> prerequisites;
	private double duration;
	/**
	 * Constructs a new `SimpleTask` object with the specified name, description, duration, and list of prerequisites.
	 *
	 * @param name          The name of the simple task.
	 * @param description   The description of the simple task.
	 * @param duration      The duration of the simple task.
	 * @param prerequisites The list of prerequisites for the simple task.
	 */

	public SimpleTask(String name, String description, double duration, List<String> prerequisites) {
		super(name);
		this.description = description;
		this.duration = duration;
		this.prerequisites = prerequisites;
	}
	/**
	 * Returns the description of the simple task.
	 *
	 * @return The description of the simple task.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Returns the duration of the simple task.
	 *
	 * @return The duration of the simple task.
	 */
	public double getDuration() {
		return duration;
	}
	/**
	 * Returns a string representation of the prerequisites for the simple task.
	 *
	 * @return A string containing the names of the prerequisites separated by commas.
	 */
	public String getPrerequisitesString() {
		if (this.prerequisites != null) {
			StringBuilder sb = new StringBuilder();
			for (String temp : this.prerequisites) {
				sb.append(temp).append(",");
			}
			return sb.substring(0, sb.length() - 1);
		}
		return "";
	}
	/**
	 * Returns the list of prerequisites for the simple task.
	 *
	 * @return The list of prerequisites.
	 */
	public List<String> getPrerequisites() {
		return prerequisites;
	}
	/**
	 * Sets the duration of the simple task to the specified value.
	 *
	 * @param newValue The new duration for the simple task.
	 */
	public void setDuration(double newValue) {
		if (duration_checking(newValue))
			this.duration = newValue;
	}

	/**
	 * Sets the list of prerequisites for the simple task to the specified value.
	 *
	 * @param newValue The new list of prerequisites for the simple task.
	 */
	public void setPrerequisite(List<String> newValue) {
		if (prerequisites_checking(newValue))
			this.prerequisites = newValue;
	}
	/**
	 * Sets the description of the simple task to the specified value.
	 *
	 * @param newValue The new description for the simple task.
	 */
	public void setDescription(String newValue) {
		if (description_checking(newValue))
			this.description = newValue;
	}
	/**
	 * Creates a new simple task based on the current simple task and adds it to the simple task list.
	 * Prints a success message if the creation is successful.
	 */

	public void createSimpleTask() {
		try {
			if (this.checkSimpleTask()) {
				getSimpleTaskList().add(new SimpleTask(this.getName(), this.getDescription(), this.getDuration(), this.getPrerequisites()));
				System.out.println("Successfully created " + this.getName() +"\t");
			}
		} catch (HeadlessException e) {
			errorMessage();
		}
	}
	/**
	 * Returns `true` if the duration is greater than 0, prints an error message and returns `false` otherwise.
	 *
	 * @param duration The duration to be checked.
	 * @return `true` if the duration is valid, `false` otherwise.
	 */
	private boolean duration_checking(double duration) {
		double Check_target = duration;
		if (Check_target <= 0) {
			System.out.println("The value of duration must > 0\t");
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Checks if the list of prerequisites is valid by comparing it with the existing tasks (both simple and composite).
	 * Prints an error message if some or all of the prerequisites are not defined.
	 *
	 * @param prerequisites The list of prerequisites to be checked.
	 * @return `true` if the prerequisites are valid, `false` otherwise.
	 */
	private boolean prerequisites_checking(List<String> prerequisites) {
		ArrayList<String> nameList = new ArrayList<>();
		for (SimpleTask simpleTask : getSimpleTaskList()){
			nameList.add(simpleTask.getName());
		}
		for (CompositeTask compositeTask : getCompositeTaskList()){
			nameList.add(compositeTask.getName());
		}
		if (prerequisites != null) {
			if (nameList.isEmpty() || !nameList.containsAll(prerequisites)) {
				System.out.println("Some(All) of the prerequisites are not defined.\t");
				return false;
			}
		}
		return true;
	}
	/**
	 * Checks if the simple task is valid by ensuring that its name, description, duration, and prerequisites are valid.
	 * Prints an error message if the simple task is not valid or if it already exists.
	 *
	 * @return `true` if the simple task is valid, `false` otherwise.
	 */
	public boolean checkSimpleTask() {
		if (!checkNameValid(this.getName()) || !description_checking(this.getDescription()) ||
				    !duration_checking(this.getDuration()) || !prerequisites_checking(this.getPrerequisites())) {
			return false;
		}
		if (checkNameExist(this.getName())) {
			System.out.println(this.getName() + " Already Exist!\t");
			return false;
		}
		return true;
	}
	/**
	 * Prints the names of all simple tasks in the `simpleTaskList`.
	 *
	 * @return An `ArrayList` containing the names of all simple tasks.
	 */
	public static ArrayList<String> printAllSimpleTasks() {
		ArrayList<String> tasklist = new ArrayList<>();

		for (SimpleTask tempTask : getSimpleTaskList()) {
			tasklist.add(tempTask.getName());
		}
		return tasklist;
	}
	/**
	 * Returns the simple task with a name equal to the specified string, or `null` if not found.
	 *
	 * @param str The name of the simple task to search for.
	 * @return The simple task with the specified name, or `null` if not found.
	 */
	public static SimpleTask returnLineIfSthEqualSimpleTaskName(String str) {
		SimpleTask task = null;
		for (SimpleTask tempTask : getSimpleTaskList()) { // return a list name 'task' if equal to the name we are searching
			if (str.equals(tempTask.getName())) {
				task = tempTask;
				break;
			}
		}
		return task;
	}
	/**
	 * Checks if the specified task is a prerequisite of any simple task.
	 *
	 * @param str The name of the task to check as a prerequisite.
	 * @return `true` if the task is a prerequisite of any simple task, `false` otherwise.
	 */
	public static boolean returnPrerequisite(String str) {
		for (SimpleTask tempTask : getSimpleTaskList()) { // return a list name 'task' if equal to the name we are searching
			if (tempTask.getPrerequisites() != null) {
				if (tempTask.getPrerequisites().contains(str)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Deletes the simple task with the specified name from the `simpleTaskList`.
	 *
	 * @param str The name of the simple task to be deleted.
	 */
	public static void deleteSimpleTask(String str) {
		SimpleTask simp = SimpleTask.returnLineIfSthEqualSimpleTaskName(str);
		if (simp != null) {
			getSimpleTaskList().remove(simp);
		}

	}
	/**
	 * Calculates and returns the earliest finish time of the simple task.
	 * The earliest finish time is the sum of the task's duration and the durations of its prerequisites.
	 *
	 * @param str The name of the simple task.
	 * @return The earliest finish time of the simple task.
	 */
	public double returnEarliestFinishTime(String str) {
		SimpleTask resultS = returnLineIfSthEqualSimpleTaskName(str);
		double totalDuration = 0;
		totalDuration = resultS.getDuration(); // convert string to double
		if (resultS.getPrerequisites() != null) {
			List<String> subtasks = resultS.getPrerequisites(); //{task1,task2,task3}
			for (int j = 0; j < subtasks.size(); j++) {
				SimpleTask tempResult = SimpleTask.returnLineIfSthEqualSimpleTaskName(subtasks.get(j));
				totalDuration += tempResult.getDuration();
			}
		}
		return totalDuration;
	}

	public String toString() {
		if (this.getPrerequisitesString().equals("")) {
			return super.getName() + " " + this.getDescription() + " " + this.getDuration();
		}
		return super.getName() + " " + this.getDescription() + " " + this.getDuration() + " " + this.getPrerequisitesString();
	}
}
