package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.swing.*;

public class MainView extends BorderPane {

    public MainView(SpreadsheetViewModel viewModel) {
        TextBarView textfield = new TextBarView(viewModel);
        MySpreadsheetView spreadsheetView = new MySpreadsheetView(viewModel);
        MenuBarView menuBarView = new MenuBarView(viewModel);

        VBox topBox = new VBox(menuBarView, textfield);

        this.setTop(topBox);
        this.setCenter(spreadsheetView);
    }
}
