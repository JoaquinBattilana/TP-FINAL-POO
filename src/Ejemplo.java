import controllers.PrincipalViewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import source.ObservableTask;
import source.TaskHolder;

import java.util.*;

public class Ejemplo  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        TaskHolder holder= new TaskHolder();

        primaryStage.setTitle("Titulo de la aplicación");
        FXMLLoader loader=new FXMLLoader(getClass().getResource("views/PrincipalView.fxml"));
        Parent root=loader.load();
        PrincipalViewController controler= loader.getController();
        controler.initModel(holder);
        Scene scene= new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args){
        launch(args);
    }
}
