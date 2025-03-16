package excel.viewmodel;

import excel.model.SpreadsheetCellModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;

public class SpreadsheetCellViewModel {
    private final SpreadsheetCellModel model;
    private boolean inEdition = false; // true si la vue est en mode édition
    private ObjectProperty<Object> cellContentProperty;

    public SpreadsheetCellViewModel(SpreadsheetCellModel model) {
        this.model = model;
    }

    private static String getStringFromObject(Object value) {
        return value == null ? "" : value.toString();
    }

    public void setCellContentProperty(ObjectProperty<Object> cellContentProperty) {
        this.cellContentProperty = cellContentProperty;

        // Listener pour mettre à jour le contenu de la cellule visuelle lorsque la valeur change dans le modèle
        ChangeListener<Object> valueListener = (obs, ov, nv) -> {
            if (!inEdition) { // Si on n'est pas en mode édition
                this.cellContentProperty.set(getStringFromObject(nv));
            }
        };
        // On associe ce listener à la propriété de la valeur calculée (valueBinding)
        model.valueBindingProperty().addListener(valueListener);

        // Listener pour mettre à jour le modèle lorsque la cellule visuelle change en mode édition
        ChangeListener<Object> expressionSetterListener = (obs, ov, nv) -> {
            if (inEdition) { // Si on est en mode édition
                model.setFormula(getStringFromObject(nv)); // Met à jour la formule dans le modèle
            }
        };
        // On associe le listener à la propriété de contenu visuel de la cellule
        this.cellContentProperty.addListener(expressionSetterListener);
    }

    public void setEditionMode(boolean editionMode) {
        this.inEdition = editionMode;

        this.cellContentProperty.set(inEdition ? model.getFormulaProperty() : getCellValue());
    }

    public String getCellValue() {
        return model.getValueBinding();

    }
    public void setCellValue(String value) {
        model.setDisplayedValue(value);
    }
    public String getFormula() {
        return model.getFormulaProperty();
    }

    public void setFormula(String formula) {
        model.setFormula(formula);
    }
    public void updateValue() {
        // Cette méthode sera appelée pour recalculer la valeur de la cellule
        String newValue = model.calculateValue();
        this.cellContentProperty.set(newValue);



    }
    public SpreadsheetCellModel getModel() {
        return model;
    }


}
