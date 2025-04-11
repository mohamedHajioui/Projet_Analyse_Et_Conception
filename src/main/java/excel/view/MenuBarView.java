package excel.view;

import excel.viewmodel.SpreadsheetViewModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class MenuBarView extends HBox {
    private final SpreadsheetViewModel viewModel;
    private final MenuBar menuBar;

    public MenuBarView(SpreadsheetViewModel viewModel) {
        this.viewModel = viewModel;
        //la barre de menu
        menuBar = new MenuBar();
        //file
        Menu fileMenu = new Menu("File");
        //items open et save
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().addAll(openItem, saveItem);
        //edit
        Menu editMenu = new Menu("Edit");
        menuBar.getMenus().addAll(fileMenu, editMenu);
        Menu optionsMenu = new Menu("Options");
        MenuItem clearItem = new MenuItem("clear");
        optionsMenu.getItems().addAll(clearItem);
        menuBar.getMenus().addAll(optionsMenu);
        

        //definir les actions quand on clique sur file ou edit
        clearItem.setOnAction(e -> viewModel.clear());
        openItem.setOnAction(e -> viewModel.handleOpen());
        saveItem.setOnAction(e -> viewModel.handleSave());

        //ajouter dans le HBox
        getChildren().add(menuBar);
    }

}
