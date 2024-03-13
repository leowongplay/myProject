package hk.edu.polyu.comp.comp2021.tms.model;

import hk.edu.polyu.comp.comp2021.tms.model.CommandLineTMS;
import hk.edu.polyu.comp.comp2021.tms.model.Main.Controller;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ICommandLineTMSControllerTest {
    private static Controller ct;
    private static CommandLineTMS cml;

    @BeforeClass
    public static void setup(){
        ct = new Controller();
        cml =new CommandLineTMS();
    }
    @Test
    public void primitiveTaskTest(){
        List<String> list = new ArrayList<>();
        list.add("Task1");
        ct.createSimpleTask("Task10","de1",1,null);
        list.add("Task10");
        ct.createSimpleTask("Task1","de1",1,null);
        ct.createSimpleTask("Task2","de2",1,list);
        list.add("asda");
        ct.createSimpleTask("Task3","de3",1,list);
        ct.createSimpleTask("Task4","de3",-1,list);
        ct.createSimpleTask("Task5sdfsdf","des3",0,list);
        list.clear();
        list.add("abc");
        ct.createSimpleTask("Task6","des3",100,list);
        ct.printAllTasks();
    }

    @Test
    public void compositeTaskTest(){

        ct.loadPath( "fileGroup12.txt");
        ArrayList<String> subtask = new ArrayList<String>();
        subtask.add("Task1");
        subtask.add("Task2");
        ct.createCompositeTask("CTask1","Description",subtask);
        ct.createCompositeTask("CTask1","Description",subtask);
        subtask.add("CTask1");
        ct.createCompositeTask("CTask2","Description",subtask);
        subtask.add("CTask2");
        ct.createCompositeTask("CTask3","Description",subtask);
        ct.createCompositeTask("CTask410111","Description",subtask);
        subtask.clear();
        subtask.add("task1");
        ct.createCompositeTask("CTa","hihihi",subtask);
        try{
            ct.storePath("fileGroup12.txt");
        } catch(IOException e){
            throw new RuntimeException(e);
        }

    }
    @Test
    public void printAllCriteriaTest(){
        ct.printAllCriteria();
        ct.loadPath("fileGroup12.txt");
        ct.printAllCriteria();
    }

    @Test
    public void defineNegatedCriterionTest(){
        ct.loadPath( "fileGroup12.txt");
        ct.defineNegatedCriterion("dd1","Crite");
        ct.defineNegatedCriterion("dd2","Crite");
        ct.defineNegatedCriterion("ne1","Crite");
        ct.defineNegatedCriterion("ne2","Crite2");
        ct.defineNegatedCriterion("ne3","Crite2");
        ct.defineNegatedCriterion("ne4","Cg");
        ct.defineNegatedCriterion("ne5","ne4");
        ct.defineNegatedCriterion("Nag","Nag");
        ct.defineNegatedCriterion("ated","Crite2");
        ct.search("Nagated");
    }
    @Test
    public void defineBinaryCriterionTest(){
        ct.loadPath("fileGroup12.txt");
        ct.defineBinaryCriterion("dd4","dd2","00","dd3");
        ct.defineBinaryCriterion("Bi","dd2","00","dd3");
        ct.defineBinaryCriterion("Bin","Crite","&&","Crite");
        ct.search("Bin");
        ct.defineBinaryCriterion("Bin2","Crite","||","Crite");
        ct.search("Bin2");
        ct.defineBinaryCriterion("Bin9","abc","||","Crite");
        ct.search("Bin9");
        ct.defineBinaryCriterion("Bin3","Crite","&&","Bin8");
        ct.search("Bin3");
        ct.defineBinaryCriterion("Bin10","Crit0e","00","Bin8");
        ct.search("Bin10");
        ct.defineBinaryCriterion("Bin8dfgdfgddfg","Bin2","||","Bin");
        ct.search("Bin8dfgdfgddfg");

    }


    @Test
    public void searchTest(){
        ct.loadPath("fileGroup12.txt");
        ct.search("Crite");
        ct.search("d9");
        ct.search("d0");
        ct.search("10");
        ct.search("dd2");
        ct.search("dd3");
        ct.search("dd4");
        ct.search("dd5");
        ct.search("dd6");
        ct.search("dn1");
        ct.search("db2");
        ct.search("db1");
        ct.search("abc1");
        ct.search("abc2");
        ct.search("dp2");
        ct.search("dbc4");
        ct.search("dbc5");
        ct.search("dbc1");
        ct.search("dbc2");
        ct.search("dbc3");
        ct.search("dd7");
        ct.search("dd8");

    }
    @Test
    public void changeTasksTest(){
        ct.loadPath("fileGroup12.txt");
        ct.changeTasks("Tk1","duration","2");
        ct.changeTasks("Task1","duration","2");
        ct.changeTasks("Task2","name","Task20");
        ct.changeTasks("Task1","ne","Task20");
        ct.changeTasks("Task3","prerequisites","Task1");
        ct.changeTasks("task4","description","abcccd");
        ct.changeTasks("compo5","subtasks","Task1");
        ct.changeTasks("compo5","sasks","Task1");
        ct.changeTasks("compo5","name","compo5");
        ct.changeTasks("compo2","description","abccc");
        ct.changeTasks("Task1","duration","asd");
        ct.printTask("Task1");
        ct.printTask("Task20");
        ct.printTask("Task3");
    }
    @Test
    public void printTaskTest(){
        ct.printTask("Task1");
        ct.loadPath("fileGroup12.txt");
        ct.printTask("Task1");
        ct.printTask("compo2");
        ct.printTask("compo3");
        ct.printTask("Ta1");
        ct.printTask("Task2");
        ct.printTask("task6");
    }

    @Test
    public void printAllTaskTest(){
        ct.printAllTasks();
        ct.loadPath("fileGroup12.txt");
        ct.printAllTasks();
    }

    @Test
    public void deleteTaskTest(){
        ct.loadPath("fileGroup12.txt");
        ct.deleteTask("Task1");
        ct.deleteTask("Task2");
        ct.deleteTask("Task3");
        ct.deleteTask("compo2");
        ct.deleteTask("compo5");
        ct.deleteTask("Ta");
        ct.printAllTasks();
    }

    @Test
    public void reportDurationTest(){
        ct.loadPath("fileGroup12.txt");
        ct.reportDuration("Task1");
        ct.reportDuration("STask2");
        ct.reportDuration("po3");
        ct.reportDuration("compo1");
        ct.reportDuration("compo4");
        ct.reportDuration("compo3");
        ct.reportDuration("compo5");
    }
    @Test
    public void reportEarliestFinishTimeTest(){
        ct.loadPath("fileGroup12.txt");
        ct.reportEarliestFinishTime("compo4");
        ct.reportEarliestFinishTime("compo3");
        ct.reportEarliestFinishTime("compo5");
        ct.reportEarliestFinishTime("po3");
        ct.reportEarliestFinishTime("Task1");

    }
    @Test
    public void loadPathTest(){

        ct.loadPath("fileGroup12.txt");
        ct.printAllTasks();

    }

    @Test
    public void storePathTest() throws IOException {
        ct.createSimpleTask("Task1","description1",1,null);
        ct.createSimpleTask("Task2","description2",1,null);
        ct.createSimpleTask("Task3","description3",1,null);

        ct.storePath("fileGroup12.txt");
        ct.printAllTasks();
    }
    @Test
    public void defineBasicCriterionTest(){
        ct.defineBasicCriterion("c10","duration",">=", "122121");
        ct.defineBasicCriterion("c111","duration",">=", "4");
        ct.defineBasicCriterion("Cripte","prerequisites","contains", "task2,task3");
        ct.defineBasicCriterion("c1","prerequisites","contains", "task2");
        ct.defineBasicCriterion("c2","name","contains", "\"task2\"");
        ct.defineBasicCriterion("Cr4","duration","==","10");
        ct.defineBasicCriterion("Cripte","duration","=","1");
        ct.defineBasicCriterion("Criptdfgdfgdfge","duration","=","1");
        ct.defineBasicCriterion("Cripte","durdfgation","==","1");
        ct.defineBasicCriterion("Cripte","duration","=","hu9ouy");
        ct.defineBasicCriterion("Cri pte","duration","==","1");
        ct.defineBasicCriterion("Cri3","duration","==","-1");
        ct.defineBasicCriterion("Crite1","description","contains","abc");
        ct.defineBasicCriterion("Critde2","duration",">","asd");
        ct.defineBasicCriterion("Crite3","subtasks","contains","Task1");
        ct.defineBasicCriterion("Criste3","sdf","||","Task1");
        ct.defineBasicCriterion("Crisate3","subtasks","contains","Task1,task");
        ct.defineBasicCriterion("Criste3","subtasks","contains","Task1,asd");
        ct.defineBasicCriterion("454453","subtasks","contains","Task1,asd");

        ct.search("Crite");

    }


    @AfterClass
    public static void testQuit(){

        ct.quit();
    }



}
