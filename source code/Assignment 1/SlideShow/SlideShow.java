// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 1
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import java.util.*;
import ecs100.*;
import java.awt.Color;


/**
 * This class contains the main method of the program. 
 * 
 * A SlideShow object represents the slideshow application and sets up the buttons in the UI. 
 * 
 * @author pondy
 */
public class SlideShow {

    public static final double LARGE_SIZE = 450;   // size of images during slide show
    public static final double SMALL_SIZE = 100;   // size of images when editing list
    public static final int GAP = 10;           // gap between images when editing
    public static final int COLUMNS = 6;        // Number of columns of thumbnails
    public static int imageNum = 0;   			//index of all displayed image
  
    public static int temp = 0;					//use for transfer function
    public static int trans = 0;


    private int x;		//position of currently selected image
    private int y;
    

    private List<String> images = new ArrayList<String>(); //  List of image file names. 
    
    private static int currentImage = 0;     // index of currently selected image.
                                       // Should always be a valid index if there are any images

    private boolean showRunning;      // flag signalling whether the slideshow is running or not

    
    /**
     * Constructor 
     */
    public SlideShow() {
        /*# YOUR CODE HERE */
    	 setupGUI();

    	

    	 
    }

    /**
     * Initialises the UI window, and sets up the buttons. 
     */
    public void setupGUI() {
        UI.initialise();

        UI.addButton("Run show",   this::runShow);
        UI.addButton("Edit show",    this::editShow);
        UI.addButton("add before",   this::addBefore);
        UI.addButton("add after",    this::addAfter);
        UI.addButton("move left",      this:: moveLeft);
        UI.addButton("move right",     this:: moveRight);
        UI.addButton("move to start",  this:: moveStart);
        UI.addButton("move to end",    this:: moveEnd);
        UI.addButton("remove",       this::remove);
        UI.addButton("remove all",   this::removeAll);
        UI.addButton("reverse",      this::reverse);
        UI.addButton("shuffle",      this::shuffle);
        UI.addButton("Testing",      this::setTestList);
        UI.addButton("Quit",         UI::quit);

        UI.setKeyListener(this::doKey);
        UI.setMouseListener(this::doMouse);
        UI.setDivider(0);
        UI.printMessage("Mouse must be over graphics pane to use the keys");

    }


    // RUNNING

    /**
     * As long as the show isn't already running, and there are some
     * images to show, start the show running from the currently selected image.
     * The show should keep running indefinitely, as long as the
     * showRunning field is still true.
     * Cycles through the images, going back to the start when it gets to the end.
     * The currentImage field should always contain the index of the current image.
     */
    public void runShow(){
        /*# YOUR CODE HERE */
    	if(currentImage == -1) return;
    	UI.clearGraphics();
    	showRunning = true;
    	
    	while(showRunning){
    		if(currentImage >= images.size()) {
    			currentImage = 0;
    		}
    		imagePos(currentImage);
    		UI.drawImage(images.get(currentImage), 0, 0, LARGE_SIZE, LARGE_SIZE);
    		temp = currentImage;
    		currentImage++; 
  
    		UI.sleep(1000);
    	}
    }

    /**
     * Stop the show by changing showRunning to false.
     * Redisplay the list of images, so they can be edited
     */
    public void editShow(){
        /*# YOUR CODE HERE */

       	UI.clearGraphics();
    	showRunning = false;
    	
    	
    	display();
    	imagePos(temp);
		chooseImage(x,y);
    	currentImage = temp;

    }
  
 

    /**
     * Display just the current slide if the show is running.
     * If the show is not running, display the list of images
     * (as thumbnails) highlighting the current image
     */
    public void display(){        
    	/*# YOUR CODE HERE */
	   	imageNum = 0;

		while(imageNum < images.size()) {
			
			imagePos(imageNum);
			UI.drawImage(images.get(imageNum), x, y, SMALL_SIZE, SMALL_SIZE);        			
			imageNum++;        			
		}
		
	    	

    }
    

