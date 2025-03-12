package excel.model;

public class SubstractionExpression extends BinaryExpression{
    public SubstractionExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected double operator(double left, double right) {
        return left - right;
    }
}
