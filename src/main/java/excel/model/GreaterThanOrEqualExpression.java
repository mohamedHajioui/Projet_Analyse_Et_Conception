package excel.model;

public class GreaterThanOrEqualExpression extends Expression {
    private Expression left;
    private Expression right;

    public GreaterThanOrEqualExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object interpret() {
        Object leftValue = left.interpret();
        Object rightValue = right.interpret();

        if (leftValue instanceof Double && rightValue instanceof Double) {
            return (Double) leftValue >= (Double) rightValue;
        } else {
            throw new IllegalArgumentException("Incompatible types for comparison.");
        }
    }
}

