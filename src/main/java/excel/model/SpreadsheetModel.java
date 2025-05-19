package excel.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private final IntegerProperty countSum = new SimpleIntegerProperty(0);

    public ReadOnlyIntegerProperty countSumProperty() {
        return countSum;
    }

    public void updateSumCount(String oldValue, String newValue) {
        SpreadsheetCellModel cell = getCurrentCell();
        if (cell == null) return;

        String cellValue = getCurrentCell().getValue();
        if ("SYNTAX_ERROR".equals(cellValue) || "#VALEUR".equals(cellValue) || "#CIRCULAR_REF".equals(cellValue)) {
            return;
        }

        Pattern patternSum = Pattern.compile("SUM\\(", Pattern.CASE_INSENSITIVE);
        Pattern patternExp = Pattern.compile("\\^");

        int countOld = 0;
        if (oldValue != null && oldValue.startsWith("=")) {
            Matcher matchSumOld = patternSum.matcher(oldValue);
            Matcher matchExpOld = patternExp.matcher(oldValue);
            while (matchSumOld.find()) countOld++;
            while (matchExpOld.find()) countOld++;
        }


        int countNew = 0;
        if (newValue != null && newValue.startsWith("=")) {
            Matcher matchSumNew = patternSum.matcher(newValue);
            Matcher matchExpNew = patternExp.matcher(newValue);
            while (matchSumNew.find()) countNew++;
            while (matchExpNew.find()) countNew++;
        }

        int diff = countNew - countOld;
        countSum.set(Math.max(0, countSum.get()) + diff);
    }






}
