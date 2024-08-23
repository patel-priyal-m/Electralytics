package com.project.web.Controllers;

import com.project.web.Models.Product;
import com.project.web.Models.RequestSchema;
import com.project.web.Service.InvertedIndexingWithHTMLParser;
import com.project.web.Service.ProductService;
import com.project.web.Service.SpellChecking;
import com.project.web.Service.WordCompletion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ProductController {


	@Value("${resources.path}")
	String resourcesPath;
	@Autowired
	ProductService productService;

	@Value("${word.completion.path}")
	String filePath;

	Map<String,Integer> wordSearched = new TreeMap<>();



	@GetMapping("/home")
	public String getController() {
		return "Hello world";
	}

	@PostMapping("/search")
	public ResponseEntity searchProduct(@RequestBody RequestSchema product) {

		boolean isValid = inputValidation(product.getProduct());
		if (isValid) {
			String spellChecker = spellChecker(product.getProduct());
			String finalProduct = wordCompletion(spellChecker);
			invertedIndexing(finalProduct);
			calculateWordSearchFrequency(finalProduct);

			ResponseEntity<Map<String, List<Product>>> allProducts = productService.getAllProducts(finalProduct);

		return allProducts;
	}
		return new ResponseEntity<String>("Please input valid text.",HttpStatus.OK);
	}

	public boolean inputValidation(String product){

		// Regex to remove punctuation
		String regexToMatch = "^[A-Za-z,'0-9]+";
		boolean isValid = Pattern.matches(regexToMatch,product.replaceAll("\\s+",""));
		return isValid;
	}
//	@GetMapping("/invertedindexing")
	public void invertedIndexing(String product){

		InvertedIndexingWithHTMLParser indexer = new InvertedIndexingWithHTMLParser();


		// The names of the subdirectories
		String[] subDirectories = {"amazon", "bestbuy", "visions"};

		try

		{
			// Process each subdirectory in the resources folder
			for (String subDir : subDirectories) {
				String fullPath = Paths.get(resourcesPath, subDir).toString();
				System.out.println("Processing directory: " + fullPath);
				indexer.processHTMLFilesFromDirectory(fullPath);

				//				// Example search
//				String searchTerm = product;



				if (product.contains(" ")){
					int searchResults = 0;
					searchResults = indexer.getIndexOfPage(fullPath, product);
					if (searchResults == -1) {
						System.out.println("The product was not found in any file.");
					} else {
						System.out.println(subDir+" has "+searchResults+" documents containing "+product+".");
					}
				}
				else{
					Set<String> searchResults = indexer.search(product);
					System.out.println(subDir+" has "+searchResults.size()+" documents containing "+product+".");
					searchResults.clear();

				}
			}
		} catch(IOException e)
		{
			System.out.println("An error occurred while processing the directories.");
		}
	}


	private void calculateWordSearchFrequency(String product){
		int count = 0;
		if(wordSearched.containsKey(product)){
			count = wordSearched.get(product);
			count++;
		}
		wordSearched.put(product,count);
		System.out.println(product + " has been searched for "+wordSearched.get(product)+" times previously.");
	}

	public String wordCompletion(String searchPrefix){
		WordCompletion productTrie = new WordCompletion();
		String finalProduct=searchPrefix;


		// Attempt to read product names from the specified file
		try (BufferedReader productReader = new BufferedReader(new FileReader(filePath))) {
			String productName; // Variable to hold each read line (product name)
			// Read each product name line by line until the end of the file
			while ((productName = productReader.readLine()) != null) {
				// Insert the trimmed product name into the trie for future searching
				productTrie.insertWord(productName.trim());
			}
		} catch (IOException ioException) {
			// Print an error message if there was an issue reading the file
			System.err.println("Error reading from file: " + ioException.getMessage());

//			ioException.printStackTrace();
		}



		// Find all completions in the trie that match the given prefix
		List<String> productCompletions = productTrie.findCompletionsForPrefix(searchPrefix);

		// Check if there are any completions for the provided prefix
		if (!productCompletions.isEmpty()) {
			productCompletions.add(0,"Continue with "+searchPrefix+" .");
			System.out.println("Completions for \"" + searchPrefix + "\":");
			// Iterate over the completions and print each one with an index
			for (int i = 0; i < productCompletions.size(); i++) {
				System.out.println((i + 1) + ". " + productCompletions.get(i));
			}
			int userChoice = readUserInput(productCompletions.size());
			// Validate the user's choice and respond accordingly
			if (userChoice > 0 && userChoice <= productCompletions.size()) {
				// User's choice is valid; print the selected product
				finalProduct = productCompletions.get(userChoice - 1);
				System.out.println("You selected: "+finalProduct);
			} else {
				// User's choice is invalid; print an error message
				System.out.println("Invalid choice. Exiting.");
			}
		}
//		else if(!productCompletions.isEmpty() && productCompletions.contains(searchPrefix)){
//			System.out.println();
//		}
		else {
			// No completions were found for the provided prefix
			System.out.println("No completions found for: " + searchPrefix);
		}

		// Close the scanner to prevent resource leaks
//		userInputScanner.close();
		return finalProduct;
	}

	public String spellChecker(String itemToSearch){
		SpellChecking spellChecking = new SpellChecking();
		String spellCheckerWord=itemToSearch;

		// minimum threshold value
		int threshold = 2;
		// Store suggestions in the list
		List<String> suggestions =spellChecking.suggestCorrections(itemToSearch, threshold);

		// If no suggestions found
		if (suggestions.isEmpty()) {
			System.out.println("No corrections found.");

		}
		if(!suggestions.isEmpty()) {
			suggestions.add(0,"Continue with "+itemToSearch+" .");
			System.out.println("Suggested corrections:");
			for (int i = 0; i < suggestions.size(); i++) {
				System.out.println((i + 1) + ". " + suggestions.get(i));
			}
			int userChoice = readUserInput(suggestions.size());

			// Validate the user's choice and respond accordingly
			if (userChoice > 0 && userChoice <= suggestions.size()) {
				// User's choice is valid; print the selected product
				spellCheckerWord = suggestions.get(userChoice - 1);
				System.out.println("You selected: "+spellCheckerWord);
			} else {
				// User's choice is invalid; print an error message
				System.out.println("Invalid choice. Exiting.");
			}
		}
		return spellCheckerWord;
	}


	public int readUserInput(int size){
		// Prepare to read input from the user
		int userChoice =0 ;
		Scanner userInputScanner = new Scanner(System.in);
		try{
			while(userChoice <=0 || userChoice>size){
				// Ask the user to select one of the completions by its index
				System.out.println("Enter the number of the product you were looking for (From the given options): ");

				if(userInputScanner.hasNextInt()) {
					userChoice = userInputScanner.nextInt();
				}
			}
		}catch (Exception e){
//			e.printStackTrace();
			System.out.println("Exception while reading input!");
		}
		return userChoice;
	}
}
