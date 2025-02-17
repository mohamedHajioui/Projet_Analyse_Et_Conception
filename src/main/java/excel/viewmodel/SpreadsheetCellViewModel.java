package excel.viewmodel;

import excel.model.SpreadsheetCellModel;
import javafx.beans.property.ReadOnlyObjectProperty;

public class SpreadsheetCellViewModel {
    private final SpreadsheetCellModel model;

    public SpreadsheetCellViewModel(SpreadsheetCellModel model) {
        this.model = model;
    }

    public ReadOnlyObjectProperty<String> getCellValueProperty() {
        return model.valueProperty();
    }

    public void setCellValue(String value) {
        model.set(value);
    }
}
