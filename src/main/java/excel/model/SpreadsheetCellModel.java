package excel.model;

import excel.tools.ExcelConverter;
import javafx.application.Platform;
import javafx.beans.property.*;

public class SpreadsheetCellModel {
    private final StringProperty formulaProperty = new SimpleStringProperty();
    private final SimpleObjectProperty<String> valueProperty = new SimpleObjectProperty<>();
    private final int row;
    private final int column;

    public SpreadsheetCellModel(String value, int row, int column) {
        this.row = row;
        this.column = column;
        this.formulaProperty.set(value);
        this.valueProperty.set(value);
        valueProperty.addListener((obs, oldVal, newVal) -> {
            System.out.println("Mise à jour affichage : " + newVal);
        });
    }

    public void setFormula(String formula){
        System.out.println("DEBUG : setFormula() appelé avec " + formula);

        this.formulaProperty.set(formula);

        if (formula.startsWith("=")) {
            Expression expr = new ExpressionBuilder().build(formula);

            if (expr != null) {
                String result = String.valueOf(expr.interpret());
                System.out.println("DEBUG : Résultat calculé = " + result);
                this.valueProperty.set(result);
            } else {
                System.out.println("DEBUG : Erreur dans le calcul");
                this.valueProperty.set("ERROR");
            }
        } else {
            this.valueProperty.set(formula);
        }
    }





    public ReadOnlyObjectProperty<String> valueProperty(){
        return this.valueProperty;
    }

    public void set(String value){
        this.valueProperty.set(value);
    }

    void bindBidirectional (){
        this.formulaProperty.bindBidirectional(this.valueProperty);
    }

    public String toString(){
        return "cell " + ExcelConverter.rowColToExcel(this.row, this.column)
                + " (row " + this.row + ", column " + this.column + ") = \"" + this.valueProperty.get() + "\"";
    }

    public String getFormulaProperty() {
        return formulaProperty.get();
    }

    public StringProperty formulaProperty() {
        return formulaProperty;
    }

    public String getValueProperty() {
        return valueProperty.get();
    }

    public SimpleObjectProperty<String> valuePropertyProperty() {
        return valueProperty;
    }


}
