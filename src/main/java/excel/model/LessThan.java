package excel.model;

public class LessThan extends ComparisonExpression {
    public LessThan(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double interpret() {
        if(getLeft().interpret() < getRight().interpret()){
            return 1.0;
        }
        return 0.0;
    }
}
