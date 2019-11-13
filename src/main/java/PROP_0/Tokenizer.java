package main.java.PROP_0;

import java.io.IOException;


public class Tokenizer implements ITokenizer {
    Scanner scanner = new Scanner();
    Lexeme current = null;

    public void open(String fileName) throws IOException, TokenizerException {

    }

    public Lexeme current() {
        return current;
    }

    public void moveNext() throws IOException, TokenizerException {
        getNonBlank();
        try {
            current = buildLex();
        } catch (TokenizerException e) {

        }
    }

    private void getNonBlank() {
        try {
            while (Character.isWhitespace(scanner.current())) {
                scanner.moveNext();
            }
        } catch (IOException e) {

        }

    }

    private Lexeme buildLex() throws TokenizerException {
        char currentChar = scanner.current();

        if (Character.isLetter(currentChar)) {
            return buildIdentifier();
        }
        if (Character.isDigit(currentChar)) {
            return buildIntLiteral();
        }
        if (charIsOperator(currentChar)) {
            return identifyOperator(currentChar);
        }
        if (currentChar == '('){
            return new Lexeme(currentChar,Token.LEFT_PAREN);
        }
        if (currentChar == ')'){
            return new Lexeme(currentChar, Token.RIGHT_PAREN);
        }
        if (currentChar == '{'){
            return new Lexeme(currentChar,Token.LEFT_CURLY);
        }
        if(currentChar == '}'){
            return  new Lexeme(currentChar,Token.RIGHT_CURLY);
        }

        throw new TokenizerException("Illegal character in stream");

    }

    private Lexeme buildIdentifier() throws TokenizerException {
        try {
            char currentChar = scanner.current();
            StringBuilder identValue = new StringBuilder();
            while (Character.isLetter(currentChar)) {
                identValue.append(currentChar);
                scanner.moveNext();
                currentChar = scanner.current();
            }
            if (currentChar == '=' || Character.isWhitespace(currentChar)) {
                return new Lexeme(identValue, Token.IDENT);
            }
        } catch (IOException e) {

        }
        throw new TokenizerException("Illegal character combination in stream");
    }

    private Lexeme buildIntLiteral() throws TokenizerException {
        try {
            char currentChar = scanner.current();
            StringBuilder identValue = new StringBuilder();
            while (Character.isDigit(currentChar)) {
                identValue.append(currentChar);
                scanner.moveNext();
                currentChar = scanner.current();
            }
            if (charIsOperator(currentChar) || Character.isWhitespace(currentChar)) {
                return new Lexeme(Integer.valueOf(identValue.toString()), Token.INT_LIT);
            }
        } catch (IOException e) {

        }
        throw new TokenizerException("Illegal character combination in stream");
    }

    private Lexeme identifyOperator(char currentChar) {
        if (currentChar == '+') {
            return new Lexeme(currentChar, Token.ADD_OP);
        }
        if (currentChar == '-') {
            return new Lexeme(currentChar, Token.SUB_OP);
        }
        if (currentChar == '*') {
            return new Lexeme(currentChar, Token.MULT_OP);
        }

        return new Lexeme(currentChar, Token.DIV_OP);


    }
/*
    private boolean charIsParanthesis(char in) {
        if (in == '(' || in == ')') {
            return true;
        }
        return false;
    }
*/
    private boolean charIsOperator(char in) {
        if (in == '/' || in == '*' || in == '+' || in == '-') {
            return true;
        }
        return false;
    }

    public void close() throws IOException {

    }
}
