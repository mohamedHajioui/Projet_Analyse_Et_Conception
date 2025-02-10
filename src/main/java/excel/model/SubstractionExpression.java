package excel.model;

public class SubstractionExpression extends BinaryExpression{

    public SubstractionExpression(Expression left, Expression right) {
        super(left, right);

    }
    @Override
    public double interpret() {
        return getLeft().interpret() - getRight().interpret();
    }

    public static void main(String[] args) {
        Expression exp = new SubstractionExpression(new NumberExpression(10), new NumberExpression(5));
        System.out.println(exp.interpret());
    }
}
