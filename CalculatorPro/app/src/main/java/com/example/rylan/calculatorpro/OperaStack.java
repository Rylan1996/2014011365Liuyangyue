package com.example.rylan.calculatorpro;

/**
 * Created by Rylan on 2016/9/18.
 */

public class OperaStack {
    char[] num;                                              //字符数组，用来存放操作符
    public int top;                                          //栈顶
    final int maxlen;                                        //最长

    public OperaStack(){
        maxlen = 200;
        num = new char[maxlen];
        top = 0;
    }

    public char pop(){
        if(top == 0){
            System.out.println("当前栈为空");
            //return '@';
            System.exit(0);
        }

        char i = num[top-1];
        top--;
        return i;
    }

    public void push(char n){
        if(top >= maxlen){
            System.out.println("栈已满");
            //return;
            System.exit(0);
        }

        num[top] = n;
        top++;
    }

    public char top(){
        if(top == 0){
            System.out.println("当前栈为空");
            //return '@';
            System.exit(0);
        }

        return num[top-1];
    }
}
