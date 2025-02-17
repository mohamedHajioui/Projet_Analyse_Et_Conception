package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane {

    public MainView(SpreadsheetViewModel viewModel) {
        this.setCenter(new MySpreadsheetView(viewModel));
    }
}
