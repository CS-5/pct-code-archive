
/*
Carson Seese - CIT260-02 - Text Processing
03-22-19

This program reads a text file and performs several operations:

	1. Turns all words to lowercase
	2. Removes all words with less that 4 characters
	3. Remove words containing digits
	4. Remove words listed in the STOP_WORDS array
	5. Remove duplicate words
	6. Sorts words alphabetically

Following the above operations, it prints the total number of words and the average word length.
It also prints the words in several pages with 80 words per-page (20 rows, 4 columns).
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

	private final static String INPUT_FILE = "ALICES ADVENTURES IN WONDERLAND.txt"; // The path of the file being read
	private final static String TOKENS = " \t\n\r\f\",.:;?![]‘()*-/“”"; // Tokens used to split the data being read
	private final static String INVALID_CHARACTERS = "\\p{C}"; // The regex used to select non-printable characters
	private final static int MIN_SIZE = 4; // Minimum word size to keep
	private final static String NUMBERS_PATTERN = "([a-z]*[0-9])\\w*"; // The regex used to select words with numbers
	private final static String[] STOP_WORDS = { "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you",
			"your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it",
			"its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
			"these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do",
			"does", "did", "doing", "would", "should", "could", "ought", "i’m", "you’re", "he’s", "she’s", "it’s", "we’re",
			"they’re", "i’ve", "you’ve", "we’ve", "they’ve", "i’d", "you’d", "he’d", "she’d", "we’d", "they’d", "i’ll",
			"you’ll", "he’ll", "she’ll", "we’ll", "they’ll", "isn’t", "aren’t", "wasn’t", "weren’t", "hasn’t", "haven’t",
			"hadn’t", "doesn’t", "don’t", "didn’t", "won’t", "wouldn’t", "shan’t", "shouldn’t", "can’t", "cannot", "couldn’t",
			"mustn’t", "let’s", "that’s", "who’s", "what’s", "here’s", "there’s", "when’s", "where’s", "why’s", "how’s", "a",
			"an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with",
			"about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from",
			"up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there",
			"when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no",
			"nor", "not", "only", "own", "same", "so", "than", "too", "very" };
	private final static int PAGE_ROWS = 20; // Number of rows to display per-page. Can be changed.
	private final static int PAGE_COLUMNS = 4; // This value should effectively be hardcoded since changing this here does
																							// not change the number of columns created. It only effects the math used
																							// when calculating words-per-page

	public static void main(String[] args) {
		// Read the file into an ArrayList for processing
		ArrayList<String> words = new ArrayList<>();
		try {
			words = tokenizeFile(INPUT_FILE, TOKENS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Convert all words to lowercase and replace all non-printable characters
		for (int i = 0; i < words.size(); i++)
			words.set(i, words.get(i).toLowerCase().replaceAll(INVALID_CHARACTERS, ""));

		// Remove words less than 4 characters
		words.removeIf(u -> u.length() < MIN_SIZE);

		// Remove words with any numbers
		words.removeIf(Pattern.compile(NUMBERS_PATTERN).asPredicate());

		// Remove all stop words
		words.removeAll(Arrays.asList(STOP_WORDS));

		// Remove all duplicates
		words = (ArrayList<String>) words.stream().distinct().collect(Collectors.toList());

		// Alphabetize
		words.sort(Comparator.naturalOrder());

		// Calculate average word length and the longest word in the file (used to
		// dynamically scale the pages in the terminal)
		double totalChars = 0;
		int longestWord = 0;
		for (int i = 0; i < words.size(); i++) {
			int wl = words.get(i).length();
			totalChars += wl;
			if (wl > longestWord)
				longestWord = wl;
		}

		// Print total words and average word length
		System.out.println("Total number of words: " + words.size());
		System.out.println("Average word length: " + new DecimalFormat("#.0").format(totalChars / words.size()));

		// Print out the pages of data
		System.out.println(buildPages(words, PAGE_ROWS, PAGE_COLUMNS, longestWord + 1));
	}

	/**
	 * A method used to build a string with pages of data.
	 * 
	 * @param words   An ArrayList of words to include in the pages
	 * @param rows    The total number of rows per-page
	 * @param columns The total number of columns per-page
	 * @param width   The width each column should be (can be static, or set by the
	 *                longest word)
	 * @return A string containing the formatted pages
	 */
	public static String buildPages(ArrayList<String> words, int rows, int columns, int width) {
		// Calculate the total number of words that will be on a page. Use that value to
		// determine how many pages to make
		Double wordsPerPage = (double) rows * columns;
		int pages = (int) Math.ceil(words.size() / 80); // Round up to the next whole number

		// Build a 2d array of strings representing the words on each page
		String[][] pageWords = new String[pages][wordsPerPage.intValue()];
		int wordIndex = 0;
		for (int p = 0; p < pageWords.length; p++) {
			for (int w = 0; w < pageWords[p].length; w++) {
				// If wordIndex is larger than the total number of words, set the remaining
				// values of the array to blanks
				if (wordIndex < words.size()) {
					pageWords[p][w] = words.get(wordIndex);
					wordIndex++;
				} else {
					pageWords[p][w] = "";
				}
			}
		}

		// Since the column width changes dynamically, the upper and lower boarders will
		// need to change dynamically as well
		StringBuilder spaces = new StringBuilder();
		for (int i = 1; i <= width * 4 + 3; i++) {
			spaces.append("-");
		}

		// Format the output page by page
		StringBuilder out = new StringBuilder("\n");
		for (int p = 0; p < pageWords.length; p++) {
			// Make new string builder for just this page and setup the page header
			StringBuilder page = new StringBuilder("Page " + (p + 1) + "\n+-" + spaces + "-+\n");
			// For each row, generate the contents, with dynamic column width adjustment.
			for (int r = 0; r < rows; r++) {
				page.append(String.format("| %-" + width + "s %-" + width + "s %-" + width + "s %-" + width + "s |\n", pageWords[p][r], pageWords[p][rows + r], pageWords[p][rows * 2 + r], pageWords[p][rows * 3 + r]));
			}

			// Append the page to the total output string
			page.append("+-" + spaces + "-+\n");
			out.append(page + "\n");
		}

		// Return the formatted string
		return out.toString();
	}

	/**
	 * Takes an input file and returns an ArrayList of it's contents as determined
	 * by the specified tokens
	 * 
	 * @param fileName The path/name of the input file
	 * @param tokens   The token string that determines how to split up the file's
	 *                 contents
	 * @return An ArrayList of the contents of the file
	 * @throws Exception
	 */
	public static ArrayList<String> tokenizeFile(String fileName, String tokens) throws Exception {
		// Initialize local variables
		ArrayList<String> words = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		StringTokenizer tokenizer;
		String line;

		// While the file still has lines, read them into the words ArrayList using the
		// specified tokens
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line, tokens);
			while (tokenizer.hasMoreTokens())
				words.add(tokenizer.nextToken());
		}

		br.close();
		return words;
	}
}