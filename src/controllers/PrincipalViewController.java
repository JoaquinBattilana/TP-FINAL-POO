package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import source.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class PrincipalViewController implements Initializable {
    private ObservableList<ObservableTask> list;

    private TaskHolder holder;

    @FXML
    private DatePicker addDatePicker;
    @FXML
    private Button addButton;
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

    @Override
    public void initialize(URL location, ResourceBundle resources){
        taskTable.setItems(list);
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty());
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        expirationColumn.setCellValueFactory(cellData -> cellData.getValue().getExpirationDateProperty());
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
        taskTable.setItems(list);
     }

     @FXML
     protected void completeAction(){
        /*
        ObservableTask task=taskTable.getSelectionModel().getSelectedItem();
        holder.complete(task.getId());
        */
     }


}