    // Other Methods (you will need quite a lot of additional methods).
    /*
     * do mouse methods for users to choose images by clicking the mouse and change the position of the image
     */
    public void doMouse(String action, double a, double b){
    	    	
    	if (action.equals("released")){    		
    		  
    		trans = currentImage;
    		
    		selectPos(a, b);
    		
    		if(currentImage < images.size()) {            //for preventing bugs
    			imagePos(currentImage);
				chooseImage(x,y); 
    			    	 		
    			if(trans >= 0) { 
    				UI.clearGraphics();   
    				Collections.swap(images,trans, currentImage); 		//swap the selected image position to another clicked image 
    				display();
    				currentImage = -1;
    				return;
    				
    			}
    			else {
    				UI.clearGraphics();      			
    				display();
    				
    				imagePos(currentImage);
    				chooseImage(x,y);  

    			}
    			
    			
    		}
    		else {													
    			UI.clearGraphics();      			
				display();
				currentImage = -1;
				return;
    		}
    	}
    	
    }
    /*
     * get images position for users to choose
     */
    public void imagePos(int temp) {   
    	int row = 0;
    	int col = 0;
    	if(temp == 0){
    		col = 0;
    		row = 0;
    		x = 10;
        	y = 10;
    	}
    	else {
    		row = temp/COLUMNS;
        	col = temp%COLUMNS;
       	
        	x = (col+1)*10+(100*col);		
        	y = (row+1)*10+(100*row);
    	}
    }
    
    
    /*
     * get the index of  the current clicked image by mouse
     */
    public void selectPos(double a, double b) {
    	
    	int c =(int)b/110;
    	c*=6;
    	currentImage = c + (int)a/110;
    	
    	
    }
    /*
     * add an image from files before the selected image
     */
    public void addBefore(){
        /*# YOUR CODE HERE */
    	if (showRunning) return;
    		String imgFileName = UIFileChooser.open("Choose an image file");
    		if(imgFileName == null) return;
    		UI.clearGraphics();
    			
            images.add(currentImage, imgFileName);
            display();	
            imagePos(currentImage);
            chooseImage(x,y);            		


    }
    
    
    /*
     * add an image from files after the selected image 
     */
    public void addAfter(){
    	if (showRunning) return;
    	String imgFileName = UIFileChooser.open("Choose an image file");
    	if(imgFileName == null) return;
    	UI.clearGraphics();   	
    	if(currentImage >= images.size()) {    		
    		images.add(imgFileName);
    		display();
        	imagePos(currentImage);
    		chooseImage(x,y);
    	}
    	else { 
    		images.add(currentImage+1, imgFileName);
    		display();
    		imagePos(currentImage);
    		chooseImage(x,y);
    	}
    }
    
    /*
     * let the selected image's position move to left 
     */
    public void moveLeft(){
    	if (showRunning) return;
    	if(currentImage <= 0) return;    
    	else {
    		if(currentImage > images.size()-1) return;
    		else {
    			UI.clearGraphics();        	
           		images.add(currentImage-1, images.get(currentImage));
    			images.remove(currentImage+1);
    		
    			display();
    			imagePos(currentImage-1);
    			chooseImage(x,y);
    			currentImage--;
    		}
    	}
    }
    
    
    /*
     * let the selected image's position move to right
     */
    public void moveRight(){
    	if (showRunning) return;
    	if(currentImage >= images.size()-1) return;
    	else {
    		if(currentImage < 0) return;
    		UI.clearGraphics();
    		images.add(currentImage+2, images.get(currentImage));
    		images.remove(currentImage);
    		
    		display();
    		
    		imagePos(currentImage+1);
    		chooseImage(x,y);
    		currentImage++;
    	
    	}
    }
    
    /*
     * let the selected image's position move to start
     */
    public void moveStart(){
    	if (showRunning) return;
    	if(currentImage <= 0) {
    		
    	}
    	else {
    		UI.clearGraphics();
    		images.add(0, images.get(currentImage));
    		images.remove(currentImage+1);
    		
    		display();
    		imagePos(0);
    		chooseImage(x,y);
    		currentImage = 0;
    	}
    }
    
    
    /*
     * let the selected image's position move to end
     */
    public void moveEnd(){
    	if (showRunning) return;
    	if(currentImage >= images.size()-1) {    		
    	}
    	else {
    		UI.clearGraphics();
    		images.add(images.size(), images.get(currentImage));
    		images.remove(currentImage);
    		display();
    		imagePos(images.size()-1);
    		chooseImage(x,y);
    		currentImage = images.size()-1;
    	
    	}
    }
    /*
     * remove the selected image
     */
    public void remove(){
    	if (showRunning) return;
    	
    	
    	if(currentImage < images.size()) {
    		UI.clearGraphics();
    		images.remove(currentImage);    	
    		display();    	
    		imagePos(currentImage);
    		chooseImage(x,y);
    	
    	}
    	else return;
    
    
    }
    
    
    /*
     * remove all the images on screen
     */
    public void removeAll(){
    	if (showRunning) return;
       	UI.clearGraphics();
    	images.removeAll(images);
    	currentImage = 0;
    	
    }
    
    
    /*
     * reverse all the images on screen to the original order
     */
    public void reverse(){
    	if (showRunning) return;
       	UI.clearGraphics();
    	Collections.sort(images);   	
    	display();
    	if(currentImage == 0) return;
    } 
    
