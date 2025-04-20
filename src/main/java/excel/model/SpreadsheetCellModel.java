package excel.model;

import excel.tools.ExcelConverter;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SpreadsheetCellModel {
    private final StringProperty formulaProperty = new SimpleStringProperty(""); // Texte de la formule
    private final StringBinding valueBinding; // Valeur calculée de la cellule sous forme de String
    private final int row;
    private final int column;
    private final SpreadsheetModel model;
    protected final Set<SpreadsheetCellModel> dependentCells = new HashSet<>();


    public SpreadsheetCellModel(String value, int row, int column, SpreadsheetModel model) {
        this.row = row;
        this.column = column;
        this.model = model;
        this.formulaProperty.set(value);
        this.valueBinding = Bindings.createStringBinding(this::calculateValue, this.formulaProperty);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String calculateValue() {
        String formula = formulaProperty.get();
        if (formula.startsWith("=")) {
            try {
                model.setCurrentCell(this);
                Set<SpreadsheetCellModel> visitedCells = new HashSet<>();
                if (checkCircularReference(this, visitedCells)) {
                    //Affichage dans current cell
                    return "#CIRCULAR_REF";
                }

                List<String> referencedCells = extractCellReferences(formula);
                for (String cell : referencedCells){
                    int[] coords = ExcelConverter.excelToRowCol(cell);
                    SpreadsheetCellModel referencedCell = model.getCell(coords[0], coords[1]);
                    referencedCell.dependentCells.add(this);

                }
                Expression expr = new ExpressionBuilder(model).build(formula);
                if (expr != null) {
                    Object result = expr.interpret();
                    if (result != null && result.equals("#VALEUR")) {
                        return "#VALEUR";
                    }
                    return result != null ? result.toString() : "SYNTAX_ERROR";
                } else {
                    return "SYNTAX_ERROR";
                }
            } catch (IllegalArgumentException e) {
                if (formula.toUpperCase().contains("OR") || formula.toUpperCase().contains("AND")) {
                    return "#VALEUR";
                }
                // Gérer les exceptions liées à des arguments invalides dans l'expression
                return "SYNTAX_ERROR";
            } catch (Exception e) {
                // Gérer toute autre exception inattendue
                return "SYNTAX_ERROR";
            }
        } else {
            return formula; // Si ce n'est pas une formule, on retourne la valeur brute
        }
    }

    private boolean checkCircularReference(SpreadsheetCellModel currentCell, Set<SpreadsheetCellModel> visitedCells) {

        //Si current cell est dans visitedCells set alors c'est reference circulaire
        if (visitedCells.contains(currentCell)) {
            return true;
        }

        visitedCells.add(currentCell);

        List<String> cellReferences = extractCellReferences(currentCell.getFormula());
        Set<String> alreadyCheckedRefs = new HashSet<>();

        for (String cellRef : cellReferences) {
            int[] coords = ExcelConverter.excelToRowCol(cellRef);
            SpreadsheetCellModel referencedCell = model.getCell(coords[0], coords[1]);
            if (alreadyCheckedRefs.contains(cellRef)) {
                continue;
            }
            alreadyCheckedRefs.add(cellRef);
            if (referencedCell != null) {

                List<String> referencedCellReferences = extractCellReferences(referencedCell.getFormula());
                if (referencedCellReferences.isEmpty()) {
                    return false;
                }
                if (checkCircularReference(referencedCell, visitedCells)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> extractCellReferences(String formula) {
        List<String> cellReferences = new ArrayList<>();
        // Utiliser une expression régulière pour extraire les références de cellules (par exemple, A1, B2, etc.)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[A-Z]+[0-9]+");
        java.util.regex.Matcher matcher = pattern.matcher(formula);
        while (matcher.find()) {
            cellReferences.add(matcher.group());
        }
        return cellReferences;
    }
    private boolean isUpdating = false;
    public void updatevalue() {
        // Éviter les mises à jour récursives
        if (isUpdating) {
            return;
        }

        try {
            isUpdating = true;

            String currentFormula = formulaProperty.get();
            // Vérifier si c'est une formule SUM et si elle contient une référence circulaire
            if (currentFormula.toUpperCase().startsWith("=SUM(")) {
                Set<SpreadsheetCellModel> visitedCells = new HashSet<>();
                if (checkCircularReference(this, visitedCells)) {
                    formulaProperty.set("#CIRCULAR_REF");
                    return;
                }
            }

            formulaProperty.set("");
            formulaProperty.set(currentFormula);

            // Mettre à jour les cellules dépendantes
            if (!dependentCells.isEmpty()) {
                // Créer une copie pour éviter les ConcurrentModificationException
                Set<SpreadsheetCellModel> cellsToUpdate = new HashSet<>(dependentCells);
                for (SpreadsheetCellModel cell : cellsToUpdate) {
                    cell.updatevalue();
                }
            }
        } finally {
            isUpdating = false;
        }
    }

    public String getValue() {
        return valueBinding.get();
    }

    public String getFormula() {
        return formulaProperty.get();
    }

    public void setFormula(String formula) {
        updatevalue();
        this.formulaProperty.set(formula);
    }

    public StringBinding valueBindingProperty() {
        return valueBinding;  // Exposer le StringBinding de la valeur
    }

    public String getFormulaProperty() {
        return formulaProperty.get();
    }


    public String getValueBinding() {
        return valueBinding.get();
    }

    @Override
    public String toString() {
        return "cell " + ExcelConverter.rowColToExcel(this.row, this.column)
                + " (row " + this.row + ", column " + this.column + ") = \"" + this.valueBinding.get() + "\"";
    }

}
