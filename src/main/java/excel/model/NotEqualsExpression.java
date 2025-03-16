package excel.model;

public class NotEqualsExpression extends Expression {
    private Expression left;
    private Expression right;

    public NotEqualsExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object interpret() {
        Object leftValue = left.interpret();
        Object rightValue = right.interpret();

        // Handle comparison for both Double and Boolean
        if (leftValue instanceof Double && rightValue instanceof Double) {
            return !(leftValue.equals(rightValue));
        } else if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            return (Boolean) leftValue != (Boolean) rightValue;
        } else {
            throw new IllegalArgumentException("Incompatible types for comparison.");
        }
    }
}

