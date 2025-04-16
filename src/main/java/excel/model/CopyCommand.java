package excel.model;

public class CopyCommand implements Command {
    private final SpreadsheetModel model;
    private final int row;
    private final int col;
    private String copiedValue;

    public CopyCommand(SpreadsheetModel model, int row, int col) {
        this.model = model;
        this.row = row;
        this.col = col;
    }

    @Override
    public void execute() {
        copiedValue = model.getCell(row, col).getFormula();

    }

    @Override
    public void undo() {
    }
    public String getCopiedValue() {
        return copiedValue;
    }
}
