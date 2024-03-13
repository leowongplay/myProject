package hk.edu.polyu.comp.comp2021.tms.model.IOManipulate;

import hk.edu.polyu.comp.comp2021.tms.model.Criteria.Criterion;
import hk.edu.polyu.comp.comp2021.tms.model.Task.CompositeTask;
import hk.edu.polyu.comp.comp2021.tms.model.Task.SimpleTask;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static hk.edu.polyu.comp.comp2021.tms.model.Criteria.Criterion.getNegatedList;

/**
 * This class manage the IO of TMS system
 */
public class FileProcessor {
    private String[] taskType = {"SimpleTask", "CompositeTask", "Criterion"};

    /**
     * Create a new file if file not exist
     * @param pathName filename
     * @throws IOException IOException
     */
    public void CreateFile(String pathName) throws IOException {
        File f = new File(pathName);
        if (!f.exists()){f.createNewFile();}
    }

    /**
     * Load data from file to TMS
     * @param name file name
     * @param simpleTaskList simpleTask List
     * @param compositeTaskList compositeTask List
     * @param criterionList criterion List
     * @throws IOException IOException
     */
    public void loadFileData(String name, ArrayList<SimpleTask> simpleTaskList,
                             ArrayList<CompositeTask> compositeTaskList,
                             ArrayList<Criterion> criterionList) throws IOException {
        simpleTaskList.clear();
        compositeTaskList.clear();
        criterionList.clear();
        getNegatedList().clear();

        String lineInFile;
        BufferedReader br = new BufferedReader( new FileReader(name));
        while ((lineInFile = br.readLine()) != null) {
            String[] lineList = lineInFile.split(" ");
            String tempType = lineList[0];
            if (tempType.equals(taskType[0])) {
                if (lineList.length == 4){
                    simpleTaskList.add(new SimpleTask(lineList[1],lineList[2],
                            Double.parseDouble(lineList[3]), null));
                }
                else{
                    simpleTaskList.add(new SimpleTask(lineList[1], lineList[2],
                            Double.parseDouble(lineList[3]), List.of(lineList[4].split(","))));
                }
            }
            else if (tempType.equals(taskType[1])) {
                compositeTaskList.add(new CompositeTask(lineList[1],lineList[2],
                        List.of(lineList[3].split(","))));
            }
            else if (tempType.equals(taskType[2])) {
                if (lineList.length == 3) {
                    criterionList.add(new Criterion(lineList[1], lineList[2], null, null));
                    if (getNegatedList().contains(lineList[2])) getNegatedList().remove(lineList[2]);
                    else getNegatedList().add(lineList[2]);
                }
                else {
                    criterionList.add(new Criterion(lineList[1], lineList[2],
                            lineList[3], lineList[4]));
                }
            }
        }
        br.close();
        System.out.println("Data successfully loaded :)\t");
    }

    /**
     * Write data into file
     * @param name filename
     * @param simpleTaskList simpleTask List
     * @param compositeTaskList compositeTask List
     * @param criterionList criterion List
     * @throws IOException IOException
     */
    public void writeIntoFile(String name, ArrayList<SimpleTask> simpleTaskList,
                              ArrayList<CompositeTask> compositeTaskList,
                              ArrayList<Criterion> criterionList) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(name, false));
        for (SimpleTask task : simpleTaskList) {
            bw.write(taskType[0] + " " + task.getName() + " " + task.getDescription() + " " +
                    task.getDuration() + " " + task.getPrerequisitesString() + "\n");
        }
        for (CompositeTask task : compositeTaskList) {
            bw.write(taskType[1] + " " + task.getName() + " " + task.getDescription() + " " +
                    task.getSubtasksString() + "\n");
        }
        for (Criterion task : criterionList) {
            bw.write(taskType[2] + " " + task.getName() + " " + task.getProperty() + " " +
                    task.getOp() + " " + task.getValue() + "\n");
        }
        bw.close();
        System.out.println("Successfully written into file :)\n" +
                                   "Remember to load path first if you want to continue with previous data."+
                                   "\t");

    }
}
