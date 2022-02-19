/* Code for COMP103 Lecture
 * Name: Pondy
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import javax.swing.JColorChooser;

/**
 * BrickConstructor
 * Allows user to edit a collection of bricks on the screen
 * User can add bricks, move bricks, remove bricks,
 * User can also change the color and restart
*/

public class BrickConstructor{

    private Collection<Brick> bricks = new HashSet<Brick>();  // the Bricks
    private Color currentColor = Color.red;

    private Brick pressedBrick; // Brick (if any) the user pressed on

    
    /**
     * Construct a new BrickConstructor object
     */
    public BrickConstructor(){
        setupGUI();
    }

    /**
     * Initialise the interface
     */
    public void setupGUI(){
        UI.addButton("Clear", this::clear);
        UI.addButton("Set Color", this::setColor);
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
            }
            else if (pressedBrick!=null&&releasedBrick==null){
                // move brick
                pressedBrick.moveTo(x,y);
            }
            else if (pressedBrick==releasedBrick){
                // delete brick
                bricks.remove(pressedBrick);
            }
            redraw();
        }
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
       BrickConstructor obj = new BrickConstructor();
    }	


}
