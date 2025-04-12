package excel.model;

public class NumberExpression extends Expression {
    private double value;

    public NumberExpression(double value) {
        this.value = value;
    }

    @Override
    public Object interpret() {
        return value;
    }
}

