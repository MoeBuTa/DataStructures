// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 2
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/** 
 *  Renders a molecule on the graphics pane from different positions.
 *  
 *  A molecule consists of a collection of molecule elements, i.e., atoms.
 *  Each atom has a type or element (eg, Carbon, or Hydrogen, or Oxygen, ..),
 *  and a three dimensional position in the molecule (x, y, z).
 *
 *  Each molecule is described in a file by a list of atoms and their positions.
 *  The molecule is rendered by drawing a colored circle for each atom.
 *  The size and color of each atom is determined by the type of the atom.
 * 
 *  The description of the size and color for rendering the different types of
 *  atoms is stored in the file "element-info.txt" which should be read and
 *  stored in a Map.  When an atom is rendered, the element type should be looked up in
 *  the map to find the size and color.
 * 
 *  A molecule can be rendered from different perspectives, and the program
 *  provides buttons to control the perspective of the rendering.
 *  
 */

public class MoleculeRenderer {

    public static final double MIDX = 300;   // The middle on the (x axis)
    public static final double MIDY = 0;     // The middle on the (y axis)
    public static final double MIDZ = 200;   // The middle depth (z axis)

    public static double Horizontal = 0;
    public static double vertical = 0;
    // Map containing info about the size and color of each element type.
    private Map<String, ElementInfo> elements = new TreeMap<String,ElementInfo>(); 
    // The collection of the atoms in the current molecule
    private List<Atom> molecule = new ArrayList<Atom>();  
    
    private List<Atom> atomPos = new ArrayList<Atom>();
	private char[] elem = new char[100];    		
	private int[] elementIndex1 = new int[100];
	private int[] elementIndex2 = new int[100];
 
    // Constructor:
    /** 
     * Sets up the Graphical User Interface and reads the file of element data of
     * each possible type of atom into a Map: where the type is the key
     * and an ElementInfo object is the value (containing size and color).
     */
    public MoleculeRenderer() {
        setupGUI();
        readElementInfo();    //  Read the atom info
    }

    public void setupGUI(){
        UI.addButton("Molecule", this::loadMolecule);
        UI.addButton("Molecule7", this::showMoleculeWithBonds);
        UI.addButton("FromFront", this::showFromFront);
        UI.addButton("FromBack", this::showFromBack);
        UI.addButton("FromRight", this::showFromRight);
        UI.addButton("FromLeft", this::showFromLeft);
        UI.addButton("FromTop", this::showFromTop);
        UI.addButton("FromBot", this::showFromBot);
        UI.addButton("turn right", this::turnRight);
        UI.addButton("turn left", this::turnLeft);
        UI.addButton("turn up", this::turnUp);
        UI.addButton("turn down", this::turnDown);
        UI.addButton("Quit", UI::quit);
        UI.setKeyListener(this::doKey);
        UI.setDivider(0.0);
    }

    /** 
     * Reads the file "element-info.txt" which contains radius and color
     * information about each type of element:
     *   element name, a radius, and red, green, blue, components of the color (integers)
     * Stores the info in a Map in the elements field.
     * The element name is the key.
     */
    private void readElementInfo() {
        UI.printMessage("Reading the element information...");
        try {
            Scanner scan = new Scanner(new File("element-info.txt"));
            /*# YOUR CODE HERE */
            while(scan.hasNext()) {
         	
            	String element =scan.next();         	//get the name of the element   	
            	int radius = scan.nextInt();			//get the size of the element
            	int colorLeft = scan.nextInt(); 			//get the color information of the element 
            	int colorMiddle = scan.nextInt(); 			
            	int colorRight = scan.nextInt(); 			
            	
            	Color color = new Color (colorLeft, colorMiddle, colorRight);
            	ElementInfo elem = new ElementInfo(element,(double)radius ,color );	
            	elements.put(element, elem);            	
            }
            scan.close();
            UI.printMessage("Reading element information: " + elements.size() + " elements found.");
        }
        catch (IOException ex) {UI.printMessage("Reading element information FAILED."+ex);}
    }

    /**
     * Ask the user to choose a file that contains info about a molecule,
     * load the information, and render it on the graphics pane.
     */
    public void loadMolecule(){
    	String filename = UIFileChooser.open();
        readMoleculeFile(filename);
        showFromFront();
    }
    
    

