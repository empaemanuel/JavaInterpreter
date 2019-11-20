package main.java.PROP_0;

import java.io.IOException;

public class Tokenizer implements ITokenizer {
    Scanner scanner = new Scanner();
    Lexeme current = null;

    public void open(String fileName) throws IOException, TokenizerException {
        scanner.open(fileName);
        scanner.moveNext();
    }

    public Lexeme current() {
        return current;
    }

    public void moveNext() throws IOException, TokenizerException {
        getNonBlank();
        current = buildLex();
    }

    private void getNonBlank() throws IOException {
        while (Character.isWhitespace(scanner.current())) {
            scanner.moveNext();
        }
    }

    private Lexeme buildLex() throws TokenizerException, IOException {
        char currentChar = scanner.current();
        Lexeme lex = null;
        if (Character.isLetter(currentChar)) {
            lex = buildIdentifier();
        } else if (Character.isDigit(currentChar)) {
            lex = buildIntLiteral();
        } else {
            if (charIsOperator(currentChar)) {
                lex = identifyOperator(currentChar);
            } else if (currentChar == '(') {
                lex = new Lexeme(currentChar, Token.LEFT_PAREN);
            } else if (currentChar == ')') {
                lex = new Lexeme(currentChar, Token.RIGHT_PAREN);
            } else if (currentChar == '{') {
                lex = new Lexeme(currentChar, Token.LEFT_CURLY);
            } else if (currentChar == '}') {
                lex = new Lexeme(currentChar, Token.RIGHT_CURLY);
            } else if (currentChar == ';') {
                lex = new Lexeme(currentChar, Token.SEMICOLON);
            } else if (currentChar == '=') {
                lex = new Lexeme(currentChar, Token.ASSIGN_OP);
            } else if (currentChar == scanner.EOF) {
                lex = new Lexeme(currentChar, Token.EOF);
            } else if (lex == null) {
                throw new TokenizerException("Illegal character in stream");
            }
            scanner.moveNext();
        }

        return lex;
    }

    private Lexeme buildIdentifier() throws IOException {
        char currentChar = scanner.current();
        StringBuilder identValue = new StringBuilder();
        while (Character.isLetter(currentChar)) {
            identValue.append(currentChar);
            scanner.moveNext();
            currentChar = scanner.current();
        }
        return new Lexeme(identValue, Token.IDENT);
    }

    private Lexeme buildIntLiteral() throws IOException {
        char currentChar = scanner.current();
        StringBuilder identValue = new StringBuilder();
        while (Character.isDigit(currentChar)) {
            identValue.append(currentChar);
            scanner.moveNext();
            currentChar = scanner.current();
        }
        return new Lexeme(Integer.valueOf(identValue.toString()), Token.INT_LIT);
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
    private boolean charIsOperator(char in) {
        if (in == '/' || in == '*' || in == '+' || in == '-') {
            return true;
        }
        return false;
    }

    public void close() throws IOException {

    }
}
