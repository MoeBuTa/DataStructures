// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 1
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/** 
 * Sokoban
 */

public class Sokoban {

    private Cell[][] cells;             // the array representing the warehouse
    private int rows;                   // the height of the warehouse
    private int cols;                   // the width of the warehouse
    private int level = 1;              // current level 

    private Position workerPos;         // the position of the worker
    private String workerDir = "left";  // the direction the worker is facing

    private Stack<ActionRecord> undoStack = new Stack<ActionRecord>();   
    private Stack<ActionRecord> redoStack = new Stack<ActionRecord>(); 
    
    /** 
     *  Constructor: set up the GUI, and load the 0th level.
     */
    public Sokoban() {
        setupGUI();
        doLoad();
    }

    /**
     * Add the buttons and set the key listener.
     */
    public void setupGUI(){
        UI.addButton("New Level", () -> {level++; doLoad();});
        UI.addButton("Restart",   this::doLoad);
        UI.addButton("left",      () -> {moveOrPush("left");});
        UI.addButton("up",        () -> {moveOrPush("up");});
        UI.addButton("down",      () -> {moveOrPush("down");});
        UI.addButton("right",     () -> {moveOrPush("right");});
        UI.addButton("undo",      this::undo);
        UI.addButton("redo",      this::redo);
        UI.addButton("Quit",      UI::quit);

        UI.setKeyListener(this::doKey);
        UI.setDivider(0.0);
    }
    
    
 
    
    /** 
     * Respond to key actions
     */
    public void doKey(String key) {
        key = key.toLowerCase();
        if (key.equals("i")|| key.equals("w") ||key.equals("up")) {
            moveOrPush("up");
        }
        else if (key.equals("k")|| key.equals("s") ||key.equals("down")) {
            moveOrPush("down");
        }
        else if (key.equals("j")|| key.equals("a") ||key.equals("left")) {
            moveOrPush("left");
        }
        else if (key.equals("l")|| key.equals("d") ||key.equals("right")) {
            moveOrPush("right");
        }
    }
    /*
     * undo the action on the top of the undo stack
     */
    public void undo() {
    	if(undoStack.empty()) return;
    	redoStack.push(undoStack.peek());  	          //put the ActionRecord onto a redo stack
    	
    	ActionRecord actionRec = undoStack.pop();
    	if(actionRec.action.equals("push")) {           
    		
        	workerDir = actionRec.Dir;
        	workerPos = new Position(actionRec.x,actionRec.y);  //the position where the worker at    		
        	pull(actionRec.Dir);
    	}
    		
   	
    	if(actionRec.action.equals("move")) {
   		
    		drawCell(workerPos);                   // redisplay cell under worker
        	workerDir = actionRec.Dir;
        	workerPos = new Position(actionRec.x,actionRec.y);  //the position where worker in  before
        	drawWorker();                          // display worker at original position	  

    			  
        	}
    }
    /*
     * redo the action on the top of the redo stack
     */
    public void redo() {
      	if(redoStack.empty()) return;
      	undoStack.push(redoStack.peek());			//put the ActionRecord onto undo stack
      	
      	ActionRecord actionRec = redoStack.pop();
      	
      	if(actionRec.action.equals("push")) {
      		push(actionRec.Dir);
      	}
                	
 	
    	if(actionRec.action.equals("move")) {
    		
    		drawCell(workerPos);                   // redisplay cell under worker
    		workerDir = actionRec.Dir;
    		workerPos = workerPos.next(actionRec.Dir);  //the position where worker in  before
    		drawWorker();                          // display worker at original position	  

    			  
        	}
    }
    
    
    
    
    /** 
     *  Moves the worker in the given direction, if possible.
     *  If there is box in front of the Worker and a space in front of the box,
     *  then push the box.
     *  Otherwise, if the worker can't move, do nothing.
     */
    public void moveOrPush(String direction) {
        redoStack.clear();							//If the player don't click the "undo" button, the player can't do "redo" move.
        											//so clear the redo stack when the player control the worker to move or push.
        
      
    	workerDir = direction;                       // turn worker to face in this direction  
        
        Position nextP = workerPos.next(direction);  // where the worker would move to
        
        Position nextNextP = nextP.next(direction);  // where a box would be pushed to

        // is there a box in that direction which can be pushed?
        if ( cells[nextP.row][nextP.col].hasBox() &&
             cells[nextNextP.row][nextNextP.col].isFree() ) { 
        	undoStack.push( new ActionRecord("push", direction, workerPos.row, workerPos.col));
        	push(direction);
            
            
            
            if (isSolved()) { reportWin(); }
        }
        // is the next cell free for the worker to move into?
        else if ( cells[nextP.row][nextP.col].isFree() ) { 
        	undoStack.push( new ActionRecord("move", direction, workerPos.row, workerPos.col));
        	move(direction);
          
           
        }

        
        
    }

    /**
     * Moves the worker into the new position (guaranteed to be empty) 
     * @param direction the direction the worker is heading
     */
    public void move(String direction) {
        drawCell(workerPos);                   // redisplay cell under worker
        workerPos = workerPos.next(direction); // put worker in new position
        drawWorker();                          // display worker at new position
        
        
        Trace.println("Move " + direction);    // for debugging
    }

    
    /**
     * Push: Moves the Worker, pushing the box one step 
     *  @param direction the direction the worker is heading
     */
    public void push(String direction) {
        Position boxPos = workerPos.next(direction);   // where box is
        Position newBoxPos = boxPos.next(direction);   // where box will go

        cells[boxPos.row][boxPos.col].removeBox();     // remove box from current cell
        cells[newBoxPos.row][newBoxPos.col].addBox();  // place box in its new position

        drawCell(workerPos);                           // redisplay cell under worker
        drawCell(boxPos);                              // redisplay cell without the box
        drawCell(newBoxPos);                           // redisplay cell with the box
     
        workerPos = boxPos;                            // put worker in new position
        drawWorker();                                  // display worker at new position
        
        
        Trace.println("Push " + direction);   // for debugging
    }
    

