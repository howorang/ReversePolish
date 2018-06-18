package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Piotr Borczyk on 17.03.2017.
 */

public class Tokenizer {

    public static List<String> tokenize(String input) throws InvalidInputException {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("((\\d*\\.\\d+)|(\\d+)|([\\^+\\-*/()]))");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            tokens.add(matcher.group(1));
        }
        zipOperators(tokens);
        //addImplicitMultipilication(tokens);
        validate(tokens);
        if (tokens.size() == 0) throw new InvalidInputException();
        return tokens;
    }

    private static void addImplicitMultipilication(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (i != 0 && token.equals("(")) {
                String procedingToken = tokens.get(i - 1);
                if(Parser.isNumeric(procedingToken) || procedingToken.equals(")")) {
                    tokens.add(i, Operator.MULTIPLICATION.symbol);
                }
            }
            if (i != tokens.size() - 1 && token.equals(")")) {
                String nextToken = tokens.get(i + 1);
                if (Parser.isNumeric(nextToken)) {
                    tokens.add(i + 1, Operator.MULTIPLICATION.symbol);
                }
            }
        }

    }

    private static void validate(List<String> tokens) throws InvalidInputException {
        for(String token : tokens) {
            if (!Parser.isNumeric(token) && !Parser.isOperator(token) && !token.equals("(") && !token.equals(")")) {
                throw new InvalidInputException();
            }
            if(token.charAt(0) == '.') {
                throw new InvalidInputException();
            }
        }
    }

    private static void zipOperators(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals(Operator.SUBTRACTION.symbol)) {
                if (i == 0 || Parser.isOperator(tokens.get(i - 1) ) || tokens.get(i - 1).equals("(")) {
                    String nextToken = tokens.get(i + 1);
                    nextToken = "-" + nextToken;
                    tokens.set(i + 1, nextToken);
                    tokens.remove(i);
                }
            }
        }
    }
}
