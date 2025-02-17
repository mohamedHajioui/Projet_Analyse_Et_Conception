package excel.model;

public class EqualsExpression extends BinaryExpression {

    public EqualsExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double interpret() {
        if (getLeft().equals(getRight()))
            return 1.0;
        return 0.0;
    }

}


