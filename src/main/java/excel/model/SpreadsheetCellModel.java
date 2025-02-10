package excel.model;

import excel.tools.ExcelConverter;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SpreadsheetCellModel {
    private final SimpleObjectProperty<String> valueProperty = new SimpleObjectProperty<>();
    private final int row;
    private final int column;

    public SpreadsheetCellModel(String value, int row, int column) {
        this.valueProperty.set(value);
        this.row = row;
        this.column = column;
    }

    public ReadOnlyObjectProperty<String> valueProperty(){
        return this.valueProperty;
    }

    public void set(String value){
        this.valueProperty.set(value);
    }

    void bindBidirectional (SpreadsheetCellModel other){
        this.valueProperty.bindBidirectional(other.valueProperty);
    }

    public String toString(){
        return "cell " + ExcelConverter.rowColToExcel(this.row, this.column)
                + " (row " + this.row + ", column " + this.column + ") = \"" + this.valueProperty.get() + "\"";
    }
}
