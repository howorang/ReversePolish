package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        System.out.println("By wyjść podaj 'q'");
        while (!exit) {
            System.out.println("Podaj wyrażenie: ");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                break;
            }
            try {
                Parser parser = new Parser(input);
                parser.process();
                System.out.println(parser.getPostfix());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Błąd!");
            }
        }
    }

}
