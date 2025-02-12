package excel.model;

public class Test {
    public static void main(String[] args) {
        String a = "=    3       - 6";
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression ex1 = builder.build(a);
        System.out.println(ex1.interpret());
    }
}
