import ecs100.*;
import java.util.*;
import java.io.*;


/** VocabularySet
 * Count vocabulary with a Set
 */

public class VocabularySet{


    /**
     * Construct a new VocabularySet object
     */
    public VocabularySet(){
        setupGUI();
    }

    /**
     * Initialise the interface
     */
    public void setupGUI(){
        UI.addButton("Analyse", () -> {analyse(UIFileChooser.open());});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }


    /**
     * Count and print the number of words and the number of
     * distinct words in a file.
     */
    public void analyse(String filename){
        Set<String> vocab = new HashSet<String>();
        int count = 0;
        UI.printf("Book: %s \n", filename);
        try{
            Scanner sc = new Scanner(new File(filename));
            long startTime = System.currentTimeMillis();
            while (sc.hasNext()){
                String word = sc.next();
                count++;
                vocab.add(word);
            }
            long endTime = System.currentTimeMillis();
            UI.printf(" Word count: %,d \n", count);
            UI.printf(" Vocabulary: %,d \n", vocab.size());
            UI.printf(" Time:       %,d mS\n", (endTime-startTime));
            
        }
        catch(IOException e){UI.println("Fail: " + e);}
    }







    public static void main(String[] arguments){
        new VocabularySet();
    }	


}