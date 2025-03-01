package excel.model;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
          Scanner sc = new Scanner(System.in);
        String a = "= true and false or false";
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression ex1 = builder.build(a);
        System.out.println(ex1.interpret());
    }
}
