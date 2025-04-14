package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.io.File;
import java.io.IOException;

import static excel.view.SaveOpenFile.fileChooser;

public class MainView extends BorderPane {
    private final SpreadsheetViewModel viewModel;
    private final FileChooser fileChooser = new FileChooser();
    private final MySpreadsheetView spreadsheetView;

    public MainView(SpreadsheetViewModel viewModel) {
        this.viewModel = viewModel;
        this.spreadsheetView = new MySpreadsheetView(viewModel);
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
        //SaveOpenFile.openFile(viewModel);
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                viewModel.loadFromFile(file);
                spreadsheetView.refreshView();
                spreadsheetView.getGrid().setRows(spreadsheetView.createGridAndBindings().getRows());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleSave() {
        SaveOpenFile.saveFile(viewModel);
    }
}

