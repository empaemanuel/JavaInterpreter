package sem.java.SEM1;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class Tokenizer {

    private static Map<Character, Token> symbols = null;
    private static HashSet<String> nouns = null;
    private static HashSet<String> verbs = null;
    private static HashSet<String> determiners = null;

    private Scanner scanner = null;
    private Lexeme current = null;
    private Lexeme next = null;


    public Tokenizer() {

	symbols = new HashMap<Character, Token>();
	nouns = new HashSet<String>();
	verbs = new HashSet<String>();
	determiners = new HashSet<String>();

	symbols.put('.', Token.STOP);
	symbols.put(Scanner.EOF, Token.EOF);

	verbs.add("scares");
	verbs.add("hates");

	nouns.add("cat");
	nouns.add("mouse");

	determiners.add("a");
	determiners.add("the");
    }

    /**
     * Opens a file for tokenizing.
     */
    void open(String fileName) throws IOException, TokenizerException {
	scanner = new Scanner();
	scanner.open(fileName);
	scanner.moveNext();
	next = extractLexeme();
    }

    /**
     * Returns the current token in the stream.
     */
    Lexeme current(){
	return current;
    }

    /**
     * Moves current to the next token in the stream.
     */
    void moveNext() throws IOException, TokenizerException {
	if (scanner == null)
	    throw new IOException("No open file.");
	current = next;
	if (next.token() != Token.EOF)
	    next = extractLexeme();
    }

    private void consumeWhiteSpaces() throws IOException {
	while (Character.isWhitespace(scanner.current())){
	    scanner.moveNext();
	}
    }

    private Lexeme extractLexeme() throws IOException, TokenizerException {
	consumeWhiteSpaces();

	Character ch = scanner.current();
	StringBuilder strBuilder = new StringBuilder();

	if (ch == Scanner.EOF)
	    return new Lexeme(ch, Token.EOF);
	else if (Character.isLetter(ch)) {
	    while (Character.isLetter(scanner.current())) {
		strBuilder.append(scanner.current());
		scanner.moveNext();
	    }
	    String lexeme = strBuilder.toString();
	    //System.out.println(lexeme);

	    if (nouns.contains(lexeme)) {
		return new Lexeme(lexeme, Token.NOUN);
	    } else if (verbs.contains(lexeme)) {
		return new Lexeme(lexeme, Token.VERB);
	    } else if (determiners.contains(lexeme)) {
		return new Lexeme(lexeme, Token.DETERMINER);
	    } else throw new TokenizerException("Unknown lexeme: " + strBuilder.toString());
	}
	else if (symbols.containsKey(ch)) {
	    scanner.moveNext();
	    return new Lexeme(ch, symbols.get(ch));
	}
	else
	    throw new TokenizerException("Unknown character: " + String.valueOf(ch));
    }


    /**
     * Closes the file and releases any system resources associated with it.
     */
    public void close() throws IOException {
		if (scanner != null)
			scanner.close();
    }

    public static void main(String[] args) {

	     try {
	        Tokenizer t = new Tokenizer();
	        t.open("test_program.txt");
	     } catch (Exception e) {
	        System.out.println(e);
	     }
    }
}
