package excel.model;

import excel.tools.ExcelConverter;

public class CellReferenceExpression extends Expression {
    private final String cellRef;
    private final SpreadsheetModel model;

    public CellReferenceExpression(String cellRef, SpreadsheetModel model) {
        this.cellRef = cellRef;
        this.model = model;
    }

    @Override
    public Object interpret() {

        int[] coords = ExcelConverter.excelToRowCol(cellRef);
        SpreadsheetCellModel cell = model.getCell(coords[0], coords[1]);
        if (cell == null) {
            return "#VALEUR";
        }

        String value = cell.getValueBinding();

        //Si la cell est vide, on retourne #VALEUR
        if (value == null || value.isEmpty()) {
            return "#VALEUR";
        }

        // Si la valeur est numérique, on la retourne en tant que Double
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // sinon c'est un boolean
            if (value.equalsIgnoreCase("true")) {
                return true;
            } else if (value.equalsIgnoreCase("false")) {
                return false;
            }
            // Si c'est un autre type de valeur (texte), on retourne la chaîne
            return "#VALEUR";
        }
    }
}

