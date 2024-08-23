package com.project.web.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndexingWithHTMLParser{
    private Map<String, Set<String>> invertedIndex;

    public InvertedIndexingWithHTMLParser() {
        this.invertedIndex = new HashMap<>();
    }

    /**
     * Parses the HTML content to extract text and then adds it to the inverted index.
     *
     * @param documentId Unique identifier for the document.
     * @param htmlContent HTML content to be parsed and indexed.
     */
    public void addHTMLDocument(String documentId, String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        String text = doc.text(); // Extracts text from HTML
        indexDocument(documentId, text);
    }

    /**
     * Indexes the provided text by updating the inverted index.
     *
     * @param documentId Unique identifier for the document.
     * @param text Text content of the document to be indexed.
     */
    private void indexDocument(String documentId, String text) {
        String[] words = text.toLowerCase().split("\\W+");
        for (String word : words) {
            invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(documentId);
        }
    }

    /**
     * Searches the inverted index for documents containing the specified term.
     *
     * @param term Search term.
     * @return Set of document IDs containing the term.
     */
    public Set<String> search(String term) {
        return invertedIndex.getOrDefault(term.toLowerCase(), Collections.emptySet());
    }

    /**
     * Processes HTML files from a directory and adds them to the inverted index.
     *
     * @param directoryPath Path to the directory containing HTML files.
     * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
     */
    public void processHTMLFilesFromDirectory(String directoryPath) throws IOException {
        Files.walk(Path.of(directoryPath))
                .filter(Files::isRegularFile)
                .forEach(filePath -> {
                    try {
                        String htmlContent = Files.readString(filePath, StandardCharsets.UTF_8);
                        addHTMLDocument(filePath.toString(), htmlContent);
                    } catch (IOException e) {
//                        e.printStackTrace();
                        System.err.println("Error reading file: " + filePath);
                    }
                });
    }
    public int getIndexOfPage(String fullPath, String product) {

        int count = 0;
        try {
            Path dir = Paths.get(fullPath);
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.html");
            for (Path entry : stream) {
                String content = new String(Files.readAllBytes(entry));
                Document doc = Jsoup.parse(content);
                if (doc.body().text().contains(product)) {
                    count++;
                }
            }
        } catch (IOException e) {
//            e.printkTrace();
            System.out.println("Exception occured during file reading!");
        }
        return count;  // Return the count of files containing the product

    }
}
