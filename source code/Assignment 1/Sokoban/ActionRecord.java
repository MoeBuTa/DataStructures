// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 1
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

/** 
 * Object containing the record of an action (move or push) in
 * a given direction.
 * Used for the Undo process.
 * Every move or push should put an ActionRecord on the history stack
 * Undo should pop an ActionRecord off the history stack and
 *  undo the recorded action.
 */

public class ActionRecord {
    private final boolean isPush;   // if it is not a "push", it is a "move"
    private final String direction; // direction of the move or push
    public final String action;    //move or push
    public final String Dir;		//direction of the move or push
    public final int x;			//position of the worker
    public final int y;
    /**
     * Constructor
     */
    public ActionRecord(String action, String dir ,int x,int y) {
    	this.action = action;
    	this.Dir = dir;
        this.x = x;
        this.y = y;
    	isPush = (action.equalsIgnoreCase("push"));
        direction = dir;
       
        
    }

    /**
     * Is the recorded action a push?
     */
    public boolean isPush() {
        return isPush;
    }

    /**
     * Is the recorded action a move?
     */
    public boolean isMove() {
        return !isPush;
    }

    /**
     * Return the direction of the recorded action
     */
    public String direction() {
        return direction;
    }

    /**
     * Return a String describing the recorded action
     */
    public String toString() {
        return ((isPush ? "Push" : "Move") + " to " + direction);
    }

}
