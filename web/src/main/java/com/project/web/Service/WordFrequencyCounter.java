package com.project.web.Service;


import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import java.nio.file.Paths;
@Component
public class WordFrequencyCounter {


    //Function:calculate Frequency
    //Input: String origin, String inputStr, int minStrLength, String product
    //Output: int
    public static int calculateFrequency(String origin, String inputStr, int minStrLength, String product) {
        Map<String, Integer> frequencyCounter = new HashMap<>();

        // Punctuation removal via regular expression
        String regexToMatch = "[.,!?;:'']";

        // Instantiated Pattern object
        Pattern patternToMatch = Pattern.compile(regexToMatch);

        // Instantiated Matcher object
        Matcher patternMatcher = patternToMatch.matcher(inputStr);

        // remove punctuation
        String output = patternMatcher.replaceAll("");

        // Instantiated tokenizer
        StringTokenizer tokenizer = new StringTokenizer(output);

        // create token and add into the map with frequency
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken().toLowerCase();

            if (!(frequencyCounter.containsKey(token))) {
                if (token.length() > minStrLength) {
                    frequencyCounter.put(token, 1);
                }
            } else {
                int value = frequencyCounter.get(token);
                frequencyCounter.put(token, value + 1);
            }
        }
        int productFrequency = printWordFrequencyAndLexicalRichness(origin,frequencyCounter,product);
        return productFrequency;
    }


    //Function:print word frequency and lexical richness
    //Input: String origin, Map<String,Integer> frequencyCounter, String product
    //Output: int
    public static int printWordFrequencyAndLexicalRichness(String origin, Map<String, Integer> freuencyCounter, String product) {
        System.out.println("Frequency of "+product);
        int productFrequency =0;
        try{
            if (product.contains(" ")){
                productFrequency = getFrequencyCount(origin,product);
                System.out.println(productFrequency);
            }
            else {
                productFrequency = freuencyCounter.get(product.toLowerCase());
                System.out.println(productFrequency);
            }
        }catch (Exception e){
            System.out.println(product+" not found.");
        }

        //Calculating lexical richness of a document
        //lexical richness = total word in the document/unique words * 100 %

        float totalWords = 0;
        float uniqueWords = 0;
        float lexicalRichness = 0;
        totalWords = freuencyCounter.values().stream().reduce(0, Integer::sum);
        for (String key : freuencyCounter.keySet()) {
            if (freuencyCounter.get(key) == 1) {
                uniqueWords += 1;
            }
        }

        lexicalRichness = (uniqueWords / totalWords) * 100;
        System.out.println("Lexical richness of the document is: " + lexicalRichness + "%");
        return productFrequency;
    }

    //Function:get frequency counter
    //Input: String origin, String product
    //Output: int
    private static int getFrequencyCount(String origin, String product) {
        int count = 0;
       String filePath = "C:\\\\Users\\\\admin\\\\Desktop\\\\git\\\\ACC_Project\\\\web\\\\src\\\\main\\\\resources\\\\HTML templates" +"\\"+origin+"\\"+product+"_"+origin+".html";
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Document doc = Jsoup.parse(content);
            String text = doc.body().text().toLowerCase();

            String[] parts = text.split(product.toLowerCase(), -1);
            count = parts.length - 1;
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Exception occured during file reading!");
        }
        return count;
    }

}