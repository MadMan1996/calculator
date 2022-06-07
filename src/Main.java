import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String []args) throws IOException {
        while(true){
            Scanner sc = new Scanner(System.in);
            String inputStr = sc.nextLine();
            calc(inputStr);
        }
    }
    public static String calc (String input) throws IOException {
            //grab nums from expression
            String[] nums = input.split("[+-/*]");
            String numStr1 = nums[0].trim();
            String numStr2 = nums[1].trim();
            //check is expression valid and is it Arabic
            boolean isArabic = isArabicExpression(input);

            //grab operator from expression
            Pattern operatorPattern = Pattern.compile("[-/+*]");
            Matcher operatorMatcher = operatorPattern.matcher(input);
            String currentOperator;
            String calculationResult = "";
            if(operatorMatcher.find()){
                currentOperator = operatorMatcher.group(0);
            //do calculation
                if(isArabic){
                    int num1 = Integer.parseInt(numStr1);
                    int num2 = Integer.parseInt(numStr2);
                    calculationResult = String.valueOf(calculateArabic(num1, num2, currentOperator));
                    System.out.println(calculationResult);
                }
                if(!isArabic){
                    calculationResult = calculateRoman(numStr1,numStr2,currentOperator);
                    System.out.println(calculationResult);
                }
            }
            return calculationResult;
    }

    //check is number roman
    static boolean isRoman(String inputValue){
        return inputValue.toUpperCase().matches("^\s?(I|II|III|IV|V|VI|VII|VIII|IX|X)\s?$");
    }
    //check is number arabic
    static boolean isArabic(String inputValue){
        return inputValue.matches("^\s?([1-9]|10)\s?$");
    }
    //format roman numbers to arabic
    static int romanToArabic(String num){
        Roman numRoman = Roman.valueOf(num.toUpperCase().trim());
        int numArabic = numRoman.ordinal()+1;
        return numArabic;
    }
    //do calculations with arabic numbers
    static int calculateArabic (int num1, int num2, String operator){
        int result = switch (operator){
            case "+":
                yield num1+num2;
            case "-":
                yield num1-num2;
            case "*":
                yield num1*num2;
            case "/":
                 yield num1/num2;
            default:
                yield 1;
        };
        return result;

    }
    //do calculations with roman numbers
    static String calculateRoman(String num1, String num2, String operator){
        //transform roman input to arabic
        int numArabic1 = romanToArabic(num1);
        int numArabic2 = romanToArabic(num2);
        //do arabic calculation
        int result = calculateArabic(numArabic1, numArabic2, operator);
        String resultRoman = "";
        //transform arabic result to roman
        while (result >= 90) {
            resultRoman += "XC";
            result -= 90;
        }
        while (result >= 50) {
            resultRoman += "L";
            result -= 50;
        }
        while (result >= 40) {
            resultRoman += "XL";
            result -= 40;
        }
        while (result >= 10) {
            resultRoman += "X";
            result -= 10;
        }
        while (result >= 9) {
            resultRoman += "IX";
            result -= 9;
        }
        while (result >= 5) {
            resultRoman += "V";
            result -= 5;
        }
        while (result >= 4) {
            resultRoman += "IV";
            result -= 4;
        }
        while (result >= 1) {
            resultRoman += "I";
            result -= 1;
        }
        return resultRoman;
    }
    //check is input correct
    static boolean isArabicExpression(String expression) throws IOException {
        String[] nums = expression.split("[+-/*]");

        //if there ara less or more then two numbers
        if(nums.length<2|nums.length>2) throw new IOException("Неверное выражение");

        //check is number arabic or roman
        boolean numArabic1 = isArabic(nums[0]);
        boolean numRoman1 = isRoman(nums[0]);
        boolean numArabic2 = isArabic(nums[1]);
        boolean numRoman2 = isRoman(nums[1]);

        //if fist number is not arabic and is not roman => wrong expresion
        if(!numArabic1&&!numRoman1) throw new IOException("Неверное выражение");

        //if second number is not arabic and is not roman => wrong expresion
        if(!numArabic2&&!numRoman2) throw new IOException("Неверное выражение");

        //if one number arabic and other roman => wrong expression
        if(numArabic1&&numRoman2) throw new IOException("Неверное выражение");
        if(numArabic2&&numRoman1) throw new IOException("Неверное выражение");
        //if roman1 - roman2 <=o
        if(romanToArabic(nums[0]) - romanToArabic(nums[1])<=0)throw  new IOException("Неверное выражение");


        return numArabic1&&numArabic2;
    }
}
