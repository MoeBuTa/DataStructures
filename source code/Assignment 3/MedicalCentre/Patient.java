/* Code for COMP103 Assignment 3, 2018
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;


/** 
 * Represents a Patient at a medical center
 * Each Patient has
 *     a name and
 *     an arrival time,
 *     treatment time
 *     treatment (doctor or nurse)
 *     priority
 *     
 * Methods:
 *   completedTreatment() -> boolean
 *   waitForATick()
 *   advanceTreatmentByTick()
 */

public class Patient{

    public static final double PROB_DOCTOR = 0.8;   // probability patient needs to see a doctor, rather than a nurse

    private String name;
    private int arrival;
    private int priority;  // 1 is highest priority, 3 is lowest priority
    private String treatment;
    private int treatmentTime;    // Remaining time for the treatment.
    private int waitTime = 0;     // How long the patient has waited
    
    private static final Random random = new Random();  //used for generating the random values.


    /**
     * Construct a new Patient object
     * parameters are the arrival time and the priority.
     */
    public Patient(int time, int pri){
        arrival = time;
        priority = pri;
        makeRandomName();
        makeRandomTreatment();
    }


    public String getName(){return name;}
    public int getArrivalTime(){return arrival;}
    public String getTreatment(){return treatment;}

    /**
     * Return true if the patient has completed their treatments.
     */
    public boolean completedTreatment(){
        return (treatmentTime == 0);
    }
        
    /**
     * Make Patient wait for one tick.
     * Recorded on waitTime
     */
    public void waitForATick(){
        waitTime++;
    }

    /**
     * Reduce the remaining time of the treatment by 1 tick. 
     */
    public void advanceTreatmentByTick(){
        if (treatmentTime == 0){
            throw new RuntimeException("patient has finished their treatment: "+this);
        }
        treatmentTime--;
    }


    /**
     * Compare this Patient with another Patient to determine who should
     *  be treated first.
     * A patient should be earlier in the ordering if they should be treated first.
     * The ordering can depend on the priority and the arrival time.
     */
    public int compareTo(Patient other){
        if (this.priority < other.priority) { return -1; }
        if (this.priority > other.priority) { return 1; }
        return 0;
    }

    /** toString */
    public String toString(){
        return name+", pri:"+priority+", Ar:"+arrival+"  "+ treatment+ ": " +
            treatmentTime+ " wait: "+ waitTime;
    }

    /**
     * Draw the patient
     */
    public void redraw(double x, double y){
        if (priority == 1) UI.setColor(Color.red);
        else if (priority == 2) UI.setColor(Color.orange);
        else UI.setColor(Color.green);
        UI.fillOval(x-3, y-28, 6, 8);
        UI.fillRect(x-3, y-20, 6, 20);
        UI.setColor(Color.black);
        UI.drawOval(x-3, y-28, 6, 8);
        UI.drawRect(x-3, y-20, 6, 20);
        UI.setFontSize(10);
    }


    // Creating random names and treatments

    /**
     * Create a sequence of random treatments in trts and times
     * The sequence is influenced by priority of the patient:
     *  - high priority patients are more likely to need the operating
     *    theatre first, and a more complicated treatment sequence.
     *  low priority patients are more likely to just need an ERbed treatment.
     */
    public void makeRandomTreatment(){
        //
        if (random.nextDouble()<PROB_DOCTOR) {
            treatment = "Doctor";
            treatmentTime = randomTreatmentTime(15);
        }
        else {
            treatment = "Nurse";
            treatmentTime = randomTreatmentTime(20);
        }
    }


    /**
     * Returns a random treatment time with the given median
     * (half the treatment times will be below the median; half above).
     * Always at least 1 tick; but some take a long time.
     * (Based on a log-normal distribution, mu=0, sigma=0.6.
     *  increase sigma to spread it out more)
     */
    public int randomTreatmentTime(int median){
        double sigma = 0.6;
        double logNorm = Math.exp(sigma*random.nextGaussian());
        int m = Math.max(0, median-1);
        return (int)(1 + m*logNorm);
    }


    /**
     * Create a random name using the lists below
     */
    public void makeRandomName(){
        name = names1[random.nextInt(names1.length)]+" "+names2[random.nextInt(names2.length)];
    }


    

