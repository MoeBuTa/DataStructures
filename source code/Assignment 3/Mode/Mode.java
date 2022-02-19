
import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;


/** Mode   */

public class Mode{

    public static final int MAX_VALUE= 100;

    /**
    * The Mode of a set of values is the value that occurs the most often
    */
    public Mode(){
        UI.addButton("Mode", ()->{computeMode(makeNumbers(100));});
        UI.addButton("Measure", this::measure);
        UI.addButton("Quit", UI::quit);
    }


    /** Print the mode of a list of integers, and the number of times it occurred */
    public void computeMode(List<Integer> numbers){
        int mode=numbers.get(0);
        int modeCount=0;
        
 
        UI.println(mode+"("+modeCount+")");
    }



    public List<Integer> makeNumbers(int n){
        List<Integer> numbers = new ArrayList<Integer>();
        for (int i=0; i<n; i++){
            numbers.add((int)(Math.random()*MAX_VALUE));
        }
        if (n <= 100){
            for (int i=0; i<n; i++){
                UI.fillRect(i*5, MAX_VALUE+10-numbers.get(i), 4, numbers.get(i));
            }
        }
        return numbers;
    }

    public static void main(String[] arguments){
       new Mode();
    }



























    /**
     * Compute the mode of a list of numbers, by counting occurrences of each number
     */
    public int modeBad(List<Integer> numbers){
        int mode=numbers.get(0);
        int modeCount = 0;
        int sig=0;
        for (int i=0; i<numbers.size(); i++){
            int value = numbers.get(i);
            int count = 1;
            for (int k=i+1; k<numbers.size(); k++){
                if (numbers.get(k) == value){
                    count++;
                }
            }
            if (count > modeCount){
                mode = value;
                modeCount = count;
            }
            if (sig==0) UI.printMessage(""+i);
            sig++;if(sig==1000)sig=0;
        }
        return mode;
    }

    /**
     * Compute the mode of a list of numbers, by sorting and scanning
     */
    public int modeSort(List<Integer> numbers){
        Collections.sort(numbers);
        int mode=numbers.get(0);
        int modeCount = 1;
        int count = 1;                   //the count of the current value
        for (int i=1; i<numbers.size(); i++){
            int value = numbers.get(i);
            if (value == numbers.get(i-1)){  // same as last -> increment count
                count++;
            }
            else {  // different, check if lastValue is the new mode, then reset
                if (count> modeCount){
                    mode = numbers.get(i-1);
                    modeCount = count;
                }
                count = 1;
            }
        }
        //check the final one
        if (count> modeCount){
            mode = numbers.get(numbers.size()-1);
            modeCount = count;
        }
        return mode;
    }

    /**
     * Compute the mode of a list of numbers, using a map to count occurrences
     */
    public int modeMap(List<Integer> numbers){
        Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
        for (Integer num : numbers){
            if (counts.containsKey(num)){
                counts.put(num, counts.get(num)+1);
            }
            else {
                counts.put(num, 1);
            }
        }
        int mode=numbers.get(0);
        int modeCount=0;
        for (Integer num : counts.keySet()){
            if (counts.get(num)> modeCount){
                mode = num;
                modeCount = counts.get(num);
            }
        }
        return mode;
    }


    /**
     * Test the three methods:
     */
    public void test(){
        for (int i=0; i<10; i++){
            List<Integer> numbers = makeNumbers(10000);
            UI.println("Bad:   "+ modeBad(numbers));
            UI.println("Map:   "+ modeMap(numbers));
            UI.println("Sort:  "+ modeSort(numbers));
            UI.println("---------");
        }
    }

    /**
     *Measure the time to compute the mode for
     * different size lists
     * with both algorithms
     */
    public void measure(){
        UI.println("======= Using Map =============");
        for (int size=10000; size<=10240000; size=size*2){
            List<Integer> numbers = makeNumbers(size);
            System.gc();
            long start = System.currentTimeMillis();
            modeMap(numbers);
            long good = System.currentTimeMillis() - start;

            UI.printf("%,10d : %8.3f\n", size, good/1000.0);
        }
        UI.println("======= Sorting =============");
        for (int size=10000; size<=10240000; size=size*2){
            List<Integer> numbers = makeNumbers(size);
            System.gc();
            long start = System.currentTimeMillis();
            modeSort(numbers);
            long sort = System.currentTimeMillis() - start;

            UI.printf("%,10d : %8.3f\n", size, sort/1000.0);
        }
        UI.println("======= Bad =============");
        for (int size=10000; size<=10240000; size=size*2){
            List<Integer> numbers = makeNumbers(size);
            System.gc();
            long start = System.currentTimeMillis();
            modeBad(numbers);
            long bad = System.currentTimeMillis() - start;
            UI.printf("%,10d : %8.3f\n", size, bad/1000.0);
        }
    }



}
