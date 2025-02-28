package excel.view;

import excel.tools.ExcelConverter;
import excel.viewmodel.SpreadsheetViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.util.Objects;


public class MySpreadsheetView extends SpreadsheetView {
    private static final int CELL_PREF_WIDTH = 150;
    private final SpreadsheetViewModel viewModel;
    private final GridBase grid;


    public MySpreadsheetView(SpreadsheetViewModel viewModel) {
        this.viewModel = viewModel;

        this.grid = createGridAndBindings();
        this.setGrid(this.grid);

        this.editableProperty().bind(viewModel.editableProperty());

        this.editingCellProperty().addListener((observableValue, oldVal, newVal) -> {
            if(newVal != null) {
                System.out.println("edit cell " + ExcelConverter.rowColToExcel(newVal.getRow(), newVal.getColumn()));
                viewModel.addAction("edit cell " + ExcelConverter.rowColToExcel(newVal.getRow(), newVal.getColumn()));
            }
        });

        // Gérer la sélection d'une cellule
        this.getSelectionModel().getSelectedCells().addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (!change.getList().isEmpty()) {
                TablePosition cell = change.getList().get(0);
                int row = cell.getRow();
                int column = cell.getColumn();

                System.out.println("select cell " + ExcelConverter.rowColToExcel(row, column));
                viewModel.addAction("select cell " + ExcelConverter.rowColToExcel(row, column));

                // Mettre à jour la cellule sélectionnée dans le ViewModel
                viewModel.setSelectedCell(row, column);
            }
        });

        layoutSpreadSheet();
    }


    private void layoutSpreadSheet() {
        for (int column = 0; column < grid.getColumnCount(); column++) {
            this.getColumns().get(column).setPrefWidth(CELL_PREF_WIDTH);
        }
    }

    private GridBase createGridAndBindings() {
        GridBase grid = new GridBase(viewModel.getRowCount(), viewModel.getColumnCount());

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");

                int finalRow = row;
                int finalColumn = column;

                // Mise à jour de la cellule lorsque la valeur change dans le modèle
                viewModel.getCellValueProperty(finalRow, finalColumn).addListener((obs, oldVal, newVal) -> {
                    if (!Objects.equals(oldVal, newVal)) {
                        cell.setItem(newVal); // Mise à jour manuelle
                    }
                });

                // Lorsque l'utilisateur édite une cellule
                cell.itemProperty().addListener((obs, oldVal, newVal) -> {
                    if (!Objects.equals(oldVal, newVal)) {
                        if (newVal instanceof String value) {
                            viewModel.setCellFormula(finalRow, finalColumn, value); // Met à jour le modèle
                        }
                    }
                });

                //  Quand l'utilisateur entre une formule, on la traite
                cell.itemProperty().addListener((observableValue, oldVal, newVal) -> {
                    if (!Objects.equals(oldVal, newVal)) {
                        if (newVal instanceof String value) {
                            viewModel.setCellFormula(finalRow, finalColumn, value);
                        }
                    }
                });

                list.add(cell);
            }
            rows.add(list);
        }
        grid.setRows(rows);
        return grid;
    }

}
