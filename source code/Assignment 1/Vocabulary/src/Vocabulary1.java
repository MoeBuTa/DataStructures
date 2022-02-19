/* 
 * Name:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

/** <description of class Vocabulary1>
 */
public class Vocabulary1{

    /**      */
    public Vocabulary1(){
        UI.initialise();
        UI.addButton("Analyse", () -> {analyse(UIFileChooser.open());});
        UI.addButton("All", this::analyseAll);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    public void analyse(String fname){
        UI.printf("======================================\n%s\n", fname);  
        analyseHSet(fname);
        analyseTSet(fname);
        analyseList(fname);
    }
   
    /** Count words and distinct words using a HashSet */
    public void analyseHSet(String fname){
        int count = 0;
        Set<String> vocab = new HashSet<String>();
        try{
            Scanner sc = new Scanner (new File(fname));
            long start = System.currentTimeMillis();
            while (sc.hasNext()){
                String word = sc.next();
                count++;
                vocab.add(word);
            }
            sc.close();
            double time = (System.currentTimeMillis()-start)/1000.0;
            UI.printf("HSet: Time  = %6.3fs,  Words = %,10d  Vocab = %,d\n", 
                time, count, vocab.size());
        }catch(IOException e){UI.println("bad"+e);}
    }

    /** Count words and distinct words using a TreeSet */
    public void analyseTSet(String fname){
        int count = 0;
        Set<String> vocab = new TreeSet<String>();
        try{
            Scanner sc = new Scanner (new File(fname));
            long start = System.currentTimeMillis();
            while (sc.hasNext()){
                String word = sc.next();
                count++;
                vocab.add(word);
            }
            sc.close();
            double time = (System.currentTimeMillis()-start)/1000.0;
            UI.printf("TSet: Time  = %6.3fs,  Words = %,10d  Vocab = %,d\n", 
                time, count, vocab.size());
        }catch(IOException e){UI.println("bad"+e);}
    }

    /** Count words and distinct words using a List */
    public void analyseList(String fname){  
        System.gc();
        int count = 0;
        int i = 0;
        List<String> vocab = new ArrayList<String>();
        try{
            Scanner sc = new Scanner (new File(fname));
            long start = System.currentTimeMillis();
            while (sc.hasNext()){
                String word = sc.next();
                count++;
                if(!vocab.contains(word)){vocab.add(word);}
                if (i++==10000){i=0;UI.printMessage(count/1000+"k...");}
            }
            sc.close();
            double time = (System.currentTimeMillis()-start)/1000.0;
            UI.printf("List: Time  = %6.3fs,  Words = %,10d  Vocab = %,d\n", 
                time, count, vocab.size());

        }catch(IOException e){UI.println("bad"+e);}
    }

    public static void main(String[] args){
        Vocabulary1 obj = new Vocabulary1();
    }

    public void analyseAll(){
        for (String fname : new String[]{"713-MouseBirdSausage.txt", "100k-AnneOfGreenGables.txt", "500k-WarAndPeace.txt", "1M-Clarissa.txt", "2M-cyrus.txt", "5M-all.txt"}){
            analyse("texts/"+fname);
        }
    }
}