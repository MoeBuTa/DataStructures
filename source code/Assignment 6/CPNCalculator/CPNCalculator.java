// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 6
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

/** 
 * Calculator for Cambridge-Polish Notation expressions
 * User can type in an expression (in CPN) and the program
 * will compute and print out the value of the expression.
 * The template is based on the version in the lectures,
 *  which only handled + - * /, and did not do any checking
 *  for valid expressions
 * The program should handle a wider range of operators and
 *  check and report certain kinds of invalid expressions
 */

public class CPNCalculator{



    /**
     * Main Read-evaluate-print loop
     * Reads an expression from the user then evaluates it and prints the result
     * Invalid expressions could cause errors when reading.
     * The try-catch prevents these errors from crashing the programe - 
     *  the error is caught, and a message printed, then the loop continues.
     */
    public static void main(String[] args){
        UI.addButton("Quit", UI::quit); 
        UI.setDivider(1.0);
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        while (true){
        	
            UI.println();
            Scanner sc = new Scanner(UI.askString("expr:"));
            try {            	
                GTNode<String> expr = readExpr(sc);  
                
                UI.println(" -> " + evaluate(expr));
                printExpr(expr);
            }catch(Exception e){UI.println("invalid expression"+e);}
        }
    }




    /**
     * Recursively construct expression tree from scanner input
     */
    public static GTNode<String> readExpr(Scanner sc){
        if (sc.hasNext("\\(")) {                     // next token is an opening bracket
            sc.next();                               // the opening (
            String op = sc.next();                   // the operator
            
            
            //check if the expression empty or not
            if(op.equals(")")) {
            	UI.println("Empty bracket!");
            	return null;
            }
            
            //check if the operator valid or not
            if(!op.equals("+") &&
                	!op.equals("-") &&
                	!op.equals("*") &&
                	!op.equals("/") &&
                	!op.equals("sqrt") &&
                	!op.equals("^") &&
                	!op.equals("log") &&
                	!op.equals("ln") &&
                	!op.equals("sin") &&
                	!op.equals("cos") &&
                	!op.equals("tan") &&
                	!op.equals("dist") &&
                	!op.equals("avg")){
                UI.println(op + " is not a valid operator!");
                return null;
            }
            
            GTNode<String> node = new GTNode<String>(op);  
            while (! sc.hasNext("\\)")){
            	
            	//check if the brackets match or not
           	 	if(!sc.hasNext()) {           	 		
           	 		UI.println("Missing closing bracket!");
           	 		return null;
           	 	}
                GTNode<String> child = readExpr(sc); // the arguments              
                node.addChild(child); 
            }
            sc.next();                               // the closing )
            return node;
        }
        else {                                       // next token must be a number

            return new GTNode<String>(sc.next());
        }
    }

