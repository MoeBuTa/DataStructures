// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 3
 * Name:Wenxiao Zhang
 * Username:zhangwenx4
 * ID:300462639
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;

/**
 * Simulation of an EmergencyRoom,
 * The Emergency room has a collection of departments for treating patients (ER beds, X-ray,
 *  Operating Theatre, MRI, Ultrasound, etc).
 * 
 * When patients arrive at the emergency room, they are immediately assessed by the
 *  triage team who determine the priority of the patient and a sequence of treatments
 *  that the patient will need.
 *
 * Each department has
 *  - a Set of patients that they are currently treating,
 *    (There is a maximum size of this set for each department)
 *  - a Queue for the patients waiting for that department.
 *
 * The departments should be in a Map, with the department name (= treatment type) as the key.
 * 
 * When a patient has finished a treatment, they should be moved to the
 *   department for the next treatment they require.
 *
 * When a patient has finished all their treatments, they should be discharged:
 *  - a record of their total time, treatment time, and wait time should be printed,
 *  - the details should be added to the statistics. 
 *
 *
 * The main simulation should consist of
 * a setting up phase which initialises all the queues,
 * a loop that steps through time:
 *   - advances the time by one "tick"
 *   - Processes one time tick for each patient currently in each department
 *     (either making them wait if they are on the queue, or
 *      advancing their treatment if they are being treated)
 *   - Checks for any patients who have completed their current treatment,
 *      and remove from the department
 *   - Move all Patients that completed a treatment to their next department (or discharge them)
 *   - Checks each department, and moves patients from the front of the
 *       waiting queues into the Sets of patients being treated, if there is room
 *   - Gets any new patient that has arrived (depends on arrivalInterval) and
 *       puts them on the appropriate queue
 *   - Redraws all the departments - showing the patients being treated, and
 *     the patients waiting in the queues
 *   - Pauses briefly
 *
 * The simple simulation just has one department - ER beds that can treat 5 people at once.
 * Patients arrive and need treatment for random times.
 */

public class EmergencyRoom{

    private Map<String, Department> departments = new HashMap<String, Department>();
    private boolean running = false;
    
    // fields controlling the probabilities.
    private int arrivalInterval = 5;   // new patient every 5 ticks, on average
    private double probPri1 = 0.1; // 10% priority 1 patients
    private double probPri2 = 0.2; // 20% priority 2 patients
    private Random random = new Random();  // The random number generator.
    private Set<Patient> patient = new HashSet<Patient>();
    private int time = 0; // The simulated time
    List<Patient> check = new ArrayList<Patient>();  //a temporary list to check if the patients complete their current treatment
    List<Patient> temp = new ArrayList<Patient>();	//a temporary list to discharge the patients
    
    
    Queue<Patient> Q = new ArrayDeque<Patient>();
    /**
     * Construct a new EmergencyRoom object, setting up the GUI
     */
    

    public EmergencyRoom(){
        setupGUI();
        reset();
    }

    public void setupGUI(){
        UI.addButton("Reset", this::reset);
        UI.addButton("Start", this::run);
        UI.addButton("Stop", ()->{running=false;});
        UI.addSlider("Av arrival interval", 1, 50, arrivalInterval,
            (double val)-> {arrivalInterval = (int)val;});
        UI.addSlider("Prob of Pri 1", 1, 100, probPri1*100,
            (double val)-> {probPri1 = val/100;});
        UI.addSlider("Prob of Pri 2", 1, 100, probPri2*100,
            (double val)-> {probPri2 = Math.min(val/100,1-probPri1);});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
        
        UI.addButton("Evacuating Simulation", ()->{ running=false;evacuate();});
        
    }
    
    
    
    public void evacuate(){
    	
    	
    	for (String dept : new String[]{"ENT","MRI","Ultrasound","X-ray","Operating Theatre","ER beds"}) {
    		
    		while(!departments.get(dept).getWaitingPatients().isEmpty() || !Q.isEmpty()) {
    			
    			if(random.nextDouble() < 1.0/5 && !Q.isEmpty()) {
    				Patient p = Q.poll();
    				System.out.println(p.getName()+" is save");
    			}
    				
    			if(!departments.get(dept).getWaitingPatients().isEmpty()) {
        			Patient p = departments.get(dept).evacuate();
            		if(p!=null) {
            			Q.offer(p);
            		}
    			}
    			redraw();
    			UI.sleep(600);
    		}
    		
    		for(Patient p : departments.get(dept).getCurrentPatients()) {
    			Q.offer(p);
    			
    		}
 
    		
    	}

    	
    }

