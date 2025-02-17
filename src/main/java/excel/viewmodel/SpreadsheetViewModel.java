package excel.viewmodel;

import excel.model.SpreadsheetModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class SpreadsheetViewModel {
    private final SpreadsheetModel model;
    private final SimpleBooleanProperty editableProperty = new SimpleBooleanProperty(true);
    private final ObservableList<String> actions = FXCollections.observableArrayList();
    private final StringProperty lastAction = new SimpleStringProperty("");

    public SpreadsheetViewModel(SpreadsheetModel model) {
        this.model = model;

        actions.addListener((ListChangeListener<String>) c -> {
            if (!actions.isEmpty()) {
                lastAction.set(actions.get(actions.size() - 1));
            }
        });
    }

    private SpreadsheetCellViewModel getCellViewModel(int line, int column) {
        return new SpreadsheetCellViewModel(model.getCell(line, column));
    }

    public int getRowCount() {
        return model.getRowCount();
    }

    public int getColumnCount() {
        return model.getColumnCount();
    }

    public ReadOnlyObjectProperty<String> getCellValueProperty(int line, int column) {
        return getCellViewModel(line, column).getCellValueProperty();
    }

    public void setCellValue(int line, int column, String value) {
        getCellViewModel(line, column).setCellValue(value);
    }

    public ReadOnlyBooleanProperty editableProperty() {
        return this.editableProperty;
    }

    public void toggleEditable() {
        this.editableProperty.set(this.editableProperty.not().get());
    }

    public boolean addAction(String action) {
        return actions.add(action);
    }

    public ReadOnlyStringProperty lastActionProperty() {
        return lastAction;
    }

    public ObservableList<String> getActions() {
        return actions; //TODO : return an unmodifiable list mais dont le listener est toujours actif
    }
}

