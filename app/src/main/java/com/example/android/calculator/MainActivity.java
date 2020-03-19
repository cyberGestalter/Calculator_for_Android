package com.example.android.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String stringResult = "";
    private double result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*public void display(double number) {
        TextView resultTextView = (TextView) findViewById(R.id.result_field);
        resultTextView.setText(""+number);
    }*/

    public void displayString(String numberString) {
        TextView resultTextView = (TextView) findViewById(R.id.result_field);
        resultTextView.setText(numberString);
    }


    public void one(View view) {
        stringResult+=1;
        displayString(stringResult);
    }

    public void two(View view) {
        stringResult+=2;
        displayString(stringResult);
    }

    public void three(View view) {
        stringResult+=3;
        displayString(stringResult);
    }

    public void four(View view) {
        stringResult+=4;
        displayString(stringResult);
    }

    public void five(View view) {
        stringResult+=5;
        displayString(stringResult);
    }

    public void six(View view) {
        stringResult+=6;
        displayString(stringResult);
    }

    public void seven(View view) {
        stringResult+=7;
        displayString(stringResult);
    }

    public void eight(View view) {
        stringResult+=8;
        displayString(stringResult);
    }

    public void nine(View view) {
        stringResult+=9;
        displayString(stringResult);
    }

    public void zero(View view) {
        stringResult+=0;
        displayString(stringResult);
    }

    public void dot(View view) {
        stringResult+=".";
        displayString(stringResult);
    }

    public void reset(View view) {
        result = 0;
        stringResult = "";
        displayString("0");
    }

    public void undo(View view) {
        stringResult = stringResult.substring(0, stringResult.length()-1);
        displayString(stringResult);
    }

    public void percent(View view) {
        compareLastSymbolWithNew('%');
    }

    public void minus(View view) {
        compareLastSymbolWithNew('-');
    }

    public void plus(View view) {
        compareLastSymbolWithNew('+');
    }

    public void multiply(View view) {
        compareLastSymbolWithNew('*');
    }

    public void divide(View view) {
        compareLastSymbolWithNew('/');
    }

    private void compareLastSymbolWithNew(char newSymbol) {
        char lastSymbol;
        if (!stringResult.equals("")) {
            lastSymbol = stringResult.charAt(stringResult.length() - 1);
            if (newSymbol != '%') {
                if (lastSymbol == '/' || lastSymbol == '*') {
                    switch (newSymbol) {
                        case '/':
                        case '*':
                            if (newSymbol != lastSymbol) {
                                stringResult = stringResult.substring(0, stringResult.length() - 1) + newSymbol;
                            }
                            break;
                        case '-':
                            stringResult += "(" + newSymbol;
                            break;
                        case '+':
                            stringResult = stringResult.substring(0, stringResult.length() - 1) + newSymbol;
                            break;
                    }
                } else {
                    if (lastSymbol == '-' || lastSymbol == '+') {
                        if (newSymbol != lastSymbol) {
                            stringResult = stringResult.substring(0, stringResult.length() - 1) + newSymbol;
                        }
                    } else {
                        stringResult += newSymbol;
                    }
                }
            } else {
                if (lastSymbol != '/' && lastSymbol != '*' && lastSymbol != '+' && lastSymbol != '-' && lastSymbol != '%') {
                    stringResult += "/100";
                }
            }
            displayString(stringResult);
        } else {
            if (newSymbol != '%') {
                stringResult += newSymbol;
                displayString(stringResult);
            } else {
                displayString("0");
            }
        }
    }

    public void openBracket(View view) {
        if (!stringResult.equals("")) {
            if (stringResult.substring(stringResult.length()-1).matches("[0-9]")) {
                stringResult += "*(";
            }
        } else
            stringResult += "(";
        displayString(stringResult);
    }

    public void closeBracket(View view) {
        stringResult += ")";
        displayString(stringResult);
    }

    //Список операторов (+, -, *, /, открывающая скобка)
    List<Character> operations = new ArrayList<>();
    List<Double> numbers = new ArrayList<>();
    //Список для постфиксной записи переданного выражения
    List<String> operationsAndNumbers = new ArrayList<>();

    //В соответствии с приоритетом оператора (* и / над + и -)
    //В цикле, пока список опреаторов не пуст:
    //Взять последний добавленный в список операторов символ
    //Если символ (, вернуть ( в список операторов
    //Если другой символ (*, /, + или -), то
    //Если его важность меньше важности переданного символа, вернуть в список операторов и прервать цикл
    //Если его важность больше либо равна важности переданного символа, добавить символ в список с постфиксной записью выражения
    //и продолжить цикл
    //После цикла добавить переданный символ в список операторов
    private void gotOper(char opThis, int prec1) {
        while (!operations.isEmpty()) {
            char opTop = operations.remove(operations.size()-1);
            if (opTop == '(') {
                operations.add(opTop);
                break;
            } else {
                int prec2;
                if (opTop == '+' || opTop == '-') {
                    prec2 = 1;
                } else {
                    prec2 = 2;
                }
                if (prec2 < prec1) {
                    operations.add(opTop);
                    break;
                }
                else {
                    operationsAndNumbers.add("" + opTop);
                } //добавить opTop в строку с операндами (числами) - в numbers, но там элементы другого типа

            }
        }
        operations.add(opThis);
    }

    //В цикле, пока список опреаторов не пуст:
    //Взять последний добавленный в список операторов символ
    //Если символ ( , то прервать цикл
    //Если нет, то добавить символ в постфиксную запись выражения
    private void gotParen(char ch) {
        while (!operations.isEmpty()) {
            char chx = operations.remove(operations.size()-1);
            if (chx == '(')
                break;
            else {
                operationsAndNumbers.add(""+chx);
            }
        }
    }

    //Преобразование stringResult в постфиксную последовательность чисел и операторов для вычисления выражения
    //Запись полученного выражения в operationsAndNumbers
    private void postfixExpression() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < stringResult.length(); i++) {
            char ch = stringResult.charAt(i);
            switch (ch) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                    number.append(ch);
                    if (i == stringResult.length()-1) operationsAndNumbers.add(number.toString());
                    break;
                /*case '%':
                    double percent = Double.parseDouble(number.toString())/100;
                    number = new StringBuilder();
                    number.append(percent);
                    if (i == stringResult.length()-1) {
                        operationsAndNumbers.add(number.toString());
                        continue;
                    }
                    break;*/
                case '.':
                    number.append('.');
                    break;                                                  // + или -
                case '+':
                case '-':                                                   // Извлечение операторов (приоритет 1)
                    if (number.toString().length() != 0) {
                        operationsAndNumbers.add(number.toString());
                        number = new StringBuilder();
                    } else {
                        if (!operations.isEmpty()) {
                            if (operations.get(operations.size() - 1) == '(')
                                operationsAndNumbers.add("0");
                        }
                    }
                    if (i == stringResult.length()-1) continue;
                    if (i == 0) operationsAndNumbers.add("0");
                    gotOper(ch, 1);
                    break;
                case '*':
                case '/':                                                   // Извлечение операторов (приоритет 2)
                    if (number.toString().length() != 0) {
                        operationsAndNumbers.add(number.toString());
                        number = new StringBuilder();
                    }
                    if (i == stringResult.length()-1) continue;
                    if (i == 0) operationsAndNumbers.add("0");
                    gotOper(ch, 2);
                    break;
                case '(':                                                   // Открывающая круглая скобка - Занести в стек
                    if (number.toString().length() != 0) {
                        operationsAndNumbers.add(number.toString());
                        number = new StringBuilder();
                    }
                    if (i == stringResult.length()-1) continue;
                    operations.add(ch);
                    break;
                case ')':                                                   // Закрывающая круглая скобка - Извлечение операторов
                    if (number.toString().length() != 0) {
                        operationsAndNumbers.add(number.toString());
                        number = new StringBuilder();
                    }
                    gotParen(ch);
                    break;
                /*default:
                    //numbers.add(Double.parseDouble(""+ch));
                    operationsAndNumbers.add(""+ch);                // Остается операнд - // Записать в выходную строку
                    break;*/
            }
        }

        while (!operations.isEmpty()) {
            operationsAndNumbers.add("" + operations.remove(operations.size() - 1)); // Извлечение оставшихся операторов
        }

        //В operationsAndNumbers содержится постфиксная последовательность символов для вычисления выражения
    }

    //Вычисления значения выражения из чисел и операторов в постфиксной записи из operationsAndNumbers
    private void resultCalculate() {
        for (int i = 0; i < operationsAndNumbers.size(); i++) {
            double firstNumber = 0;
            double secondNumber = 0;
            switch (operationsAndNumbers.get(i)) {
                case "+" :
                    if (numbers.size() == 1 || numbers.size() == 0) break;
                    firstNumber = numbers.remove(numbers.size()-1);
                    secondNumber = numbers.remove(numbers.size()-1);
                    numbers.add(firstNumber+secondNumber);
                    break;
                case "-":
                    if (numbers.size() == 1 || numbers.size() == 0) break;
                    firstNumber = numbers.remove(numbers.size()-1);
                    secondNumber = numbers.remove(numbers.size()-1);
                    numbers.add(secondNumber-firstNumber);
                    break;
                case "*":
                    if (numbers.size() == 1 || numbers.size() == 0) break;
                    firstNumber = numbers.remove(numbers.size()-1);
                    secondNumber = numbers.remove(numbers.size()-1);
                    numbers.add(firstNumber*secondNumber);
                    break;
                case "/":
                    if (numbers.size() == 1 || numbers.size() == 0) break;
                    firstNumber = numbers.remove(numbers.size()-1);
                    secondNumber = numbers.remove(numbers.size()-1);
                    numbers.add(secondNumber/firstNumber);
                    break;
                case "(" :
                    break;
                default:
                    numbers.add(Double.parseDouble(operationsAndNumbers.get(i)));
                    break;
            }
        }
        if (numbers.size() != 0) {
            result = numbers.remove(0);
        }
        operationsAndNumbers = new ArrayList<>();
    }

    public void equal (View view) {
        try {
            postfixExpression();
            resultCalculate();
            if (result % 1 == 0)
                displayString(stringResult + " =\n" + (int) result);
            else
                displayString(stringResult + " =\n" + result);
        } catch (Exception e) {
            displayString("Ошибка");
        }
    }
}
