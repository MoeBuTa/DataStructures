// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 6
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import ecs100.UI;
import java.awt.*;
import java.util.*;

/**
 * Search for a path to the goal in a maze.
 * The maze consists of a graph of Cells:
 *  Each cell has a collection of neighbouring cells.
 *  Each cell can be "visited" and it will remember that it has been visited
 *  A Cell is Iterable, so that you can iterate through its neighbour cells with
 *    for(Cell neigh : cell){....
 *
 * The maze has a goal cell (in the bottom right corner)
 * The user can click on a cell, and the program will search for a path
 * from that cell to the goal.
 * 
 * Every cell that is looked at during the search is coloured  yellow, and then,
 * if the cell turns out to be on a dead end, it is coloured red.
 */

public class MazeSearch {

    public static final int DELAY = 20;

    private Maze maze;
    private String search = "first";   // "first", "all", or "shortest"
    private int pathCount = 0;
    
    boolean flag = false;
    
    Queue<Cell> queue = new ArrayDeque<Cell>();
    
    public MazeSearch() {
        setupGui();
        makeMaze(10);
    }
        
    public void setupGui(){
        UI.addTextField("Maze Size", (String v)->{makeMaze(Integer.parseInt(v));});
        UI.setMouseListener((action, x, y) -> {doMouse(action, x, y);});
        UI.addButton("First path",    ()->{search="first";});
        UI.addButton("All paths",     ()->{search="all";});
        UI.addButton("Shortest path", ()->{search="shortest";});
        UI.addButton("Stop", ()->{flag = true;});
        UI.addButton("Quit", UI::quit);
    }

    /**
     * Creates a new maze and draws it .
     */
    public void makeMaze(int size){
        maze = new Maze(size);
        maze.draw();
    }

    /**
     * Clicking the mouse on a cell should make the program
     * search for a path from the clicked cell to the goal.
     * @throws Exception 
     */
    public void doMouse(String action, double x, double y) {
        if (action.equals("released")){
            UI.clearText();
            maze.reset();
            maze.draw();
            pathCount = 0;
            Cell start = maze.getCellAt(x, y);
            if (search=="first"){
                exploreCell(start);
            }
            else if (search=="all"){
                exploreCellAll(start);
            }
            if (search=="shortest"){
                exploreCellShortest(start);
            }
        }
    }

    /**
     * Search for a path from a cell to the goal.
     * Return true if we got to the goal via this cell (and don't
     *  search for any more paths).
     * Return false if there is not a path via this cell.
     * 
     * If the cell is the goal, then we have found a path - return true.
     * If the cell is already visited, then abandon this path - return false.
     * Otherwise,
     *  Mark the cell as visited, and colour it yellow (and sleep for a short time)
     *  Recursively try exploring from the cell's neighbouring cells, returning true
     *   if a neighbour leads to the goal
     *  If no neighbour leads to a goal,
     *    colour the cell red (to signal failure)
     *    abandon the path - return false.
     */
    public boolean exploreCell(Cell cell) {
        if (cell == maze.getGoal()) {
            cell.draw(Color.blue);   // to indicate finding the goal
            return true;
        }
        /*# YOUR CODE HERE */
        
        boolean signal=false;
        if(cell.isVisited()) {
        	return false; //abandon this path if the cell is already visited
        }
        
        else {
        	cell.visit();
        	cell.draw(Color.yellow);
        	UI.sleep(20);
        	for(Cell neigh : cell) {
        		signal = exploreCell(neigh);
        		if(signal == true) {return true;}	//return true if a neighbour leads to the goal
        	}       	
        	if(signal == false) {					//return false if no neighbour leads to the goal
        		cell.draw(Color.red);
        		return false;
        	}
        }
        return false;

    }


    /** COMPLETION
     * Search for all paths from a cell,
     * If we reach the goal, then we have found a complete path,
     *  so pause for 1 second
     * Otherwise,
     *  visit the cell, and colour it yellow
     *  Recursively explore from the cell's neighbours, 
     *  unvisit the cell and colour it white.
     * 
     */
    public void exploreCellAll(Cell cell) {
        /*# YOUR CODE HERE */
    	
    	if(flag) {
			try {
				throw new Exception();// stop the recursion.
			} catch(Exception e) {
				while(true) {
					if(flag == false) {
						return;
					}
				}
			  } 
    	}
    	UI.printMessage(pathCount + " paths");

    	if(cell == maze.getGoal()) {   //If we reach the goal, then we have found a complete path 		
    		cell.draw(Color.blue);
    		UI.sleep(500);
    		cell.draw(Color.green);
    		pathCount++;
    		return;
    	}
    	
    	else if(!cell.isVisited()){	//Recursively explore from the cell's neighbours
        	cell.visit();
        	cell.draw(Color.yellow);
        	UI.sleep(10);
    		for(Cell neigh : cell) {
    			exploreCellAll(neigh);
    		}
    		cell.unvisit();			//unvisit the cell and colour it white
    		cell.draw(Color.white);
    	}
   	
    }

    
    /** CHALLENGE
     * Search for shortest path from a cell,
     * Use Breadth first search.
     */
    public void exploreCellShortest(Cell start) {
        /*# YOUR CODE HERE */
    	Queue<Cell> queue = new ArrayDeque<Cell>(); //create a queue to do the Breadth first search for the whole graph
    	Map<Cell,Cell> map = new HashMap<Cell,Cell>(); //create a map to store the path by associating cells
    	queue.offer(start);
    	while(!queue.isEmpty()){
    		Cell cell = queue.poll();   		
    		if(cell == maze.getGoal()) {
    			cell.draw(Color.blue);
    			while(map.containsKey(cell)) {
    				Cell pre = map.get(cell);   				
        			pre.draw(Color.yellow);
        			cell = map.get(pre);
    				cell.draw(Color.yellow);
    			}	
    			return;
    		}
    		cell.visit();
        	for(Cell c : cell) {	
        		if(!c.isVisited()) {
        			queue.offer(c);
        			map.put(c, cell);
        		}
        	}
    	}

    	
    }


    public static void main(String[] args) {
        new MazeSearch();
    }
}