    /** 
     * Reads the molecule data from a file containing one line for
     *  each atom in the molecule.
     * Each line contains an atom type and the 3D coordinates of the atom.
     * For each atom, the method constructs a Atom object,
     * and adds it to the List of molecule elements in the molecule.
     * To obtain the color and the size of each atom, it has to look up the name
     * of the atom in the Map of ElementInfo objects.
     */
    public void readMoleculeFile(String fname) {
        try {
            /*# YOUR CODE HERE */
 
        	Scanner scan = new Scanner(new File(fname));
            while(scan.hasNext()) {
         	
            	String element =scan.next();         	//get the name of the element   	
            	int x = scan.nextInt(); 			//get the color information of the element 
            	int y = scan.nextInt(); 			
            	int z = scan.nextInt(); 			

            	Atom atom = new Atom((double)x ,(double)y,(double)z,element );	
            	molecule.add(atom);
            	
            }
            scan.close();

        }
        catch(IOException ex) {
            UI.println("Reading molecule file " + fname + " failed."+ex);
        }
    }
    
/*
 * read the file molecule-with-bonds.txt    
 */
    public void readMolecule7() {

    	int i = 0;
    	int j = 1;   
    	try{
    		
            Scanner sc = new Scanner(new File("molecule7-with-bonds.txt"));
    
            while(sc.hasNext()) {
            	String element = sc.next();  
            	elem[i] = element.charAt(0);
            	if(Character.isDigit(elem[i])) {
            		elementIndex1[0] = Integer.parseInt(element);
            		break;
            	}
            	double x = sc.nextInt();         	   	
            	double y = sc.nextInt();			
            	double z = sc.nextInt();           	
            	Atom atom = new Atom((double)x ,(double)y,(double)z,element);		
            	molecule.add(atom);
            	i++;            	
            }
            while(sc.hasNext()) {
            	elementIndex2[j-1] =sc.nextInt();
            	if(sc.hasNext()) {
                	elementIndex1[j] = sc.nextInt(); 
                	j++;         	
            	}                  	
            }
            
            
            sc.close();
        }catch(IOException e){throw new RuntimeException("Loading molecule7-with-bonds.txt failed" + e);}
    }

    
    public void showMoleculeWithBonds() {
    	readMolecule7();
    	
    	Collections.sort(molecule,(Atom element1,Atom element2) ->{
    		if(element1.getZ() > element2.getZ()) {return -1;}
    		else if (element1.getZ() < element2.getZ()) {return 1;}
    		else {return 0;}});
        UI.clearGraphics();


        
        UI.setColor(Color.black);
    	UI.setLineWidth(5);
    	for(int i = 0; i < elementIndex1.length; i++) {

    		double x1 = molecule.get(elementIndex1[i]).getX()+MIDX;
    		double y1 = molecule.get(elementIndex1[i]).getY();
    		double x2 = molecule.get(elementIndex2[i]).getX()+MIDX;
    		double y2 = molecule.get(elementIndex2[i]).getY();
    		 UI.drawLine(x1, y1, x2, y2);
    		 
    	}
        for(Atom atom : molecule) {
        	 atom.renderWithBonds(MIDX+atom.getX(), atom.getY(), elements);
        }   
        molecule.removeAll(molecule);
    }
    
    
    /**
     * Renders the molecule from the front.
     * Sorts the Atoms in the List by their z value, back to front
     * Uses the default ordering of the Atoms
     * Then renders each atom at the position (MIDX+x,y)
     */
    public void showFromFront() {
        /*# YOUR CODE HERE */
    	Horizontal = 0;
    	vertical = 0;
    	Collections.sort(molecule,(Atom element1,Atom element2) ->{
    		if(element1.getZ() > element2.getZ()) {return -1;}
    		else if (element1.getZ() < element2.getZ()) {return 1;}
    		else {return 0;}});
        UI.clearGraphics();
        for(Atom atom : molecule) {
            atom.render(MIDX+atom.getX(), atom.getY(), elements);
        }
    }

    /**
     * Renders the molecule from the back.
     * Sorts the Atoms in the List by their z value (front to back)
     * Then renders each atom at (MIDX-x,y) position
     */
    public void showFromBack() {
        /*# YOUR CODE HERE */
    	Horizontal = 0;
    	vertical = 0;
       	Collections.sort(molecule,(Atom element1,Atom element2) ->{
    		if(element1.getZ() < element2.getZ()) {return -1;}
    		else if (element1.getZ() > element2.getZ()) {return 1;}
    		else {return 0;}});
    	UI.clearGraphics();
        for(Atom atom : molecule) {
            atom.render(MIDX-atom.getX(), atom.getY(), elements);
        }
    }

    /**
     * Renders the molecule from the left.
     * Sorts the Atoms in the List by their x value (larger x first)
     * Then renders each atom at (MIDZ-z,y) position
     */
    public void showFromLeft() {
        /*# YOUR CODE HERE */
    	Horizontal = 0;
    	vertical = 0;
       	Collections.sort(molecule,(Atom element1,Atom element2) ->{
    		if(element1.getX() > element2.getX()) {return -1;}
    		else if (element1.getX() < element2.getX()) {return 1;}
    		else {return 0;}});
    	UI.clearGraphics();
        for(Atom atom : molecule) {
            atom.render(MIDZ-atom.getZ(), atom.getY(), elements);
        }
    }