    /**
     * Pull: (could be useful for undoing a push)
     *  move the Worker in the direction,
     *  pull the box into the Worker's old position
     */
    public void pull(String direction) {
        /*# YOUR CODE HERE */
    	Position oldBoxPos = workerPos.next(direction);		//the original pushed box's position
    	Position newBoxPos = oldBoxPos.next(direction);		//the pushed box's position
    	
    	cells[newBoxPos.row][newBoxPos.col].removeBox();     // remove box from current cell
        cells[oldBoxPos.row][oldBoxPos.col].addBox();  // place box in its new position
        drawCell(workerPos);                           // redisplay cell under worker
        drawCell(newBoxPos);                              // redisplay cell without the box
        drawCell(oldBoxPos);                           // redisplay cell with the box
        
       
        drawWorker();                                  // display worker at new position
    }


    /**
     * Report a win by flickering the cells with boxes
     */
    public void reportWin(){
        for (int i=0; i<12; i++) {
            for (int row=0; row<cells.length; row++)
                for (int column=0; column<cells[row].length; column++) {
                    Cell cell=cells[row][column];

                    // toggle shelf cells
                    if (cell.hasBox()) {
                        cell.removeBox();
                        drawCell(row, column);
                    }
                    else if (cell.isEmptyShelf()) {
                        cell.addBox();
                        drawCell(row, column);
                    }
                }

            UI.sleep(100);
        }
    }
    
    /** 
     *  Returns true if the warehouse is solved, 
     *  i.e., all the shelves have boxes on them 
     */
    public boolean isSolved() {
        for(int row = 0; row<cells.length; row++) {
            for(int col = 0; col<cells[row].length; col++)
                if(cells[row][col].isEmptyShelf())
                    return  false;
        }

        return true;
    }

    /** 
     * Returns the direction that is opposite of the parameter
     * useful for undoing!
     */
    public String opposite(String direction) {
        if ( direction.equals("right")) return "left";
        if ( direction.equals("left"))  return "right";
        if ( direction.equals("up"))    return "down";
        if ( direction.equals("down"))  return "up";
        throw new RuntimeException("Invalid  direction");
    }





    // Drawing the warehouse

    private static final int LEFT_MARGIN = 40;
    private static final int TOP_MARGIN = 40;
    private static final int CELL_SIZE = 25;

    /**
     * Draw the grid of cells on the screen, and the Worker 
     */
    public void drawWarehouse() {
        UI.clearGraphics();
        // draw cells
        for(int row = 0; row<cells.length; row++)
            for(int col = 0; col<cells[row].length; col++)
                drawCell(row, col);

        drawWorker();
    }

    /**
     * Draw the cell at a given position
     */
    private void drawCell(Position pos) {
        drawCell(pos.row, pos.col);
    }

    /**
     * Draw the cell at a given row,col
     */
    private void drawCell(int row, int col) {
        double left = LEFT_MARGIN+(CELL_SIZE* col);
        double top = TOP_MARGIN+(CELL_SIZE* row);
        cells[row][col].draw(left, top, CELL_SIZE);
    }

    /**
     * Draw the worker at its current position.
     */
    private void drawWorker() {
        double left = LEFT_MARGIN+(CELL_SIZE* workerPos.col);
        double top = TOP_MARGIN+(CELL_SIZE* workerPos.row);
        UI.drawImage("worker-"+workerDir+".gif",
                     left, top, CELL_SIZE,CELL_SIZE);
    }



    /**
     * Load a grid of cells (and Worker position) for the current level from a file
     */
    public void doLoad() {
        File f = new File("warehouse" + level + ".txt");

        if (! f.exists()) {
            UI.printMessage("Run out of levels!");
            level--;
        }
        else {
        	undoStack.clear();			//if the player do a new level,then clear the undo stack and redo stack
        	redoStack.clear();
            List<String> lines = new ArrayList<String>();
            try {
                Scanner sc = new Scanner(f);
                while (sc.hasNext()){
                    lines.add(sc.nextLine());
                }
                sc.close();
            } catch(IOException e) {UI.println("File error: " + e);}

            int rows = lines.size();
            cells = new Cell[rows][];

            for(int row = 0; row < rows; row++) {
                String line = lines.get(row);
                int cols = line.length();
                cells[row]= new Cell[cols];
                for(int col = 0; col < cols; col++) {
                    char ch = line.charAt(col);
                    if (ch=='w'){
                        cells[row][col] = new Cell("empty");
                        workerPos = new Position(row,col);
                    }
                    else if (ch=='.') cells[row][col] = new Cell("empty");
                    else if (ch=='#') cells[row][col] = new Cell("wall");
                    else if (ch=='s') cells[row][col] = new Cell("shelf");
                    else if (ch=='b') cells[row][col] = new Cell("box");
                    else {
                        throw new RuntimeException("Invalid char at "+row+","+col+"="+ch);
                    }
                }
            }
            drawWarehouse();
            UI.printMessage("Level "+level+": Push the boxes to their target positions. Use buttons or put mouse over warehouse and use keys (arrows, wasd, ijkl, u)");
        }
    }

    public static void main(String[] args) {
        new Sokoban();
    }
}
