package excel.view;

import excel.viewmodel.SpreadsheetCellViewModel;
import excel.viewmodel.SpreadsheetViewModel;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class MySpreadsheetView extends SpreadsheetView {
    private static final int CELL_PREF_WIDTH = 150;
    private final SpreadsheetViewModel viewModel;
    private final GridBase grid;

    private SpreadsheetCell selectedCell = null; // Référence vers la cellule sélectionnée

    public MySpreadsheetView(SpreadsheetViewModel viewModel) {
        this.viewModel = viewModel;
        this.grid = createGridAndBindings();
        this.setGrid(this.grid);
        configEditLogic();
        layoutSpreadSheet();
        addKeyboardShortcuts();
    }

    private void layoutSpreadSheet() {
        for (int column = 0; column < grid.getColumnCount(); column++) {
            this.getColumns().get(column).setPrefWidth(CELL_PREF_WIDTH);
        }
    }

    GridBase createGridAndBindings() {
        GridBase grid = new GridBase(viewModel.getRowCount(), viewModel.getColumnCount());
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {


                SpreadsheetCellViewModel cellVM = viewModel.getCellViewModel(row, column); // On récupère le VM de la cellule
                String cellValue = cellVM.getCellValue();
                SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, cellValue);
                cellVM.setCellContentProperty(cell.itemProperty()); // On passe la ref de itemProperty au VM
                cell.itemProperty().set(viewModel.getCellValue(row, column));
                list.add(cell);
            }
            rows.add(list);
        }
        grid.setRows(rows);
        return grid;
    }

    private void configEditLogic() {
        editingCellProperty().addListener((observable, oldValue, newValue) -> {
            boolean editMode = (newValue != null);
            if (editMode) {
                // Si on rentre en édition
                changeEditionMode(true);
            } else {
                // Si on quitte l’édition
                changeEditionMode(false);
            }
        });

        // Listener pour gérer la sélection d'une cellule (inchangé)
        this.getSelectionModel().getSelectedCells().addListener((InvalidationListener) il -> {
            if (getSelectionModel().getSelectedCells().isEmpty()) {
                changeEditionMode(false);
                this.selectedCell = null;
            } else {
                var tablePosition = getSelectionModel().getSelectedCells().get(0);
                int row = tablePosition.getRow(), col = tablePosition.getColumn();
                viewModel.setSelectedCell(row, col);
                updateSelectedCellInView(row, col);
            }
        });
    }


    // Quand une nouvelle cellule est sélectionnée, on désactive le mode édition de l'ancienne cellule
    private void updateSelectedCellInView(int row, int col) {
        changeEditionMode(false);
        this.selectedCell = this.grid.getRows().get(row).get(col);
    }

    private void changeEditionMode(boolean inEdition) {
        if (selectedCell == null) return; //Si aucune cellule n'est sélectionnée, on ne fait rien

        //Obtenir le VM correspondant à la cellule sélectionnée
        SpreadsheetCellViewModel cellVM = viewModel.getCellViewModel(selectedCell.getRow(), selectedCell.getColumn());


        //On indique au VM le mode d'édition
        cellVM.setEditionMode(inEdition);

    }
    private void addKeyboardShortcuts() {
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown()) {
                if (event.getCode() == KeyCode.Z) {
                    // Appeler la méthode undo() du ViewModel
                    viewModel.undo();
                } else if (event.getCode() == KeyCode.Y) {
                    // Appeler la méthode redo() du ViewModel
                    viewModel.redo();
                } else if (event.getCode() == KeyCode.C) {
                    viewModel.copySelectedCell();
                } else if (event.getCode() == KeyCode.V) {
                    viewModel.pasteToSelectedCell();
                }
            }
        });
    }
}