    /**
     * Renders the molecule from the right.
     * Sorts the Atoms in the List by their x value (smaller x first)
     * Then renders each atom at (MIDZ+z,y) position
     */
    public void showFromRight() {
        /*# YOUR CODE HERE */
    	Horizontal = 0;
    	vertical = 0;
       	Collections.sort(molecule,(Atom element1,Atom element2) ->{
    		if(element1.getX() < element2.getX()) {return -1;}
    		else if (element1.getX() > element2.getX()) {return 1;}
    		else {return 0;}});
    	UI.clearGraphics();
        for(Atom atom : molecule) {
            atom.render(MIDZ+atom.getZ(), atom.getY(), elements);
        }
    }

    /**
     * Renders the molecule from the top.
     * Sorts the Atoms in the List by their y value (larger y first)
     * Then renders each atom at (MIDX+x, MIDZ-z) position
     */
    public void showFromTop() {
        /*# YOUR CODE HERE */
    	Horizontal = 0;
    	vertical = 0;
       	Collections.sort(molecule,(Atom element1,Atom element2) ->{
    		if(element1.getY() > element2.getY()) {return -1;}
    		else if (element1.getY() < element2.getY()) {return 1;}
    		else {return 0;}});
    	UI.clearGraphics();
        for(Atom atom : molecule) {
            atom.render(MIDX+atom.getX(), MIDZ-atom.getZ(), elements);
        }
    }

    /**
     * Renders the molecule from the bottom.
     * Sorts the Atoms in the List by their y value (smaller y first)
     * Then renders each atom at (MIDX+x, MIDZ+z) position
     */
    public void showFromBot() {
        /*# YOUR CODE HERE */
    	Horizontal = 0;
    	vertical = 0;
       	Collections.sort(molecule,(Atom element1,Atom element2) ->{
    		if(element1.getY() < element2.getY()) {return -1;}
    		else if (element1.getY() > element2.getY()) {return 1;}
    		else {return 0;}});
      	UI.clearGraphics();
        for(Atom atom : molecule) {
            atom.render(MIDX+atom.getX(), MIDZ+atom.getZ(), elements);
        }

    }
    
    /*
     * create a view from direction method to deal with the render of molecule in different directions
     */
    public void viewFromDirection(double theta, double phi) {
    	double x = 0;
	    double y = 0;
	    double z = 0;	    
        UI.clearGraphics();    
        
    	for(Atom atom : molecule) {   
    		x = (atom.getX()*Math.cos(theta)) - (atom.getZ()*Math.sin(theta));   		
    		z = ((atom.getZ()*Math.cos(theta)) + (atom.getX()*Math.sin(theta)))*Math.cos(phi) - (atom.getY()*Math.sin(phi));
    		y = ((atom.getY()-200)*Math.cos(phi)) + (((atom.getZ()*Math.cos(theta)) + (atom.getX()*Math.sin(theta)))*Math.sin(phi)+210);

    		Atom atomPosition = new Atom(x ,y,z, atom.getKind());
            atomPos.add(atomPosition);  
    	}
    	Collections.sort(atomPos,(Atom element1,Atom element2) ->{
    		if(element1.getZ() > element2.getZ()) {return -1;}
        	else if (element1.getZ() < element2.getZ()) {return 1;}
        	else {return 0;}});
    	for(Atom atom : atomPos) {
            atom.render(MIDX+atom.getX(), atom.getY(), elements);    
    	}
    	atomPos.removeAll(atomPos);
    }
    
    
    //show the molecule from left to right
    public void turnRight() {

    	viewFromDirection(Horizontal + (Math.PI/36),vertical);
    	Horizontal = Horizontal + (Math.PI/36);
    	if(Horizontal >= 2*Math.PI) Horizontal = 0;

    }
    
    //show the molecule from right to left
    public void turnLeft() {

    	viewFromDirection(Horizontal - (Math.PI/36),vertical);
    	Horizontal = Horizontal - (Math.PI/36);
    	if(Horizontal <= -2*Math.PI) Horizontal = 0;
    }
    
    //show the molecule from down to up
    public void turnUp() {

    	viewFromDirection(Horizontal ,vertical + (Math.PI/36));
    	vertical = vertical + (Math.PI/36);
    	if(vertical >= 2*Math.PI) vertical = 0;
    }
    //show the molecule from up to down
    public void turnDown() {

    	viewFromDirection(Horizontal,vertical - (Math.PI/36));
    	vertical = vertical - (Math.PI/36);
    	if(vertical <= -2*Math.PI) vertical = 0;
    }
    /*
     * use keys to control the move of the molecule
     */
    public void doKey(String key) {
        key = key.toLowerCase();
        if (key.equals("w") ||key.equals("up")) {
        	turnUp();
        }
        else if (key.equals("s") ||key.equals("down")) {
        	turnDown();
        }
        else if (key.equals("a") ||key.equals("left")) {
        	turnLeft();
        }
        else if (key.equals("d") ||key.equals("right")) {
            turnRight();
        }
    }
    
    public static void main(String args[]) {
        new MoleculeRenderer();
    }
}
