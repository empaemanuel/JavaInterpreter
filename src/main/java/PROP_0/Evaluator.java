package main.java.PROP_0;

import org.w3c.dom.ls.LSOutput;

import java.util.HashMap;

public class Evaluator {
    private Token operator;
    private double value;
    private HashMap<String, Double> map;

    public Evaluator(){
        map = new HashMap<>();
    }

    public void setPassValue(double value, Token operator) {
        this.value = value;
        this.operator = operator;
    }

    public void putIdentValue(String key, Double val){
        map.put(key, val);
    }

    public double getIdentValue(String key){
        return map.get(key);
    }

    public double updateSum(double sum){
        if(operator == Token.ADD_OP){
            value += sum;
        } else if(operator == Token.SUB_OP){
            value -= sum;
        } else if(operator == Token.DIV_OP){
            value /= sum;
        } else if(operator == Token.MULT_OP){
            value *= sum;
        } else {
            value = sum;
        }
        operator = null;

        return value;
    }

}
