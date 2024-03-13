package hk.edu.polyu.comp.comp2021.tms.model.Criteria;

import hk.edu.polyu.comp.comp2021.tms.model.Task.CompositeTask;
import hk.edu.polyu.comp.comp2021.tms.model.Task.SimpleTask;
import hk.edu.polyu.comp.comp2021.tms.model.Task.Task;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Criterion class
 */
public class Criterion extends Task {
	private String property;
	private String op;
	private String value;
	private static ArrayList<String> negatedList = new ArrayList<>();

	/**
	 * Constructor of criterion
	 *
	 * @param name     Criteria Name
	 * @param property name, description, duration, subtask or prerequite of primitive task or composite task
	 * @param op       <,>,>=,<=,== or !=
	 * @param value    Threshold value
	 */
	public Criterion(String name, String property, String op, String value) {
		super(name);
		this.property = property;
		this.op = op;
		this.value = value;
	}

	/**
	 * Constructor
	 *
	 * @param name criteria name
	 */
	public Criterion(String name) {
		super(name);
	}

	/**
	 * get property
	 *
	 * @return this.property
	 */
	public String getProperty() {
		return this.property;
	}

	/**
	 * get OP
	 *
	 * @return OP
	 */
	public String getOp() {
		if (this.op == null)
			return "";
		return this.op;
	}

	/**
	 * get getNegatedList
	 *
	 * @return NegatedList
	 */
	public static ArrayList<String> getNegatedList() {
		return negatedList;
	}

	/**
	 * get value
	 *
	 * @return value
	 */
	public String getValue() {
		if (this.value == null)
			return "";
		return this.value;
	}

	/**
	 * set property
	 *
	 * @param property new property
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * set op
	 *
	 * @param op new op
	 */
	public void setOp(String op) {
		this.op = op;
	}

	/**
	 * set value
	 *
	 * @param value new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return this.getName() + " " + this.getProperty() + " " + this.getOp() + " " + this.getValue();
	}

	/**
	 * define basic criteria
	 */
	public void defineBasicCriterion() {
		try {
			if (this.checkDefineBasicCriterion()) {
				getCriterionList().add(new Criterion(this.getName(), this.getProperty(), this.getOp(), this.getValue()));
				System.out.println("Successfully defined " + this.getName() + "\t");
			}
		} catch (HeadlessException e) {
			errorMessage();
		}
	}

	/**
	 * Check if a task is primitive
	 *
	 * @return true if a task is primitive
	 */
	public boolean isPrimitive() {
		try {
			String checkName = this.getValue();
			if (this.getOp().equals("contains")) {
				if (checkName.charAt(0) == '"') { // "name1"
					checkName = checkName.substring(1, checkName.length() - 1); // erase ""
					for (SimpleTask task : getSimpleTaskList()) {
						if (task.getName().equals(checkName)) {
							return true;
						}
					}
				} else { // simpleName1,compositeName2,name3...
					String[] checkNameList = checkName.split(",");
					List<String> simpleTaskName = new ArrayList<>();
					for (SimpleTask task : getSimpleTaskList()) {
						simpleTaskName.add(task.getName());
					}
					if (simpleTaskName.containsAll(Arrays.asList(checkNameList))) {
						return true;
					}
				}
			}
		} catch (Exception e) {errorMessage();}
		return false;
	}

	/**
	 * reture all criteria name
	 *
	 * @return arraylist carrying all criteria name
	 */
	public ArrayList<String> printAllCriteria() {
		ArrayList<String> arrList = new ArrayList<String>();
		try {
			for (Criterion criterion : getCriterionList()) {
				switch (criterion.getProperty()) {
					case "name":
					case "prerequisites":
						case "subtasks":
						if (criterion.getOp().equals("contains"))
							arrList.add(criterion.toString() + " " + criterion.isPrimitive());
						else arrList.add(criterion.toString());
						break;
					default:
						arrList.add(criterion.toString());
						break;
				}
			}
		} catch (Exception e) {errorMessage();}
		return arrList;
	}

	/**
	 * define a negated criterion.
	 */
	public void defineNegatedCriterion() {
		try {
			if (this.checkDefineNegatedCriterion()) {
				getCriterionList().add(new Criterion(this.getName(), this.getProperty(), null, null));
				if (negatedList.contains(this.getProperty())) {
					negatedList.remove(this.getProperty());
					System.out.println("The criterion " + this.getProperty() + " was a negated criterion, but now ready to use.\t");
				}
				else {
					negatedList.add(this.getProperty());
					System.out.println("Successfully defined " + this.getName() + "\t");
				}
			}
		} catch (HeadlessException e) {errorMessage();}
	}

	/**
	 * define a binary criterion
	 */
	public void defineBinaryCriterion() {
		try {
			if (this.checkDefineBinaryCriterion()) {
				getCriterionList().add(new Criterion(this.getName(), this.getProperty(), this.getOp(), this.getValue()));
				System.out.println("Successfully defined " + this.getName() + "\t");
			}
		} catch (HeadlessException e) {errorMessage();}

	}

