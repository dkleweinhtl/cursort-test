package com.parkmanager.viewmodel;

import com.parkmanager.model.Tree;
import com.parkmanager.service.DatabaseService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class TreeViewModel {
    private final DatabaseService databaseService;
    private final ObservableList<Tree> trees;
    
    // Properties for binding
    private final StringProperty species = new SimpleStringProperty();
    private final DoubleProperty circumference = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> plantingDate = new SimpleObjectProperty<>();
    private final BooleanProperty estimatedPlantingDate = new SimpleBooleanProperty();
    private final IntegerProperty condition = new SimpleIntegerProperty();
    private final ObjectProperty<Tree> selectedTree = new SimpleObjectProperty<>();

    public TreeViewModel() {
        this.databaseService = DatabaseService.getInstance();
        this.trees = FXCollections.observableArrayList();
        loadTrees();
    }

    public void loadTrees() {
        trees.clear();
        trees.addAll(databaseService.getAllTrees());
    }

    public void saveTree() {
        Tree tree = new Tree(
            species.get(),
            circumference.get(),
            plantingDate.get(),
            estimatedPlantingDate.get(),
            condition.get()
        );
        
        databaseService.saveTree(tree);
        loadTrees();
        clearForm();
    }

    public void updateTree() {
        if (selectedTree.get() != null) {
            Tree tree = selectedTree.get();
            tree.setSpecies(species.get());
            tree.setCircumference(circumference.get());
            tree.setPlantingDate(plantingDate.get());
            tree.setEstimatedPlantingDate(estimatedPlantingDate.get());
            tree.setCondition(condition.get());
            
            databaseService.updateTree(tree);
            loadTrees();
            clearForm();
        }
    }

    public void deleteTree() {
        if (selectedTree.get() != null) {
            databaseService.deleteTree(selectedTree.get().getId());
            loadTrees();
            clearForm();
        }
    }

    public void clearForm() {
        species.set("");
        circumference.set(0.0);
        plantingDate.set(LocalDate.now());
        estimatedPlantingDate.set(false);
        condition.set(1);
        selectedTree.set(null);
    }

    public void selectTree(Tree tree) {
        if (tree != null) {
            selectedTree.set(tree);
            species.set(tree.getSpecies());
            circumference.set(tree.getCircumference());
            plantingDate.set(tree.getPlantingDate());
            estimatedPlantingDate.set(tree.isEstimatedPlantingDate());
            condition.set(tree.getCondition());
        }
    }

    // Getters for properties
    public ObservableList<Tree> getTrees() {
        return trees;
    }

    public StringProperty speciesProperty() {
        return species;
    }

    public DoubleProperty circumferenceProperty() {
        return circumference;
    }

    public ObjectProperty<LocalDate> plantingDateProperty() {
        return plantingDate;
    }

    public BooleanProperty estimatedPlantingDateProperty() {
        return estimatedPlantingDate;
    }

    public IntegerProperty conditionProperty() {
        return condition;
    }

    public ObjectProperty<Tree> selectedTreeProperty() {
        return selectedTree;
    }
} 