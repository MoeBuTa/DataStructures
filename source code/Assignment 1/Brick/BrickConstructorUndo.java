/* Code for COMP103 Lecture
 * Name: Pondy
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import javax.swing.JColorChooser;

/**
 * BrickConstructorUndo
 * Allows user to edit a collection of bricks on the screen
 * User can add bricks, move bricks, remove bricks,
 * User can also change the color and restart
 * Allows user to undo their actions
*/

public class BrickConstructorUndo{

    private Collection<Brick> bricks = new HashSet<Brick>();  // the Bricks
    private Color currentColor = Color.red;

    private Brick pressedBrick; // Brick (if any) the user pressed on
                                
    private Stack<BrickAction> undoStack = new Stack<BrickAction>(); //***UNDO***
    
    /**
     * Construct a new BrickConstructor object
     */
    public BrickConstructorUndo(){
        setupGUI();
    }

    /**
     * Initialise the interface
     */
    public void setupGUI(){
        UI.addButton("Clear", this::clear);
        UI.addButton("Set Color", this::setColor);
        UI.addButton("Undo", this::undo);             //***UNDO***
        UI.addButton("Quit", UI::quit);
        UI.setMouseListener(this::doMouse);
        UI.setDivider(0.0);
    }

    /**
     * Clear the collection of bricks and clear the window
     */
    public void clear(){
        bricks.clear();
        redraw();
    }

    /**
     * Set the current color.
     */
    public void setColor(){
        Color col=JColorChooser.showDialog(UI.getFrame(), "", currentColor);
        if (col!=null) {
            currentColor=col;
        }
    }
    
    /**
     * Respond to the mouse:
     *  add brick if pressed and released on empty space
     *  move brick if pressed on a brick and released on space
     *  delete brick if pressed and released on the same brick
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){
            pressedBrick = findBrickAt(x, y);
        }
        else if (action.equals("released")){
            Brick releasedBrick = findBrickAt(x, y);
            if (pressedBrick==null && releasedBrick==null){
                //add new brick
                Brick newBrick = new Brick(x, y, currentColor);
                bricks.add(newBrick);
                undoStack.push(new BrickAction("add", newBrick));   //***UNDO***
            }
            else if (pressedBrick!=null&&releasedBrick==null){
                // move brick
                double oldx = pressedBrick.getX();   //***UNDO***
                double oldy = pressedBrick.getY();   //***UNDO***
                undoStack.push(new BrickAction("move", pressedBrick, oldx, oldy));   //***UNDO***
                pressedBrick.moveTo(x,y);
            }
            else if (pressedBrick==releasedBrick){
                // delete brick
                bricks.remove(pressedBrick);
                undoStack.push(new BrickAction("delete", pressedBrick));   //***UNDO***
            }
            redraw();
        }
    }

   //***UNDO***
    /**
     * Undo the next earlier action 
     */
    public void undo(){
        if (undoStack.isEmpty()){
            UI.printMessage("No more actions to undo");
            return;
        }
        BrickAction act = undoStack.pop();
        UI.printMessage("Undoing "+act.getAction());
        if (act.getAction().equals("add")){
            bricks.remove(act.getBrick());
        }
        else if (act.getAction().equals("delete")){
            bricks.add(act.getBrick());
        }
        else if (act.getAction().equals("move")){
            act.getBrick().moveTo(act.getX(), act.getY());
        }
        redraw();
    }

    /**
     * Find (and return) a brick that is over the position (x,y)
     * return null if (x,y) is not over a brick
     */
    public Brick findBrickAt(double x, double y){
        for (Brick brick : bricks){
            if (brick.on(x,y)){
                return brick;
            }
        }
        return null;
    }

    /**
     * Redraw the collection of bricks.
     */
    public void redraw(){
        UI.clearGraphics();
        for (Brick brick : bricks){
            brick.draw();
        }
    }
        

    public static void main(String[] arguments){
        new BrickConstructorUndo();
    }	


}