	/**
	 * Check if a criteria exist
	 *
	 * @param str name
	 * @return true if str exist in criteria list
	 */
	public static Criterion returnLineIfSthEqualCriterionName(String str) {
		Criterion task = null;
		for (Criterion tempTask : getCriterionList()) {     // return a list name 'task' if equal to the name we are searching
			if (str.equals(tempTask.getName())) {
				task = tempTask;
				break;
			}
		}
		return task;
	}

	/**
	 * the binary criteria execution
	 *
	 * @param name criteria name
	 * @return task list
	 */
	public Set<String> searchCriteria(String name) {
		Criterion task = returnLineIfSthEqualCriterionName(name); // the criteria we are using
		Set<String> set1 = new HashSet<>();
		if (task == null) return set1;
		if (checkIfNegated(task)) return set1;
		try {
			if (task.getOp().equals("&&") || task.getOp().equals("||")) {
				Criterion task1 = returnLineIfSthEqualCriterionName(task.getProperty()); // the criteria of name2
				Criterion task2 = returnLineIfSthEqualCriterionName(task.getValue()); // the criteria of name3
				// the tasks that fulfill the criteria name2
				findWithCriteria(task1, set1);
				// the tasks that fulfill the criteria name3
				Set<String> set2 = new HashSet<>();
				findWithCriteria(task2, set2);
				if (task.getOp().equals("&&")) {
					set1.retainAll(set2);
				} // intersection oof two sets
				else set1.addAll(set2);    // union oof two sets
			} else {
				Criterion task1 = returnLineIfSthEqualCriterionName(task.getName()); // the criteria we are using
				findWithCriteria(task1, set1);
			}
		} catch (Exception e) {errorMessage();}
		return set1;
	}

	private boolean checkIfNegated(Criterion criterion) {
		try {
			if (negatedList.contains(criterion.getName())) {
				return true;
			}
			if (criterion.getOp() == null && criterion.getValue() == null) {
				return true;
			}
		} catch (Exception e) {errorMessage();}
		return false;
	}

	private void findWithCriteria(Criterion task, Set<String> set) {
		try {
			if (task == null) return;
			if (checkIfNegated(task)) return;
			try {
				if (task.getOp().equals("||") || task.getOp().equals("&&")) {
					Criterion task1 = returnLineIfSthEqualCriterionName(task.getProperty()); // the criteria of name2
					Criterion task2 = returnLineIfSthEqualCriterionName(task.getValue()); // the criteria of name3
					findWithCriteria(task1, set);
					Set<String> set2 = new HashSet<>();
					findWithCriteria(task2, set2);
					if (task.getOp().equals("&&")) {
						set.retainAll(set2);
					} // intersection oof two sets
					else set.addAll(set2);
				}
			} catch (Exception e) {
				System.out.println("Error!\t");
				return;
			}
			switch (task.getProperty()) {
				case "duration":
					switch (task.getOp()) {
						case ">":
							for (SimpleTask simpleTask : getSimpleTaskList()) {
								if (simpleTask.getDuration() > Double.parseDouble(task.getValue())) {
									set.add(simpleTask.toString());
								}
							}
							break;
						case "<":
							for (SimpleTask simpleTask : getSimpleTaskList()) {
								if (simpleTask.getDuration() < Double.parseDouble(task.getValue())) {
									set.add(simpleTask.toString());
								}
							}
							break;
						case ">=":
							for (SimpleTask simpleTask : getSimpleTaskList()) {
								if (simpleTask.getDuration() >= Double.parseDouble(task.getValue())) {
									set.add(simpleTask.toString());
								}
							}
							break;
						case "<=":
							for (SimpleTask simpleTask : getSimpleTaskList()) {
								if (simpleTask.getDuration() <= Double.parseDouble(task.getValue())) {
									set.add(simpleTask.toString());
								}
							}
							break;
						case "==":
							for (SimpleTask simpleTask : getSimpleTaskList()) {
								if (Double.parseDouble(task.getValue()) == simpleTask.getDuration()) {
									set.add(simpleTask.toString());
								}
							}
							break;
						case "!=":
							for (SimpleTask simpleTask : getSimpleTaskList()) {
								if (Double.parseDouble(task.getValue()) != simpleTask.getDuration()) {
									set.add(simpleTask.toString());
								}
							}
							break;
					}
					break;
				case "name":
					for (SimpleTask simpleTask : getSimpleTaskList()) {
						if (simpleTask.getName().equals(task.getValue().substring(1, task.getValue().length() - 1))) {
							set.add(simpleTask.toString());
						}
					}
					for (CompositeTask compositeTask : getCompositeTaskList()) {
						if (compositeTask.getName().equals(task.getValue().substring(1, task.getValue().length() - 1))) {
							set.add(compositeTask.toString());
						}
					}
					break;
				case "description":
					for (SimpleTask simpleTask : getSimpleTaskList()) {
						if (simpleTask.getDescription().equals(task.getValue().substring(1, task.getValue().length() - 1))) {
							set.add(simpleTask.toString());
						}
					}
					for (CompositeTask compositeTask : getCompositeTaskList()) {
						if (compositeTask.getDescription().equals(task.getValue().substring(1, task.getValue().length() - 1))) {
							set.add(compositeTask.toString());
						}
					}
					break;
				case "prerequisites":
					String[] taskPrerequisites = task.getValue().split(",");
					for (SimpleTask simpleTask : getSimpleTaskList()) {
						List<String> simpleTaskPrerequisites = new ArrayList<>(Arrays.asList(simpleTask.getPrerequisitesString().split(",")));
						if (simpleTaskPrerequisites.containsAll(Arrays.asList(taskPrerequisites))) {
							set.add(simpleTask.toString());
						}
					}
					break;
				case "subtasks":
					String[] taskSubtasks = task.getValue().split(",");
					for (CompositeTask compositeTask : getCompositeTaskList()) {
						List<String> compositeSubtasks = new ArrayList<>(Arrays.asList(compositeTask.getSubtasksString().split(",")));
						if (compositeSubtasks.containsAll(Arrays.asList(taskSubtasks))) {
							set.add(compositeTask.toString());
						}
					}
					break;
			}
		} catch (Exception e) {errorMessage();}
	}

