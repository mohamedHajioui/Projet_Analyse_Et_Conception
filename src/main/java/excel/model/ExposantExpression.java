package excel.model;

public class ExposantExpression extends BinaryExpression {

    public ExposantExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected double operator(double left, double right) {
        return Math.pow(left, right);
    }

    @Override
    protected boolean compare(double left, double right) {
        return false;
    }

    @Override
    protected boolean isComparator() {
        return false;
    }

    @Override
    protected boolean isDivision() {
        return false;
    }
}
