/* 
 * Name:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;


/** 
 * Calculator for lisp-style expressions (Cambridge-Polish notation)
 */
public class LispExpressionCalculator{

    /**  Read-evaluate-print loop    */
    public static void main(String[] args){
        UI.addButton("Quit", UI::quit); 
        UI.setDivider(1.0);
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        while (true){
            Scanner sc = new Scanner(UI.askString("expr:"));
            try {
                GTNode<String> expr = readExpr(sc);  
                printExpr(expr);
                UI.println(" -> " + evaluate(expr));
            }catch(Exception e){UI.println("invalid expression"+e);}
            UI.println();
        }
    }

    /** Recursively construct expression tree from scanner input */
    public static GTNode<String> readExpr(Scanner sc){
        if (sc.hasNext("\\(")){
            sc.next();   // the opening (
            String op = sc.next();  // the operator
            GTNode<String> node = new GTNode<String>(op);  
            while (! sc.hasNext("\\)")){
                node.addChild(readExpr(sc));                        // the arguments
            }
            sc.next();    // the closing )
            return node;
        }
        else {  // next token is an number
            return new GTNode<String>(sc.next());
        }
    }

    /** Print expression tree with brackets */
    public static void printExpr( GTNode<String> expr){
        if (expr.numberOfChildren()==0){
            UI.print(expr.getItem());
        }
        else {  
            UI.print("("+ expr.getItem());
            for (GTNode<String> child : expr){
                UI.print(" ");
                printExpr(child);
            }
            UI.print(")");
        }
    }

    /** Evaluate an expressionand return the value */
    public static double evaluate(GTNode<String> expr){
        if (expr==null){ return Double.NaN; }
        if (expr.numberOfChildren()==0){
            try { return Double.parseDouble(expr.getItem());}
            catch(Exception e){return Double.NaN;}
        }
        else {
            double ans = Double.NaN;
            if (expr.getItem().equals("*"))     { 
                ans = 1;
                for(GTNode<String> child: expr) {ans*= evaluate(child); }
            }
            else if (expr.getItem().equals("+")){
                ans = 0;
                for(GTNode<String> child: expr) {ans+=evaluate(child); }
            }
            else if (expr.getItem().equals("-")){ 
                ans = evaluate(expr.getChild(0));
                for(int i=1; i<expr.numberOfChildren(); i++){ans-=evaluate(expr.getChild(i));}
            }
            else if (expr.getItem().equals("/")){                
                ans = evaluate(expr.getChild(0));
                for(int i=1; i<expr.numberOfChildren(); i++){ans/=evaluate(expr.getChild(i));}
            }
            return ans; 
        }
    }

}

