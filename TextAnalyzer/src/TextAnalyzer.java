import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * TextAnalyzer - A console-based tool for analyzing text input.
 * Performs character count, word count, frequency analysis, and more.
 */
public class TextAnalyzer {

    // -------------------------------------------------------------------------
    // Core Analysis Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the total number of characters in the text, including spaces.
     */
    public static int countCharacters(String text) {
        return text.length();
    }

    /**
     * Returns the total number of words, assuming words are separated by spaces.
     * Trims the text first to avoid counting empty tokens.
     */
    public static int countWords(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        // Split on one or more whitespace characters
        String[] words = trimmed.split("\\s+");
        return words.length;
    }

    /**
     * Finds and returns the most common character in the text (case-insensitive).
     * Spaces are excluded from the search.
     * In the case of a tie, the first character encountered with the highest
     * frequency is returned.
     */
    public static char findMostCommonCharacter(String text) {
        String lowerText = text.toLowerCase();
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char ch : lowerText.toCharArray()) {
            if (ch != ' ') { // Exclude spaces
                frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
            }
        }

        char mostCommon = ' ';
        int highestFrequency = 0;

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > highestFrequency) {
                highestFrequency = entry.getValue();
                mostCommon = entry.getKey();
            }
        }

        return mostCommon;
    }

    /**
     * Returns how many times a given character appears in the text.
     * The search is case-insensitive.
     */
    public static int countCharacterFrequency(String text, char targetChar) {
        String lowerText = text.toLowerCase();
        char lowerTarget = Character.toLowerCase(targetChar);
        int count = 0;

        for (char ch : lowerText.toCharArray()) {
            if (ch == lowerTarget) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns how many times a given word appears in the text.
     * The search is case-insensitive and matches whole words only.
     */
    public static int countWordFrequency(String text, String targetWord) {
        String lowerText = text.toLowerCase().trim();
        String lowerTarget = targetWord.toLowerCase().trim();

        if (lowerText.isEmpty() || lowerTarget.isEmpty()) {
            return 0;
        }

        String[] words = lowerText.split("\\s+");
        int count = 0;

        for (String word : words) {
            // Strip punctuation from each word before comparing
            String cleanWord = word.replaceAll("[^a-z0-9]", "");
            if (cleanWord.equals(lowerTarget)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of unique words in the text (case-insensitive).
     */
    public static int countUniqueWords(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }

        String[] words = trimmed.toLowerCase().split("\\s+");
        Set<String> uniqueWords = new HashSet<>();

        for (String word : words) {
            // Strip punctuation before adding to the set
            String cleanWord = word.replaceAll("[^a-z0-9]", "");
            if (!cleanWord.isEmpty()) {
                uniqueWords.add(cleanWord);
            }
        }
        return uniqueWords.size();
    }

    // -------------------------------------------------------------------------
    // Input Validation Helpers
    // -------------------------------------------------------------------------

    /**
     * Prompts the user until a non-empty line of text is entered.
     */
    public static String readNonEmptyLine(Scanner scanner, String prompt) {
        String input = "";
        while (input.trim().isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("  [!] Input cannot be empty. Please try again.");
            }
        }
        return input;
    }

    /**
     * Prompts the user until exactly one character is entered.
     */
    public static char readSingleCharacter(Scanner scanner, String prompt) {
        String input = "";
        while (input.trim().length() != 1) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.trim().length() != 1) {
                System.out.println("  [!] Please enter exactly one character.");
            }
        }
        return input.trim().charAt(0);
    }

    // -------------------------------------------------------------------------
    // Display Helper
    // -------------------------------------------------------------------------

    /**
     * Prints a formatted section divider with a title.
     */
    public static void printSection(String title) {
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }

    // -------------------------------------------------------------------------
    // Main Program
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║           TEXT ANALYSIS TOOL                    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // --- Step 1: Get the main text input ---
        printSection("STEP 1: Enter Your Text");
        System.out.println("  Please enter a paragraph or lengthy text below.");
        System.out.println("  (Press ENTER when done)");
        System.out.println();
        String inputText = readNonEmptyLine(scanner, "  > ");

        // --- Step 2: Character Count ---
        printSection("STEP 2: Character Count");
        int characterCount = countCharacters(inputText);
        System.out.printf("  Total characters (including spaces): %d%n", characterCount);

        // --- Step 3: Word Count ---
        printSection("STEP 3: Word Count");
        int wordCount = countWords(inputText);
        System.out.printf("  Total words: %d%n", wordCount);

        // --- Step 4: Most Common Character ---
        printSection("STEP 4: Most Common Character");
        char mostCommon = findMostCommonCharacter(inputText);
        int mostCommonFreq = countCharacterFrequency(inputText, mostCommon);
        System.out.printf("  Most common character: '%c' (appears %d times)%n",
                mostCommon, mostCommonFreq);

        // --- Step 5: Character Frequency ---
        printSection("STEP 5: Character Frequency Search");
        System.out.println("  Enter a character to search for its frequency:");
        char targetChar = readSingleCharacter(scanner, "  > ");
        int charFrequency = countCharacterFrequency(inputText, targetChar);
        System.out.printf("  The character '%c' appears %d time(s) in the text.%n",
                targetChar, charFrequency);

        // --- Step 6: Word Frequency ---
        printSection("STEP 6: Word Frequency Search");
        System.out.println("  Enter a word to search for its frequency:");
        String targetWord = readNonEmptyLine(scanner, "  > ");
        int wordFrequency = countWordFrequency(inputText, targetWord);
        System.out.printf("  The word \"%s\" appears %d time(s) in the text.%n",
                targetWord, wordFrequency);

        // --- Step 7: Unique Words ---
        printSection("STEP 7: Unique Words");
        int uniqueWordCount = countUniqueWords(inputText);
        System.out.printf("  Number of unique words (case-insensitive): %d%n", uniqueWordCount);

        // --- Summary ---
        printSection("ANALYSIS SUMMARY");
        System.out.printf("  %-35s %d%n", "Total Characters:", characterCount);
        System.out.printf("  %-35s %d%n", "Total Words:", wordCount);
        System.out.printf("  %-35s '%c' (%d times)%n", "Most Common Character:", mostCommon, mostCommonFreq);
        System.out.printf("  %-35s '%c' → %d time(s)%n", "Searched Character Frequency:", targetChar, charFrequency);
        System.out.printf("  %-35s \"%s\" → %d time(s)%n", "Searched Word Frequency:", targetWord, wordFrequency);
        System.out.printf("  %-35s %d%n", "Unique Words:", uniqueWordCount);

        System.out.println();
        System.out.println("  Thank you for using the Text Analysis Tool!");
        System.out.println("=".repeat(50));

        scanner.close();
    }
}
