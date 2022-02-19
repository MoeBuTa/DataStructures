/* Code for COMP103 Lecture
 * Name: Pondy
 */
import ecs100.*;

/**
 * BrickAction
 * Records information about an add, move, or delete action
 * in the BrickConstructor
 */

public class BrickAction{

    private String action; // "add", "move", or "delete"
    private Brick brick;  // the brick that was operated on
    private double x, y;  // the position the brick was moved from.

    /**
     * Construct a new BrickAction object with just the brick
     */
    public BrickAction(String action, Brick brick){
        this.action = action;
        this.brick = brick;
        Trace.println(action+ "brick at" + brick.getX()+","+brick.getY());
    }

    /**
     * Construct a new BrickAction object with a brick and previous position
     */
    public BrickAction(String action, Brick brick, double x, double y){
        this.action = action;
        this.brick = brick;
        this.x = x;
        this.y = y;
        Trace.println(action+ "brick at" + brick.getX()+","+brick.getY()+" from "+x+","+y);
    }

    //getters

    /** Return the value of the action field */
    public String getAction(){return action;}

    /** Return the value of the brick field */
    public Brick getBrick(){return brick;}

    /** Return the value of the x field */
    public double getX(){return x;}

    /** Return the value of the y field */
    public double getY(){return y;}

}
