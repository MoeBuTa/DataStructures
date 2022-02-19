// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 3
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;

import javax.swing.JButton;

/**
 *  Simple 'Minesweeper' program.
 *  There is a grid of cellss, some of which contain a mine.
 *  The user can click on a cells to either expose it or to
 *  mark/unmark it.
 *  
 *  If the user exposes a cells with a mine, they lose.
 *  Otherwise, it is uncovered, and shows a number which represents the
 *  number of mines in the eight cellss surrounding that one.
 *  If there are no mines adjacent to it, then all the unexposed cellss
 *  immediately adjacent to it are exposed (and and so on)
 *
 *  If the user marks a cells, then they cannot expose the cells,
 *  (unless they unmark it first)
 *  When all squares with mines are marked, and all the squares without
 *  mines are exposed, the user has won.
 */
public class MineSweeper {

    public static final int ROWS = 15;
    public static final int COLS = 15;

    public static final double LEFT = 10; 
    public static final double TOP = 10;
    public static final double CELLs_SIZE = 20;
    
    private static int cellsNumber = 0; //the number of current exposed cellss
    private static int mineNumber = 0;	//the number of the mines in the current game
    
    public int[][]  grid;   //the array of integers representing the mines and the empty cellss
    // Fields
    private boolean marking;
    
    private Cells[][] cellss;
    private JButton mrkButton;
    private JButton expButton;
    Color defaultColor;

    /** 
     * Construct a new MineSweeper object
     * and set up the GUI
     */
    public MineSweeper(){
        setupGUI();
        setMarking(false);
        makeGrid();
    }

