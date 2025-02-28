package excel.model;

import excel.tools.ExcelConverter;

public class CellReferenceExpression extends Expression{
    private final String cellRef;
    private final SpreadsheetModel model;

    public CellReferenceExpression(String cellRef, SpreadsheetModel model) {
        this.cellRef = cellRef;
        this.model = model;
    }

    @Override
    public double interpret() {
        int[] coords = ExcelConverter.excelToRowCol(cellRef);
        SpreadsheetCellModel cell = model.getCell(coords[0], coords[1]);

        try {
            return Double.parseDouble(cell.getValueProperty());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
