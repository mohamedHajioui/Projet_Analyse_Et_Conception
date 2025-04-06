package excel.model;

import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionBuilder {
    private final SpreadsheetModel model;

    public ExpressionBuilder(SpreadsheetModel model) {
        this.model = model;
    }

    public Expression build(String strExpr) {
        if (strExpr.charAt(0) == '=') {
            List<String> tokens = tokenize(strExpr.substring(1));
            SpreadsheetCellModel currentCell = model.getCurrentCell();
            if (currentCell != null) {
                return buildExpression(tokens);
            } else {
                throw new IndexOutOfBoundsException("Invalid expression");
            }
        }
        return null;
    }

    public List<String> tokenize(String str) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        String[] keywords = {"AND", "OR", "NOT"};

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
            } else if (Character.isWhitespace(c)) {
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
            } else {
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

    private Expression buildSumExpression(String sumToken) {
        // Enlever "SUM(" et la dernière ")"
        String inner = sumToken.substring(4, sumToken.length() - 1).trim();
        if (!inner.contains(":")) {
            throw new IllegalArgumentException("SYNTAX_ERROR");
        }

        String[] parts = inner.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("SYNTAX_ERROR");
        }
        String ref1 = parts[0].trim(); // ex "A1"
        String ref2 = parts[1].trim(); // ex "B2"
        // Créer l'expression
        return new SumFunctionExpression(ref1, ref2, model);
    }



    private Expression buildExpression(List<String> tokens) {
        if (tokens.size() == 1) {
            String token = tokens.get(0).toUpperCase();
            if (token.startsWith("SUM(") && token.endsWith(")")) {
                return buildSumExpression(tokens.get(0));
            }
        }

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
            } else if (isAnd(op)) {
                Expression left = buildExpression(leftTokens);
                Expression right = buildExpression(rightTokens);
                return new AndExpression(left, right);
            } else if (isNot(op)) {
                Expression right = buildExpression(rightTokens);
                return new NotExpression(right);
            } else {
                Expression left = buildExpression(leftTokens);
                Expression right = buildExpression(rightTokens);
                return makeOpExpression(op, left, right);
            }
        }

        String token = tokens.get(0);

        // Gestion des références de cellules (par exemple A1)
        if (token.matches("[A-Z]+[0-9]+")) {
            return new CellReferenceExpression(token, model);
        }

        // Gestion des booléens true/false
        if (token.equalsIgnoreCase("true")) {
            return new BooleanExpression(true);  // Création d'une expression booléenne
        } else if (token.equalsIgnoreCase("false")) {
            return new BooleanExpression(false);  // Création d'une expression booléenne
        }

        // Gestion des nombres
        try {
            return new NumberExpression(Double.parseDouble(token)); // Gère les nombres
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
    }

    private int findLastOperator(List<String> tokens) {
        // Recherche des OR
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isOr(token)) {
                return i;
            }
        }

        // Recherche des AND
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isAnd(token)) {
                return i;
            }
        }

        // Recherche de NOT(opérateur unaire)
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isNot(token)) {
                return i;
            }
        }

        // Recherche des opérateurs de comparaison et d'égalité
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isComparison(token) || isEquals(token)) {
                return i;
            }
        }

        // Recherche des opérateurs arithmétiques (+, -, *, /)
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isPlusOrMinus(token)) {
                return i;
            }
        }

        // Recherche des opérateurs mult et div (*, /)
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (isMult(token)) {
                return i;
            }
        }

        return -1;
    }

    // Méthodes pour identifier les opérateurs
    public boolean isPlusOrMinus(String s) {
        return s.equals("+") || s.equals("-");
    }

    public boolean isMult(String s) {
        return s.equals("*") || s.equals("/");
    }

    public boolean isComparison(String s) {
        return s.equals(">") || s.equals("<") || s.equals(">=") || s.equals("<=") || s.equals("==") || s.equals("!=");
    }

    public boolean isEquals(String s) {
        return s.equals("=");
    }

    public boolean isNot(String s) {
        return s.equalsIgnoreCase("not");
    }

    public boolean isAnd(String s) {
        return s.equalsIgnoreCase("and");
    }

    public boolean isOr(String s) {
        return s.equalsIgnoreCase("or");
    }

    private Expression makeOpExpression(String op, Expression left, Expression right) {
        switch (op) {
            case "+":
                return new AdditionExpression(left, right);
            case "-":
                return new SubstractionExpression(left, right);
            case "*":
                return new MultiplicationExpression(left, right);
            case "/":
                return new DivideExpression(left, right);
            case ">":
                return new GreaterThanExpression(left, right);
            case "<":
                return new LessThanExpression(left, right);
            case "=":
                return new EqualsExpression(left, right);
            case ">=":
                return new GreaterThanOrEqualExpression(left, right);
            case "<=":
                return new LessThanOrEqualExpression(left, right);
            case "!=":
                return new NotEqualsExpression(left, right);
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }
}