    /**
     * Evaluate an expression and return the value
     * Returns Double.NaN if the expression is invalid in some way.
     */
    public static double evaluate(GTNode<String> expr){
    	
        if (expr==null){return Double.NaN; }
        

        if (expr.numberOfChildren()==0){            // must be a number
        	
            try { 
                if(expr.getItem().equalsIgnoreCase("PI")) {  //Allow the constants PI to be used   
                	expr =  new GTNode<String>(String.valueOf(Math.PI));      
                }
                else if(expr.getItem().equalsIgnoreCase("E")) {   //Allow the constants E to be used            	
                	expr = new GTNode<String>(String.valueOf(Math.E));                	
                }                
            	return Double.parseDouble(expr.getItem());
            }
            catch(Exception e){return Double.NaN;}
        }
        else {
        	
            double ans = Double.NaN;                // answer if no valid operator    
            if (expr.getItem().equals("+")){        // addition operator
            	if(expr.numberOfChildren() < 2 ) {UI.println("+ has wrong number of arguments!");}
                ans = 0;
                for(GTNode<String> child: expr) {
                    ans += evaluate(child);
                }
            }
            else if (expr.getItem().equals("*")) {  // multiplication operator 
            	if(expr.numberOfChildren() < 2 ) {UI.println("* has wrong number of arguments!");}
                ans = 1;
                for(GTNode<String> child: expr) {
                    ans *= evaluate(child);
                }
            }
            else if (expr.getItem().equals("-")){  // subtraction operator 
            	if(expr.numberOfChildren() < 2 ) {UI.println("- has wrong number of arguments!");}
                ans = evaluate(expr.getChild(0));
                for(int i=1; i<expr.numberOfChildren(); i++){
                    ans -= evaluate(expr.getChild(i));
                }
            }
            else if (expr.getItem().equals("/")){  // division operator  
            	if(expr.numberOfChildren() < 2 ) {UI.println("/ has wrong number of arguments!");}
                ans = evaluate(expr.getChild(0));
                for(int i=1; i<expr.numberOfChildren(); i++){
                    ans /= evaluate(expr.getChild(i));
                }
            }
            /*# YOUR CODE HERE */
            
            else if (expr.getItem().equals("sqrt")) { //square root operator
            	if(expr.numberOfChildren() != 1) {
            		UI.println("sqrt has wrong number of arguments!");            		
            	}
            	else {
            		ans = Math.sqrt(evaluate(expr.getChild(0)));
            	}
            }
            
            else if(expr.getItem().equals("^")) {   //power operator
            	if(expr.numberOfChildren() != 2){
            		UI.println("^ has wrong number of arguments!");            		
            	}
            	else {
            		ans = Math.pow(evaluate(expr.getChild(0)), evaluate(expr.getChild(1)));
            	}
            }
            
            else if(expr.getItem().equals("log")) {  //log operator(base 10)
            	if(expr.numberOfChildren() != 1){
            		UI.println("log has wrong number of arguments!");            		
            	}
            	else {  
            		ans = Math.log10(evaluate(expr.getChild(0)));
            	}
            	
            }
            
            else if(expr.getItem().equals("ln")) {	//ln operator(base e)
            	if(expr.numberOfChildren() != 1){
            		UI.println("ln has wrong number of arguments!");            		
            	}
            	else {
            		ans = Math.log(evaluate(expr.getChild(0)));
            	}
            }
            
            else if(expr.getItem().equals("sin")) {	//sin operator
            	if(expr.numberOfChildren() != 1){
            		UI.println("sin has wrong number of arguments!");            		
            	}
            	else {
            		ans = Math.sin(evaluate(expr.getChild(0)));
            	}
            }
            
            else if(expr.getItem().equals("cos")) {	//cos operator
            	if(expr.numberOfChildren() != 1){
            		UI.println("cos has wrong number of arguments!");            		
            	}
            	else {
            		ans = Math.cos(evaluate(expr.getChild(0)));
            	}
            }
            
            else if(expr.getItem().equals("tan")) {	//tan operator
            	if(expr.numberOfChildren() != 1){
            		UI.println("tan has wrong number of arguments!");            		
            	}
            	else {
            		ans = Math.tan(evaluate(expr.getChild(0)));
            	}
            }
            
            else if(expr.getItem().equals("dist")) {	//dist operator
            	if(expr.numberOfChildren() != 4){
            		UI.println("dist has wrong number of arguments!");            		
            	}
            	else {
            		ans = Math.sqrt(Math.pow((evaluate(expr.getChild(3)) - evaluate(expr.getChild(1))), 2)+ 
            				 Math.pow((evaluate(expr.getChild(2)) - evaluate(expr.getChild(0))), 2));
            	}
            }
            else if (expr.getItem().equals("avg")){        // average operator
                ans = 0;
                if(expr.numberOfChildren() == 0 ) {UI.println("avg has wrong number of arguments!");}
                for(GTNode<String> child: expr) {
                    ans += evaluate(child);
                }
                ans /= expr.numberOfChildren();
            }

            return ans; 
        }
    }
    

    
    public static void printExpr( GTNode<String> expr){
//        if (expr.numberOfChildren()==0){
//            UI.print(expr.getItem());
//        }
//        else {  
//            UI.print("("+ expr.getItem());
//            for (GTNode<String> child : expr){
//                UI.print(" ");
//                printExpr(child);
//            }
//            UI.print(")");
//        }
    }
    

}

