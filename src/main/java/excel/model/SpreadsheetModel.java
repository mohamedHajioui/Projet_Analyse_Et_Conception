package excel.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Stack;
import java.util.stream.IntStream;

public class SpreadsheetModel {
    private final int rows;
    private final int columns;
    private final ObservableList<ObservableList<SpreadsheetCellModel>> data = FXCollections.observableArrayList();
    private SpreadsheetCellModel currentCell;
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public SpreadsheetModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        IntStream.range(0, rows).forEach(a -> addNewRow());
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
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }



}
