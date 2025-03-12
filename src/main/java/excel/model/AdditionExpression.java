package excel.model;

public class AdditionExpression extends BinaryExpression {
    public AdditionExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected double operator(double left, double right) {
        return left + right;
    }
}

