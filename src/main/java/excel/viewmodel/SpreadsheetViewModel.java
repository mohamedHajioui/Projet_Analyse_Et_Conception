package excel.viewmodel;

import excel.model.SpreadsheetModel;
import excel.tools.ExcelConverter;
import javafx.beans.property.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SpreadsheetViewModel {
    private final int NB_ROW, NB_COL;
    private final List<SpreadsheetCellViewModel> cellVMs = new ArrayList<>(); // Liste des VM associés aux cellules
    private final ObjectProperty<SpreadsheetCellViewModel> selectedCell = new SimpleObjectProperty<>(); // Cellule sélectionnée
    private final StringProperty selectedCellContent = new SimpleStringProperty(""); // Le contenu de la cellule sélectionnée (formule ou valeur)
    private final StringProperty selectedCellFormula = new SimpleStringProperty(""); // Exprression de la cellule sélectionnée
    private final SpreadsheetModel model;
    private final BooleanProperty canUndo = new SimpleBooleanProperty(false);
    private final BooleanProperty canRedo = new SimpleBooleanProperty(false);

    public SpreadsheetViewModel(SpreadsheetModel model) {
        this.NB_ROW = model.getRowCount();
        this.NB_COL = model.getColumnCount();
        this.model = model;

        for (int i = 0; i < NB_ROW; i++) {
            for (int j = 0; j < NB_COL; j++) {
                SpreadsheetCellViewModel cellViewModel = new SpreadsheetCellViewModel(model,model.getCell(i, j),this);
                cellVMs.add(cellViewModel);

            }
        }
        updateUndoRedoState();
    }

    // Récuperer le SpreadsheetCellViewModel associé à une cellule donnée
    public SpreadsheetCellViewModel getCellViewModel(int line, int column) {
        return cellVMs.get(line * NB_COL + column);
    }


    public int getRowCount() {
        return NB_ROW;
    }

    public int getColumnCount() {
        return NB_COL;
    }

    public String getCellValue(int line, int col) {
        return getCellViewModel(line, col).getCellValue(); // Accède à la valeur de la cellule
    }




    public StringProperty selectedCellContentProperty() {
        return selectedCellContent;
    }

    // Gère la cellule sélectionnée et met à jour selectedCellContent
    public void setSelectedCell(int row, int col) {
        SpreadsheetCellViewModel cellVM = getCellViewModel(row, col);
        selectedCell.set(cellVM); // Met à jour la cellule sélectionnée dans le ViewModel

        // Met à jour le contenu de la cellule sélectionnée dans selectedCellContent
        selectedCellContent.set(cellVM.getFormula()); // On affiche la formule brute
        selectedCellFormula.set(cellVM.getFormula()); // On met à jour la formule dans selectedCellFormula

    }

    // Met à jour le contenu de la cellule sélectionnée dans le modèle avec la nouvelle valeur
    public void updateSelectedCell(String newValue) {
        if (selectedCell.get() != null) {
            selectedCell.get().setFormula(newValue); // Met à jour la formule de la cellule
            selectedCellContent.set(newValue); // Met à jour le contenu dans selectedCellContent
            selectedCellFormula.set(newValue); // Met à jour la formule dans selectedCellFormula
        }
    }

    public ObjectProperty<SpreadsheetCellViewModel> selectedCellProperty() {
        return selectedCell;
    }


    public StringProperty selectedCellFormulaProperty() {
        return selectedCellFormula; // Expose la formule brute de la cellule sélectionnée
    }
    public void updateDependenciesForSelectedCell() {
        if (selectedCellProperty().get() != null) {
            selectedCellProperty().get().updateValue();
        }
    }
    public BooleanProperty canUndoProperty() {
        return canUndo;
    }

    public BooleanProperty canRedoProperty() {
        return canRedo;
    }
    public void undo() {
        model.undo();
        updateUndoRedoState();
    }

    public void redo() {
        model.redo();
        updateUndoRedoState();
    }

    public void updateUndoRedoState() {
        boolean canUndoValue = model.canUndo();
        boolean canRedoValue = model.canRedo();
        canUndo.set(canUndoValue);
        canRedo.set(canRedoValue);
    }

    public void handleOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("E4E files (*.e4e)", "*.e4e"));

        //ouvrir la boite de dialogue
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            openFile(file);
        }
    }
    
    public void openFile(File file){
        //lire le fichier
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            //lire la premiere ligne "NB_LIGNES,NB_COLONNES"
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
        //choix du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("E4E files (*.e4e)", "*.e4e"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null){
            saveFile(file);
        }

        
    }
    
    public void saveFile(File file){
        //ecriture
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            //<NB_LIGNES,NB_COLONNES
            writer.write(this.model.getRowCount() + "," + this.model.getColumnCount());
            writer.newLine();

            //parcourir toutes les cellules
            for (int r = 0; r < this.model.getRowCount(); r++){
                for (int c = 0; c < this.model.getColumnCount(); c++){
                    String formula = this.model.getCell(r, c).getFormula();
                    //Je sauvegarde que si ce n'est pas vide
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
    
    public void clear(){
        for (int r = 0; r < this.model.getRowCount(); r++){
            for (int c = 0; c < this.model.getColumnCount(); c++){
                String formula = this.model.getCell(r, c).getFormula();
                if (formula != null && !formula.isEmpty()){
                    this.model.getCell(r, c).setFormula("");
                }
            }
        }
    }

}
