package com.project.web.Service;




import java.io.*;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;

//Class:Spell Checking
public class SpellChecking {
    @Value("${word.completion.path}")
    String filePath = "C:\\\\Users\\\\admin\\\\Desktop\\\\git\\\\ACC_Project\\\\web\\\\src\\\\main\\\\resources\\\\wordcompletion.txt";
    List<String> validWords = new ArrayList<>();

    //Function: readValidWords()
    //Input: null
    //Output: void
    public  void readValidWords(){
        // Reding dictionary

        try{
            //Instantiated file reader class
            FileReader reader = new FileReader(filePath);
            //Instantiated buffer reader class
            BufferedReader br = new BufferedReader(reader);

            String line;

            // Loops till the page end
            while((line = br.readLine()) != null){
                validWords.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }

    //Function:editDistance
    //Input:  word1, word2 :Type: (String,String)
    //Output: int
    private int editDistance(String firstWord, String secondWord) {

        int w1Length = firstWord.length();
        int w2Length = secondWord.length();

        // create multidimentional integer array
        int[][] intArray = new int[w1Length + 1][w2Length + 1];


        // Loops through length of 1st word
        for (int i = 0; i <= w1Length; i++) {
            intArray[i][0] = i;
        }


        // Loops through length of 2nd word
        for (int j = 0; j <= w2Length; j++) {
            intArray[0][j] = j;
        }
        // Loops through both words to compare each characters
        for (int i = 1; i <= w1Length; i++) {
            for (int j = 1; j <= w2Length; j++) {

                // Compare characters from both words
                if (firstWord.charAt(i - 1) == secondWord.charAt(j - 1)) {
                    intArray[i][j] = intArray[i - 1][j - 1];
                } else {
                    intArray[i][j] = Math.min(intArray[i - 1][j], Math.min(intArray[i][j - 1], intArray[i - 1][j - 1])) + 1;
                }
            }
        }

        return intArray[w1Length][w2Length];
    }

    //Function: suggest corrections
    //Input: String userInput, int threshold
    //Output: List<String>
    public List<String> suggestCorrections(String userInput, int threshold) {
        readValidWords();

        // create array list to store suggested corrections
        List<String> suggestedCorrections = new ArrayList<>();
        // Set initial min distance to the max int value
        int minDistance = Integer.MAX_VALUE;

        // Loop through validWords list
        for (String word : validWords) {
//			int distance = 0;
            if (!userInput.equalsIgnoreCase(word)) {
                int distance = editDistance(userInput.toLowerCase(), word.toLowerCase());

                // When distamce is less than minimum dis. and distance is less tah or equals threshold
                if (distance < minDistance && distance <= threshold) {
                    minDistance = distance;
                    suggestedCorrections.clear();
                    suggestedCorrections.add(word);
                }
            }

        }
        return suggestedCorrections;
    }
}
