package main.java.PROP_0;

import java.io.IOException;
import java.util.Arrays;

public class Parser3 implements IParser {
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

    private Object[] addObjectToArgs(Object object, Object[] args){
        Object[] tempArgs = Arrays.copyOf(args, args.length + 1);
        tempArgs[tempArgs.length-1] = object;
        return tempArgs;
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
            if (an != null) {
                an.evaluate(args);
                args = addObjectToArgs(an, args);
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
        double intval;

        public AssignmentNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() != Token.IDENT) {
                throwException(t.current().token(), Token.IDENT);
            }
            identVal = ((StringBuilder) t.current.value()).toString();

            t.moveNext();


            if (t.current().token() != Token.ASSIGN_OP) {
                throwException(t.current().token(), Token.ASSIGN_OP);
            }


            t.moveNext();

            en = new ExpressionNode(t);

            if (t.current().token() != Token.SEMICOLON) {
                throwException(t.current().token(), Token.SEMICOLON);
            }

            t.moveNext();
        }

        public Object evaluate(Object[] args) throws Exception {
            intval = (double) en.evaluate(args);
            return this;

        }

        public String getIdentVal() {
            return identVal;
        }

        public double getIntval() {
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
        TermNode tn;
        ExpressionNode en = null;
        Token operator = null;

        public ExpressionNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            tn = new TermNode(t);

            if(t.current().token() == Token.SUB_OP || t.current().token() == Token.ADD_OP){
                operator = t.current().token();
                t.moveNext();
                en = new ExpressionNode(t);
            }
        }

        public Object evaluate(Object[] args) throws Exception {
            Evaluator eval = (Evaluator) args[0];

            double sum = (double) tn.evaluate(args);

            sum = eval.updateSum(sum);

            if(en != null){
                eval.setPassValue(sum, operator);
                en.evaluate(args);
            }

            return sum;
        }

        public void buildString(StringBuilder builder, int tabs) {
            addNewLine(builder, tabs, "Expression Node");
            tn.buildString(builder, tabs);

            if(operator != null){
                addNewLine(builder, tabs, operator.toString());
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
            double sum = (double)fn.evaluate(args);
            if(mult == 2){
                if(args.length>0 && args[args.length-1] instanceof Integer) {
                    if ((Integer) args[args.length - 1] == 1) {
                        sum *= (double) tn.evaluate(args);
                    }
                } else {
                    Object[] tempArgs = Arrays.copyOf(args, args.length+1);
                    tempArgs[tempArgs.length - 1] = 1;
                    args = tempArgs;
                    sum /= (double) tn.evaluate(args);
                }
            } else if(mult == 1){
                if(args.length>0 && args[args.length-1] instanceof Integer) {
                    if ((Integer) args[args.length - 1] == 1) {
                        Object[] tempArgs = new Object[args.length-1];
                        for(int i = 0; i < args.length-1; i++){
                            tempArgs[i] = args[i];
                        }

                    }
                } else {
                    sum *= (double) tn.evaluate(args);
                }
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
        private Integer literal = null;
        private String identifier = null;
        private ExpressionNode en = null;

        public FactorNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            Token token = t.current().token();

            if (token == Token.INT_LIT) {
                literal = (int) t.current.value();
            } else if (token == Token.IDENT) {
                identifier = t.current().value().toString();
            } else if (token == Token.LEFT_PAREN) {
                t.moveNext();
                en = new ExpressionNode(t);
                if (token != Token.RIGHT_PAREN) {
                    throwException(token, Token.RIGHT_PAREN);
                }
            }
            t.moveNext();
        }

        public Object evaluate(Object[] args) throws Exception {
            if (literal != null) {
                return literal;
            } else if (identifier != null) {
                return identifier;
            } else if(en != null){
                return en.evaluate(args);
            }
            return this;
        }

        public void buildString(StringBuilder builder, int tabs) {
            int t = ++tabs;
            addNewLine(builder, t, "FactorNode");
            if (literal != null) {
                addNewLine(builder, ++t, Token.INT_LIT + " " + literal);
            } else if (identifier != null) {
                addNewLine(builder, ++t, Token.IDENT + " " + identifier);
            } else if (en != null) {
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
