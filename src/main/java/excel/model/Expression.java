package excel.model;

public abstract class Expression {
    public abstract double interpret();


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Expression) {
            Expression exp = (Expression) obj;
            return exp.interpret() == this.interpret();
        }
        return false;
    }
}
