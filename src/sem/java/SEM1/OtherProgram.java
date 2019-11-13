package sem.java.SEM1;

public class OtherProgram {
	public static void main(String[] args) {
		String inputFileName = "test_program.txt";
		Parser p = new Parser();

		try {
		    p.open(inputFileName);
		    INode root = p.parse();
		    
		    p.close();
		}
		catch (Exception exception) {
		    System.out.println("EXCEPTION: " + exception);
		}
	}
}
