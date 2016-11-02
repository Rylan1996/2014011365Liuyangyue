package com.example.rylan.calculatorpro;
import java.math.BigDecimal;

/**
 * Created by Rylan on 2016/9/18.
 */

public class Temp {
    public Temp() {
        }
    //计算指数
    public static BigDecimal temp(double n){
        BigDecimal result = new BigDecimal(1);
        BigDecimal a;
        for(int i = 2; i <= n; i++){
            a = new BigDecimal(i);
            result = result.multiply(a);
        }
        return result;
    }
}
