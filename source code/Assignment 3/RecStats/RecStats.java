import ecs100.*;
import java.util.*;
import java.awt.Color;

public class RecStats{

    List<Double> numbers = new ArrayList<Double>();

    public RecStats(){
        UI.addButton("Numbers", this::makeNumbers);
        UI.addButton("Total", ()->{UI.println("Total = "+ total(0, numbers.size()));});
        UI.addButton("Quit", UI::quit);
    }

    public void makeNumbers(){
        numbers.clear();
        for (int i=0; i<100; i++){
            numbers.add(Math.random()*50);
        }
        for (int i=0; i<numbers.size(); i++){
            UI.fillRect(i*5, 100-numbers.get(i), 4, numbers.get(i));
        }
    }

    /*
     * Return the total of the values in numbers between start (inclusive) and end (exclusive)
     */
    public double total(int start, int end){
        if( start< end){
            return numbers.get(start) +total(start+1, end); 
        }
        else{
            return 0;  // no numbers in this region so total for this region is 0
        }
    } 

    /*
     * Return the total of the values in numbers between start (inclusive) and end (exclusive)
     */
    public double totalByHalf(int start, int end){
        if (end == start+1){
            return numbers.get(start);
        }
        else {
            int mid = (start+end)/2;
            return  total(start, mid)+ total(mid,end);
        }
    }

    /*
     * Return the maximum of the values in numbers between start (inclusive) and end (exclusive)
     */
    public double max(int start, int end){
        if( start==end-1){  //just one number
            return numbers.get(start);
        }
        else{
            double first = numbers.get(start);
            double maxRest = max(start+1, end);
            if (first > maxRest) {return first; }
            else                 {return maxRest; }
        }
    }


    /*
     * Return the maximum of the values in numbers between start (inclusive) and end (exclusive)
     */
    public double maxByHalf(int start, int end){
        if (end == start+1){
            return numbers.get(start);
        }
        else {
            int mid = (start+end)/2;
            double left = max(start, mid);
            double right = max(mid,end);
            if (left>right) { return left;}
            else            { return right; }
        }
    }

    public static void main(String[] args){
        new RecStats();
    }
}
