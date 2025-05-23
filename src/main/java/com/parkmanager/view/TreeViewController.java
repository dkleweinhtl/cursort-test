package com.parkmanager.view;

import com.parkmanager.model.Tree;
import com.parkmanager.viewmodel.TreeViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class TreeViewController {
    private TreeViewModel viewModel;

    @FXML private TextField speciesField;
    @FXML private TextField circumferenceField;
    @FXML private DatePicker plantingDatePicker;
    @FXML private CheckBox estimatedDateCheckBox;
    @FXML private Spinner<Integer> conditionSpinner;
    @FXML private TableView<Tree> treeTable;
    @FXML private TableColumn<Tree, String> speciesColumn;
    @FXML private TableColumn<Tree, Double> circumferenceColumn;
    @FXML private TableColumn<Tree, LocalDate> plantingDateColumn;
    @FXML private TableColumn<Tree, Boolean> estimatedDateColumn;
    @FXML private TableColumn<Tree, Integer> conditionColumn;

    @FXML
    public void initialize() {
        viewModel = new TreeViewModel();

        // Initialize table columns
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        circumferenceColumn.setCellValueFactory(new PropertyValueFactory<>("circumference"));
        plantingDateColumn.setCellValueFactory(new PropertyValueFactory<>("plantingDate"));
        estimatedDateColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedPlantingDate"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));

        // Bind table items
        treeTable.setItems(viewModel.getTrees());

        // Setup bindings
        speciesField.textProperty().bindBidirectional(viewModel.speciesProperty());
        
        // Bind circumference with number conversion
        circumferenceField.textProperty().bindBidirectional(viewModel.circumferenceProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                return number == null ? "" : number.toString();
            }

            @Override
            public Number fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        });

        plantingDatePicker.valueProperty().bindBidirectional(viewModel.plantingDateProperty());
        estimatedDateCheckBox.selectedProperty().bindBidirectional(viewModel.estimatedPlantingDateProperty());
        conditionSpinner.getValueFactory().valueProperty().bindBidirectional(viewModel.conditionProperty().asObject());

        // Setup table selection listener
        treeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                viewModel.selectTree(newSelection);
            }
        });

        // Format table columns
        circumferenceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f m", item));
                }
            }
        });

        estimatedDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Ja" : "Nein");
                }
            }
        });
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            viewModel.saveTree();
        }
    }

    @FXML
    private void handleUpdate() {
        if (validateInput() && treeTable.getSelectionModel().getSelectedItem() != null) {
            viewModel.updateTree();
        }
    }

    @FXML
    private void handleDelete() {
        if (treeTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Baum löschen");
            alert.setHeaderText("Baum löschen");
            alert.setContentText("Sind Sie sicher, dass Sie diesen Baum löschen möchten?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    viewModel.deleteTree();
                }
            });
        }
    }

    @FXML
    private void handleClear() {
        viewModel.clearForm();
        treeTable.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (speciesField.getText() == null || speciesField.getText().trim().isEmpty()) {
            errorMessage.append("Bitte geben Sie eine Baumart ein.\n");
        }

        try {
            double circumference = Double.parseDouble(circumferenceField.getText());
            if (circumference <= 0) {
                errorMessage.append("Der Umfang muss größer als 0 sein.\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Bitte geben Sie einen gültigen Umfang ein.\n");
        }

        if (plantingDatePicker.getValue() == null) {
            errorMessage.append("Bitte wählen Sie ein Pflanzdatum aus.\n");
        }

        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte korrigieren Sie die folgenden Fehler:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }
} 