package excel.model;

import excel.tools.ExcelConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

import java.util.HashSet;
import java.util.Set;


public class SpreadsheetCellModel {
    private final StringProperty formulaProperty = new SimpleStringProperty(""); // Texte de la formule
    private final StringBinding valueBinding; // Valeur calculée de la cellule sous forme de String
    private final int row;
    private final int column;
    private final SpreadsheetModel model;
    private final Set<SpreadsheetCellModel> dependentCells = new HashSet<>();

    public SpreadsheetCellModel(String value, int row, int column, SpreadsheetModel model) {
        this.row = row;
        this.column = column;
        this.model = model;
        this.formulaProperty.set(value);
        this.valueBinding = Bindings.createStringBinding(this::calculateValue, this.formulaProperty);

        //ajout d'un listener pour notifier de changement de valeur
      //  this.valueBinding.addListener((observable, oldValue, newValue) -> {alertDependentCells(); });

    }


    // Calculer la valeur de la cellule en fonction de la formule
    private String calculateValue() {
        String formula = formulaProperty.get();
        if (formula.startsWith("=")) {
            try {
                Expression expr = new ExpressionBuilder(model).build(formula);
                if (expr != null) {
                    return String.valueOf(expr.interpret());
                } else {
                    return "ERROR";
                }
            } catch (Exception e) { return "SYNTAX_ERROR"; }
        } else {
            return formula; // Si ce n'est pas une formule, on retourne la valeur brute
        }
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
