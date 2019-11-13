package sem.java.SEM1;

public class ProgramOne {
	public static void main(String[] args) {
		String inputFileName = "test_program.txt";
		Scanner s = new Scanner();

		try {
		    s.open(inputFileName);
		    s.moveNext();

		    while (s.current() != Scanner.EOF) {
			        System.out.println(s.current());
			        s.moveNext();
		    }
		    s.close();
		}
		catch (Exception exception) {
		    System.out.println("EXCEPTION: " + exception);
		}
	}
}
