package excel.viewmodel;

import excel.model.SpreadsheetModel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import excel.viewmodel.SpreadsheetViewModel;
import excel.view.MySpreadsheetView;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class SaveOpenFile {

    private SpreadsheetModel model;

    public SaveOpenFile(SpreadsheetViewModel viewModel) {
    }

    public void handleOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("E4E files (*.e4e)", "*.e4e"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            openFile(file);
        }
    }


    public void openFile(File file){
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line = reader.readLine();
            if (line == null){
                return;
            }

            String[] sizeParts = line.split(",");
            int nbRows = Integer.parseInt(sizeParts[0].trim());
            int nbCols = Integer.parseInt(sizeParts[1].trim());

            String cellLine;
            while ((cellLine = reader.readLine()) != null) {
                String[] rowColAndValue = cellLine.split(";");
                if (rowColAndValue.length != 2) continue;

                String[] rowColParts = rowColAndValue[0].split(",");
                int row = Integer.parseInt(rowColParts[0].trim());
                int col = Integer.parseInt(rowColParts[1].trim());
                String content = rowColAndValue[1];

                this.model.getCell(col, row).setFormula(content);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleSave(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("E4E files (*.e4e)", "*.e4e"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null){
            saveFile(file);
        }
    }

    public void saveFile(File file){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(this.model.getRowCount() + "," + this.model.getColumnCount());
            writer.newLine();
            for (int r = 0; r < this.model.getRowCount(); r++){
                for (int c = 0; c < this.model.getColumnCount(); c++){
                    String formula = this.model.getCell(r, c).getFormula();
                    if (formula != null && !formula.isEmpty() && !formula.equals(" ")){
                        writer.write(c + "," + r + ";" + formula);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
//    public static void loadFromFile(SpreadsheetModel model, File file) throws IOException {
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String[] dimensions = reader.readLine().split(",");
//            int rows = Integer.parseInt(dimensions[0].trim());
//            int cols = Integer.parseInt(dimensions[1].trim());
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(";", 2);
//                String[] coords = parts[0].split(",");
//
//                int row = Integer.parseInt(coords[0].trim());
//                int col = Integer.parseInt(coords[1].trim());
//                String value = parts.length > 1 ? parts[1] : "";
//
//                model.getCell(row, col).setFormula(value);
//            }
//        }
//    }
//
//    public static void saveToFile(SpreadsheetModel model, File file) throws IOException {
//        try (PrintWriter writer = new PrintWriter(file)) {
//            writer.printf("%d,%d%n", model.getRowCount(), model.getColumnCount());
//
//            for (int row = 0; row < model.getRowCount(); row++) {
//                for (int col = 0; col < model.getColumnCount(); col++) {
//                    String value = model.getCell(row, col).getFormula();
//                    if (value != null && !value.isEmpty()) {
//                        writer.printf("%d,%d;%s%n", row, col, value);
//                    }
//                }
//            }
//        }
//    }
}

//    public static final FileChooser fileChooser = new FileChooser();
//    private final SpreadsheetViewModel viewModel;
//   // private final MySpreadsheetView mySpreadsheetView;
//
//    public SaveOpenFile(SpreadsheetViewModel viewModel) {
//        this.viewModel = viewModel;
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter("Excel 4 EPFC Files", "*.e4e")
//        );
//    }
//
//    public void handleOpen() {
//        //SaveOpenFile.openFile(viewModel);
//        File file = fileChooser.showOpenDialog(new Stage());
//        MySpreadsheetView mySpreadsheetView = new MySpreadsheetView(viewModel);
//        if (file != null) {
//            try {
//                loadFromFile(file);
//                mySpreadsheetView.refreshView();
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    public void handleSave() {
//        //saveFile(viewModel);
//        File file = fileChooser.showSaveDialog(new Stage());
//        if (file != null) {
//            try {
//                if (!file.getName().toLowerCase().endsWith(".e4e")) {
//                    file = new File(file.getAbsolutePath() + ".e4e");
//                }
//                saveToFile(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    static {
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter("Excel 4 EPFC Files", "*.e4e")
//        );
//    }

//    public void saveFile(SpreadsheetViewModel viewModel) {
//        File file = fileChooser.showSaveDialog(new Stage());
//        if (file != null) {
//            try {
//                if (!file.getName().toLowerCase().endsWith(".e4e")) {
//                    file = new File(file.getAbsolutePath() + ".e4e");
//                }
//                saveToFile(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public static void openFile(SpreadsheetViewModel viewModel) {
//        File file = fileChooser.showOpenDialog(new Stage());
//        if (file != null) {
//            try {
//                loadFromFile(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private void saveToFile(File file) throws IOException {
//        try (PrintWriter writer = new PrintWriter(file)) {
//            // il faut ajouter les dimensions
//            writer.printf("%d,%d%n", viewModel.getRowCount(), viewModel.getColumnCount());
//
//            // note les cells remplies
//            for (int row = 0; row < viewModel.getRowCount(); row++) {
//                for (int col = 0; col < viewModel.getColumnCount(); col++) {
//                    String value = viewModel.getCellValue(row, col);
//                    if (value != null && !value.isEmpty()) {
//                        writer.printf("%d,%d;%s%n", row, col, value);
//                    }
//                }
//            }
//        }
//    }
//
//    public static SpreadsheetModel loadFromFile(File file) throws IOException {
//        List<String> lines = Files.readAllLines(file.toPath());
//        if (lines.isEmpty()) throw new IOException("File is empty");
//
//        // goes through dimensions
//        String[] dimensions = lines.get(0).split(",");
//        int rows = Integer.parseInt(dimensions[0]);
//        int cols = Integer.parseInt(dimensions[1]);
//
//        SpreadsheetModel model = new SpreadsheetModel(rows, cols);
//        SpreadsheetViewModel viewModel = new SpreadsheetViewModel(model);
//
//        viewModel.resetModel(rows, cols);
//
//        for (int i = 1; i < lines.size(); i++) {
//            String line = lines.get(i);
//            String[] parts = line.split(";", 2);
//            String[] coords = parts[0].split(",");
//
//            int row = Integer.parseInt(coords[0]);
//            int col = Integer.parseInt(coords[1]);
//            String value = parts.length > 1 ? parts[1] : "";
//
//            if (row < rows && col < cols) {
//                model.getCell(row, col).setFormula(value);
//            }
//        }
//
//        return model;
//    }
