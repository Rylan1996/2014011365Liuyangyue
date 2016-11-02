package com.example.rylan.calculatorpro;

/**
 * Created by Rylan on 2016/9/18.
 */
public class Calculate {
    Stack number;
    OperaStack mychar;

    public Calculate(){
        number = new Stack();
        mychar = new OperaStack();

    }

    public String toBehind(String express){
        String behind=new String();
        mychar.push('#');
        int a=0;
        int b=0;
        for(int i=0; i<express.length(); i++){
            if(i==0&&express.charAt(i)=='='){
                return "stop";
            }
            if(express.charAt(i)=='('){
                a++;
            }
            if(express.charAt(i)==')'){
                if(b==0 && a==0){
                    return "stop";
                }
                b++;
                if(express.charAt(i+1)=='('){
                    return "stop";
                }
            }
        }
        if(a!=b){
            return "stop";
        }


        for(int i=0; i<express.length(); i++){

            if( (express.charAt(i)<=57 && express.charAt(i)>=48) || express.charAt(i) == '.'){   //如果此字符是数字或小数点,则输出到后缀式
                behind += express.charAt(i);
            }

            else if(express.charAt(i) == '#'){                   								//如果是表达式结束符
                while(mychar.top != 1){                          								//从栈顶依次弹出所有运算符，并加到后缀式
                    behind += (";" + mychar.pop());
                }                                               								//循环完成后，已经得到想要的后缀式了
            }

            else if((express.charAt(i)>='a'&&express.charAt(i)<='z')||(express.charAt(i)>='A'&&express.charAt(i)<='Z'))
            {
                continue;
            }


            else{                                            									//如果是操作符，则用分号隔开，以便把几个表示同一个数字的字符变为double型
                behind += ";";

                if( Osp(express.charAt(i)) > Isp(mychar.top()) ){         						//如果比栈顶字符优先级高则入栈
                    mychar.push(express.charAt(i));
                }

                else if( Osp(express.charAt(i)) == Isp(mychar.top()) ){        					//如果和栈顶操作符优先级相等，则是右括号与左括号匹配的情况，应把左括号扔掉
                    mychar.pop();
                }

                else{                                                       					//如果比栈顶字符优先级低则弹出栈顶元素加入后缀式中，直至栈顶元素优先级比其低时就入栈
                    boolean higher = false;

                    while (!higher){
                        behind += (mychar.pop() + ";");

                        if( Osp(express.charAt(i)) > Isp(mychar.top())){
                            higher = true;
                            mychar.push(express.charAt(i));
                        }
                        else if( Osp(express.charAt(i)) == Isp(mychar.top()) ){        			//如果和栈顶操作符优先级相等，则是右括号与左括号匹配的情况，应把左括号扔掉
                            mychar.pop();
                            higher = true;
                        }

                    }
                }
            }
        }
        return behind;
    }

    public double caculate(String behind){
        double result = 0;

        String[] str = behind.split(";");

        for(int i = 0; i < str.length; i++){
            if(str[i].equals("+")){
                double b = number.pop();
                double a = number.pop();
                double num = a + b;
                number.push(num);
            }

            else if( str[i].equals("-") ){
                double b = number.pop();
                double a = number.pop();
                double num = a - b;
                number.push(num);
            }

            else if( str[i].equals("*") ){
                double b = number.pop();
                double a = number.pop();
                double num = a * b;
                number.push(num);
            }

            else if( str[i].equals("/") ){
                double b = number.pop();
                double a = number.pop();
                double num = a / b;
                number.push(num);
            }

            else if( str[i].equals("") ){
                continue;
            }

            else{
                double num = Double.parseDouble(str[i]);
                number.push(num);
            }
        }

        result = number.top();
        return result;
    }

    int Isp(char ch){
        switch(ch){
            case '#'  :  return 0;
            case '('  :  return 1;
            case '+'  :  return 3;
            case '-'  :  return 3;
            case '*'  :  return 5;
            case '/'  :  return 5;
            case '%'  :  return 5;
            case '^'  :  return 7;
            case ')'  :  return 8;
        }
        return -1;
    }

    int Osp(char ch){
        switch(ch){
            case '#'  : 	return 0;
            case '('  : 	return 8;
            case '+'  : 	return 2;
            case '-'  : 	return 2;
            case '*'  : 	return 4;
            case '/'  : 	return 4;
            case '%'  : 	return 4;
            case '^'  : 	return 6;
            case ')'  : 	return 1;
        }
        return -1;
    }


}
