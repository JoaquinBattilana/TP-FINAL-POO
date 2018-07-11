package Popups;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Optional;

public  class EditPopup{


    private Dialog<Pair<String, LocalDate>> alert;

    public EditPopup() {
        alert = new Dialog<>();
        alert.setTitle("Edit Window");
        ButtonType loginButtonType = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
        alert.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);


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

        alert.getDialogPane().setContent(grid);
        alert.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(description.getText(), date.getValue());
            }
            return null;
        });
    }

    public Optional<Pair<String,LocalDate>> showAndWait(){
        return alert.showAndWait();
    }

}