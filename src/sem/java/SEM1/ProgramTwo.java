package sem.java.SEM1;

public class ProgramTwo {
	public static void main(String[] args) {
		String inputFileName = "test_program.txt";
		Tokenizer t = new Tokenizer();

		try {
		    t.open(inputFileName);
		    t.moveNext();

		    while (t.current().token() != Token.EOF) {
						System.out.println(t.current());
						t.moveNext();
		    }
		    t.close();
		}
		catch (Exception exception) {
		    System.out.println("EXCEPTION: " + exception);
		}
	}
}
