package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

    public static String delims = " \t*+-/()[]";

    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     *
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
        /** COMPLETE THIS METHOD **/
        /** DO NOT create new vars and arrays - they are already created before being sent in
         ** to this method - you just need to fill them in.
         **/

        //expr = expr.trim();
        //expr = expr.replaceAll("\\s", "");

        Pattern combine = Pattern.compile("[a-zA-Z]+");
        Matcher combineMatch = combine.matcher(expr);
        Variable variableInsertInto;
        Array arrayInsertInto;

        while (combineMatch.find()) {
            String insertInto = (expr.substring(combineMatch.start(), combineMatch.end()));
            variableInsertInto = new Variable(insertInto);
            arrayInsertInto = new Array(insertInto);

            if(combineMatch.end() >= expr.length()){
                if(!vars.contains(variableInsertInto))
                    vars.add(new Variable(insertInto));
                continue;
            }

            else if (expr.charAt(combineMatch.end()) != ('[') && !vars.contains(variableInsertInto)) {
                vars.add(variableInsertInto);
            }

            else if(!arrays.contains(arrayInsertInto) && expr.charAt(combineMatch.end()) == ('[')){
                arrays.add(arrayInsertInto);
            }
        }
    }


    /**
     * Loads values for variables and arrays in the expression
     *
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
                continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
                arr = arrays.get(arri);
                arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;
                }
            }
        }
    }

    /**
     * Evaluates the expression.
     *
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
        /** COMPLETE THIS METHOD **/
        expr = expr.trim().replaceAll("\\s", "");

        String tmpString = new String();
        Stack<Float> addToStack = new Stack<>();
        Stack<Character> firstStack = new Stack<>();
        Stack<Character> secondStack = new Stack<>();
        float addFinal;
        char checkOperator;
        float vals = 0;
        int i = 0;

        while(i < expr.length()){
            if(expr.substring(i, i+1).matches("[a-zA-Z]+")){
                tmpString += expr.charAt(i); i++;
                if(i < expr.length()) {
                    while (expr.substring(i, i + 1).matches("[a-zA-Z]+")) {
                        tmpString += expr.charAt(i);
                        i++;
                        if(i >= expr.length())
                            break;
                    }

                    if (i < expr.length() && expr.charAt(i) == '[') {
                        addToStack.push((float) arrays.get(arrays.indexOf(new Array(tmpString))).values[(int) evaluate(expr.substring(i + 1), vars, arrays)]);
                        while(i < expr.length()){
                            if(expr.substring(i,i+1).matches("\\[")){
                                secondStack.push('[');
                            }

                            else if(expr.substring(i,i+1).matches("]") && !secondStack.isEmpty()){
                                secondStack.pop();
                            }

                            i++;
                            if(secondStack.isEmpty())
                                break;
                        }

                    } else{
                        addToStack.push((float) vars.get(vars.indexOf(new Variable(tmpString))).value);
                    }

                } else{
                    addToStack.push((float) vars.get(vars.indexOf(new Variable(tmpString))).value);
                }

                if(i >= expr.length()){
                    break;
                }
                tmpString = "";
            }

            else if(expr.substring(i, i+1).matches("[0-9]+")){
                tmpString += expr.charAt(i); i++;
                if(i < expr.length()) {
                    for (int j =i; expr.substring(j, j + 1).matches("[0-9]+"); j++) {
                        tmpString = tmpString + expr.charAt(j);
                        if(j >= expr.length())
                            break;
                    }
                }
                addToStack.push(Float.parseFloat(tmpString)); tmpString = "";
            }

            else if(expr.charAt(i) == '('){
                addToStack.push(evaluate(expr.substring(i+1), vars, arrays));
                for(int j = i; i < expr.length(); j++){
                    if(expr.substring(j,j+1).matches("\\(")){
                        firstStack.push('(');
                    }
                    else if(expr.substring(j,j+1).matches("\\)") && !firstStack.isEmpty()){
                        firstStack.pop();}

                    if(firstStack.isEmpty())
                        break;
                }
            }

            else if(expr.charAt(i) == ')' || expr.charAt(i) == ']'){
                addFinal = 0;
                while(!addToStack.isEmpty()){
                    addFinal = addFinal + addToStack.pop();
                }
                return addFinal;
            }

            else {
                checkOperator = expr.charAt(i); i++;
                if(expr.substring(i, i+1).matches("[0-9]+")){
                    tmpString = ""; tmpString += expr.substring(i,i+1); i++;
                    if(i < expr.length()) {
                        while (expr.substring(i, i + 1).matches("[0-9]+")) {
                            tmpString += expr.charAt(i); i++;
                            if(i >= expr.length())
                                break;
                        }
                    }
                    vals = Float.parseFloat(tmpString); tmpString = "";
                } else if(expr.substring(i, i+1).matches("[a-zA-Z]+")){
                    tmpString += expr.charAt(i); i++;
                    if(i < expr.length()) {
                        while (expr.substring(i, i + 1).matches("[a-zA-Z]+")) {
                            tmpString += expr.charAt(i);
                            i++;
                            if(i >= expr.length())
                                break;
                        }

                        if (i < expr.length() && expr.charAt(i) == '[') {
                            vals = ((float) arrays.get(arrays.indexOf(new Array(tmpString))).values[(int) evaluate(expr.substring(i + 1), vars, arrays)]);
                            for(int j = i; j < expr.length(); j++){
                                if(expr.charAt(j) == '[')
                                    secondStack.push('[');
                                else if(expr.charAt(j) == (']') && !secondStack.isEmpty())
                                    secondStack.pop();
                                if(secondStack.isEmpty())
                                    break;
                            }

                        } else{
                            vals = ((float) vars.get(vars.indexOf(new Variable(tmpString))).value); tmpString = "";
                        }

                    } else{

                        vals = ((float) vars.get(vars.indexOf(new Variable(tmpString))).value); tmpString = "";
                    }
                    if(i > expr.length()){
                        break;
                    }
                    tmpString = "";

                } else if(expr.charAt(i) == '('){
                    vals = evaluate(expr.substring(i+1), vars, arrays);
                    while(i < expr.length()){
                        if(expr.substring(i,i+1).matches("\\(")){
                            firstStack.push('(');
                        }
                        else if(expr.substring(i,i+1).matches("\\)") && !firstStack.isEmpty()){
                            firstStack.pop();
                        }
                        i++;
                        if(firstStack.isEmpty())
                            break;
                    }

                } else if(expr.charAt(i) == ')' || expr.charAt(i) == ']'){
                    addFinal = 0;
                    for(int j = i; !addToStack.isEmpty(); i++){
                        addFinal += addToStack.pop();
                    }
                    return addFinal;
                }

                if (checkOperator == '*') {
                    addToStack.push(addToStack.pop() * vals);
                } else if (checkOperator == '/') {
                    addToStack.push(addToStack.pop() / vals);
                } else if (checkOperator == '-') {
                    addToStack.push(vals * -1);
                } else if (checkOperator == '+') {
                    addToStack.push(vals);
                }
            }
        }

        addFinal = 0;

        while(!addToStack.isEmpty()){
            addFinal += addToStack.pop();
        }
        return addFinal;
    }
}



