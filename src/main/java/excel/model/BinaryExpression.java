package excel.model;

import java.util.Objects;

public abstract class BinaryExpression extends Expression {
    private Expression left;
    private Expression right;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    protected abstract double operator(double left, double right);

    @Override
    public Object interpret() {
        Object leftValue = left.interpret();
        Object rightValue = right.interpret();

        try {
            double leftNum = convertToDouble(leftValue);
            double rightNum = convertToDouble(rightValue);

            double result = operator(leftNum, rightNum);
            return formatNumber(result);
        } catch (NumberFormatException e) {
            return "SYNTAX_ERROR";
        }
    }

    protected double convertToDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid number: " + value);
            }
        }
        throw new NumberFormatException("Invalid type: " + value);
    }
}
