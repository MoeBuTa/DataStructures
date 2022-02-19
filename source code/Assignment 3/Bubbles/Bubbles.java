import ecs100.*;
import java.util.*;
import java.awt.Color;

public class Bubbles{

    public Bubbles(){
        UI.addButton("Bubbles1", ()->{drawBubbles1(100, 50, 20);});
        UI.addButton("Bubbles2", ()->{drawBubbles2(200, 50, 20);});
        UI.addButton("Bubbles3", ()->{drawBubbles3(300, 50, 20);});
        UI.addButton("Clear", UI::clearPanes);
        UI.addButton("Quit", UI::quit);
    }

    public void drawBubble(double x, double y, double diam){
        UI.setColor(Color.blue);
        UI.fillOval(x-diam/2, y-diam/2, diam, diam);
        UI.setColor(Color.black);
        UI.drawOval(x-diam/2, y-diam/2, diam, diam);
        UI.sleep(200);
    }

    public void drawBubbles1(double x, double y, int n){
        for (int i = 0; i<n; i++ ) {
            this.drawBubble(x, y, 15);
            y = y + 20;
        }
    }

    public void drawBubbles2(double x, double y, int n){
        this.drawBubble(x, y, 15);	
        if ( n > 1 ) {
            this.drawBubbles2(x, y+20, n-1);
        }
    }
/** draw n bubbles, starting at position (x, y), step down 20 units between bubbles */
    public void drawBubbles3(double x, double y, int n){
        if ( n == 1 )   {
            this.drawBubble(x, y, 15);
        } 
        else if ( n > 1 )   {
            double halfway =y+20*(n/2);
            this.drawBubbles3(x, halfway, (n - n/2));
            this.drawBubbles3(x, y, n/2);	
        }
    }
    
    public static void main(String[] args){
        new Bubbles();
    }
}
