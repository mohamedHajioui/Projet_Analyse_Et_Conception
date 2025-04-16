package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {

    public MainView(SpreadsheetViewModel viewModel) {
        TextBarView textfield = new TextBarView(viewModel);
        MySpreadsheetView spreadsheetView = new MySpreadsheetView(viewModel);
        MenuBarView menuBar = new MenuBarView(viewModel);

        VBox.setMargin(textfield, new Insets(0, 8, 8, 8));
        VBox.setMargin(spreadsheetView, new Insets(5, 5, 5, 8));
        VBox.setMargin(menuBar, new Insets(0, 5, 5, 8));
        VBox topBox = new VBox(menuBar, textfield);
        this.setTop(topBox);
        this.setCenter(spreadsheetView);
    }
}
