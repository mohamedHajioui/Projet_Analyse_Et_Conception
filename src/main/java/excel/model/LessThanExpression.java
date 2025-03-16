package excel.model;

public class LessThanExpression extends BinaryExpression{
    public LessThanExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected double operator(double left, double right) {
        return 0;
    }

    @Override
    protected boolean compare(double left, double right) {
        return left < right;
    }

    @Override
    protected boolean isComparator() {
        return true;
    }

    @Override
    protected boolean isDivision() {
        return false;
    }


}

