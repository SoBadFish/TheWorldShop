package org.badfish.theworldshop.language;


import lombok.Data;

/**
 * 转义变量
 * @author BadFish
 */
@Data
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


}
