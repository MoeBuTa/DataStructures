import ecs100.*;
import java.util.*;
import java.awt.Color;

/** Draws a Sierpinski triangle of various "depths"
 */
public class Sierpinski {
    private int depth = 7;
    private int delay = 125;
    /** Construct a new Sierpinski object
     * and set up the GUI
     */
    public Sierpinski(){
        UI.addButton("Clear", UI::clearGraphics);
        UI.addButton("ST 1", ()->{doDraw(1);});
        UI.addButton("ST 2", ()->{doDraw(2);});
        UI.addButton("ST 3", ()->{doDraw(3);});
        UI.addButton("ST 4", ()->{doDraw(4);});
        UI.addButton("ST 5", ()->{doDraw(5);});
        UI.addButton("ST 6", ()->{doDraw(6);});
        UI.addButton("ST 7", ()->{doDraw(7);});
        UI.addButton("ST 8", ()->{doDraw(8);});
        UI.addButton("ST 9", ()->{doDraw(9);});
        UI.addSlider("slow", 1,10,6, (double v)->{UI.clearGraphics();drawSlow(10,10, 512, 512, (int)v);});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(600,600);
        UI.setDivider(0.0);

    }

    public void doDraw(int i){
        UI.clearGraphics();
        depth = i;
        delay = 2000 >> depth + 1;
        draw(10, 10, 512, 512, depth);
    }

    public void draw(int x, int y, int w, int h, int depth){
        if (depth == 0){
            UI.setColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
            UI.fillRect(x, y, w-1, h-1);
        }
        else{
            draw(x,       y,       w/2, h/2, depth - 1);
            draw(x,       y + h/2, w/2, h/2, depth - 1);
            draw(x + w/2, y,       w/2, h/2, depth - 1);
        }
    }

    public void drawSlow(int x, int y, int w, int h, int depth){
        UI.setColor(Color.lightGray);
        UI.drawRect(x, y, w-1, h-1);
        if (depth == 0){
            UI.setColor(Color.gray);
            UI.drawRect(x, y, w-1, h-1);
            UI.setColor(Color.black);
            UI.fillRect(x+1, y+1, w-2, h-2);
            UI.sleep(delay);
        }
        else{
            this.drawSlow(x,       y,       w/2, h/2, depth - 1);
            this.drawSlow(x,       y + h/2, w/2, h/2, depth - 1);
            this.drawSlow(x + w/2, y,       w/2, h/2, depth - 1);
        }
    }

    // Main
    public static void main(String[] arguments){
        new Sierpinski();
    }	

}
