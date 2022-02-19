import ecs100.*;
import java.util.*;
import java.io.*;


/** VocabularyList   */

public class VocabularyList{


    /**
     * Construct a new VocabularyList object
     */
    public VocabularyList(){
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
     * Print out the vocabulary.
     */
    public void analyse(String filename){
        List<String> vocab = new ArrayList<String>();
        int count = 0;
        UI.printf("Book: %s \n", filename);
        try{
            Scanner sc = new Scanner(new File(filename));
            long startTime = System.currentTimeMillis();
            while (sc.hasNext()){
                String word = sc.next();
                count++;
                if (!vocab.contains(word)){
                    vocab.add(word);
                }
            }
            long endTime = System.currentTimeMillis();
            UI.printf(" Word count: %,d \n", count);
            UI.printf(" Vocabulary: %,d \n", vocab.size());
            UI.printf(" Time:       %,d mS\n", (endTime-startTime));
            UI.println(vocab);
            Collections.sort(vocab);
            UI.println(vocab);
        }
        catch(IOException e){UI.println("Fail: " + e);}
    }







    public static void main(String[] arguments){
        new VocabularyList();
    }	


}