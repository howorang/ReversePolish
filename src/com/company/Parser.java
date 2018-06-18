package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Piotr Borczyk on 16.03.2017.
 */

public class Parser {

    private double result;

    private String input;

    List<String> postfix = new ArrayList<>();

    public Parser(String input) {
        this.input = input;
    }

    public void process() throws InvalidInputException {
        List<String> tokens = Tokenizer.tokenize(input);
        postfix = fromInfixToPostfix(tokens);
        result = evaluatePostfix(postfix);
    }

    public double getResult() {
        return result;
    }

    private List<String> fromInfixToPostfix(List<String> tokens) {
        Stack<String> stack = new Stack<>();
        List<String> out = new Stack<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            String procedingToken = i !=0 ? tokens.get(i - 1) : null;
            String nextToken = i != tokens.size() - 1 ? tokens.get(i + 1) : null;

            if (isOperator(token)) {
                Operator operator = operatorOf(token);
                if (!stack.isEmpty() && isOperator(stack.peek()) && operator.priority <= operatorOf(stack.peek()).priority) {
                    while (!stack.isEmpty() && isOperator(stack.peek()) && operatorOf(stack.peek()).priority >= operator.priority) {
                        out.add(stack.pop());
                    }
                }
                stack.push(token);
            } else if (isNumeric(token)) {
                out.add(token);
            } else if (token.equals("(")) {
                if (i != 0) {
                    if(Parser.isNumeric(procedingToken) || procedingToken.equals(")")) {
                        stack.push(Operator.MULTIPLICATION.symbol);
                    }
                }
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop();
                }
                if (i != tokens.size() - 1) {
                    if (Parser.isNumeric(nextToken)) {
                        stack.push( Operator.MULTIPLICATION.symbol);
                    }
                }
            }
        }

        while (!stack.isEmpty()) {
            out.add(stack.pop());
        }

        return out;
    }

    private double evaluatePostfix(List<String> postfixTokens) {
        Stack<String> stack = new Stack<>();
        try {
            for (String token : postfixTokens) {
                if (isNumeric(token)) {
                    stack.push(token);
                } else if (isOperator(token)) {
                    Double a = Double.parseDouble(stack.pop());
                    Double b = Double.parseDouble(stack.pop());
                    stack.push(String.valueOf(calculate(a, b, token)));
                }
            }
            return Double.parseDouble(stack.firstElement());
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public static boolean isNumeric(String str) {
        if(str == null) return false;
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Operator operatorOf(String operatorString) {
        for (Operator operator : Operator.values()) {
            if (operator.symbol.equals(operatorString)) return operator;
        }
        return null;
    }

    public static boolean isOperator(String token) {
        Operator[] operators = Operator.values();
        for (Operator operator : operators) {
            if (operator.symbol.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private double calculate(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;

            case "-":
                return a - b;

            case "*":
                return a * b;

            case "/":
                return a / b;

            case "^":
                return Math.pow(a, b);

            default:
                return Double.NaN;
        }
    }

    public static double calculateFunction(String identifier, double argument) {
        switch (identifier) {
            case "sin":
                return Math.sin(argument);

            case "cos":
                return Math.cos(argument);
            case "tan":
                return Math.tan(argument);

            case "ln":
                return Math.log(argument);

            case "log":
                return Math.log10(argument);

            case "sqrt":
                return Math.sqrt(argument);

            case "x^2":
                return Math.pow(argument,2);

            default:
                return Double.NaN;
        }
    }

    public String getPostfix() {
        return postfix.stream().collect(Collectors.joining(" "));
    }
}
