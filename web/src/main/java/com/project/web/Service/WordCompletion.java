package com.project.web.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Defines a node in the trie
class TrieNode {
    // Each node stores its children in a map, keyed by the character
    Map<Character, TrieNode> childNodes;
    // Flag to indicate whether this node marks the end of a word
    boolean marksEndOfWord;

    public TrieNode() {
        childNodes = new HashMap<>();
        marksEndOfWord = false;
    }
}

// The trie data structure for storing strings, renamed to match the file name WordCompletion.java
public class WordCompletion {
    private TrieNode rootNode;

    public WordCompletion() {
        rootNode = new TrieNode();
    }

    // Inserts a word into the trie
    public void insertWord(String word) {
        TrieNode currentNode = rootNode;
        for (char letter : word.toCharArray()) {
            // For each character, move down the trie to the child node,
            // creating new nodes as necessary
            currentNode = currentNode.childNodes.computeIfAbsent(letter, k -> new TrieNode());
        }
        // Mark the last node as the end of a word
        currentNode.marksEndOfWord = true;
    }

    // Returns a list of all words in the trie that start with the given prefix
    public List<String> findCompletionsForPrefix(String prefix) {
        List<String> completionsList = new ArrayList<>();
        TrieNode currentNode = rootNode;
        for (char letter : prefix.toCharArray()) {
            // Navigate the trie to the end of the prefix
            TrieNode nextNode = currentNode.childNodes.get(letter);
            if (nextNode == null) {
                // If the prefix is not present, return an empty list
                return completionsList;
            }
            currentNode = nextNode;
        }
        // Recursively find all words that extend the prefix
        findAllWordsFromNode(currentNode, prefix, completionsList);
        return completionsList;
    }

    // Helper method to recursively find all words that start with a given prefix
    private void findAllWordsFromNode(TrieNode node, String wordSoFar, List<String> results) {
        if (node.marksEndOfWord) {
            // If the node marks the end of a word, add the word to the results list
            results.add(wordSoFar);
        }
        // Recursively search through all possible paths
        for (Map.Entry<Character, TrieNode> entry : node.childNodes.entrySet()) {
            findAllWordsFromNode(entry.getValue(), wordSoFar + entry.getKey(), results);
        }
    }
}