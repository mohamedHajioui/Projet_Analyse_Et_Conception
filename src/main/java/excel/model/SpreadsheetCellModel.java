package excel.model;

import excel.tools.ExcelConverter;
import javafx.beans.property.*;

public class SpreadsheetCellModel {
    private final StringProperty contentProperty = new SimpleStringProperty();
    private final SimpleObjectProperty<String> valueProperty = new SimpleObjectProperty<>();
    private final int row;
    private final int column;

    public SpreadsheetCellModel(String value, int row, int column) {
        this.valueProperty.set(value);
        this.row = row;
        this.column = column;
        bindBidirectional();
    }

    public ReadOnlyObjectProperty<String> valueProperty(){
        return this.valueProperty;
    }

    public void set(String value){
        this.valueProperty.set(value);
    }

    void bindBidirectional (){
        this.contentProperty.bindBidirectional(this.valueProperty);
    }

    public String toString(){
        return "cell " + ExcelConverter.rowColToExcel(this.row, this.column)
                + " (row " + this.row + ", column " + this.column + ") = \"" + this.valueProperty.get() + "\"";
    }

    public String getContentProperty() {
        return contentProperty.get();
    }

    public StringProperty contentPropertyProperty() {
        return contentProperty;
    }

    public String getValueProperty() {
        return valueProperty.get();
    }

    public SimpleObjectProperty<String> valuePropertyProperty() {
        return valueProperty;
    }


}
