package com.company;

/**
 * Created by Piotr Borczyk on 17.03.2017.
 */

public enum Operator {
    ADDITION("+", 1),
    MULTIPLICATION("*", 2),
    SUBTRACTION("-", 1),
    DIVISION("/", 2),
    EXPONENTIATION("^", 3);

    public final String symbol;
    public final int priority;

    Operator(String symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }
}
