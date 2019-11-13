package sem.java.SEM1;

public class Lexeme {
	private Object value;
	private Token token;

	public Lexeme(Object value, Token token) {
		this.value = value;
		this.token = token;
	}

	public Object value() {
		return value;
	}

	public Token token() {
		return token;
	}
	
	@Override
	public String toString() {
		return token + " " + value.toString();
	}
}
