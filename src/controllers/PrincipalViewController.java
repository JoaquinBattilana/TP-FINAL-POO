package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import source.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import java.io.File;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


public class PrincipalViewController implements Initializable {
    private ObservableList<ObservableTask> list;

    @FXML
    private MenuItem saveButton;
    @FXML
    private MenuItem loadButton;

    private TaskHolder holder;
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
    protected void editAction()throws java.io.IOException{
        Dialog<Pair<String, LocalDate>> dialog= createPopup();
        Optional<Pair<String,LocalDate>> pair=dialog.showAndWait();
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


    public Dialog<Pair<String, LocalDate>> createPopup(){
        // Create the custom dialog.
        Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField description = new TextField();
        description.setPromptText("New Description");
        DatePicker date = new DatePicker();
        date.setPromptText("New date");

        grid.add(new Label("Description:"), 0, 0);
        grid.add(description, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(date, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(description.getText(), date.getValue());
            }
            return null;
        });
        return dialog;
    }
    public void openAction()throws java.io.IOException,java.lang.ClassNotFoundException{
        FileChooser fileChooser =new FileChooser();
        Stage stage=new Stage();
        File file=fileChooser.showOpenDialog(stage);
        ObjectInputStream out = new ObjectInputStream(Files.newInputStream(file.toPath()));
        this.holder=(TaskHolder)out.readObject();
        out.close();
        refresh();
    }
    public void saveAction()throws java.io.IOException{
        FileChooser fileChooser =new FileChooser();
        Stage stage=new Stage();
        File file=fileChooser.showSaveDialog(stage);
        ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
        out.writeObject(holder);
        out.close();

    }



}
