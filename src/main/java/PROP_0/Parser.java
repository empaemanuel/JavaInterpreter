package main.java.PROP_0;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Parser implements IParser {
    private Tokenizer t = null;


    public void open(String fileName) throws IOException, TokenizerException {
        t = new Tokenizer();
        t.open(fileName);
        t.moveNext();
    }

    public INode parse() throws IOException, TokenizerException, ParserException {
        return new BlockNode(t);
    }

    public void close() throws IOException {

    }

    private void addNewLine(StringBuilder builder, int tabs, String str) {
        for (int i = 0; i < tabs; i++) {
            builder.append("\t");
        }
        builder.append(str);
        builder.append("\n");
    }

    public class BlockNode implements INode {
        StatementNode sn = null;
        int flag = 0;

        public BlockNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() != Token.LEFT_CURLY) {
                throwException(t.current().token(), Token.LEFT_CURLY);
            }
            t.moveNext();


            if (t.current().token() != Token.RIGHT_CURLY) {
                flag = 1;
                sn = new StatementNode(t);
            }

            if (t.current().token() != Token.RIGHT_CURLY) {
                throwException(t.current().token(), Token.RIGHT_CURLY);
            }
            t.moveNext();


            if (t.current().token() != Token.EOF) {
                throwException(t.current().token(), Token.EOF);
            }
            t.close();

        }

        public Object evaluate(Object[] args) throws Exception {
            return sn.evaluate(args);
        }

        public void buildString(StringBuilder builder, int tabs) {
            addNewLine(builder, tabs, "BlockNode");
            addNewLine(builder, tabs, Token.LEFT_CURLY.toString());
            if (flag == 1) {
                sn.buildString(builder, tabs);
            }
            addNewLine(builder, tabs, Token.RIGHT_CURLY.toString());
        }
    }

    public class StatementNode implements INode {
        AssignmentNode an = null;
        StatementNode sn = null;

        public StatementNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() == Token.IDENT) {
                an = new AssignmentNode(t);
                sn = new StatementNode(t);
            }
        }

        public Object evaluate(Object[] args) throws Exception {
            Object[] tempArgs = Arrays.copyOf(args, args.length + 1);

            if (an != null) {
                tempArgs[tempArgs.length - 1] = an.evaluate(args);
                args = tempArgs;
            }

            if (sn != null) {
                return sn.evaluate(args);
            }

            return args;
        }

        public void buildString(StringBuilder builder, int tabs) {
            tabs++;
            addNewLine(builder, tabs, "Statament Node");
            if (an != null) {
                an.buildString(builder, tabs);
            }
            if (sn != null) {
                sn.buildString(builder, tabs);
            }
        }
    }

    public class AssignmentNode implements INode {
        ExpressionNode en = null;
        String identVal;
        int intval;

        public AssignmentNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() != Token.IDENT) {
                throwException(t.current().token(), Token.IDENT);
            }
            identVal = ((StringBuilder) t.current.value()).toString();

            t.moveNext();


            if (t.current().token() != Token.ASSIGN_OP) {
                throwException(t.current().token(), Token.ASSIGN_OP);
            }
            //lägg till = till buildstring
            t.moveNext();

            en = new ExpressionNode(t);

            if (t.current().token() != Token.SEMICOLON) {
                throwException(t.current().token(), Token.SEMICOLON);
            }

            t.moveNext();
        }

        public Object evaluate(Object[] args) throws Exception {
            intval = (int) en.evaluate(args);
            return this;

        }

        public String getIdentVal() {
            return identVal;
        }

        public int getIntval() {
            return intval;
        }

        public void buildString(StringBuilder builder, int tabs) {
            tabs++;
            addNewLine(builder, tabs, "Assignment Node");
            tabs++;
            addNewLine(builder, tabs, Token.IDENT.toString() + " " + identVal);
            addNewLine(builder, tabs, Token.ASSIGN_OP.toString() + " =");
            en.buildString(builder, tabs);
            addNewLine(builder, tabs, Token.SEMICOLON.toString());
        }
    }

    public class ExpressionNode implements INode {
        TermNode tn = null;
        ExpressionNode en = null;
        int add = 0;

        public ExpressionNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            tn = new TermNode(t);


            if (t.current().token() == Token.ADD_OP) {
                add = 1;
                t.moveNext();
                en = new ExpressionNode(t);
            } else if (t.current().token() == Token.SUB_OP) {
                add = 2;
                t.moveNext();
                en = new ExpressionNode(t);
            }
        }

        public Object evaluate(Object[] args) throws Exception {

            int sum = (int) tn.evaluate(args);
            if (add == 1) {
                sum += (int) en.evaluate(args);
            }
            if (add == 2) {
                sum -= (int) en.evaluate(args);
            }
            return sum;
        }

        public void buildString(StringBuilder builder, int tabs) {
            addNewLine(builder, tabs, "Expression Node");
            tn.buildString(builder, tabs);
            if (add == 1) {
                addNewLine(builder, tabs, Token.ADD_OP.toString() + " +");
            } else if (add == 2) {
                addNewLine(builder, tabs, Token.ADD_OP.toString() + " -");
            }
            if (add > 0) {
                en.buildString(builder, tabs);
            }
        }
    }

    public class TermNode implements INode {
        FactorNode fn = null;
        TermNode tn = null;
        int mult = 0;

        public TermNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            fn = new FactorNode(t);

            if (t.current().token() == Token.MULT_OP) {
                mult = 1;
                t.moveNext();
                tn = new TermNode(t);
            } else if (t.current().token() == Token.DIV_OP) {
                mult = 2;
                t.moveNext();
                tn = new TermNode(t);
            }
        }

        public Object evaluate(Object[] args) throws Exception {
            int sum = (int) fn.evaluate(args);
            if (mult == 1) {
                sum *= (int) tn.evaluate(args);
            }
            if (mult == 2) {
                sum /= (int) tn.evaluate(args);
            }
            return sum;
        }

        public void buildString(StringBuilder builder, int tabs) {
            tabs++;
            addNewLine(builder, tabs, "Term Node");
            fn.buildString(builder, tabs);
            tabs++;
            if (mult == 1) {
                addNewLine(builder, tabs, Token.MULT_OP + " *");
            } else if (mult == 2) {
                addNewLine(builder, tabs, Token.DIV_OP + " /");
            }
            if (mult > 0) {
                tabs--;
                tn.buildString(builder, tabs);
            }
        }
    }

    public class FactorNode implements INode {
        ExpressionNode en = null;
        int intlit;
        String ident;
        int flag;

        public FactorNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() == Token.INT_LIT) {
                flag = 1;
                intlit = (int) t.current.value();
                t.moveNext();
            } else if (t.current().token() == Token.IDENT) {
                flag = 2;
                ident = t.current.value().toString();
                t.moveNext();
            } else if (t.current().token() == Token.LEFT_PAREN) {
                flag = 3;
                t.moveNext();
                en = new ExpressionNode(t);

                if (t.current().token() == Token.RIGHT_PAREN) {
                    t.moveNext();
                } else {
                    throwException(t.current().token(), Token.RIGHT_PAREN);
                }
            }
        }

        public Object evaluate(Object[] args) throws Exception {
            if (flag == 1) {
                return intlit;
            } else if (flag == 2) {
                for (Object object : args) {
                    AssignmentNode node = (AssignmentNode) object;
                    if (this.ident.equals(node.getIdentVal())){
                        return node.getIntval();
                    }
                }
                throw new Exception();
            } else if(flag == 3){
                return en.evaluate(args);
            }
            return this;
        }

        public int getIntlit() {
            return intlit;
        }

        public String getIdent() {
            return ident;
        }

        public void buildString(StringBuilder builder, int tabs) {
            int t = ++tabs;
            addNewLine(builder, t, "FactorNode");
            if (flag == 1) {
                addNewLine(builder, ++t, Token.INT_LIT + " " + intlit);
            } else if (flag == 2) {
                addNewLine(builder, ++t, Token.IDENT + " " + ident);
            } else if (flag == 3) {
                addNewLine(builder, ++t, Token.LEFT_PAREN + " (");
                en.buildString(builder, tabs);
                addNewLine(builder, ++t, Token.RIGHT_PAREN + " )");
            }


        }

    }

    private void throwException(Token foundToken, Token... expectedTokens) throws ParserException {
        String str = "Expected " + expectedTokens[0];
        for (int i = 1; i < expectedTokens.length; i++) {
            str += "or ";
            str += expectedTokens[i].toString();
        }
        str += " but found " + foundToken;

        throw new ParserException(str);
    }

}
