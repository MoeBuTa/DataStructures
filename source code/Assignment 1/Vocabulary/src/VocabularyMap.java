import ecs100.*;
import java.util.*;
import java.io.*;


/** VocabularyMap
 * Count vocabulary, and word frequencies.
 */

public class VocabularyMap{


    /**
     * Construct a new VocabularyMap object
     */
    public VocabularyMap(){
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
        Map<String, Integer> vocab = new HashMap<String, Integer>();
        int count = 0;
        UI.printf("Book: %s \n", filename);
        try{
            Scanner sc = new Scanner(new File(filename));
            long startTime = System.currentTimeMillis();
            while (sc.hasNext()){
                String word = sc.next();
                count++;
                int wordCount = 1;
                if (vocab.containsKey(word)){
                    wordCount = vocab.get(word)+1;
                }
                vocab.put(word, wordCount);
            }
            long endTime = System.currentTimeMillis();
            UI.printf(" Word count: %,d \n", count);
            UI.printf(" Vocabulary: %,d \n", vocab.size());
            UI.printf(" Time:       %,d mS\n", (endTime-startTime));
            //UI.println(vocab);
            List<Map.Entry<String,Integer>> freqList = new ArrayList<Map.Entry<String, Integer>>();
            freqList.addAll(vocab.entrySet());
            Collections.sort(freqList, (Map.Entry<String,Integer> w1, Map.Entry<String,Integer> w2) -> {return w1.getValue()-w2.getValue();});
            Collections.reverse(freqList);
            for (int i=0; i<100; i++){
                Map.Entry<String,Integer> entry = freqList.get(i);
                UI.printf("%,5d: %s\n", entry.getValue(), entry.getKey());
            }
        }
        catch(IOException e){UI.println("Fail: " + e);}
    }







    public static void main(String[] arguments){
        new VocabularyMap();
    }	


}