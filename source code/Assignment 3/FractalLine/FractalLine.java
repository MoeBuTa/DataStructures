import ecs100.*;
import java.util.*;
import java.awt.Color;

public class FractalLine{

    public FractalLine(){
        UI.setMouseListener(this::doMouse);
        UI.addButton("Clear", UI::clearPanes);
        UI.addButton("Quit", UI::quit);
    }

    private double startX, startY;

    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){
            startX = x;
            startY = y;
        }
        else if (action.equals("released")){
            fractalLine(startX, startY, x, y);
        }
    }

    public void fractalLine(double x1, double y1, double x5, double y5){
        if (Math.hypot(x5-x1, y5-y1) < 10){
            UI.setLineWidth(2);
            UI.drawLine(x1, y1, x5, y5);
        }
        else {
            double x2 = (x1+x5)/2;
            double y2 = (y1+y5)/2;
            double x3 = (x1+x5)/2 - (y5-y1)/2;
            double y3 = (y1+y5)/2 + (x5-x1)/2;
            double x4 = x5 - (y5-y1)/2;
            double y4 = y5 + (x5-x1)/2;

            fractalLine(x1,y1,x2,y2);
            fractalLine(x2,y2,x3,y3);
            fractalLine(x3,y3,x4,y4);
            fractalLine(x4,y4,x5,y5);
        }
    }

    
}
