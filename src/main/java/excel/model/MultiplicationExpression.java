package excel.model;

public class MultiplicationExpression extends BinaryExpression {
    public MultiplicationExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double interpret() {
        return getLeft().interpret() * getRight().interpret();
    }

    public static void main(String[] args) {
        double answer = new MultiplicationExpression(new NumberExpression(4), new NumberExpression(2)).interpret();
        System.out.println(answer);
    }
}
