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

import java.io.File;

public class App extends Application {
    private SpreadsheetModel model = new SpreadsheetModel(10, 4);
    private SpreadsheetViewModel viewModel = new SpreadsheetViewModel(model);

    @Override
    public void start(Stage primaryStage) {
        File lastSessionFile = new File("autoSave.e4e");
        if (lastSessionFile.exists()){
            viewModel.openFile(lastSessionFile);
        }
        MainView root = new MainView(viewModel);
        FXComponentInspectorHandler.handleAll();
        Scene scene = new Scene(root, 633, 315);
        primaryStage.setTitle("Spreadsheet");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        File lasSessionFile = new File("autoSave.e4e");
        viewModel.saveFile(lasSessionFile);
        super.stop();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}