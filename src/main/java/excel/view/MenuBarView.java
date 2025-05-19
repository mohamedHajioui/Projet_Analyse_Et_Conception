package excel.view;

import excel.App;
import excel.viewmodel.SpreadsheetViewModel;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;

public class MenuBarView extends HBox {
    private final SpreadsheetViewModel viewModel;
    private final MenuBar menuBar;
    private App app;

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
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");
        editMenu.getItems().addAll(undoItem, redoItem, copyItem, pasteItem);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        Menu optionsMenu = new Menu("Options");
        MenuItem clearItem = new MenuItem("clear");
        optionsMenu.getItems().addAll(clearItem);
        menuBar.getMenus().addAll(optionsMenu);
        undoItem.disableProperty().bind(viewModel.canUndoProperty().not());
        redoItem.disableProperty().bind(viewModel.canRedoProperty().not());
        pasteItem.disableProperty().bind(Bindings.createBooleanBinding( () -> !viewModel.copiedContent(), viewModel.selectedCellProperty()));

        //definir les actions quand on clique sur file ou edit
        clearItem.setOnAction(e -> viewModel.clear());
        openItem.setOnAction(e -> viewModel.handleOpen(app));
        saveItem.setOnAction(e -> viewModel.handleSave());
        undoItem.setOnAction(e -> viewModel.undo());
        redoItem.setOnAction(e -> viewModel.redo());
        copyItem.setOnAction(e-> viewModel.copySelectedCell());
        pasteItem.setOnAction(e->viewModel.pasteToSelectedCell());

        //ajouter dans le HBox
        this.getChildren().add(menuBar);

    }
    public void setApp(App app) {
        this.app = app;
    }

}
