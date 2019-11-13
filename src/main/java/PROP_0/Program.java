package main.java.PROP_0;

import java.io.IOException;

public class Program {



	public static void main(String[] args){
		String inputFileName = "src/program2.txt";


		Tokenizer t = new Tokenizer();
		try {
			t.open(inputFileName);

		} catch (Exception e){
			e.printStackTrace();
			System.out.println("fel");
			return;
		}

		try {
			t.moveNext();
			while(t.current().token() != Token.EOF) {
				t.moveNext();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TokenizerException e) {
			e.printStackTrace();
		}
	}
















	/*
	public static void main(String[] args) {
		String inputFileName = null;
		String outputFileName = null;
		IParser parser = null;
		INode root = null; // Root of the parse tree.
		StringBuilder builder = null;
		FileOutputStream stream = null;
		OutputStreamWriter writer = null;
		
		try {
			try {
				if (args.length < 2)
					throw new Exception("Incorrect number of parameters to program.");
				inputFileName = args[0];
				outputFileName = args[1];

				parser = new Parser();
				parser.open(inputFileName);
				root = parser.parse();
				builder = new StringBuilder();
				builder.append("PARSE TREE:\n");
				root.buildString(builder, 0);
				builder.append("\nEVALUATION:\n");
				builder.append(root.evaluate(null));
				
				stream = new FileOutputStream(outputFileName);
				writer = new OutputStreamWriter(stream);
				writer.write(builder.toString());
			}

			catch (Exception exception) {
				System.out.println("EXCEPTION: " + exception);
			}
			finally {
				if (parser != null)
					parser.close();
				if (writer != null)
					writer.close();
				if (stream != null)
					stream.close();
			}
		}
		catch (Exception exception) {
			System.out.println("EXCEPTION: " + exception);
		}
	}

	 */






}
