package excel.model;

import java.util.ArrayList;
import java.util.Arrays;
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

    public List<String> tokenize(String str) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        String[] keywords = {"AND", "OR", "NOT"};

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                currentToken.append(c);
            }

            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '>' || c == '=' || c == '!' || c == '<') {
                if (!currentToken.isEmpty()) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }

                if (i + 1 < str.length() && str.charAt(i + 1) == '=') {
                    tokens.add(String.valueOf(c) + str.charAt(i + 1));
                    i++; // Incrémente i pour sauter l'égalité déjà traitée
                } else {
                    tokens.add(String.valueOf(c)); // Ajoute l'opérateur simple
                }
            }
            else if (Character.isWhitespace(c)) {
                if (!currentToken.isEmpty()) {
                    String token = currentToken.toString();
                    // Vérifie si c'est un logique "AND", "OR", "NOT"
                    if (Arrays.asList(keywords).contains(token.toUpperCase())) {
                        tokens.add(token); // Ajoute directement le mot-clé
                    } else if (token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false")) {
                        // Si c'est "true" ou "false", ajouter un boolean
                        tokens.add(new BooleanExpression(Boolean.parseBoolean(token)).interpret().toString());
                    } else {
                        tokens.add(token); // Sinon, ajouter le token tel quel
                    }
                    currentToken.setLength(0); // Réinitialise le StringBuilder
                }
            }
            else {
                currentToken.append(c);
            }
        }

        // Ajouter le dernier token si il existe
        if (!currentToken.isEmpty()) {
            String token = currentToken.toString();
            if (token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false")) {
                tokens.add(new BooleanExpression(Boolean.parseBoolean(token)).interpret().toString());
            } else {
                tokens.add(token);
            }
        }

        return tokens;
    }



    private Expression buildExpression(List<String> tokens) {
        int idxOp = findLastOperator(tokens);
        if (idxOp != -1) {
            String op = tokens.get(idxOp);
            List<String> leftTokens = new ArrayList<>(tokens.subList(0, idxOp));
            List<String> rightTokens = new ArrayList<>(tokens.subList(idxOp + 1, tokens.size()));

            // Traitement des opérateurs logiques OR, AND, NOT
            if (isOr(op)) {
                Expression left = buildExpression(leftTokens);
                Expression right = buildExpression(rightTokens);
                return new OrExpression(left, right);
            }
            else if (isAnd(op)) {
                Expression left = buildExpression(leftTokens);
                Expression right = buildExpression(rightTokens);
                return new AndExpression(left, right);
            }
            else if (isNot(op)) {
                Expression right = buildExpression(rightTokens);
                return new NotExpression(right);
            }
            else {
                Expression left = buildExpression(leftTokens);
                Expression right = buildExpression(rightTokens);
                return makeOpExpression(op, left, right);
            }
        }

        String token = tokens.get(0);
        if (token.equalsIgnoreCase("true")) {
            return new BooleanExpression(true);  // Création d'une expression booléenne
        } else if (token.equalsIgnoreCase("false")) {
            return new BooleanExpression(false);  // Création d'une expression booléenne
        }

        try {
            return new NumberExpression(Double.parseDouble(token));
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
        // Recherche des  OR
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isOr(token)) {
                return i;
            }
        }

        // Recherche des  AND
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isAnd(token)) {
                return i;
            }
        }

        // Recherche de  NOT(opérateur unaire)
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isNot(token)) {
                return i;
            }
        }

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
        return c1 == '>' || c1 == '<';
    }
    public boolean isComparison(String s){
        return isComparison(s.charAt(0)) || s.equals(">=") || s.equals("<=") || s.equals("!=");
    }
    public boolean isNot(String c){
        return c.equals("not");
    }
    public boolean isAnd(String c){
        return c.equals("and");
    }
    public boolean isOr(String c){
        return c.equals("or");
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
            case "<":
                return new LessThanExpression(left, right);
            case "=" :
                return new EqualsExpression(left, right);
            case ">=" :
                return new GreaterThanOrEqualExpression(left, right);
            case "<=" :
                return new LessThanOrEqualExpression(left, right);
            case "!=":
                return new NotEqualsExpression(left, right);
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }


}
