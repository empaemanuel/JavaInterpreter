package main.java.PROP_0;

import java.util.HashMap;

public class Evaluator {
    private HashMap<String, Double> statementValues;

    public Evaluator(){
        this.statementValues = new HashMap<>();
    }

    public void putStatementValue(String identifier, Double value){
        this.statementValues.put(identifier, value);
    }

    public double getStatementValue(String identifier){
        return statementValues.get(identifier);
    }

    public HashMap<String, Double> getStatementValues(){
        return statementValues;
    }

}
