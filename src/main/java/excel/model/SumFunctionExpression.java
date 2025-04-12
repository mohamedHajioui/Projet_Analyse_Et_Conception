package excel.model;

import excel.tools.ExcelConverter;



public class SumFunctionExpression extends Expression {
    private final String ref1;
    private final String ref2;
    private final SpreadsheetModel model;

    public SumFunctionExpression(String ref1, String ref2, SpreadsheetModel model) {
        this.ref1 = ref1;
        this.ref2 = ref2;
        this.model = model;
    }

    @Override
    public Object interpret() {
        // 1) Convertir les références en (row, col)
        int[] coords1;
        int[] coords2;
        try {
            coords1 = ExcelConverter.excelToRowCol(ref1);
            coords2 = ExcelConverter.excelToRowCol(ref2);
        } catch (Exception e) {
            return "SYNTAX_ERROR";
        }

        // coords1 = [row1, col1], coords2 = [row2, col2]
        int row1 = coords1[0];
        int col1 = coords1[1];
        int row2 = coords2[0];
        int col2 = coords2[1];

        if (row1 > row2 || col1 > col2) {
            return "SYNTAX_ERROR";
        }

        // 3) Vérifier la référence circulaire
        SpreadsheetCellModel currentCell = model.getCurrentCell();
        if (currentCell != null) {
            // Récupérer les coords (row, col) de la cellule courante
            int currentRow = currentCellPropertyRow(currentCell);
            int currentCol = currentCellPropertyCol(currentCell);
            if (currentRow >= row1 && currentRow <= row2 && currentCol >= col1 && currentCol <= col2) {
                return "CIRCULAR_REF";
            }
        }

        // 4) Parcourir toutes les cellules de la plage et faire la somme
        double sum = 0.0;
        for (int r = row1; r <= row2; r++) {
            for (int c = col1; c <= col2; c++) {
                SpreadsheetCellModel cell = model.getCell(r, c);
                if (cell == null) {
                    return "#VALEUR";
                }
                String cellValue = cell.getValue();
                if (cellValue.equals("#VALEUR") || cellValue.startsWith("#")) {
                    return "#VALEUR";
                }
                //convertir en double
                try {
                    double numericValue = Double.parseDouble(cellValue);
                    sum += numericValue;
                } catch (NumberFormatException e) {
                    return "#VALEUR";
                }
            }
        }
        return sum;
    }

    private int currentCellPropertyRow(SpreadsheetCellModel cell) {
        return cell.getRow();
    }

    private int currentCellPropertyCol(SpreadsheetCellModel cell) {
        return cell.getColumn();
    }
}

