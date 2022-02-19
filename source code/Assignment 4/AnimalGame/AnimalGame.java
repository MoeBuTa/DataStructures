// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 4
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

/**
 * Guess the Animal Game.
 * The program will play a "guess the animal" game and learn from its mistakes.
 * It has a decision tree for determining the player's animal.
 * When it guesses wrong, it asks the player of another question that would
 *  help it in the future, and adds it to the decision tree. 
 * The program can display the decision tree, and save the tree to a file and load it again,
 *
 * A decision tree is a tree in which all the internal modes have a question, 
 * The answer to the the question determines which way the program will
 *  proceed down the tree.  
 * All the leaf nodes have answers (animals in this case).
 * 
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

public class AnimalGame {

    public DTNode questionsTree;    // root of the decision tree;


    public AnimalGame(){
        setupGUI();
        resetTree();
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Play", this::play);
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Draw Tree", this::drawTree);
        UI.addButton("Reset", this::resetTree);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.5);
    }

    /**
     * Makes an initial tree with two question nodes and three leaf nodes.
     */
    public void resetTree(){
        questionsTree = new DTNode("has whiskers",
                                   new DTNode("bigger than person",
                                              new DTNode("tiger"),
                                              new DTNode("cat")),
                                   new DTNode("has trunk",
                                              new DTNode("Elephant"),
                                              new DTNode("Snake")));
    }
                    

    /**
     * Play the game.
     * Starts at the top (questionsTree), and works its way down the tree
     *  until it finally gets to a leaf node (with an answer in it)
     * If the current node has a question, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     * If the current node is a leaf it calls processLeaf on the node
     */
    public void play () {
        /*# YOUR CODE HERE */    	
		DTNode node = questionsTree;
		// use a while loop to deal with asking questions
    	while(node.isQuestion()) {
    		String ask = UI.askString("Your animal:"+node.getText() + "(Y/N)?");
        	if(ask.equalsIgnoreCase("y")) {      		       		
        		node = node.getYes();
        	}
        	else {        		
        		node = node.getNo();
        	}
        }
    	this.processLeaf(node);    	
    }
    	

        
    
    /**
     * Process a leaf node (a node with an answer in it)
     * Tell the player what the answer is, and ask if it is correct.
     * If it is not correct, ask for the right answer, and a property to distinguish
     *  the guess from the right answer
     * Change the leaf node into a question node asking about that fact,
     *  adding two new leaf nodes to the node, with the guess and the right
     *  answer.
    */
    public void processLeaf(DTNode leaf){    
        //CurrentNode must be a leaf node (an answer node)
        if (leaf==null || leaf.isQuestion()) { return; }
        /*# YOUR CODE HERE */
        String ask = UI.askString("I think I know is it a "+leaf.getText() +"(Y/N)?");
        if(ask.equalsIgnoreCase("y")) {
        	UI.println("Yeah, i win!");
        }
        else {
        	String ani = UI.askString("OK, what animal is it?");
        	UI.println("Oh, I can't distinguish a "+ leaf.getText()+ " from a "+ ani +
        			", Tell me something that's true for a bird but not for a tiger?");
        	String property = UI.askString("Property:");
        	UI.println("Thank you!");
        	String text = leaf.getText();
        	leaf.convertToQuestion(property,new DTNode(ani),new DTNode(text));

        }
        
    }       

    /**  COMPLETION
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree, and then
     * its "no" subtree.
     * Each node should be indented by how deep it is in the tree.
     */
    public void printTree(){
        UI.clearText();
        /*# YOUR CODE HERE */
        printSubTree(questionsTree, " ");
    }


    /**
     * Recursively print a subtree, given the node at its root
     *  - print the text in the node with the given indentation
     *  - if it is a question node, then 
     *    print its two subtrees with increased indentation
    */
    public void printSubTree(DTNode node, String indent){
        /*# YOUR CODE HERE */
		UI.println(indent+node.toString());
		indent = indent+"  ";
    	if(node.isQuestion()){ 						//print questions	
    		if(node.getYes()!=null) {
    			printSubTree(node.getYes(), indent); //print "yes" subtree recursively
    		}
    		if(node.getNo()!=null) {
    			printSubTree(node.getNo(), indent);  //print "no" subtree recursively
    		}    		
    	}
    }

    /**  CHALLENGE
     * Draw the tree on the graphics pane as boxes, connected by lines.
     * To make the tree fit in a window, the tree should go from left to right
     * (ie, the root should be drawn at the left)
     * The lines should be drawn before the boxes that they are connecting
     */
    public void drawTree(){
        UI.clearGraphics();
        /*# YOUR CODE HERE */
        DTNode node = questionsTree;
        doDraw(node, 50, 200, 100, 100);
       
    }
    
    //use an recursive method to do the draw Tree
    public void doDraw(DTNode node, double x, double y, double h, double v){

    	if(node.isQuestion()){ 						   		
    		if(node.getYes()!=null) {
    			UI.drawLine(x, y, x+h, y-v);   			
    			doDraw(node.getYes(), x+h, y-v, h, v/1.5); //draw "yes" subtree line recursively    			
    		}
    		if(node.getNo()!=null) {
    			UI.drawLine(x, y, x+h, y+v);    			
    			doDraw(node.getNo(), x+h, y+v, h, v/1.5);  //draw "no" subtree line recursively    			
    		}    		
    	}
    	node.draw(x, y);
    }


    public static void main (String[] args) { 
        new AnimalGame();
    }

}
