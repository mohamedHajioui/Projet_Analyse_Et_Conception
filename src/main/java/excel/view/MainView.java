package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane {

    public MainView(SpreadsheetViewModel viewModel) {
        TextBarView textfield = new TextBarView(viewModel);
        MySpreadsheetView spreadsheetView = new MySpreadsheetView(viewModel);

        this.setTop(textfield);
        this.setCenter(spreadsheetView);
    }
}
