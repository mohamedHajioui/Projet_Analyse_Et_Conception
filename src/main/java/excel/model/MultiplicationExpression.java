package excel.model;

public class MultiplicationExpression extends BinaryExpression {
    public MultiplicationExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double interpret() {
        return getLeft().interpret() * getRight().interpret();
    }

}
