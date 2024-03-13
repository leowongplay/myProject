package hk.edu.polyu.comp.comp2021.tms.model;

import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * The command line interface of the TMS system.
 */
public class CommandLineTMS {
    //private static boolean isRunning = true;

    /**
     * Main function of command line interface
     * @param args none
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Controller ct = new Controller();
        while (true) {
            System.out.println("Enter text:\t");
            String command = scanner.next();
            switch (command) {
                case "CreatePrimitiveTask" : {
                    String str = scanner.nextLine().trim();
                    String[] strList = str.split(" ");
                    if (strList.length == 4){
                        try {
                            List<String> preList = List.of(strList[3].split(","));
                            ct.createSimpleTask(strList[0], strList[1],
                                    Double.parseDouble(strList[2]), preList);
                        } catch (NumberFormatException e) {
                            System.out.println("Duration should be a number only!\t");
                        }
                    }
                    else if(strList.length == 3) {
                        try {
                            ct.createSimpleTask(strList[0], strList[1],
                                    Double.parseDouble(strList[2]), null);
                        } catch (NumberFormatException e) {
                            System.out.println("Duration should be a number only!\t");
                        }
                    }
                    else System.out.println("Make sure you have inputs only name, description, duration, and prerequisites\n" +
                                                   "Prerequisites can be null.\t");
                    break;
                }
                case "CreateCompositeTask" : {
                    String name = scanner.next();
                    String description = scanner.next();
                    List<String> subtasks = Collections.singletonList(scanner.next());
                    ct.createCompositeTask(name, description, subtasks);
                    break;
                }
                case "DeleteTask" : {
                    String name = scanner.next();
                    ct.deleteTask(name);
                    break;
                }
                case "ChangeTask" : {
                    String name = scanner.next();
                    String property = scanner.next();
                    String newValue = scanner.next();
                    ct.changeTasks(name,property,newValue);
                    break;
                }
                case "PrintTask" : {
                    String name = scanner.next();
                    ct.printTask(name);
                    break;
                }
                case "PrintAllTasks" : {
                    ct.printAllTasks();
                    break;
                }
                case "ReportDuration" : {
                    String name = scanner.next();
                    ct.reportDuration(name);
                    break;
                }
                case "ReportEarliestFinishTime" : {
                    String name = scanner.next();
                    ct.reportEarliestFinishTime(name);
                    break;
                }
                case "DefineBasicCriterion" : {
                    String name = scanner.next();
                    String property = scanner.next();
                    String op = scanner.next();
                    String value = scanner.next();
                    ct.defineBasicCriterion(name, property, op, value);
                    break;
                }
                case "DefineNegatedCriterion" : {
                    String name1 = scanner.next();
                    String name2 = scanner.next();
                    ct.defineNegatedCriterion(name1, name2);
                    break;
                }
                case "DefineBinaryCriterion" : {
                    String name1 = scanner.next();
                    String name2 = scanner.next();
                    String logOp = scanner.next();
                    String name3 = scanner.next();
                    ct.defineBinaryCriterion(name1, name2, logOp, name3);
                    break;
                }
                case "PrintAllCriteria" : {
                    ct.printAllCriteria();
                    break;
                }
                case "Search" : {
                    String name = scanner.next();
                    ct.search(name);
                    break;
                }
                case "Load" : {
                    String path = scanner.next();
                    ct.loadPath(path);
                    break;
                }
                case "Store" : {
                    String path = scanner.next();
                    try {
                        ct.storePath(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "Quit" : {
                    ct.quit();
                    break;
                }
//                case "GUI" : {
//                    System.setOut(new PrintStream(new GuiOutputStream(), true));
//                    isRunning = false;
//                    new GUIRunMe();
//                    break;
//                }
                default : {
                    System.out.println("Invalid command!\n" + "Please input again!\t");
                    break;
                }
            }
        }

    }
}