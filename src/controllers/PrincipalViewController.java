package controllers;

import Popups.EditPopup;
import Popups.ExceptionPopup;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import source.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class PrincipalViewController implements Initializable {
    private ObservableList<ObservableTask> list;
    private TaskHolder holder;
    private Predicate<ObservableTask> predicate=item->true;

    @FXML
    private ChoiceBox<String> filterChoiceBox;
    @FXML
    private MenuItem saveButton;
    @FXML
    private MenuItem openButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button archivateButton;
    @FXML
    private DatePicker addDatePicker;
    @FXML
    private Button addButton;
    @FXML
    private TextField filterTextField;
    @FXML
    private TextField descriptionBox;
    @FXML
    private TableView<ObservableTask> taskTable;
    @FXML
    private TableColumn <ObservableTask, String> stateColumn;
    @FXML
    private TableColumn <ObservableTask, Number> idColumn;
    @FXML
    private TableColumn <ObservableTask, String> descriptionColumn;
    @FXML
    private TableColumn <ObservableTask, String> expirationColumn;
    @FXML
    private Button completeButton;

    public void initModel(TaskHolder holder){
        this.holder=holder;
    }

    private void setPredicate(Predicate<ObservableTask> predicate){
        this.predicate=predicate;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        taskTable.setItems(list);
        filterChoiceBox.setItems(FXCollections.observableArrayList("by description","overdue", "due today"));
        filterChoiceBox.getSelectionModel().selectFirst();
        filterChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.equals(0))
                    setPredicate(task->task.getDescriptionProperty().get().toLowerCase().contains(filterTextField.getText().toLowerCase()));
                else if(newValue.equals(1)){
                    setPredicate(task->
                    {
                        if(task.getExpirationDate()==null)
                            return false;
                        return task.getExpirationDate().compareTo(new StringDate(LocalDate.now()))<0;
                    });

                }
                else if(newValue.equals(2)){
                    setPredicate(task->
                            {
                                if(task.getExpirationDate()==null)
                                    return false;
                                return task.getExpirationDate().equals(new StringDate(LocalDate.now()));
                            });
                }
                refresh();
            }
        });
        filterTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null || newValue.isEmpty())
                    setPredicate(p->true);
                else
                    setPredicate(task->task.getDescriptionProperty().get().toLowerCase().contains(newValue));
                refresh();
            }
        });


        stateColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty());
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        expirationColumn.setCellValueFactory(cellData -> cellData.getValue().getExpirationDateProperty());

        completeButton.disableProperty().bind(Bindings.isEmpty(taskTable.getSelectionModel().getSelectedItems()));
        editButton.disableProperty().bind(Bindings.isEmpty(taskTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(taskTable.getSelectionModel().getSelectedItems()));
        addButton.disableProperty().bind(Bindings.isEmpty(descriptionBox.textProperty()));
        filterTextField.disableProperty().bind(Bindings.equal(filterChoiceBox.valueProperty(),"overdue").or(Bindings.equal(filterChoiceBox.valueProperty(),"due today")));

     }

     @FXML
     protected void addAction(){

        if(descriptionBox.getText()==null || descriptionBox.getText().compareTo("")==0){
            //alerta
        }
        else{
            if(addDatePicker.getValue()==null) {
                holder.add(descriptionBox.getText());
            }
            else{
                holder.add(descriptionBox.getText(),new StringDate(addDatePicker.getValue()));
            }
        }
         refresh();
    }
     private void refresh(){
        list=FXCollections.observableArrayList();
        for(Task task:holder.getSet()){
            list.add(new ObservableTask(task));
        }
        FilteredList<ObservableTask> filteredList = new FilteredList<>(list);
        filteredList.setPredicate(predicate);
        taskTable.setItems(filteredList);
     }

     @FXML
     protected void completeAction(){
        ObservableTask task=taskTable.getSelectionModel().getSelectedItem();
        holder.complete(task.getId());
        refresh();
     }
     @FXML
    protected void archivateAction(){
        holder.ac();
        refresh();
     }
     @FXML
    protected void deleteAction(){
         ObservableTask task=taskTable.getSelectionModel().getSelectedItem();
         holder.delete(task.getId());
         refresh();
     }
    @FXML
    protected void editAction(){
        EditPopup popup= new EditPopup();
        Optional<Pair<String,LocalDate>> pair=popup.showAndWait();
        ObservableTask task1=taskTable.getSelectionModel().getSelectedItem();
        Task task2=holder.findTask(task1.getId());
        holder.remove(task2);
        pair.ifPresent(usernamePassword -> {
            if(pair.get().getKey()!=null && pair.get().getKey().compareTo("")!=0)
                task2.setDescription(pair.get().getKey());
            if(pair.get().getValue()!=null)
                task2.setExpirationDate(new StringDate(pair.get().getValue()));
        });
        holder.add(task2);
        refresh();
    }
    public void openAction(){
        try {
            FileChooser fileChooser = new FileChooser();
            Stage stage = new Stage();
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null) {
                ObjectInputStream out = new ObjectInputStream(Files.newInputStream(file.toPath()));
                this.holder = (TaskHolder) out.readObject();
                out.close();
            }
        }
        catch(Exception e){
            ExceptionPopup alert = new ExceptionPopup(e);
            alert.showAndWait();
        }
        refresh();
    }
    public void saveAction(){
        try {
            FileChooser fileChooser = new FileChooser();
            Stage stage = new Stage();
            File file = fileChooser.showSaveDialog(stage);
            if(file!=null) {
                ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
                out.writeObject(holder);
                out.close();
            }
        }
        catch(Exception e){
            ExceptionPopup alert = new ExceptionPopup(e);
            alert.showAndWait();
        }
    }



}
