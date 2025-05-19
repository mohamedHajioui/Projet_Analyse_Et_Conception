package excel.view;

import excel.App;
import excel.viewmodel.SpreadsheetViewModel;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {
    private MenuBarView menuBar;
    private App app;

    public MainView(SpreadsheetViewModel viewModel) {
        view(viewModel);
    }

    private void view(SpreadsheetViewModel viewModel) {
        this.menuBar = new MenuBarView(viewModel);
        TextBarView textfield = new TextBarView(viewModel);
        MySpreadsheetView spreadsheetView = new MySpreadsheetView(viewModel);
        CountSumAndExp countSumAndExp = new CountSumAndExp(viewModel);

        // Appliquer l’objet App s’il existe déjà
        if (this.app != null) {
            this.menuBar.setApp(this.app);
        }

        VBox.setMargin(textfield, new Insets(0, 8, 8, 8));
        VBox.setMargin(spreadsheetView, new Insets(5, 5, 5, 8));
        VBox.setMargin(menuBar, new Insets(0, 5, 5, 8));
        VBox topBox = new VBox(menuBar, textfield);
        this.setTop(topBox);
        this.setCenter(spreadsheetView);
        this.setBottom(countSumAndExp);
    }

    public void setViewModel(SpreadsheetViewModel newViewModel) {
        this.getChildren().clear();
        view(newViewModel);
    }

    public void setApp(App app) {
        this.app = app; // ✅ Enregistre la référence
        if (this.menuBar != null) {
            this.menuBar.setApp(app); // ✅ Applique à la barre de menu si elle existe
        }
    }
}