    /*
     * shuffle all the images on screen
     */
    public void shuffle(){
    	if (showRunning) return;
       	UI.clearGraphics();
    	Collections.shuffle(images);    	    	
     	display();
     	if(currentImage == 0) return;
     	
    }
    


    /*
     * highlight the selected image in a 10 width red outline of a rectangle 
     */
    public void chooseImage(double c, double d) {
    	UI.setColor(Color.red);
    	UI.setLineWidth(10);
    	UI.drawRect(c, d, SMALL_SIZE, SMALL_SIZE);
   
    }

        
    // More methods for the user interface: keys (and mouse, for challenge)
    /**
     * Interprets key presses.
     * works in both editing the list and in the slide show.
     */  
    public void doKey(String key) {
        if (key.equals("a"))         goLeft(); 
        else if (key.equals("d"))   goRight();
        else if (key.equals("w"))	goup();
        else if (key.equals("s"))	godown();
        else if (key.equals("Home"))    goStart();
        else if (key.equals("End"))     goEnd();
    }
     
    //use "Home" key to let the use select the first image on screen
    public void goStart() {
    	if (showRunning) return;
    	
    	UI.clearGraphics();
    	display();
    	imagePos(0);
    	chooseImage(x,y);	
    	currentImage = 0;
	}
    //use "End" key to let the use select the last image on screen
    public void goEnd() {
		if (showRunning) return;
    	
    	UI.clearGraphics();
    	display();
    	imagePos(images.size()-1);
    	chooseImage(x,y);   
    	currentImage = images.size()-1;
		
	}
  //use key "D" to let the use select the image on the selected  one's right on screen
    public void goRight() {
    	if (showRunning) return;  
    	if(currentImage >= images.size()-1) return;
    	UI.clearGraphics();        	   		
    	display();    	
    	imagePos(currentImage+1);
    	chooseImage(x,y);
    	currentImage++;	

	}
    
  //use key "A" to let the use select the image on the selected  one's left on screen
    public void goLeft() {
		if (showRunning) return; 
		if(currentImage <= 0) return;
    	UI.clearGraphics();        	   		
    	display();
    	imagePos(currentImage-1);
    	chooseImage(x,y);
    	currentImage--;
 	
	}
   //use key "w" to let the use select the image above the selected  one on screen 
    public void goup() {
		if (showRunning) return; 
		if(currentImage<6) return;
    	UI.clearGraphics();        	   		
    	display();
    	imagePos(currentImage-6);
    	chooseImage(x,y);   	
    	currentImage=currentImage-6;

    }
  //use key "s" to let the use select the image below the selected  one on screen 
    public void godown() {
		if (showRunning) return; 
		if(currentImage>images.size()-7) return;
    	UI.clearGraphics();        	   		
    	display();
    	imagePos(currentImage+6);
    	chooseImage(x,y);
    	currentImage=currentImage+6;

    }


	/**
     * A method that adds a bunch of names to the list of images, for testing.
     */
    public void setTestList(){
        if (showRunning) return;
        String[] names = new String[] {"Atmosphere.jpg", "BachalpseeFlowers.jpg",
                                       "BoraBora.jpg", "Branch.jpg", "DesertHills.jpg",
                                       "DropsOfDew.jpg", "Earth_Apollo17.jpg",
                                       "Frame.jpg", "Galunggung.jpg", "HopetounFalls.jpg",
                                       "Palma.jpg", "Sky.jpg", "SoapBubble.jpg",
                                       "Sunrise.jpg", "Winter.jpg"};

        for(String name : names){
            images.add(name);
        }        
        currentImage = 0;
        display();
        imagePos(currentImage);
		chooseImage(x,y);
    }
            



    public static void main(String[] args) {
        new SlideShow();
    }

}
