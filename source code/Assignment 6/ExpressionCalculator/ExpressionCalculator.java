import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

/** 
 * Prints and calculates arithmetic Expressions
 */
public class ExpressionCalculator{

    /**  Loop to read expression, print in infix notation, evaluate, print answer */
    public static void main(String[] args){
        UI.addButton("Quit", UI::quit); 
        UI.setDivider(1.0);
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   * + 4 5 - 7 / 6 4     [(4+5)*(7-(6/4))]");
        while (true){
            Scanner sc = new Scanner(UI.askString("expr:"));
            BTNode<String> expr = readExpr(sc);
            printExpr(expr);
            UI.println(" -> " + evaluate(expr));
            UI.println();
        }
    }

    /** Recursively construct expression tree from scanner input */
    public static BTNode<String> readExpr(Scanner sc){
        if (sc.hasNextDouble()){
            return new BTNode<String>(sc.next());
        }
        else {  // next token is an operator
            return new BTNode<String>(sc.next(), readExpr(sc), readExpr(sc));
        }
    }

    /** Print expression tree in-order with brackets */
    public static void printExpr( BTNode<String> expr){
        if (expr.isLeaf()){
            UI.print(expr.getItem());
        }
        else {  
            UI.print("(");
            printExpr(expr.getLeft());
            UI.print(expr.getItem());
            printExpr(expr.getRight());
            UI.print(")");
        }
    }

    /** Evaluate an expressionand return the value */
    public static double evaluate(BTNode<String> expr){
        if (expr==null){ return Double.NaN; }
        if (expr.isLeaf()){
            try { return Double.parseDouble(expr.getItem());}
            catch(Exception e){return Double.NaN;}
        }
        else {
            double left = evaluate(expr.getLeft());
            double right = evaluate(expr.getRight());
            if (expr.getItem().equals("*"))     { return left * right; }
            else if (expr.getItem().equals("+")){ return left + right; }
            else if (expr.getItem().equals("-")){ return left - right; }
            else if (expr.getItem().equals("/")){ return left / right; }
            else                                { return Double.NaN; }
        }
    }


}
