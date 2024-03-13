package hk.edu.polyu.comp.comp2021.tms.model.Task;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The `CompositeTask` class represents a composite task, which is a task that
 * consists of multiple subtasks.
 * It extends the `Task` class and adds functionality for managing and
 * processing composite tasks.
 */
public class CompositeTask extends Task {
    private String description;
    private List<String> subtasks;

/**
 * Constructs a new `CompositeTask` object with the specified name, description,
 * and list of subtasks.
 *
 * @param name        The name of the composite task.
 * @param description The description of the composite task.
 * @param subtasks    The list of subtasks associated with the composite task.
 */
    public CompositeTask(String name, String description, List<String> subtasks) {
        super(name);
        this.description = description;
        this.subtasks = subtasks;
    }

/**
 * Returns a string representation of the subtasks in the composite task.
 * @return A string containing the names of the subtasks separated by commas.
 */
    public String getSubtasksString() {
        StringBuilder sb = new StringBuilder();
        for (String temp : this.subtasks) {
            sb.append(temp).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

/**
 * Returns the list of subtasks associated with the composite task.
 *
 * @return The list of subtasks.
 */
    public List<String> getSubtasks() {
        return subtasks;
    }

/**
 * Returns the description of the composite task.
 *
 * @return The description of the composite task.
 */
    public String getDescription() {
        return description;
    }

/**
 * Sets the description of the composite task to the specified value.
 *
 * @param newValue The new description for the composite task.
 */
    public void setDescription(String newValue) {
        if (description_checking(newValue))
            this.description = newValue;
    }

/**
 * Sets the list of subtasks for the composite task to the specified value.
 *
 * @param newValue The new list of subtasks for the composite task.
 */
    public void setSubtask(List<String> newValue) {
        if (subtasks_checking(newValue))
            this.subtasks = newValue;
    }
/**
 * Creates a new composite task based on the current composite task and adds it
 * to the composite task list.
 * Prints a success message if the creation is successful.
 */
    public void createCompositeTask() {
        try {
            if (this.checkCompositeTask()) {
                getCompositeTaskList().add(new CompositeTask(this.getName(), this.getDescription(), this.getSubtasks()));
                System.out.println("Successfully created " + this.getName() + "\t");
            }
        } catch (HeadlessException e) {
            errorMessage();
        }
    }
/**
 * Checks if the list of subtasks is valid by comparing it with the existing
 * tasks (both simple and composite).
 * Prints an error message if some or all of the subtasks are not defined.
 *
 * @param subtasks The list of subtasks to be checked.
 * @return `true` if the subtasks are valid, `false` otherwise.
 */
    public boolean subtasks_checking(List<String> subtasks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subtasks.size(); i++) {
            sb.append(subtasks.get(i));
        }
        ArrayList<String> checkList = new ArrayList<>(List.of(sb.toString().split(",")));
        ArrayList<String> nameList = new ArrayList<>();
        for (SimpleTask simpleTask : getSimpleTaskList()) {
            nameList.add(simpleTask.getName());
        }
        for (CompositeTask compositeTask : getCompositeTaskList()) {
            nameList.add(compositeTask.getName());
        }
        if (nameList.isEmpty() || !nameList.containsAll(checkList)) {
            System.out.println("Some(All) of the subtasks are not defined.\t");
            return false;
        }
        return true;
    }

/**
 * Checks if the composite task is valid by ensuring that its name, description,
 * and subtasks are valid.
 * Prints an error message if the composite task is not valid or if it already
 * exists.
 *
 * @return `true` if the composite task is valid, `false` otherwise.
 */

    public boolean checkCompositeTask() {
        if (!checkNameValid(this.getName()) || !description_checking(this.getDescription()) ||
                !subtasks_checking(this.getSubtasks())) {
            return false;
        }
        if (checkNameExist(this.getName())) {
            System.out.println(this.getName() + " Already Exist!\t");
            return false;
        }
        return true;
    }

/**
 * Prints the names of all composite tasks in the `compositeTaskList`.
 *
 * @return An `ArrayList` containing the names of all composite tasks.
 */
    public static ArrayList<String> printAllCompositeTasks() {
        CompositeTask task = null;
        ArrayList<String> tasklist = new ArrayList<>();

        for (CompositeTask tempTask : getCompositeTaskList()) {
            tasklist.add(tempTask.getName());
        }
        return tasklist;
    }

/**
 * Returns the composite task with a name equal to the specified string, or
 * `null` if not found.
 *
 * @param str The name of the composite task to search for.
 * @return The composite task with the specified name, or `null` if not found.
 */
    public static CompositeTask returnLineIfSthEqualCompositeTaskName(String str) {
        CompositeTask task = null;
        for (CompositeTask tempTask : getCompositeTaskList()) {
            if (str.equals(tempTask.getName())) {
                task = tempTask;
                break;
            }
        }
        return task;
    }

/**
 * Deletes the composite task with the specified name from the
 * `compositeTaskList`.
 *
 * @param str The name of the composite task to be deleted.
 */
    public static void deleteCompositeTask(String str) {
        CompositeTask comp = CompositeTask.returnLineIfSthEqualCompositeTaskName(str);
        if (comp != null) {
            getCompositeTaskList().remove(comp);
        }
    }

/**
 * Checks if a simple task with the specified name is a subtask of any composite
 * task.
 *
 * @param str The name of the simple task to check as a subtask.
 * @return `true` if the simple task is a subtask of any composite task, `false`
 *         otherwise.
 */
    public static boolean returnSubtask(String str) {
        for (CompositeTask tempTask : getCompositeTaskList()) { // return a list name 'task' if equal to the name we are
                                                                // searching
            if (tempTask.getSubtasks().contains(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates and returns the total duration of the composite task and its
     * subtasks.
     *
     * @param str The name of the composite task.
     * @return The total duration of the composite task and its subtasks.
     */
    public double returnFinalDuration(String str) {
        double maxDuration = 0;
        return returnDuration(str, maxDuration);
    }

    /**
     * Recursively calculates the total duration of the composite task and its
     * subtasks.
     *
     * @param str         The name of the composite task.
     * @param maxDuration The maximum duration encountered so far in the recursion.
     * @return The total duration of the composite task and its subtasks.
     */

    public double returnDuration(String str, double maxDuration) {
        CompositeTask resultC = returnLineIfSthEqualCompositeTaskName(str);

        if (resultC != null) {

            for (int i = 0; i < resultC.getSubtasks().size(); i++) {
                List<String> subtasks = resultC.getSubtasks();
                double totalDuration = 0.0;

                if (returnLineIfSthEqualCompositeTaskName(subtasks.get(i)) != null) {
                    totalDuration = returnDuration(subtasks.get(i), maxDuration); // if composite, then recurse
                } else {
                    SimpleTask resultS2 = SimpleTask.returnLineIfSthEqualSimpleTaskName(subtasks.get(i));

                    if (resultS2 != null) {
                        totalDuration += resultS2.getDuration(); // convert string to double

                        while (resultS2.getPrerequisites() != null) {
                            double additionalDuration = 0.0;

                            for (int j = 0; j < resultS2.getPrerequisites().size(); j++) {
                                List<String> prerequisites = resultS2.getPrerequisites();
                                SimpleTask resultS3 = SimpleTask
                                        .returnLineIfSthEqualSimpleTaskName(prerequisites.get(j));

                                if (resultS3 != null) {
                                    if (resultS3.getDuration() > additionalDuration) {
                                        additionalDuration = resultS3.getDuration();
                                    }
                                }
                            }
                            totalDuration += additionalDuration;
                            boolean prerequisiteFound = false;
                            for (int j = 0; j < getSimpleTaskList().size(); j++) {
                                if (resultS2.getPrerequisites() == null) {
                                    prerequisiteFound = true;
                                    break; // No prerequisites, break the loop
                                } else if (resultS2.getPrerequisites().equals(getSimpleTaskList().get(j).getName())) {
                                    // Prerequisite found, add its duration to the current task duration
                                    double prerequisiteDuration = getSimpleTaskList().get(j).getDuration();
                                    totalDuration += prerequisiteDuration;
                                    prerequisiteFound = true;
                                    break;
                                }
                            }
                            if (!prerequisiteFound) {
                                break; // Exit the while loop if the prerequisite is not found
                            }
                        }
                    }
                }
                if (totalDuration > maxDuration) {
                    maxDuration = totalDuration;
                }
            }
            return maxDuration;
        }
        return 0;
    }

    public String toString() {

        return super.getName() + " " + this.getDescription() + " " + this.getSubtasksString();
    }

}
