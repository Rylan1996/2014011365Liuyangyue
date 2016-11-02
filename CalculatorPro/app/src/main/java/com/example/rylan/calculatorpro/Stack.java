package com.example.rylan.calculatorpro;

/**
 * Created by Rylan on 2016/9/18.
 */

public class Stack {
    double[] num;                                                 		 //double数组，用来存放操作符
    int top;                                                             //栈顶
    final int maxlen;                                                   //最长

    public Stack(){                                                //初始化字符栈
        maxlen = 200;
        num = new double[maxlen];
        top = 0;
    }

    public double pop(){                                               //把当前栈顶丢弃，并返回它的值
        if(top == 0){
            System.out.println("当前栈为空");
            //return -1;
            System.exit(0);
        }

        double i = num[top-1];
        top--;
        return i;
    }

    public void push(double n){                                        //把参数字符n放到栈顶
        if(top >= maxlen){
            System.out.println("栈已满");
            //return;
            System.exit(0);
        }

        num[top] = n;
        top++;
    }

    public double top(){                                               //返回栈顶元素，但不丢弃，注意与pop()的区别
        if(top == 0){
            System.out.println("当前栈为空");
            //return -1;
            System.exit(0);
        }

        return num[top-1];
    }
}
