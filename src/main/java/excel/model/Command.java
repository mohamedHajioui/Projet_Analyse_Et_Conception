package excel.model;

public interface Command {
    void execute();
    void undo();
}