    /** setup buttons */
    public void setupGUI(){
        UI.setMouseListener(this::doMouse);
        UI.addButton("New Game", this::makeGrid);
        this.expButton = UI.addButton("Expose", ()->setMarking(false));
        this.mrkButton = UI.addButton("Mark", ()->setMarking(true));
        UI.addButton("AI helper", this::Helper);
        UI.addButton("Close AI helper", this::closeHelper);       
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);
    }

    /** Respond to mouse events */
    public void doMouse(String action, double x, double y) {
    	
        if (action.equals("released")){
            int row = (int)((y-TOP)/CELLs_SIZE);
            int col = (int)((x-LEFT)/CELLs_SIZE);
            if (row>=0 && row < ROWS && col >= 0 && col < COLS){
                if (marking) { mark(row, col);}
                else         { tryExpose(row, col); }
            }
        }
    }

    /**
     * Remember whether it is "Mark" or "Expose"
     * Change the colour of the "Mark", "Expose" buttons
     */
    public void setMarking(boolean v){
        marking=v;
        if (marking) {
            mrkButton.setBackground(Color.red);
            expButton.setBackground(null);
        }
        else {
            expButton.setBackground(Color.red);
            mrkButton.setBackground(null);
        }
    }

    // Other Methods
    /*
     * create an AI helper to help the player.
     */
    public void Helper() {
        for (int row=0; row < ROWS; row++){
        	double y = TOP+row*CELLs_SIZE;
            for (int col=0; col<COLS; col++){
            	double x =LEFT+col*CELLs_SIZE;
            	if(grid[row][col] == -1 || this.cellss[row][col].isExposed() &&
            		this.cellss[row][col].getAdjacentMines() == 0) {
                    UI.setColor(Color.red);
                    UI.drawString(""+grid[row][col], x+CELLs_SIZE/2-5, y+CELLs_SIZE/2+5);
                    UI.drawString(""+grid[row][col], x+CELLs_SIZE/2-4, y+CELLs_SIZE/2+5);
            	}
            }
        }    	
    }
    /*
     * close AI helper
     */
    public void closeHelper() {
    	 Color DARK_GREEN = new Color(0,127,0);
    	 Color LIGHT_GREEN = new Color(0,255,0);
        for (int row=0; row < ROWS; row++){
        	double y = TOP+row*CELLs_SIZE;
            for (int col=0; col<COLS; col++){
            	double x =LEFT+col*CELLs_SIZE;
            	if(grid[row][col] == -1 || this.cellss[row][col].isExposed() &&
                		this.cellss[row][col].getAdjacentMines() == 0) {
            		UI.setColor(DARK_GREEN);
            		UI.fillRect(x+1, y+1, CELLs_SIZE-2, CELLs_SIZE-2);
            		if(cellss[row][col].isExposed())
            		UI.setColor(LIGHT_GREEN);
            		UI.fillRect(x+1, y+1, CELLs_SIZE-2, CELLs_SIZE-2);
            	}
            }
        }
    }

   
    /** 
     * The player has clicked on a cells to expose it
     * - if it is already exposed or marked, do nothing.
     * - if it's a mine: lose (call drawLose()) 
     * - otherwise expose it (call exposeCellsAt)
     * then check to see if the player has won and call drawWon() if they have.
     * (This method is not recursive)
     */
    public void tryExpose(int row, int col){
        /*# YOUR CODE HERE */

        if(this.cellss[row][col].isExposed() &&
        	this.cellss[row][col].isMarked())
        	return;
        else {
        	if(cellss[row][col].hasMine()) drawLose();
        	else exposeCellsAt(row,col);
        }   	 	
        if (hasWon()){
            drawWin();
        }
    }

    /** 
     *  Expose a cells, and spread to its neighbours if safe to do so.
     *  It is guaranteed that this cells is safe to expose (ie, does not have a mine).
     *  If it is already exposed, we are done.
     *  Otherwise expose it, and redraw it.
     *  If the number of adjacent mines of this cells is 0, then
     *     expose all its neighbours (which are safe to expose)
     *     (and if they have no adjacent mine, expose their neighbours, and ....)
     */
    public void exposeCellsAt(int row, int col){
        /*# YOUR CODE HERE */
        int y = (int)(row*CELLs_SIZE + TOP);
        int x = (int)(col*CELLs_SIZE + LEFT);
        	
    		if(row >= 0 && row < ROWS && col >= 0 && col < COLS && 
    			!this.cellss[row][col].hasMine() &&
    			!this.cellss[row][col].isExposed()) {
    			cellsNumber++;
    			this.cellss[row][col].setExposed();
    			this.cellss[row][col].draw(x, y,CELLs_SIZE);
    			//if the select cells does not have a mine around it, then do the recursion
    			if(cellss[row][col].getAdjacentMines() == 0) {
      				exposeCellsAt(row,col-1);
        			exposeCellsAt(row,col+1);
        			exposeCellsAt(row-1,col);
        			exposeCellsAt(row+1,col);
        			exposeCellsAt(row+1,col+1);
        			exposeCellsAt(row-1,col-1);
    			}
    		}	
    }


    /**
     * Mark/unmark the cells.
     * If the cells is exposed, don't do anything,
     * If it is marked, unmark it.
     * otherwise mark it and redraw.
     * (Marking cannot make the player win or lose)
     */
    public void mark(int row, int col){
        /*# YOUR CODE HERE */

        int y = (int)(row*CELLs_SIZE + TOP);
        int x = (int)(col*CELLs_SIZE + LEFT);
        this.cellss[row][col].toggleMark();
    	this.cellss[row][col].draw(x, y, CELLs_SIZE);
    }

    /** 
     * Returns true if the player has won:
     * If all the cellss without a mine have been exposed, then the player has won.
     */
    public boolean hasWon(){
        /*# YOUR CODE HERE */
    	int blankNumber = ROWS*COLS - mineNumber;
    	
    	if(blankNumber == cellsNumber) return true;
    	else return false;
    }


    /**
     * Construct a grid with random mines.
     */
    public void makeGrid(){
        UI.clearGraphics();
        mineNumber = 0;
        cellsNumber = 0;
        grid = new int[ROWS][COLS];  //create an array of integer for AI helper
        this.cellss = new Cells[ROWS][COLS];
        for (int row=0; row < ROWS; row++){
            double y = TOP+row*CELLs_SIZE;
            for (int col=0; col<COLS; col++){
                double x =LEFT+col*CELLs_SIZE;
                boolean isMine = Math.random()<0.1;     // approx 1 in 10 cellss is a mine
                this.cellss[row][col] = new Cells(isMine);
                this.cellss[row][col].draw(x, y, CELLs_SIZE);
                                    
                if(isMine) {
                	mineNumber++; //to calculate the number of mine in a new game
                	grid[row][col] = -1;
                }
            }
        }
        // now compute the number of adjacent mines for each cells
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                int count = 0;
                //look at each cells in the neighbourhood.
                for (int r=Math.max(row-1,0); r<Math.min(row+2, ROWS); r++){
                    for (int c=Math.max(col-1,0); c<Math.min(col+2, COLS); c++){
                        if (cellss[r][c].hasMine())
                            count++;
                    }
                }
                if (this.cellss[row][col].hasMine())
                    count--;  // we weren't suppose to count this cells, just the adjacent ones.

                this.cellss[row][col].setAdjacentMines(count);
            }
        }
    }

    /** Draw a message telling the player they have won */
    public void drawWin(){
        UI.setFontSize(28);
        UI.drawString("You Win!", LEFT + COLS*CELLs_SIZE + 20, TOP + ROWS*CELLs_SIZE/2);
        UI.setFontSize(12);
    }

    /**
     * Draw a message telling the player they have lost
     * and expose all the cellss and redraw them
     */
    public void drawLose(){
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                cellss[row][col].setExposed();
                cellss[row][col].draw(LEFT+col*CELLs_SIZE, TOP+row*CELLs_SIZE, CELLs_SIZE);
            }
        }
        UI.setFontSize(28);
        UI.drawString("You Lose!", LEFT + COLS*CELLs_SIZE+20, TOP + ROWS*CELLs_SIZE/2);
        UI.setFontSize(12);
    }


    // Main
    public static void main(String[] arguments){
        new MineSweeper();
    }        

}
