package excel.model;

public abstract class Expression {
    public double value;

    public Expression(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