    /**
     * Define the departments available and put them in the map of departments.
     * Each department needs to have a name and a maximum number of patients that
     * it can be treating at the same time.
     * Simple version: just a collection of 5 ER beds.
     */

    
    public void reset(){
        UI.clearGraphics();
        UI.clearText();
        running=false;
        time = 0;
        departments.put("ER beds",           new Department("ER beds", 8));
        departments.put("Operating Theatre", new Department("Operating Theatre", 2));
        departments.put("X-ray",             new Department("X-ray", 2));
        departments.put("MRI",               new Department("MRI", 1));
        departments.put("Ultrasound",        new Department("Ultrasound", 3));
        //add a new department "ENT" which is ear nose and throat
        departments.put("ENT",        		 new Department("ENT", 4));	
    }

     
    /**
     * Main loop of the simulation
     */
    public void run(){
        running = true;
        while (running){
            // Hint: if you are stepping through a set, you can't remove
            //   items from the set inside the loop!
            //   If you need to remove items, you can add the items to a
            //   temporary list, and after the loop is done, remove all 
            //   the items on the temporary list from the set.
            /*# YOUR CODE HERE */
        	time++;
        	UI.printMessage("T: "+time);

            
            if(random.nextDouble() < 1.0/arrivalInterval){
            	
                Patient p = new Patient(time, randomPriority()); // gets any new patient that has arrived.
                patient.add(p);	//add the patient into a set
                UI.println("Arrived: "+ p.toString());   
            }

            for(Patient P : patient) {
            	if(!check.contains(P)) {
            		check.add(P);
            		P.incrementTreatmentNumber();
            	}
            	//get the current treatment
            	Department nextDepartment = departments.get(P.getCurrentTreatment());
            	//treating patient
            	if(nextDepartment.getCurrentPatients().contains(P)) 
            		P.advanceTreatmentByTick();
            	//completing
            	if(P.completedCurrentTreatment()) {
            		check.remove(P);
            		nextDepartment.removePatient(P);  
            		if(P.completedAllTreatments()) {
            			temp.add(P);
            			UI.println("Finished: "+ P.toString());    
            			break;
            		}     	  		
            	} 
                if(!check.contains(P)) {
                	//get the next treatment
                	nextDepartment = departments.get(P.getNextTreatment());
                }
                
              //moving patient into the department
          
                nextDepartment.moveFromWaiting(); 
 
            	//waiting patient
            	if(nextDepartment.getWaitingPatients().contains(P)) {
            		P.waitForATick();
            	}
            		

            	if(!nextDepartment.getCurrentPatients().contains(P) && 
            		!nextDepartment.getWaitingPatients().contains(P) ) {
            		
            		nextDepartment.addPatient(P);
            		
            		
            	}
            		//add a patient into the queue
            		 	
            		
            }                	 	            
            patient.removeAll(temp);
            redraw();             
            UI.sleep(300);
        }
        // Stopped
        reportStatistics();
    }

    /**
     * Report that a patient has been discharged, along with any
     * useful statistics about the patient
     */
    public void discharge(Patient p){
        UI.println("Discharge: " + p);
    }

    /**
     * Report summary statistics about the simulation
     */
    public void reportStatistics(){
    	int y = 80;
    	UI.println("------------------------------------------");
    	for (String dept : new String[]{"ER beds","Operating Theatre", "X-ray", "Ultrasound", "MRI", "ENT"}) {   		
    		int numberOfWaiting = departments.get(dept).getWaitingPatients().size();
    		// if the number of waiting patients of the department is more than 4, consider it as overloaded
    		if(numberOfWaiting > 4) {
    			UI.setFontSize(14);
    			UI.setColor(Color.red);
    			UI.drawString(dept+ " OVERLOADED", 0, y-35);
    			UI.println(dept+" needs more facilities!");
    		}
    		y += 50;
    	}
    	

    }

    /**
     * Redraws all the departments
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(14);
        UI.drawString("Treating Patients", 5, 15);
        UI.drawString("Waiting Queues", 200, 15);
        UI.drawString("Evacuating Queues", 400, 15);
        UI.drawLine(500, 15, 500, 350);
        UI.drawLine(0,32,400, 32);
        double y = 80;
        double y2 = 330;
        for (String dept : new String[]{"ER beds","Operating Theatre", "X-ray", "Ultrasound", "MRI", "ENT"}){
            departments.get(dept).redraw(y);
            UI.drawLine(0,y+2,400, y+2);
            y += 50;            
        }
        
		if(!Q.isEmpty()) {
			for(Patient p : Q) {
				p.redraw(450, y2);
				y2 -= 52; 
			}
		}
    }

    /**  (COMPLETION)
     * Returns a random priority 1 - 3
     * Probability of a priority 1 patient should be probPri1
     * Probability of a priority 2 patient should be probPri2
     * Probability of a priority 3 patient should be (1-probPri1-probPri2)
     */
    public int randomPriority(){
        /*# YOUR CODE HERE */
    	int randomPri;
    	double randomRate = 0;
    	randomRate = Math.random();
    	if(randomRate > 0 && randomRate <= probPri1)
    		randomPri = 1;
    	else if (randomRate > probPri1 && randomRate <= probPri1 + probPri2)
    		randomPri = 2;
    	else
    		randomPri = 3;
    	return randomPri;
    }


    public static void main(String[] arguments){
        new EmergencyRoom();
    }        

}
