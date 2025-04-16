package excel.view;

import excel.viewmodel.SaveOpenFile;
import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainView extends BorderPane {

    public MainView(SpreadsheetViewModel viewModel) {
        TextBarView textfield = new TextBarView(viewModel);
        MySpreadsheetView spreadsheetView = new MySpreadsheetView(viewModel);
        MenuBarView menuBar = new MenuBarView(viewModel);
        this.setTop(menuBar);
        this.setCenter(textfield);
        this.setBottom(spreadsheetView);
    }

}

