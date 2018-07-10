package source;

import javafx.beans.property.*;

public class ObservableTask {
    StringProperty complete;
    IntegerProperty id;
    StringProperty description;
    StringProperty expirationDate;
    Task task;
    public ObservableTask(Task task){
        this.task=task;
        if(task.completed())
            complete = new SimpleStringProperty("completado");
        else
            complete = new SimpleStringProperty("pendiente");
        this.id = new SimpleIntegerProperty(task.getId());
        this.description = new SimpleStringProperty(task.getDescription());
        this.expirationDate = new SimpleStringProperty("");
        if(task.getExpirationDate()!=null)
            this.expirationDate = new SimpleStringProperty(task.getExpirationDate().toString());
    }

    public IntegerProperty getIdProperty(){
        return id;
    }
    public StringProperty completedProperty(){
        return complete;
    }
    public StringProperty getExpirationDateProperty(){
        return expirationDate;
    }
    public boolean completed(){
        return task.completed();
    }
    public String getDescription(){
        return task.getDescription();
    }
    public StringDate getExpirationDate(){
        return task.getExpirationDate();
    }
    public Integer getId(){
        return task.getId();
    }
    public StringProperty getDescriptionProperty(){
        return description;
    }
}


