package excel.viewmodel;

import excel.model.Command;
import excel.model.CommandManager;
import excel.model.SpreadsheetCellModel;
import excel.model.SpreadsheetModel;
import excel.model.SpreadsheetModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class SpreadsheetCellViewModel {
    private final SpreadsheetCellModel model;
    private boolean inEdition = false; // true si la vue est en mode édition
    private ObjectProperty<Object> cellContentProperty;
    private final SpreadsheetModel spreadsheetModel;
    private final SpreadsheetViewModel spreadsheetViewModel;

    public SpreadsheetCellViewModel(SpreadsheetModel spreadsheetModel,SpreadsheetCellModel model,SpreadsheetViewModel spreadsheetViewModel) {
        this.spreadsheetModel = spreadsheetModel;
        this.model = model;
        this.spreadsheetViewModel = spreadsheetViewModel;
    }

    private static String getStringFromObject(Object value) {
        return value == null ? "" : value.toString();
    }

    public void setCellContentProperty(ObjectProperty<Object> cellContentProperty) {
        this.cellContentProperty = cellContentProperty;

        // Listener pour mettre à jour le contenu de la cellule visuelle lorsque la valeur change dans le modèle
        ChangeListener<Object> valueListener = (obs, ov, nv) -> {
            if (!inEdition) {
                String value = getStringFromObject(nv);
                try {
                    // Appliquer le formatage directement
                    this.cellContentProperty.set(formatNumber(value));
                } catch (NumberFormatException e) {
                    // Si ce n'est pas un nombre, afficher la valeur telle quelle
                    this.cellContentProperty.set(value);
                }
            }
        };
        // On associe ce listener à la propriété de la valeur calculée (valueBinding)
        model.valueBindingProperty().addListener(valueListener);

        // Listener pour mettre à jour le modèle lorsque la cellule visuelle change en mode édition
        ChangeListener<Object> expressionSetterListener = (obs, ov, nv) -> {
            if (inEdition) {
                String newValue = getStringFromObject(nv);
                if (!newValue.equals(model.getFormulaProperty())) {  // Vérification supplémentaire
                    executecomand(newValue);
                    setFormula(newValue);
                    model.updatevalue();
                }
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
        String value = model.getValueBinding();
        try {
            // Essaie de formater si c'est un nombre
            return formatNumber(value);
        } catch (NumberFormatException e) {
            // Si ce n'est pas un nombre, retourne la valeur telle quelle
            return value;
        }
    }
    public String getFormula() {
        return model.getFormulaProperty();
    }

    public void setFormula(String formula) {
        model.setFormula(formula);
    }
    public void executecomand(String formula) {
        // Ne créer une commande que si la nouvelle formule est différente de l'ancienne
        String currentFormula = model.getFormulaProperty();
        if (!formula.equals(currentFormula)) {
            Command command = new CommandManager(model, formula);
            spreadsheetModel.executeCommand(command);
            spreadsheetViewModel.updateUndoRedoState();
        }
    }
    public void updateValue() {
        model.updatevalue();
    }
    public SpreadsheetCellModel getModel() {
        return model;
    }
    protected static String formatNumber(String value){
         if (value != null) {
            try {
                String strValue = (value).replace(",", ".");
                double doubleParse = Double.parseDouble(value);
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.');
                DecimalFormat df = new DecimalFormat("#.##" +
                        "", symbols);
                return df.format(doubleParse);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid number: " + value);
            }
        }

        return null;
    }


}
