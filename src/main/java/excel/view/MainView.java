package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import jdk.jfr.consumer.EventStream;

public class MainView extends BorderPane {
    private final SpreadsheetViewModel viewModel;

    public MainView(SpreadsheetViewModel viewModel) {
        this.viewModel = viewModel;
        initializeUI();
    }

    private void initializeUI() {
        //creation menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");

        fileMenu.getItems().addAll(openItem, saveItem);
        menuBar.getMenus().add(fileMenu);

        openItem.setOnAction(e -> handleOpen());
        saveItem.setOnAction(e -> handleSave());

        TextBarView textfield = new TextBarView(viewModel);
        MySpreadsheetView spreadsheetView = new MySpreadsheetView(viewModel);

        this.setTop(menuBar);
        this.setCenter(textfield);
        this.setBottom(spreadsheetView);
    }

    private void handleOpen() {
        FileIOHelper.openFile(viewModel);
    }

    private void handleSave() {
        FileIOHelper.saveFile(viewModel);
    }
}

