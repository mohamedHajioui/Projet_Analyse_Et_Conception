package excel.model;

public class AdditionExpression extends Expression {
    private Expression left;
    private Expression right;

    public AdditionExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public double value() {
        return left.value() + right.value();
    }

}
