package main.java.PROP_0;

import java.io.IOException;

public class Parser implements IParser{

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

    public class BlockNode implements INode {
        private StatementNode sn = null;

        public BlockNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() != Token.LEFT_CURLY) {
                throwException(t.current().token(), Token.LEFT_CURLY);
            }
            t.moveNext();

            if (t.current().token() == Token.IDENT) {
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

        @Override
        public Object evaluate(Object[] args) throws Exception {
            Evaluator e = (Evaluator) args[0];
            if(sn!=null){
                e = (Evaluator) sn.evaluate(args);
            }
            return e;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            addNewLineToBuilder(builder, tabs, "BlockNode");
            addNewLineToBuilder(builder, tabs, Token.LEFT_CURLY.toString());
            if (sn != null){
                tabs++;
                sn.buildString(builder, tabs);
                tabs--;
            }
            addNewLineToBuilder(builder, tabs, Token.RIGHT_CURLY.toString());

        }
    }

    public class StatementNode implements INode {
        private AssignmentNode an = null;
        private StatementNode sn = null;

        public StatementNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() == Token.IDENT) {
                an = new AssignmentNode(t);
                sn = new StatementNode(t);
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            if(an != null) {
                an.evaluate(args);
            }
            if(sn != null){
                sn.evaluate(args);
            }
            return args[0];
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            addNewLineToBuilder(builder, tabs, "StatementNode");
            if(an != null){
                tabs++;
                an.buildString(builder, tabs);
                tabs--;
            }
            if(sn != null){
                tabs++;
                sn.buildString(builder, tabs);
            }
        }
    }

    public class AssignmentNode implements INode {
        private String identifier;
        private ExpressionNode en;

        public AssignmentNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            if (t.current().token() != Token.IDENT) {
                throwException(t.current().token(), Token.IDENT);
            }
            identifier = t.current.value().toString();
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

        @Override
        public Object evaluate(Object[] args) throws Exception {
            Evaluator eval = (Evaluator) args [0];

            eval.putStatementValue(identifier, 0.0);

            Double sum = (double) en.evaluate(args);

            eval.putStatementValue(identifier, sum);

            return eval;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            addNewLineToBuilder(builder, tabs, "AssignmentNode");
            tabs++;
            addNewLineToBuilder(builder, tabs, Token.IDENT.toString() + " " + identifier);
            addNewLineToBuilder(builder, tabs, Token.ASSIGN_OP.toString() + " " + "=");
            en.buildString(builder, tabs);
            addNewLineToBuilder(builder, tabs, Token.SEMICOLON.toString() + " ;");
        }
    }

    public class ExpressionNode implements INode {
        private TermNode tn;
        private Token operator;
        private ExpressionNode en = null;

        public ExpressionNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            tn = new TermNode(t);

            operator = t.current().token();
            if (operator == Token.SUB_OP || operator == Token.ADD_OP) {
                t.moveNext();
                en = new ExpressionNode(t);
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            double sum = (double) args[1];
            Token op = (Token) args[2];
            args[1] = 0.0; args[2] = null;

            double end = (double) tn.evaluate(args);

            if(op == Token.ADD_OP){
                sum += end;
            } else if(op == Token.SUB_OP){
                sum -= end;
            } else {
                sum = end;
            }

            if(en != null){
                args[1] = sum;
                args[2] = operator;
                sum = (double) en.evaluate(args);
            }

            return sum;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            addNewLineToBuilder(builder, tabs, "ExpressionNode");
            tabs++;
            tn.buildString(builder, tabs);

            if(en != null){
                String ext = Token.SUB_OP.toString() + " -";;
                if(operator == Token.ADD_OP){
                    ext = Token.ADD_OP.toString() + " +";
                }
                addNewLineToBuilder(builder, tabs, ext);

                en.buildString(builder,tabs);
            }
        }

    }

    public class TermNode implements INode {
        private FactorNode fn;
        private Token operator;
        private TermNode tn = null;

        public TermNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            fn = new FactorNode(t);
            operator = t.current().token();

            if (operator == Token.MULT_OP || operator == Token.DIV_OP) {
                t.moveNext();
                tn = new TermNode(t);
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            double sum = (double) args[1];
            Token op = (Token) args[2];
            args[1] = 0.0; args[2] = null;

            double end = (double) fn.evaluate(args);

            if(op == Token.MULT_OP){
                sum *= end;
            } else if(op == Token.DIV_OP){
                sum /= end;
            } else {
                sum = end;
            }

            if(tn != null){
                args[1] = sum;
                args[2] = operator;
                sum = (double) tn.evaluate(args);
            }

            return sum;

        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            addNewLineToBuilder(builder, tabs, "TermNode");
            tabs++;
            fn.buildString(builder,tabs);
            if(tn!=null){
                String ext = Token.MULT_OP.toString() + " *";
                if(operator == Token.DIV_OP){
                    ext = Token.DIV_OP.toString() + " /";
                }
                addNewLineToBuilder(builder, tabs, ext);
                tn.buildString(builder, tabs);
            }
        }
    }

    public class FactorNode implements INode {
        private String identifier = null;
        private Integer literal = null;
        private ExpressionNode en = null;

        public FactorNode(Tokenizer t) throws TokenizerException, IOException, ParserException {
            Token token = t.current().token();

            if (token == Token.IDENT) {
                identifier = t.current().value().toString();
            } else if (token == Token.INT_LIT) {
                literal = (int) t.current.value();
            } else if (token == Token.LEFT_PAREN) {
                t.moveNext();
                en = new ExpressionNode(t);
                token = t.current().token();
                if (token != Token.RIGHT_PAREN) {
                    throwException(token, Token.RIGHT_PAREN);
                }
            }
            t.moveNext();
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            if(identifier != null){
                Evaluator eval = (Evaluator) args[0];
                return eval.getStatementValue(identifier);
            }else if(literal != null){
                return (double) literal;
            } else {
                return en.evaluate(args);
            }

        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            addNewLineToBuilder(builder, tabs, "FactorNode");
            tabs++;
            if(identifier != null){
                addNewLineToBuilder(builder, tabs, Token.IDENT + " " + identifier);
            }
            if(literal != null){
                addNewLineToBuilder(builder, tabs, Token.INT_LIT + " " + (double) literal);
            }

            if(en != null){
                addNewLineToBuilder(builder, tabs, Token.LEFT_PAREN.toString() + " (");
                en.buildString(builder, tabs);
                addNewLineToBuilder(builder,tabs,Token.RIGHT_PAREN.toString() + " )");
            }
        }
    }

    private void addNewLineToBuilder(StringBuilder builder, int tabs, String str) {
        for (int i = 0; i < tabs; i++) {
            builder.append("\t");
        }
        builder.append(str);
        builder.append("\n");
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
