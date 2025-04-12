package excel.model;

public class CommandManager implements Command {
    private final SpreadsheetCellModel cell;
    private final String newFormula;
    private final String oldFormula;
    public CommandManager(SpreadsheetCellModel cell, String newFormula) {
        this.newFormula = newFormula;
        this.cell = cell;
        this.oldFormula = cell.getFormula();
    }
    @Override
    public void execute() {
        cell.setFormula(newFormula);
    }
    @Override
    public void undo() {
        System.out.println("Undoing formula change. Old formula: " + oldFormula + ", New formula: " + newFormula);
        cell.setFormula(oldFormula);
        System.out.println("Formula after undo: " + cell.getFormula());
    }
}
