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

public class SaveOpenFile {
    public static final FileChooser fileChooser = new FileChooser();
  //  private static final Pattern CELL_LINE_PATTERN = Pattern.compile("^(\\d+),(\\d+);(.*)$");

    public static void handleOpen() {
        //SaveOpenFile.openFile(viewModel);
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                SpreadsheetViewModel.loadFromFile(file);
                MySpreadsheetView.refreshView();
                MySpreadsheetView.getGrid().setRows(spreadsheetView.createGridAndBindings().getRows());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void handleSave() {
        saveFile(viewModel);
    }


    static {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel 4 EPFC Files", "*.e4e")
        );
    }

    public static void saveFile(SpreadsheetViewModel viewModel) {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                if (!file.getName().toLowerCase().endsWith(".e4e")) {
                    file = new File(file.getAbsolutePath() + ".e4e");
                }
                saveToFile(viewModel, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void openFile(SpreadsheetViewModel viewModel) {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                loadFromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveToFile(SpreadsheetViewModel viewModel, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            // il faut ajouter les dimensions
            writer.printf("%d,%d%n", viewModel.getRowCount(), viewModel.getColumnCount());

            // note les cells remplies
            for (int row = 0; row < viewModel.getRowCount(); row++) {
                for (int col = 0; col < viewModel.getColumnCount(); col++) {
                    String value = viewModel.getCellValue(row, col);
                    if (value != null && !value.isEmpty()) {
                        writer.printf("%d,%d;%s%n", row, col, value);
                    }
                }
            }
        }
    }

    public static SpreadsheetModel loadFromFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        if (lines.isEmpty()) throw new IOException("File is empty");

        // goes through dimensions
        String[] dimensions = lines.get(0).split(",");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);

        SpreadsheetModel model = new SpreadsheetModel(rows, cols);





         //SpreadsheetViewModel.resetModel(rows, cols);

        // goes through cell data
//        for (int i = 1; i < lines.size(); i++) {
//            String line = lines.get(i);
//            var matcher = CELL_LINE_PATTERN.matcher(line);
//            if (matcher.matches()) {
//                int row = Integer.parseInt(matcher.group(1));
//                int col = Integer.parseInt(matcher.group(2));
//                String value = matcher.group(3);
//
//                if (row < rows && col < cols) {
//                    viewModel.setCellValue(row, col, value);
//                }
//            }
//        }
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(";", 2);
            String[] coords = parts[0].split(",");

            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);
            String value = parts.length > 1 ? parts[1] : "";

            if (row < rows && col < cols) {
                model.getCell(row, col).setFormula(value);
            }
        }

        return model;
    }

//    private void handleOpen() {
//        //SaveOpenFile.openFile(viewModel);
//        File file = fileChooser.showOpenDialog(new Stage());
//        if (file != null) {
//            try {
//                viewModel.loadFromFile(file);
//                spreadsheetView.refreshView();
//                spreadsheetView.getGrid().setRows(spreadsheetView.createGridAndBindings().getRows());
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private void handleSave() {
//        SaveOpenFile.saveFile(viewModel);
//    }
}