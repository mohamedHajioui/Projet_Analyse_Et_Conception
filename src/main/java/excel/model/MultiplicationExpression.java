package excel.model;

public class MultiplicationExpression extends BinaryExpression {
    public MultiplicationExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected double operator(double left, double right) {
        return left * right;
    }

    @Override
    protected boolean compare(double left, double right) {
        return false;
    }

    @Override
    protected boolean isComparator() {
        return false;
    }
}
