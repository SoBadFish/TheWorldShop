package org.badfish.theworldshop.language;




/**
 * 转义变量
 * @author BadFish
 */

public class TransferVariable {

    private int var;

    private String value;

    public TransferVariable(Object value){
        this.var = 1;
        this.value = value.toString();
    }

    public TransferVariable(int var,Object value){
        this.var = var;
        this.value = value.toString();
    }

    public int getVar() {
        return var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVar(int var) {
        this.var = var;
    }
}
