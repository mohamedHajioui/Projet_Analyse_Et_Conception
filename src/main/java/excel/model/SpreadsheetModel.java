package excel.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

public class SpreadsheetModel {
    private final int rows;
    private final int columns;
    private final ObservableList<ObservableList<SpreadsheetCellModel>> data = FXCollections.observableArrayList();

    public SpreadsheetModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        IntStream.range(0, rows).forEach(a -> addNewRow());
    }


    public int getRowCount() {
        return this.rows;
    }

    public int getColumnCount() {
        return this.columns;
    }

    public SpreadsheetCellModel getCell(int line, int column) {
        return data.get(line).get(column);
    }

    private void addNewRow() {
        ObservableList<SpreadsheetCellModel> newRow = FXCollections.observableArrayList();
        for (int column = 0; column < columns; column++) {
            newRow.add(new SpreadsheetCellModel("", data.size(), column));
        }
        data.add(newRow);
    }


}
