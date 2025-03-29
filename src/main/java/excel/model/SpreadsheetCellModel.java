package excel.model;

import excel.tools.ExcelConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SpreadsheetCellModel {
    private final StringProperty displayedValue = new SimpleStringProperty("");
    private final StringProperty formulaProperty = new SimpleStringProperty(""); // Texte de la formule
    private final StringBinding valueBinding; // Valeur calculée de la cellule sous forme de String
    private final int row;
    private final int column;
    private final SpreadsheetModel model;

    public SpreadsheetCellModel(String value, int row, int column, SpreadsheetModel model) {
        this.row = row;
        this.column = column;
        this.model = model;
        this.formulaProperty.set(value);
        this.displayedValue.set(value);
        this.valueBinding = Bindings.createStringBinding(this::calculateValue, this.formulaProperty);
    }

    public String calculateValue() {
        String formula = formulaProperty.get();
        String displayed = displayedValue.get();
        if (formula.startsWith("=")) {
            try {
                model.setCurrentCell(this);
                Set<SpreadsheetCellModel> visitedCells = new HashSet<>();
                if (checkCircularReference(this, visitedCells)) {
                    // Affichage dans cellule concerné par circlar ref
                    //  for (SpreadsheetCellModel cell : visitedCells) {
                    //   cell.setDisplayedValue("#CIRCULAR_REF");
                    // }
                    //Affichage dans current cell
                    return "#CIRCULAR_REF";
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





    //avertir cell dependante du changement de valeur
   /* private void alertDependentCells() {
        for (SpreadsheetCellModel dependentCell : dependentCells) {
            dependentCell.recalculateValue();
        }
    } */




    public StringBinding valueProperty() {
        return valueBinding;
    }

    public String getValue() {
        return valueBinding.get();
    }

    public String getFormula() {
        return formulaProperty.get();
    }

    public void setFormula(String formula) {
        this.formulaProperty.set(formula);
    }

    public void setDisplayedValue(String displayed) {
        this.displayedValue.set(displayed);
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

    public StringProperty formulaProperty() {
        return formulaProperty;
    }

    public SimpleObjectProperty<String> valuePropertyProperty() {
        return new SimpleObjectProperty<>(getValue());
    }
}
