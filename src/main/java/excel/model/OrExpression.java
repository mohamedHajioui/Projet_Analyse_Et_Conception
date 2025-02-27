package excel.model;

public class OrExpression extends BinaryExpression {
    public OrExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double interpret() {
        if (getLeft().interpret() != 0.0 || getRight().interpret() != 0.0) {
            return 1.0;
        }
        return 0.0;
    }
}
