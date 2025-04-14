package excel.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

public class SpreadsheetModel {
    private final int rows;
    private final int columns;
    private final ObservableList<ObservableList<SpreadsheetCellModel>> data = FXCollections.observableArrayList();
    private SpreadsheetCellModel currentCell;

    public SpreadsheetModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        IntStream.range(0, rows).forEach(a -> addNewRow());
    }

//    public void reset(int rows, int columns) {
//
//        this.rows = rows;
//        this.columns = columns;
//
//        data.clear();
//        IntStream.range(0,rows).forEach(a-> addNewRow());
//    }
    public static SpreadsheetModel newSpreadsheetModel(int rows, int columns) {
        return new SpreadsheetModel(rows, columns);
    }


    public void setCurrentCell(SpreadsheetCellModel cell) {
        this.currentCell = cell;
    }
    public SpreadsheetCellModel getCurrentCell() {
        return currentCell;
    }


    public int getRowCount() {
        return this.rows;
    }

    public int getColumnCount() {
        return this.columns;
    }

    public SpreadsheetCellModel getCell(int line, int column) {
        if (line >= 0 && line < this.rows && column >= 0 && column < this.columns) {
            return this.data.get(line).get(column);
        } else {
            throw new IndexOutOfBoundsException();
        }

    }

    private void addNewRow() {
        ObservableList<SpreadsheetCellModel> newRow = FXCollections.observableArrayList();
        for (int column = 0; column < columns; column++) {
            newRow.add(new SpreadsheetCellModel("", data.size(), column, this));
        }
        data.add(newRow);
    }


}
