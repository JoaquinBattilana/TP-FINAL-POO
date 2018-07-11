package source;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class TaskHolder implements Serializable {

    private int currentId=1;
    private Set<Task> set= new TreeSet<>();

    public void add(Task task){
        set.add(task);
    }
    public void add(String description){
        Task task;
        task = new Task(currentId ,description);
        set.add(task);
        this.currentId+=1;
    }
    public void add(String description, StringDate date){
        Task task= new Task(currentId,description, date);
        set.add(task);
        this.currentId+=1;
    }
    public void delete(int id){
        set.remove(findTask(id));
    }
    public boolean complete(int id){
        Task task=findTask(id);
        if (task.completed())
            return false;
        refreshTask(task);
        return true;
    }
    private void refreshTask(Task task){
        set.remove(task);
        task.complete();
        set.add(task);
    }
    public Task findTask(int id) throws IllegalArgumentException{
        for(Task task:set ){
            if(task.getId()==id)
                return task;
        }
        throw new IllegalArgumentException();
    }
    public void ac(){
        List<Task> taskList=new ArrayList<>();
        for (Task task : set) {
            if (task.completed())
                    taskList.add(task);
            }
            set.removeAll(taskList);
    }

    public Set<Task> getSet() {
        return set;
    }
    public void remove(Task task){
        set.remove(task);
    }
}
