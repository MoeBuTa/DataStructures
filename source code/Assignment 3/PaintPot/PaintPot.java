import ecs100.*;
import java.awt.Color;

/**
 * Does a paint fill
 */
public class PaintPot{

    // constants
    public static final int ROWS = 25;
    public static final int COLS = 35;

    public static final int SIZE = 20;
    public static final int LEFT = 10;
    public static final int TOP = 10;

    //fields
    private Color[][] grid = new Color[ROWS][COLS]; // the grid of colours

    private boolean paintMode = true;   // painting mode (true) or pouring mode (false)
    private Color paint = Color.red;
    private int delay = 30;

    /** 
     * Construct a new PaintPot object
     * and set up the GUI
     */
    public PaintPot(){
        setupGUI();
        resetGrid();
    }

    public void setupGUI(){
        UI.setMouseMotionListener(this::doMouse);

        UI.addButton("Paint", ()->{paintMode = true;});
        UI.addButton("Pour", ()->{paintMode = false;});
        UI.addButton("Red", ()->{paint = Color.red;});
        UI.addButton("Yellow", ()->{paint = Color.yellow;});
        UI.addButton("Green", ()->{paint = Color.green;});
        UI.addButton("Blue", ()->{paint = Color.blue;});
        UI.addButton("Orange", ()->{paint = Color.orange;});
        UI.addButton("Clear", this::resetGrid);
        UI.addSlider("Delay", 0,500,delay, (double val)->{delay=(int)val;});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);  // Hide the text area.        
    }

    /** 
     * Respond to mouse events
     */
    public void doMouse(String action, double x, double y){
        int row = (int) ( (y-TOP) / SIZE);   // how many rows down
        int col = (int) ( (x-LEFT) / SIZE);  // how many columns across
        if ((row <0 || row >= ROWS || col < 0 || col >= COLS) ) {
            return;
        }
        if (action.equals("released")) {
            if ( paintMode ){setPixel(row, col, paint);}
            else            {pour(row, col);}
        }
        else if (action.equals("dragged") && paintMode){
            setPixel(row, col, paint);
        }
    }

    /** set (and redraw) a pixel on the grid */
    public void setPixel(int row, int col, Color c){
        grid[row][col] = c;
        int x = LEFT+col*SIZE;
        int y = TOP +row*SIZE;
        UI.setColor(c);
        UI.fillRect(x, y, SIZE, SIZE);
        UI.setColor(Color.lightGray);
        UI.drawRect(x, y, SIZE, SIZE);
    }

    /**
     *  If this pixel is a different color from the current paint colour,
     *  then pour the paint on this pixel, and let it spread out
     *  through all adjacent pixels that are the same colour as this pixel was.
     */
    public void pour(int row, int col){
        Color oldColor = grid[row][col];
        if ( !(oldColor.equals(paint)) )
            spread(row, col, paint, oldColor );
    }

    /*
     * Paint the newColor on this pixel in place of the oldColor
     * and spread out, (recursively) to its neighbours.
     */
    public void spread(int row, int col, Color newColor, Color oldColor){
    	if(row >= 0 && row < ROWS && col >= 0 && col < COLS && grid[row][col].equals(oldColor)) {

        	setPixel(row, col, newColor);
        	spread(row, col-1, newColor, oldColor);
        	spread(row, col+1, newColor, oldColor);
        	spread(row-1, col, newColor, oldColor);
        	spread(row+1, col, newColor, oldColor);
    	}


    }


    /**
     * Set all the pixels to white (redrawing them)
     */
    public void resetGrid(){
        for (int col=0; col<COLS; col++){
            for (int row=0; row<ROWS; row++){
                setPixel(row, col, Color.white);
            }
        }
        /*
         for (int col=0; col<COLS; col++){ 
            setPixel(0,col, Color.black);
            setPixel(ROWS-1,col, Color.black);
        }
         for (int row=0; row<ROWS; row++){ 
            setPixel(row, 0, Color.black);
            setPixel(row, COLS-1, Color.black);
        }
        */
    }

        // Main
        public static void main(String[] arguments){
        new PaintPot();
    }   
}














/*
if (row >= 0 && row < ROWS && col >= 0 && col < COLS &&
grid[row][col].equals(oldColor) ){
setPixel(row, col, newColor);
UI.sleep(delay);

spread(row-1, col, oldColor, newColor);
spread(row, col+1, oldColor, newColor);
spread(row+1, col, oldColor, newColor);
spread(row, col-1, oldColor, newColor);

 */
