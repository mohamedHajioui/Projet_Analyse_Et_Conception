package excel;

import com.tangorabox.componentinspector.fx.FXComponentInspectorHandler;
import excel.model.SpreadsheetModel;
import excel.view.MainView;
import excel.viewmodel.SpreadsheetViewModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        SpreadsheetModel model = new SpreadsheetModel(10, 4);
        SpreadsheetViewModel viewModel = new SpreadsheetViewModel(model);
        MainView root = new MainView(viewModel);

        FXComponentInspectorHandler.handleAll();

        Scene scene = new Scene(root, 633, 315);
        primaryStage.setTitle("Spreadsheet");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}