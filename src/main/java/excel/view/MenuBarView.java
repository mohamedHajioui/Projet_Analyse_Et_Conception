package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import excel.viewmodel.SaveOpenFile;

public class MenuBarView extends HBox {
    //private final SpreadsheetViewModel viewModel;
    private final MenuBar menuBar;
    public final SaveOpenFile handleFile;

    public MenuBarView(SpreadsheetViewModel viewModel) {
        this.handleFile = new SaveOpenFile(viewModel);
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().addAll(openItem, saveItem);
        menuBar.getMenus().add(fileMenu);

        openItem.setOnAction(e -> handleFile.handleOpen());
        saveItem.setOnAction(e -> handleFile.handleSave());
        this.getChildren().add(menuBar);
    }
}