    private String[] names1 =
    {"Lisa","Ramon","Janet","Catherine","Chris","Wokje","Thuong","Andrea",
     "Manjeet","Toby","Philip","Bing","Renee","Derek","David","John",
     "Christian","Yongxin","Charles","Michael","Colin","Helen","Mansoor","Rod",
     "Todd","Dan","Colin","Shirley","Alex","John","Michael","Peter",
     "Paul","Ian","Jenny","Bob","Jeffrey","Joanna","Kathryn","Andy",
     "Inge","Maree","Rosie","Joanne","Yau","Rebecca","Robyn","Christine",
     "Guy","Christina","Tirta","Ruiping","Victoria","Bernadette","Catherine","Mo",
     "Tom","Natalie","Harold","Dimitrios","Alexander","James","Michael",
     "Yu-Wei","Emily","Christian","Alia","Zohar","Kimberly","Ocean","Yi",
     "Jamy","Travis","Deborah","Kim","Linda","Gillian","Bronwyn","Bruce",
     "Miriam","Gillian","Jenny"};

    private String[] names2 =
    {"Alcock","Ansell","Armstrong","Bai","Bates","Biddle","Bradley","Brunt","Calvert",
     "Chawner","Cho","Clark","Coxhead","Cullen","Daubs","Day","Dinica","Downey",
     "Dunbar","Elinoff","Fortune","Gabrakova","Geng","Goreham","Groves","Hall",
     "Harris","Hodis","Horgan","Hunt","Jackson","Jones","Keane-Tuala","Khaled",
     "Kidman","Krtalic","Laufer","Levi","Locke","Mackay","Marquez","Maskill",
     "Maxwell","McCrudden","McGuinness","McMillan","Mei","Millington","Moore",
     "Murphy","Nelson","Niemetz","O'Hare","Owen","Pearce","Perris","Pirini",
     "Pratt","Randal","Reilly","Rimoni","Robinson","Ruck","Schipper",
     "Servetto","Shuker","Skinner","Speedy","Stevens","Sweet","Taylor",
     "Terreni","Timperley","Turetsky","Vignaux","Wallace","Welch","Wilson",
     "Ackerley","Adds","Anderson","Anslow","Antunes","Armstrong","Arnedo-Gomez",
     "Bacharach","Bai","Barrett","Baskerville","Bennett","Berman","Boniface",
     "Boston","Brady","Bridgman","Brunt","Buettner","Calhoun","Calvert",
     "Capie","Carmel","Chiaroni","Chicca","Chu","Chu","Clark",
     "Clayton","Coxhead","Craig","Cuffe","Cullen","Dalli","Das",
     "Davidson","Davies","Desai","Devue","Dinneen","Dmochowski","Downey",
     "Doyle","Dumitrescu","Dunbar","Elgort","Elias","Faamanatu-Eteuati","Feld",
     "Fraser","Frean","Galvosas","Gamble","Geng","George","Goh",
     "Goreham","Gregory","Grener","Guy","Haggerty","Hammond","Hannah",
     "Harvey","Haywood","Hodis","Hogg","Horgan","Horgan","Hubbard",
     "Hui","Ingham","Jack","Johnston","Johnston","Jordan","Joyce",
     "Keane-Tuala","Kebbell","Keyzers","Khaled","Kiddle","Kiddle","Kirkby",
     "Knewstubb","Kuehne","Lacey","Leah","Leggott","Levi","Lindsay",
     "Loader","Locke","Lynch","Ma","Mallett","Mares","Marriott",
     "Marshall","Maslen","Mason","Maxwell","May","McCarthy","McCrudden",
     "McDonald","McGregor","McKee","McKinnon","McNeill","McRae","Mercier",
     "Metuarau","Millington","Mitsotakis","Molloy","Moore","Muaiava","Muckle",
     "Natali","Neha","Newton","Nguyen","Nisa","Noakes-Duncan","",
     "Ok","Overton","Park","Parkinson","Penetito","Perkins","Petkov",
     "Pham","Pivac","Plank","Price","Raman","Rees","Reichenberger",
     "Riad","Rice-Davis","Ritchie","Robb","Rofe","Rook","Ruegg",
     "Schick","Scott","Seals","Sheffield","Shewan","Sim","Simpson",
     "Smaill","Smith","Spencer","Stern","Susilo","Sutherland","Tariquzzaman",
     "Tatum","Te Huia","Te Morenga","Thirkell-White","Thomas","Tokeley","Trundle",
     "Van Belle","Van Rij","Vowles","Vry","Ward","Warren","White",
     "Whittle","Wilson","Wilson","Wood","Yao","Yu","Zareei",
     "de Saxe","de Sylva","van der Meer", "Woods","Yates","Zhang","van Zijl"
    };


}