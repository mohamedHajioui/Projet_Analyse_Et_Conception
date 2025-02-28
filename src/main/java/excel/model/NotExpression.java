package excel.model;


public class NotExpression extends Expression {
    private Expression operand;

    public NotExpression(Expression operand) {
        this.operand = operand;
    }

    @Override
    public double interpret() {
        if (operand.interpret() == 0.0) {
            return 1.0;
        }
        return 0.0;
    }
}

