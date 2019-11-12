package main.java.PROP_0;

import java.io.IOException;

public class Parser implements IParser {
    //Den här klassen ska vi skriva
    public void open(String fileName) throws IOException, TokenizerException {

    }

    public INode parse() throws IOException, TokenizerException, ParserException {
        return null;
    }

    public void close() throws IOException {

    }





    public class BlockNode implements INode {
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        public void buildString(StringBuilder builder, int tabs) {

        }
    }


    public class AssignmentNode implements INode {
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        public void buildString(StringBuilder builder, int tabs) {

        }
    }


    public class ExpressionNode implements INode{
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        public void buildString(StringBuilder builder, int tabs) {

        }
    }

    public class FactorNode implements INode {
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        public void buildString(StringBuilder builder, int tabs) {

        }
    }


    public class StatementNode implements INode{
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        public void buildString(StringBuilder builder, int tabs) {

        }
    }


    public class TermNode implements INode {
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        public void buildString(StringBuilder builder, int tabs) {

        }
    }



}
