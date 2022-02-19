// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 2
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/** Genealogy:
 * Prints out information from a genealogical database
 */

public class Genealogy  {

    // all the people:  key is a name,  value is a Person object
    private final Map<String, Person> database = new HashMap<String, Person>();
    
    private Set<String> missName = new HashSet<String>();
    
    private Set<String> ancestor = new TreeSet<String>();
    private List<String> ancestors = new ArrayList<String>();
    
    private String selectedName;  //currently selected name.
    
    public int count = 0;

    private boolean databaseHasBeenFixed = false;
    
    

    
    /**
     * Constructor
     */
    public Genealogy() {
        loadData();
        setupGUI();
    }

    /**
     * Buttons and text field for operations.
     */
    public void setupGUI(){
        UI.addButton("Print all names", this::printAllNames);
        UI.addButton("Print all details", this::printAllDetails);
        UI.addTextField("Name", this::selectPerson);
        UI.addButton("Parent details", this::printParentDetails);
        UI.addButton("Add child", this::addChild);
        UI.addButton("Find & print Children", this::printChildren);
        UI.addButton("Fix database", this::fixDatabase);
        UI.addButton("Print GrandChildren", this::printGrandChildren);
        UI.addButton("print missing", this::printMissing);
        UI.addButton("print ancestors", this::printAncestors);
        UI.addButton("Clear Text", UI::clearText);
        UI.addButton("Reload Database", this::loadData);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    /** 
     *  Load the information from the file "database.txt".
     *        Each line of the file has information about one person:
     *        name, year of birth, mother's name, father's name
     *        (note: a '-' instead of a name means  the mother or father are unknown)
     *        For each line,
     *         - construct a new Person with the information, and
     *   - add to the database map.
     */
    public void loadData() {
    	
    	try{
    		
            Scanner sc = new Scanner(new File("database.txt"));
            // read the file to construct the Persons to put in the map
            /*# YOUR CODE HERE */
           
            while(sc.hasNext()) {
            	String name = sc.next();           //get the name of each person            	
            	int dob = sc.nextInt();         	//get the date of birth of each person   	
            	String mother = sc.next();			//get the mother of each person
            	String father = sc.next(); 			//get the father of each person           	
            	Person person = new Person(name, dob, mother, father);		
            	database.put(name, person);
            }
            sc.close();
            UI.println("Loaded "+database.size()+" people into the database");
        }catch(IOException e){throw new RuntimeException("Loading database.txt failed" + e);}
    }

    /** Prints out names of all the people in the database */
    public void printAllNames(){
    	for (String name : database.keySet()) {
            UI.println(name);
        }
        UI.println("-----------------");
    }

    /** Prints out details of all the people in the database */
    public void printAllDetails(){
        /*# YOUR CODE HERE */    	
    	 for (Person details : database.values()) {
             UI.println(details);
         }
        UI.println("-----------------");
    }

    /**
     * Store value (capitalised properly) in the selectedName field.
     * If there is a person with that name currently in people,
     *  then print out the details of that person,
     * Otherwise, offer to add the person:
     * If the user wants to add the person,
     *  ask for year of birth, mother, and father
     *  create the new Person,
     *  add to the database, and
     *  print out the details of the person.
     * Hint: it may be useful to make an askPerson(String name) method
     * Hint: remember to capitalise the names that you read from the user
     */
    public void selectPerson(String value){
        selectedName = capitalise(value); 
        /*# YOUR CODE HERE */
        
        //print out the details of that person if there is a person with that name currently in people
        if(database.containsKey(selectedName)) {
        	Person details = database.get(selectedName);
        	UI.println(details);
        }
        else {
      	
        	askPerson(selectedName);

        	
        	
        }
    }
    /*
     * make a method to ask user the details of the selected person, and put the detail into the database
     */
    public void askPerson(String name) {
    	String ask = UI.askString("add " + name + " to the database?(y/n):");
    	if(ask.equals("n") ) {
    		UI.println("-----------------");
    		return;
    	}
    	else if(ask.equals("y")){
    		int dob = UI.askInt("Please enter " + name +"'s year of birth: ");	//ask the user to enter the person's date of birth
        	String mother = UI.askString("Please enter " + name +"'s mother: ");	//ask the user to enter the person's mother's name
        	mother = capitalise(mother); 					//capitalise the mother's name
        	String father = UI.askString("Please enter " + name +"'s father: ");	//ask the user to enter the person's father's name
        	father = capitalise(father); 					//capitalise the father's name
        	Person personalDet = new Person(name, dob, mother, father);
        	database.put(name, personalDet);		//put the details of the person into the database
        	UI.println(database.get(selectedName));			// print out the details of the person
    	}
    	
    	
    }

    /**
     * Print all the details of the mother and father of the person
     * with selectedName (if there is one).
     * (If there is no person with the current name, print "no person called ...")
     * If the mother or father's names are unknown, print "unknown".
     * If the mother or father names are known but they are not in
     *  the database, print "...: No details known".
     */
    public void printParentDetails(){
        /*# YOUR CODE HERE */
    	if(selectedName == null) { 
    		UI.println("please enter a name first");
    		return;
    	}
    	else {
    		selectedName = capitalise(selectedName); //capitalise the selectedName
    		if(database.containsKey(selectedName)) {
    			database.get(selectedName);	
    			Person person = database.get(selectedName);
    			
    			String mother = person.getMother();
    			String father = person.getFather();
    			getParent(selectedName,mother);
    			getParent(selectedName,father);  	
    			
    	}

    		else {UI.println("no person called " + selectedName);}
    		UI.println("-----------------");
    	}
    }
    /*
     * make a method to get the selectedName's parent from the database
     */
    public void getParent(String name,String parent) {
    	Person person = database.get(name);
		String mother = person.getMother();
		String father = person.getFather();
    	if(database.containsKey(parent) && parent != null) {
    		if(parent == mother)
    			UI.println("M:"+database.get(parent));    		
    		else if(parent == father)
				UI.println("F:"+database.get(parent));
    	}
		else if(!database.containsKey(parent) && parent != null) {
			if(parent == mother)
				UI.println("M:"+parent +": No details known");   		
    		else if(parent == father)
    			UI.println("F:"+parent +": No details known");
		} 	
		else if(parent == null) {
			if(parent == mother)
				UI.println("M:unknown");  		
    		else if(parent == father)
    			UI.println("F:unknown");
		} 
    }

    /**
     * Add a child to the person with selectedName (if there is one).
     * If there is no person with the selectedName,
     *   print "no person called ..." and return
     * Ask for the name of a child of the selectedName
     *  (remember to capitalise the child's name)
     * If the child is already recorded as a child of the person,
     *  print a message
     * Otherwise, add the child's name to the current person.
     * If the child's name is not in the current database,
     *   offer to add the child's details to the current database.
     *   Check that the selectedName is either the mother or the father.
     */
    public void addChild(){
        /*# YOUR CODE HERE */
    	if(selectedName == null) { 
    		UI.println("please enter a name first");
    		return;
    	}
    	else {
    		
    		selectedName = capitalise(selectedName); //capitalise the selectedName
    		
        	if(!database.containsKey(selectedName)) {
        		UI.println("no person called " + selectedName);
        		UI.println("-----------------");
        		return;
        	}
        	
        	else {
        		String child = UI.askString("Please enter " + selectedName + "'s child: ");	//ask the user to enter the person's child's name	
        		child = capitalise(child);	  
        		
        		if(findChildren(selectedName).contains(child)) 
        			UI.println(child + " is already recorded as a child of " + selectedName);

        		else {
        			
        			if(!database.containsKey(child)){	
        				
        				String ask = UI.askString("add " + child + " to the database?(y/n):");
        		    	if(ask.equals("n") ) {
        		    		UI.println("-----------------");
        		    		return;
        		    	}
        		    	else if(ask.equals("y")) {
		
        					int dob = UI.askInt("Please enter " + child +"'s year of birth: ");	//ask the user to enter the person's date of birth
        					String mother = UI.askString("Please enter " + child +"'s mother: ");	//ask the user to enter the person's mother's name
        					mother = capitalise(mother); 					//capitalise the mother's name
        					String father = UI.askString("Please enter " + child +"'s father: ");	//ask the user to enter the person's father's name
        					father = capitalise(father); 					//capitalise the father's name
        					Person personalDet = new Person(child, dob, mother, father);
        					database.put(child, personalDet);		//put the details of the person into the database
        				
        					if(selectedName.equals(personalDet.getFather()))
        						UI.println(selectedName + " is " + child + "'s father");
        					else if(selectedName.equals(personalDet.getMother()))
        						UI.println(selectedName + " is " + child + "'s mother");
        		    	}
        			}
        		
        		}
        	}
            UI.println("-----------------");
    	}
    	
    }

    /**
     * Print the number of children of the selectedName and their names (if any)
     * Find the children by searching the database for people with
     * selectedName as a parent.
     * Hint: Use the findChildren method (which is needed for other methods also)
     */
    public void printChildren(){
        /*# YOUR CODE HERE */
    	if(selectedName == null) { 
    		UI.println("please enter a name first");
    		return;
    	}
    	else {
    		if (!database.containsKey(selectedName)){
                UI.println("That person is not known");
                UI.println("-----------------");
                return;
            }
    		selectedName = capitalise(selectedName); //capitalise the selectedName
        	findChildren(selectedName);
        	if(count == 1) UI.println(selectedName + " has 1 child");
        	else UI.println(selectedName + " has " + count + " children");
        	
        	for(String child : findChildren(selectedName)) UI.println(child);	
        	
        	UI.println("-----------------");
    	}
    	
    }

    /**
     * Find (and return) the set of all the names of the children of
     * the given person by searching the database for every person 
     * who has a mother or father equal to the person's name.
     * If there are no children, then return an empty Set
     */
    public Set<String> findChildren(String name){
        /*# YOUR CODE HERE */
    	
    	Person person = database.get(name);   		//the person
    	count = 0;
    	for(String Name:database.keySet()) {
    		Person child = database.get(Name);		//the children of the person

    	
        	if(name.equals(child.getFather()) || 
        		name.equals(child.getMother())) {
        		
        		person.addChild(child.getName());
        		count++;	


        	}

    	}
    	return person.getChildren();
    	
    	//    	return null;   // just so the template will compile!
    }

    /**
     * When the database is first loaded, none of the Persons will
     * have any children recorded in their children field. 
     * Fix the database so every Person's children includes all the
     * people that have that Person as a parent.
     * Hint: use the findChildren method
     */
    public void fixDatabase(){
        /*# YOUR CODE HERE */
    	for(String name : database.keySet())
    		findChildren(name);
        databaseHasBeenFixed = true;
        UI.println("Found children of each person in database\n-----------------");
    }


    /**
     * Print out all the grandchildren of the selectedName (if any)
     * Assume that the database has been "fixed" so that every Person
     * contains a set of all their children.
     * If the selectedName is not in the database, print "... is not known"
     */
    public void printGrandChildren(){
        if (!databaseHasBeenFixed) {
        	UI.println("Database must be fixed first!");
        	return;
        }
        if(selectedName == null) { 
    		UI.println("please enter a name first");
    		return;
    	}
        else {
        	 selectedName = capitalise(selectedName);
             if (!database.containsKey(selectedName)){
                 UI.println("That person is not known");
                 UI.println("-----------------");
                 return;
             }
             /*# YOUR CODE HERE */
             Set<String> children = findChildren(selectedName);
             Set<String> grandChildren = new HashSet<String>();		//initialise the grandchildren
             UI.println("Grandchildren of " + selectedName + ":");		
             for(String child : children) {							
             	grandChildren = findChildren(child);
             	for(String GrandCh : grandChildren) UI.println(GrandCh);		//print the grandchildren of the selected person

             }
             
        }
       
    	UI.println("------------------");
    }

    /**
     * Print out all the names that are in the database but for which
     * there is no Person in the database. Do not print any name twice.
     * These will be names of parents or children of Persons in the Database
     * for which a Person object has not been created.
     */
    public void printMissing(){
        UI.println("Missing names:");
        /*# YOUR CODE HERE */

        for (String name : database.keySet()) {
        	Person person = database.get(name);
        	addMissing(person.getMother());			
        	addMissing(person.getFather());
        	for(String child : findChildren(name))
        		addMissing(child);
        }
        
        for(String missing : missName)
        	UI.println(missing);
        UI.println("------------------");
    }
    /*
     * add names which is no Person in the database.
     */
    public void addMissing(String name) {
    	
    	if(!database.containsKey(name) && name != null)
    		missName.add(name);
    }
    
   /*
    * print the person's ancestors 
    */
    public void printAncestors(){
    	if(selectedName == null) { 
    		UI.println("please enter a name first");
    		return;
    	}
    	else {
        	selectedName = capitalise(selectedName);
        	
        	getAncestors(selectedName);
        	ancestors.addAll(ancestor);
        	Collections.reverse(ancestors);

        	for(String ance : ancestors)
        		UI.println(ance);
        	ancestors.removeAll(ancestors);
    	} 	
    }
    
    /*
     * get the person's ancestors
     */
    public void getAncestors(String name) {

    	int i = 1;
    	Person person = database.get(name);
    	
    	ancestor.add(person.getName()+" "+person.getDOB());
    	Person mother = database.get(person.getMother());
    	Person father = database.get(person.getFather());
    	Person temp;
    	temp = father;

    	while(person.getMother() != null || person.getFather() != null) {
    		
    		if(person.getMother() != null)
    			ancestor.add(repeatSpace(i)+"M:"+mother.getName()+" "+mother.getDOB());
    		if(person.getFather() != null)
    			ancestor.add(repeatSpace(i)+"F:"+father.getName()+" "+father.getDOB());
    		person = (mother != null)? mother: father;
    		person = (father != null)? father: mother;
    		if(person == null) break;
    		if(person.getMother() == null) mother = null;
    		else mother = database.get(person.getMother());
    		if(person.getFather() == null) father = null;
    		else father = database.get(person.getFather());
    		i++;
    	}
    	
    	i=2;
    	person = temp;
    	if(person == null) return;
    	if(person.getMother() != null)
    		mother = database.get(person.getMother());
    	if(person.getFather() != null)
    		father = database.get(person.getFather());
    	
     	while(person.getMother() != null || person.getFather() != null) {
     		if(person.getMother() != null)
     			ancestor.add(repeatSpace(i)+"M:"+mother.getName()+" "+mother.getDOB());
     		if(person.getFather() != null)
     			ancestor.add(repeatSpace(i)+"F:"+father.getName()+" "+father.getDOB());
     		person = (mother != null)? mother: father;
    		person = (father != null)? father: mother;
    		if(person == null) break;
    		if(person.getMother() == null) mother = null;
    		else mother = database.get(person.getMother());
    		if(person.getFather() == null) father = null;
    		else father = database.get(person.getFather());
    		i++;
    	}

    }
    public static String repeatSpace(int n) {			 //creat a repeat space method
    	 StringBuilder b = new StringBuilder();
    	 for (int x = 0; x <n; x++)
    	 b.append("  ");
    	 return b.toString();
    	}
    
    
    
    /**
     * Return a capitalised version of a string
     */
    public String capitalise(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }


    public static void main(String[] args) throws IOException {
        new Genealogy();
    }
}
