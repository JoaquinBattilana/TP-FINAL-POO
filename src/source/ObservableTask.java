package source;

import javafx.beans.property.*;

public class ObservableTask {
    Task task;
    public ObservableTask(Task task){
        this.task=task;
    }

    public IntegerProperty getIdProperty(){
        return new SimpleIntegerProperty(task.getId());
    }
    public StringProperty completedProperty(){
        if(task.completed())
            return new SimpleStringProperty("completed");
        return new SimpleStringProperty("pending");
    }
    public StringProperty getExpirationDateProperty(){
        if (task.getExpirationDate()==null)
            return new SimpleStringProperty("");
        return new SimpleStringProperty(task.getExpirationDate().toString());
    }
    public Integer getId(){
        return task.getId();
    }
    public StringProperty getDescriptionProperty(){
        return new SimpleStringProperty(task.getDescription());
    }
    public StringDate getExpirationDate(){
        return task.getExpirationDate();
    }
}


