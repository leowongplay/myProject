package hk.edu.polyu.comp.comp2021.tms.model.Task;

import hk.edu.polyu.comp.comp2021.tms.model.Criteria.Criterion;

import java.util.ArrayList;
/**
 * The `Task` class serves as the base class for simple tasks, composite tasks, and criteria.
 * It contains common attributes and methods shared by these task types.
 */
public class Task {
    private String name;
    /**
     * The list of simple tasks.
     */
    private static ArrayList<SimpleTask> simpleTaskList =  new ArrayList<SimpleTask>();

    /**
     * The list of composite tasks.
     */
    private static ArrayList<CompositeTask> compositeTaskList = new ArrayList<>();
    /**
     * The list of criteria.
     */
    private static ArrayList<Criterion> criterionList = new ArrayList<Criterion>();

    /**
     * Constructs a new `Task` object with the specified name.
     *
     * @param name The name of the task.
     */

    public Task(String name){
        this.name = name;
    }

    /**
     * Gets the list of simple tasks.
     *
     * @return The list of simple tasks.
     */
    public static ArrayList<SimpleTask> getSimpleTaskList() {
        return simpleTaskList;
    }
    /**
     * Gets the list of composite tasks.
     *
     * @return The list of composite tasks.
     */
    public static ArrayList<CompositeTask> getCompositeTaskList() {
        return compositeTaskList;
    }
    /**
     * Gets the list of criteria.
     *
     * @return The list of criteria.
     */
    public static ArrayList<Criterion> getCriterionList() {
        return criterionList;
    }
    /**
     * Gets the name of the task.
     *
     * @return The name of the task.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Sets the name of the task to the specified value, if the new name is both valid and does not exist already.
     *
     * @param newValue The new name for the task.
     */
    public void setName(String newValue) {
        if (!checkNameExist(newValue) && checkNameValid(newValue))
            this.name = newValue;
    }
    /**
     * Checks if the specified name is valid according to length and format constraints.
     *
     * @param checkTarget The name to be checked.
     * @return `true` if the name is valid, `false` otherwise.
     */
    public boolean checkNameValid(String checkTarget) {
        if (checkTarget.length() > 8) {
            System.out.println("The name length should be <= 8\t");
            return false;
        }
        if (!Character.isLetter(checkTarget.charAt(0))) {
            System.out.println("The name should not start with a digit\t");
            return false;
        }
        for (int i = 1; i < checkTarget.length(); i++) {
            if (!((checkTarget.charAt(i) >='A' && checkTarget.charAt(i) <='Z')||
                         (checkTarget.charAt(i) >='a' && checkTarget.charAt(i) <='z')||
                         (checkTarget.charAt(i) >='0' && checkTarget.charAt(i) <='9'))) {
                System.out.println("The name should only include English letters or digits\t");
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if the specified description is valid according to format constraints.
     *
     * @param checkTarget The description to be checked.
     * @return `true` if the description is valid, `false` otherwise.
     */
    public boolean description_checking(String checkTarget) {
        for (int i = 1; i < checkTarget.length(); i++) {
            if (!(((checkTarget.charAt(i) >= 'A' && checkTarget.charAt(i) <= 'Z') ||
                         (checkTarget.charAt(i) >= 'a' && checkTarget.charAt(i) <= 'z') ||
                         (checkTarget.charAt(i) >= '0' && checkTarget.charAt(i) <= '9')) ||
                        checkTarget.charAt(i) == '-')) {
                System.out.println("The description should only include English letters, " +
                                           "digits or the hyphen letter (-)\t");
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if a task with the specified name already exists in the lists of simple tasks, composite tasks, or criteria.
     *
     * @param checkTarget The name to be checked for existence.
     * @return `true` if a task with the specified name exists, `false` otherwise.
     */
    public boolean checkNameExist(String checkTarget){
        for (Task task : simpleTaskList) {
            if (checkTarget.equals(task.getName())){
                return true;
            }
        }
        for (Task task : compositeTaskList) {
            if (checkTarget.equals(task.getName())){
                return true;
            }
        }
        for (Task task : criterionList) {
            if (checkTarget.equals(task.getName())){
                return true;
            }
        }
        return false;
    }
    /**
     * Prints an error message indicating that something went wrong.
     */
    public void errorMessage(){
        System.out.println("Something went wrong...\t");
    }
}
