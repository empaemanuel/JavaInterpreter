package sem.java.SEM1;

import java.io.IOException;

class Parser {
    Tokenizer t = null;

    void open(String fileName) throws IOException, TokenizerException {
	     t = new Tokenizer();
	     t.open(fileName);
	     t.moveNext();
    }

    public INode parse() throws IOException, TokenizerException, ParserException {
	    if (t == null)
		    throw new IOException("No open file.");
	    return new TextNode(t);
	  }

    public void close() throws IOException {
	     if (t != null)
	     t.close();
    }

    class TextNode implements INode{
	     SentenceNode s = null;
	     TextNode tx = null;

	     public TextNode(Tokenizer t) {
	        s = new SentenceNode(t);
	        if (t.current().token() != Token.EOF)
		        tx = new TextNode(t);
	         }
    }

    class SentenceNode implements INode{
	     NounphraseNode n = null;
	     VerbphraseNode v = null;

	     public SentenceNode(Tokenizer t) {
	        n = new NounphraseNode(t);
	        v = new VerbphraseNode(t);
	     }
    }

    class NounphraseNode implements INode {
	     DeterminerNode d = null;
	     NounNode n = null;
	     public NounphraseNode(Tokenizer t) {
	        d = new DeterminerNode(t);
	        n = new NounNode(t);
	     }
    }

    class VerbphraseNode implements INode {
	     VerbNode v = null;
       NounphraseNode n = null;

	     public VerbphraseNode(Tokenizer t) {
	        v = new VerbNode(t);
	        n = new NounphraseNode(t);
	     }
    }

    class DeterminerNode implements INode {
	     Lexeme l = null;
	     public DeterminerNode(Tokenizer t) {
	        try {
		          l = t.current();
		          t.moveNext();
		          // get next token and check that it's a determiner
	        } catch (Exception e) {
		             System.out.println(e);
	        }
	     }
    }

    class NounNode implements INode {
	     Lexeme l = null;
	     public NounNode(Tokenizer t) {
	        try {
		          l = t.current();
		          t.moveNext();
		          // get next token and check that it's a Noun
	        } catch (Exception e) {
		          System.out.println(e);
	        }
	     }
    }

    class VerbNode implements INode {
	     Lexeme l = null;
	     public VerbNode(Tokenizer t) {
	        try {
		          l = t.current();
		          t.moveNext();
              // get next token and check that it's a Verb
	        } catch (Exception e) {
		          System.out.println(e);
	        }
	     }
    }
}
