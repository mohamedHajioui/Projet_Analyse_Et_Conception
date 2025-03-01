package excel.model;

public class NotExpression extends Expression {
    private Expression operand;

    public NotExpression(Expression operand) {
        this.operand = operand;
    }

    @Override
    public Object interpret() {
        Object operandValue = operand.interpret();

        if (operandValue instanceof Boolean) {
            return !(Boolean) operandValue;
        } else {
            throw new IllegalArgumentException("Incompatible type for NOT operation.");
        }
    }
}

