package excel.model;

import excel.tools.ExcelConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

public class SpreadsheetCellModel {
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
        this.valueBinding = Bindings.createStringBinding(this::calculateValue, this.formulaProperty);
    }

    private String calculateValue() {
        String formula = formulaProperty.get().trim();

        // Si la formule est vide, retourner une chaîne vide
        if (formula.isEmpty()) {
            return "";
        }

        // Si la formule commence par '=', traiter comme une expression
        if (formula.startsWith("=")) {
            String expression = formula.trim();

            // Vérifier si l'expression se termine par un opérateur

            if (expression.matches(".*[+\\-*/%<>=!&|^]$")) {
                return "SYNTAX_ERROR";
            }

            // Construire et évaluer l'expression
            Expression expr = new ExpressionBuilder(model).build(expression);
            if (expr != null) {
                try {
                    return String.valueOf(expr.interpret());
                } catch (Exception e) {
                    return "SYNTAX_ERROR";
                }
            } else {
                return "SYNTAX_ERROR";
            }
        } else {
            // Si la formule ne commence pas par '=', la traiter comme une chaîne de caractères brute
            return formula;
        }
    }



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
