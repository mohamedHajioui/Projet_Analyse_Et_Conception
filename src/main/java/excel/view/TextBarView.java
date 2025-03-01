package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TextBarView extends HBox {
    private final TextField formulaField;
    private final SpreadsheetViewModel viewModel;

    public TextBarView(SpreadsheetViewModel viewModel) {
        this.viewModel = viewModel;
        this.formulaField = new TextField();

        formulaField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(formulaField, Priority.ALWAYS);

        // Liaison bidirectionnelle avec la formule de la cellule sélectionnée
        formulaField.textProperty().bindBidirectional(viewModel.selectedCellContentProperty());

        // Lorsqu'on appuie sur Entrée, on met à jour la cellule sélectionnée
        formulaField.setOnAction(e -> {
            viewModel.updateSelectedCell(formulaField.getText());
        });
        this.getChildren().add(formulaField);
    }
}
