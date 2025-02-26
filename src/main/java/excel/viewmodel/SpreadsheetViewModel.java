package excel.viewmodel;

import excel.model.SpreadsheetCellModel;
import excel.model.SpreadsheetModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class SpreadsheetViewModel {
    private final SpreadsheetModel model;
    private final ObjectProperty<SpreadsheetCellModel> selectedCell = new SimpleObjectProperty<>();
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

    public void setSelectedCell(int row, int column) {
        SpreadsheetCellModel cell = model.getCell(row, column);
        if (cell != null) {
            selectedCell.set(cell);
        }
    }

    public void updateSelectedCell(String newValue) {
        if (selectedCell.get() != null) {
            selectedCell.get().setFormula(newValue);
        }
    }

    public StringProperty selectedCellFormulaProperty() {
        return selectedCell.get() != null ? selectedCell.get().formulaProperty() : new SimpleStringProperty("");
    }

    public ReadOnlyObjectProperty<String> selectedCellValueProperty() {
        return selectedCell.get() != null ? selectedCell.get().valueProperty() : new SimpleObjectProperty<>("");
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
        return model.getCell(line, column).valueProperty();
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

    public void setCellFormula(int row, int column, String formula) {
        SpreadsheetCellModel cell = model.getCell(row, column);
        if (cell != null) {
            cell.setFormula(formula);
        }
    }







}

