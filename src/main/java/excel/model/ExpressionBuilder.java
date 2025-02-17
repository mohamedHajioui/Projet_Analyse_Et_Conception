package excel.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilder {

    public Expression build(String strExpr){
        if (strExpr.charAt(0) == '=') {
            List<String> tokens = tokenize(strExpr.substring(1));
            System.out.println(tokens);
            return buildExpression(tokens);
        }
        return null;
    }

    public List<String> tokenize(String str){
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        List<String> keywords = List.of("not", "and", "or");

        for (char c : str.toCharArray()){
            if (Character.isDigit(c) || c == '.'){
                currentToken.append(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c=='>' || c=='=') {
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
        int idxOp = findLastOperator(tokens);
        if(idxOp != -1){
            String op = tokens.get(idxOp);
            List<String> leftTokens = new ArrayList<>(tokens.subList(0, idxOp)); //sublist -> 0 inclus, idxOp exclus
            List<String> rightTokens = new ArrayList<>(tokens.subList(idxOp + 1, tokens.size())); //sublist -> idxOp+1 inclus, tokens .size exclus
            //recursivité sur left and right tokens
            Expression left = buildExpression(leftTokens);
            Expression right = buildExpression(rightTokens);
            return makeOpExpression(op, left, right);

        }
        return new NumberExpression(Double.parseDouble(tokens.get(0)));
    }

    private int indiceOp(List<String> tokens){
        for (int i = 0; i < tokens.size(); i++){
            if ("+-*/".contains(tokens.get(i))){
                return i;
            }
        }
        return -1;
    }


    private int  findLastOperator(List<String> tokens) {

        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if(isComparison(token) || isEquals(token)){
                return i;
            }
        }

        // On cherche de droite à gauche pour respecter l'associativité
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isPlusOrMinus(token)) {
                return i;
            }
        }
        for (int i = tokens.size()-1; i >= 0; i--) {
            String token = tokens.get(i);
            if(isMult(token)){
                return i;
            }
        }
        return -1;
    }

    public boolean isPlusOrMinus(char c) {
        return c == '+' || c == '-';
    }
    public boolean isPlusOrMinus(String s) {
        return isPlusOrMinus(s.charAt(0));
    }
    public boolean isMult(char c) {
        return c == '*' || c == '/';
    }
    public boolean isMult (String s){
        return isMult(s.charAt(0));
    }
    public boolean isComparison(char c1){
        return c1 == '>';
    }
    public boolean isComparison(String s){
        return isComparison(s.charAt(0));
    }
    public boolean isNot(String c){
        return c.equals("not");
    }

    public boolean isEquals(char c){
        return c == '=';
    }

    public boolean isEquals(String s){
        return isEquals(s.charAt(0));
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
            case ">" :
                return new GreaterThanExpression(left, right);
            case "=" :
                return new EqualsExpression(left, right);
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }
    
}