	/**
	 * Check the basic criterion meet the creation requirement
	 *
	 * @return true if no input error
	 */
	public boolean checkDefineBasicCriterion() {
		if (!checkNameValid(this.getName())) {
			return false;
		}
		if (!checkNameExist(this.getName())) {
			switch (this.getProperty()) {
				case "duration":
					if (checkOpIsSymbol(this.getOp()) && checkIsNumeric(this.getValue())) {
						return true;
					}
					break;
				case "name":
					case "description":
					if (checkValue(this.getValue()) && checkOpIsContains(this.getOp())) {
						return true;
					}
					break;
				case "prerequisites":
					case "subtasks":
					if (checkPrerequisites(this.getValue()) && checkOpIsContains(this.getValue())) {
						return true;
					}
					break;
				default:
					System.out.println(" Invalid 'property' input!"+
							"It can only be duration, name, description or prerequisites."+
							"Please input again!\t");

					break;
			}
		} else {
			System.out.println("The name " + this.getName() + " is already existed!\t");
		}
		return false;
	}

	private boolean checkOpIsSymbol(String op) {
		if (op.equals(">") || op.equals("<") || op.equals("<=") || op.equals(">=")
				    || op.equals("==") || op.equals("!=")) {
			return true;
		}
		System.out.println("Invalid 'op' input!\n" +
				                   "'op' can only be >, <, >=, <=, ==, or !=, if property is duration.\t");
		return false;
	}

	private boolean checkOpIsContains(String op) {
		if (op.equals("contains")) {
			return true;
		}
		System.out.println("Invalid 'op' input!" + "\n" +
				                   "'op' can only be 'contains', if property is name, description or prerequisites.\t");
		return false;
	}

	private boolean checkLogOp(String op) {
		if (op.equals("||") || op.equals("&&")) {
			return true;
		} else {
			System.out.println("The logical operators is either && or || .\nPlease input again.\t");
			return false;
		}
	}

	private boolean checkIsNumeric(String strNum) {
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			System.out.println("Invalid 'value' input!" + "\n" + "'value' can only be a real number.\t");
			return false;
		}
		return true;
	}

	private boolean checkPrerequisites(String strings) {
		String[] strList = strings.split(",");
		for (int i = 0; i < strList.length - 1; i++) {
			if (strList[i] == null) {
				System.out.println("Invalid 'Prerequisites' input!\t");
				return false;
			}
		}
		return true;
	}

	private boolean checkValue(String strings) {
		if (strings.startsWith("\"") && strings.endsWith("\"")) {return true;}
		System.out.println("Invalid 'value' input!\n" +
				                   "It should be inside a double quotes.\t");
		return false;
	}

	/**
	 * Check the Negated criterion meet the creation requirement
	 *
	 * @return true if no input error
	 */
	public boolean checkDefineNegatedCriterion() {
		if (checkNameExist(this.getName())) {
			System.out.println(this.getName() + " is already existed.\t");
			return false;
		}
		if (!checkNameExist(this.getProperty())) {
			System.out.println(this.getProperty() + " is not existed.\t");
			return false;
		}
		if (!checkNameValid(this.getName())) return false;
		return true;
	}

	/**
	 * Check the binary criterion meet the creation requirement
	 *
	 * @return true if no input error
	 */
	public boolean checkDefineBinaryCriterion() {
		if (!checkNameValid(this.getName())) {
			return false;
		}
		if (!checkNameExist(this.getName())) {
			if (!checkNameExist(this.getProperty())) {
				System.out.println("name '" + this.getProperty() + "' not existed.\nPlease input again.\t");
				return false;
			}
			if (!checkNameExist(this.getValue())) {
				System.out.println("name '" + this.getValue() + "' not existed.\nPlease input again.\t");
				return false;
			}
			if (!checkLogOp(this.getOp())) {
				return false;
			}
		} else {
			System.out.println("The name " + this.getName() + " is already existed!\t");
			return false;
		}
		return true;
	}
}



