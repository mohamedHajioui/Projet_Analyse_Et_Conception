package excel.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilder {

    public Expression build(String strExpr){
        if (strExpr.charAt(0) == '='){
            List<String> tokens = tokenize(strExpr.substring(1));
            System.out.println(tokens);
            
            int idxOp = indiceOp(tokens);
            
            return makeOpExpression(tokens.get(idxOp),
                    buildExpression(tokens.subList(0, idxOp)),
                    buildExpression(tokens.subList(idxOp + 1, tokens.size())));
        }
        return new NumberExpression(Double.parseDouble(strExpr));
    }

    public List<String> tokenize(String str){
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        
        for (char c : str.toCharArray()){
            if (Character.isDigit(c) || c == '.'){
                currentToken.append(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (!currentToken.isEmpty()){
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }
        if (!currentToken.isEmpty()){
            tokens.add(currentToken.toString());
        }
        return tokens;
    }
    
    private Expression buildExpression(List<String> tokens){
        if (tokens.size() == 1){
            return new NumberExpression(Double.parseDouble(tokens.get(0)));
        }
        int idxOp = indiceOp(tokens);
        return makeOpExpression(tokens.get(idxOp),
                buildExpression(tokens.subList(0, idxOp)),
                buildExpression(tokens.subList(idxOp + 1, tokens.size())));
    }

    private int indiceOp(List<String> tokens){
        for (int i = 0; i < tokens.size(); i++){
            if ("+-*/".contains(tokens.get(i))){
                return i;
            }
        }
        return -1;
    }

    private Expression makeOpExpression(String op, Expression left, Expression right){
        switch (op){
            case "+":
                return new AdditionExpression(left, right);
            case "-":
                return new SubstractionExpression(left, right);
            case "*":
                return new MultiplicationExpression(left, right);
            case "/":
                return new DivideExpression(left, right);
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }
    
}
