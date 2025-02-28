package excel.model;

import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilder {
    private final SpreadsheetModel model;

    public ExpressionBuilder(SpreadsheetModel model) {
        this.model = model;
    }

    public Expression build(String strExpr){
        if (strExpr.charAt(0) == '=') {
            List<String> tokens = tokenize(strExpr.substring(1));
            System.out.println(tokens);
            return buildExpression(tokens);
        }
        return null;
    }

    public List<String> tokenize(String str) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isLetterOrDigit(c) || c == '.') {
                currentToken.append(c);
            } else if ("+-*/><=!".indexOf(c) != -1) {
                if (!currentToken.isEmpty()) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }

                // Gérer les opérateurs composés (>=, <=, !=, ==)
                if (i + 1 < str.length() && str.charAt(i + 1) == '=') {
                    tokens.add(String.valueOf(c) + str.charAt(i + 1));
                    i++;
                } else {
                    tokens.add(String.valueOf(c));
                }
            }
        }

        if (!currentToken.isEmpty()) {
            tokens.add(currentToken.toString());
        }
        return tokens;
    }


    private Expression buildExpression(List<String> tokens){
        int idxOp = findLastOperator(tokens);
        if(idxOp != -1){
            String op = tokens.get(idxOp);
            List<String> leftTokens = new ArrayList<>(tokens.subList(0, idxOp));
            List<String> rightTokens = new ArrayList<>(tokens.subList(idxOp + 1, tokens.size()));
            //recursivité sur left and right tokens
            Expression left = buildExpression(leftTokens);
            Expression right = buildExpression(rightTokens);
            return makeOpExpression(op, left, right);

        }
        String token = tokens.get(0);

        if (token.matches("[A-Z]+[0-9]+")) { // Vérifie si c'est une référence Excel (ex: A1, B3)
            return new CellReferenceExpression(token, model);
        }

        try {
            return new NumberExpression(Double.parseDouble(token)); // Gère les nombres
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

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
