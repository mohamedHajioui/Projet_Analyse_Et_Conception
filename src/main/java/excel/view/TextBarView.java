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

        formulaField.textProperty().bindBidirectional(viewModel.selectedCellProperty());

        this.getChildren().add(formulaField);
    }
}
