package source;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public class TaskHolder {

    private int currentId=1;
    private Set<Task> set= new TreeSet<>();
    private StringDate defaultDate = null;

    public void add(String description){
        Task task;
        if(defaultDate==null)
            task = new Task(currentId ,description);
        else
            task = new Task(currentId, description, defaultDate);
        set.add(task);
        this.currentId+=1;
    }
    public void add(String description, StringDate date){
        Task task= new Task(currentId,description, date);
        set.add(task);
        this.currentId+=1;
    }
    public boolean complete(int id){
        Task task=findTask(id);
        if (task.completed())
            return false;
        refreshTask(task);
        return true;
    }

    public void set_date(String date){
        //this.defaultDate=new StringDate(date);
    }
    private void refreshTask(Task task){
        set.remove(task);
        task.complete();
        set.add(task);
    }
    public boolean isEmpty(){
        return set.isEmpty();
    }
    private Task findTask(int id) throws IllegalArgumentException{
        for(Task task:set ){
            if(task.getId()==id)
                return task;
        }
        throw new IllegalArgumentException();
    }
    public Set<Task> list(){
        return new TreeSet<>(set);
    }
    public Set<Task> listToday(){
        Set<Task> temp=new TreeSet<>();
        StringDate today=new StringDate(LocalDate.now());
        for(Task task:set){
            if(task.getExpirationDate()!=null && task.getExpirationDate()==today){
                temp.add(task);
            }
        }
        return temp;
    }
    public Set<Task> listOverdue(){
        Set<Task> temp=new TreeSet<>();
        StringDate today=new StringDate(LocalDate.now());
        for(Task task:set){
            if(task.getExpirationDate()!=null && task.getExpirationDate().compareTo(today)<1){
                temp.add(task);
            }
        }
        return temp;
    }
    public Set<Task> findByText(String text){
        Set<Task> temp=new TreeSet<>();
        for(Task task:set){
            if(task.getDescription().toLowerCase().contains(text.toLowerCase())){
                temp.add(task);
            }
        }
        return temp;
    }
    public void ac(){
        for(Task task:set) {
            if (task.completed())
                set.remove(task);
        }
    }
    public void save(Path path) throws IOException {
        String f = path.toString();
        DataOutputStream out = new DataOutputStream(Files.newOutputStream(path));
        out.writeInt(this.currentId);
        for(Task task:set) {
            out.writeInt(task.getId());
            out.writeUTF(task.getDescription());
            out.writeUTF(task.getExpirationDate().toString());
            out.writeBoolean(task.completed());
        }
        out.close();
    }
    public void load(Path path) throws IOException {
        DataInputStream in = new DataInputStream(Files.newInputStream(path));
        this.currentId=in.readInt();
        while(in.available()!=0) {
            int id = in.readInt();
            String description = in.readUTF();
            String group = in.readUTF();
            String expiration_date = in.readUTF();
            Boolean completed=in.readBoolean();
            this.addAll(id,description,expiration_date,completed);
        }
        in.close();
    }

    public void addAll(int id,String description,String expiration_date,boolean completed){
        Task new_task= new Task(currentId,description, new StringDate(expiration_date));
        if(completed)
            new_task.complete();
        set.add(new_task);
    }

    public Set<Task> getSet() {
        return set;
    }

    public void setSet(Set<Task> set){
        this.set=set;
    }
}