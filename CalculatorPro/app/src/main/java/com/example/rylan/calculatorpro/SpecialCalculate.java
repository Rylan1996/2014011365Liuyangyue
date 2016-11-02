package com.example.rylan.calculatorpro;

import java.math.BigDecimal;

/**
 * Created by Rylan on 2016/9/18.
 */
public class SpecialCalculate {

    public SpecialCalculate() {

    }

    public String calculateOthers(String str) {

        double result = 0;

        if (str.matches("^sin.[0-9]{0,20}")) {

            String subscsin = str.substring(3, str.length());
            result = Math.sin(Math.toRadians(Double.parseDouble(subscsin)));

            return String.valueOf(result);

        }

        if (str.matches("^cos.[0-9]{0,20}")) {

            String subsccos = str.substring(3, str.length());
            result = Math.cos(Math.toRadians(Double.parseDouble(subsccos)));

            return String.valueOf(result);

        }
        if (str.matches("^tan.[0-9]{0,20}")) {

            String subsctan = str.substring(3, str.length());
            result = Math.tan(Math.toRadians(Double.parseDouble(subsctan)));

            return String.valueOf(result);

        }

        if (str.matches("^ln.[0-9]{0,20}")) {

            String subsc = str.substring(2, str.length());
            result = Math.log(Double.parseDouble(subsc));
            return String.valueOf(result);
        }

        if(str.matches("[0-9]{0,20}\\^[0-9]{0,20}")) {
            BigDecimal res = new BigDecimal(1);
            String[] sub = str.split("\\^");
            String subsc1 = sub[0];
            String subsc2 = sub[1];
            double sub1 = Double.parseDouble(sub[0]);
            double sub2 = Double.parseDouble(sub[1]);
            for (int i=1; i <= sub2; i++)
                res = res.multiply(new BigDecimal(sub1));
            return String.valueOf(res);

        }


        if (str.matches("^[0-9]{0,20}!$")) {
            String subsc = str.substring(0, str.length() - 1);
            double tn = Double.parseDouble(subsc);
            Temp fac = new Temp();
            return String.valueOf(fac.temp(tn).stripTrailingZeros().toPlainString());

        }
        if (str.matches("^sqr.[0-9]{0,20}")) {

            String subsc = str.substring(3, str.length());
            result = Math.sqrt(Double.parseDouble(subsc));

            return String.valueOf(result);
        }
        if (str.matches("^log\\(.[0-9]{0,20},.[0-9]{0,20}\\)")) {
            /* static public double log(double value, double base) {
            　　 return Math.log(value) / Math.log(base);*/

            String subsc = str.substring(4, str.length()-1);
            String[] sub = subsc.split(",");

            double sub1 = Double.parseDouble(sub[0]);
            double sub2 = Double.parseDouble(sub[1]);
            result = Math.log(sub1)/ Math.log(sub2);

            return String.valueOf(result);
        }


        return "Error";

    }



}


