package excel.viewmodel;

import excel.model.SpreadsheetModel;
import excel.tools.ExcelConverter;
import javafx.beans.property.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpreadsheetViewModel {
    private final int NB_ROW, NB_COL;
    private SpreadsheetModel model;
    private final List<SpreadsheetCellViewModel> cellVMs = new ArrayList<>(); // Liste des VM associés aux cellules
    private final ObjectProperty<SpreadsheetCellViewModel> selectedCell = new SimpleObjectProperty<>(); // Cellule sélectionnée
    private final StringProperty selectedCellContent = new SimpleStringProperty(""); // Le contenu de la cellule sélectionnée (formule ou valeur)
    private final StringProperty selectedCellFormula = new SimpleStringProperty(""); // Exprression de la cellule sélectionnée
    private final FileChooser fileChooser = new FileChooser();

    public SpreadsheetViewModel(SpreadsheetModel model) {
        this.NB_ROW = model.getRowCount();
        this.NB_COL = model.getColumnCount();


        for (int i = 0; i < NB_ROW; i++) {
            for (int j = 0; j < NB_COL; j++) {
                SpreadsheetCellViewModel cellViewModel = new SpreadsheetCellViewModel(model.getCell(i, j));
                cellVMs.add(cellViewModel);
                final int rowIndex = i;
                final int colIndex = j;

                cellViewModel.getModel().valueBindingProperty().addListener((observable, oldValue, newValue) -> {
                    // Si la cellule change, on vérifie ses dépendances et on les met à jour
                    System.out.println("Cell value changed: " + oldValue + " -> " + newValue);
                    updateDependentCells(rowIndex, colIndex);
                });
            }
        }
    }
    public void updateDependentCells(int row, int column) {
        for (SpreadsheetCellViewModel cellViewModel : cellVMs) {
            String formula = cellViewModel.getFormula();  // Récupère la formule de la cellule

            if (formula.startsWith("=")) {  // Si la cellule a une formule, on vérifie ses dépendances
                List<String> cellReferences = ExcelConverter.extractCellReferences(formula);
                for (String reference : cellReferences) {
                    int[] cellIndices = ExcelConverter.excelToRowCol(reference);
                    int referencedRow = cellIndices[0];
                    int referencedCol = cellIndices[1];

                    // Si la cellule référencée correspond à la cellule modifiée, on met à jour la cellule
                    if (referencedRow == row && referencedCol == column) {
                        cellViewModel.updateValue();


                        break;
                    }
                }
            }
        }
    }


    // Récuperer le SpreadsheetCellViewModel associé à une cellule donnée
    public SpreadsheetCellViewModel getCellViewModel(int line, int column) {
        return cellVMs.get(line * NB_COL + column);
    }


    public int getRowCount() {
        return NB_ROW;
    }

    public int getColumnCount() {
        return NB_COL;
    }

    public String getCellValue(int line, int col) {
        return getCellViewModel(line, col).getCellValue(); // Accède à la valeur de la cellule
    }

    public void setCellValue(int line, int column, String value) {
        getCellViewModel(line, column).setCellValue(value); // Met à jour la valeur de la cellule
    }


    public StringProperty selectedCellContentProperty() {
        return selectedCellContent;
    }

    // Gère la cellule sélectionnée et met à jour selectedCellContent
    public void setSelectedCell(int row, int col) {
        SpreadsheetCellViewModel cellVM = getCellViewModel(row, col);
        selectedCell.set(cellVM); // Met à jour la cellule sélectionnée dans le ViewModel

        // Met à jour le contenu de la cellule sélectionnée dans selectedCellContent
        selectedCellContent.set(cellVM.getFormula()); // On affiche la formule brute
        selectedCellFormula.set(cellVM.getFormula()); // On met à jour la formule dans selectedCellFormula

    }

    // Met à jour le contenu de la cellule sélectionnée dans le modèle avec la nouvelle valeur
    public void updateSelectedCell(String newValue) {
        if (selectedCell.get() != null) {
            selectedCell.get().setFormula(newValue); // Met à jour la formule de la cellule
            selectedCellContent.set(newValue); // Met à jour le contenu dans selectedCellContent
            selectedCellFormula.set(newValue); // Met à jour la formule dans selectedCellFormula
        }
    }

    public ObjectProperty<SpreadsheetCellViewModel> selectedCellProperty() {
        return selectedCell;
    }

    public StringProperty selectedCellFormulaProperty() {
        return selectedCellFormula; // Expose la formule brute de la cellule sélectionnée
    }


}
