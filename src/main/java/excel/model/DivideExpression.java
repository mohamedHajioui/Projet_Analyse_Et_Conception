package excel.model;

public class DivideExpression extends Expression {

    private Expression left;
    private Expression right;

    public DivideExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object interpret() {
        Object leftValue = left.interpret();
        Object rightValue = right.interpret();
        Double divisor = (double) rightValue;
        if (divisor == 0){
            return "#VALEUR";
        }
        if (leftValue instanceof Double && rightValue instanceof Double) {
            return (Double) leftValue / (Double) rightValue;
        } else {
            throw new IllegalArgumentException("Incompatible types for division.");
        }
    }
}
