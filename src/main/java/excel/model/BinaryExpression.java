package excel.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
    protected abstract boolean compare(double left, double right);
    protected abstract boolean isComparator();
    protected abstract boolean isDivision();

    @Override
    public Object interpret() {
        Object leftValue = left.interpret();
        Object rightValue = right.interpret();

        if (leftValue.equals("#VALEUR") || rightValue.equals("#VALEUR")) {
            return "#VALEUR";
        }

        try {
            double leftNum = convertToDouble(leftValue);
            double rightNum = convertToDouble(rightValue);

            if (isDivision() && rightNum == 0) {
                return "#VALEUR";
            }

            if (isComparator()) {
                return compare(leftNum, rightNum);
            } else {
                double result = operator(leftNum, rightNum);
                return result;
            }
        } catch (NumberFormatException | ClassCastException e) {
            return "SYNTAX_ERROR";
        }
    }

    protected double convertToDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        }
        throw new NumberFormatException("Invalid type: " + value);
    }

}
