import ecs100.*;
import java.util.*;

/**
 * Simulation of an EmergencyRoomMedical center with one doctor and one nurse
 *
 */

public class MedicalCentre{

    private Queue<Patient> waitForDoctor = new ArrayDeque<Patient>();
    private Queue<Patient> waitForNurse = new ArrayDeque<Patient>();

    private Patient inDoctorsOffice;
    private Patient inNursesOffice;

    private boolean running = false;

    // fields controlling the probabilities.
    private int arrivalInterval = 20;   // new patient every 20 ticks, on average
    private double probDoctor = 0.8; // probability of docotr vs nurse

    private Random random = new Random();  // The random number generator.

    /**
     * Construct a new MedicalCenter object, setting up the GUI
     */
    public MedicalCentre(){
        setupGUI();
    }

    public void setupGUI(){
        UI.addButton("Start", this::run);
        UI.addButton("Stop", ()->{running=false;});
        UI.addSlider("Av arrival interval", 1, 50, arrivalInterval,
            (double val)-> {arrivalInterval = (int)val;});
        UI.addSlider("Prob of Doctor", 1, 100, probDoctor*100,
            (double val)-> {probDoctor = val/100;});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
    }

    /**
     * Main loop of the simulation
     */
    public void run(){
        running = true;
        int time = 0;
        while (running){
            //  advances the time by one "tick"
            time++;
            UI.printMessage("T: "+time);

            // Processes one time tick for each patient currently being treated 
            if (inDoctorsOffice!=null){
                inDoctorsOffice.advanceTreatmentByTick();
                if(inDoctorsOffice.completedTreatment()){
                    discharge(inDoctorsOffice);
                    inDoctorsOffice=null;
                }
            }

            //move patients from waitlist
            if(inDoctorsOffice==null && !waitForDoctor.isEmpty()){
                inDoctorsOffice = waitForDoctor.poll();
            }
            for(Patient p : waitForDoctor){
                p.waitForATick();
            }
            // gets any new patient that has arrived and puts them on the appropriate queue.
            if(random.nextDouble() < 1.0/arrivalInterval){
                Patient p = new Patient(time, 1);
                UI.println("Arrived: "+ p);
                waitForDoctor.offer(p);
            }

            redraw();
            UI.sleep(300);
        }
        // Stopped
        reportStatistics();
    }

    /**
     * Report statistics about
     */
    public void discharge(Patient p){
        UI.println("Finished: "+ p);
    }

    public void reportStatistics(){
    }

    /**
     * Redraws all the departments
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(14);
        UI.drawString("In treatment", 5, 15);
        UI.drawString("Waiting Queues", 100, 15);
        UI.drawLine(0,32,400, 32);
        double y = 80;
        UI.setFontSize(14);
        UI.drawString("Doctors Office", 0, y-35);
        if (inDoctorsOffice!=null){
            inDoctorsOffice.redraw(10, y);
        }
        double x = 100;
        for(Patient p : waitForDoctor){
            p.redraw(x, y);
            x += 10;
        }
        y = 130;
        UI.setFontSize(14);
        UI.drawString("Nurses Office", 0, y-35);
        if(inNursesOffice!=null){
            inNursesOffice.redraw(10, y);
        }
        x = 100;
        for(Patient p : waitForNurse){
            p.redraw(x, y);
            x += 10;
        }
    }


    public static void main(String[] arguments){
        new MedicalCentre();
    }   

}