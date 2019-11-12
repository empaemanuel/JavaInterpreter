package main.java.PROP_0;

import java.io.IOException;

public class Tokenizer implements ITokenizer{
    //Denna ska vi skriva
    //Börjar med pseudo

    public void open(String fileName) throws IOException, TokenizerException {
        //öppna scannern
    }

    public Lexeme current() {
        return null;
    }

    public void moveNext() throws IOException, TokenizerException {
        //OM next inte är EOL
        //Consume whitespace
        //
        //Gå till nästa char, consume whitespace, EOL, SOL.

    }

    public void close() throws IOException {

    }
}
