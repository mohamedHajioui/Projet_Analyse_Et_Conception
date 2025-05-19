package excel;

import com.tangorabox.componentinspector.fx.FXComponentInspectorHandler;
import excel.model.SpreadsheetModel;
import excel.view.MainView;
import excel.viewmodel.SpreadsheetViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {
    private SpreadsheetModel model = new SpreadsheetModel(10, 4);
    private SpreadsheetViewModel viewModel = new SpreadsheetViewModel(model);
    private MainView root;

    @Override
    public void start(Stage primaryStage) {
        this.root = new MainView(viewModel);
        this.root.setApp(this);
        File lastSessionFile = new File("autoSave.e4e");
        if (lastSessionFile.exists()){
            viewModel.openFile(lastSessionFile, this);
        }
        FXComponentInspectorHandler.handleAll();
        Scene scene = new Scene(root, 633, 315);
        primaryStage.setTitle("Spreadsheet");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        File lastSessionFile = new File("autoSave.e4e");
        viewModel.saveFile(lastSessionFile);
        super.stop();
    }

    public void replaceViewModel(SpreadsheetViewModel newViewModel) {
        this.viewModel = newViewModel;
        root.setViewModel(newViewModel);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}