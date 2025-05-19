package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.awt.*;

public class CountSumAndExp extends HBox {
    private SpreadsheetViewModel viewModel;
    private Label counterLabel = new Label();

    public CountSumAndExp(SpreadsheetViewModel viewModel) {
        this.viewModel = viewModel;


        viewModel.sumCountPropertyVm().addListener((observable, oldValue, newValue) -> {
            updateLabelCounter();
        });
        updateLabelCounter();

        this.getChildren().add(counterLabel);
    }

    public void updateLabelCounter() {
        int count = viewModel.sumCountPropertyVm().get();
        if (count <= 0){
            counterLabel.setTextFill(Color.RED);
            counterLabel.setText("Il n'y a aucun operateur SUM et ^");
        } else {
            counterLabel.setTextFill(Color.GREEN);
            counterLabel.setText("Il y a " + count + " operateurs SUM et ^");
        }
    }
}
