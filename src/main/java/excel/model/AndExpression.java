package excel.model;

public class AndExpression extends Expression {
    private Expression left;
    private Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object interpret() {
        Object leftValue = left.interpret();
        Object rightValue = right.interpret();

        if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            return (Boolean) leftValue && (Boolean) rightValue;
        } else {
            throw new IllegalArgumentException("Incompatible types for AND operation.");
        }
    }
}
