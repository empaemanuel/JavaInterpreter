/* developed by Emil Madrell and Mohammed Hussein Nov2019 */

package main.java.PROP_0;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Program {

    static INode resultNode;

    public static void main(String[] args) {
        try {

            String inputFileName = args[0];
            String outputFileName = args[1];
            try {
                Parser parser = new Parser();
                parser.open(inputFileName);
                StringBuilder sb = new StringBuilder();

                resultNode = parser.parse();
                resultNode.buildString(sb, 0);

                Object[] evalArgs = {0.0, null};
                resultNode.evaluate(evalArgs);
                HashMap<String, Double> evalmap = parser.getStatementValues();
                DecimalFormat numberFormat = new DecimalFormat("#0.0");
                for (HashMap.Entry<String, Double> entry : evalmap.entrySet()) {
                    String furg = (entry.getKey() + " = " + numberFormat.format(entry.getValue()) + "\n");
                    sb.append(furg);
                }
                File file = new File(outputFileName);
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.write(sb.toString());
                } finally {
                    if (writer != null) writer.close();
                }
                parser.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
