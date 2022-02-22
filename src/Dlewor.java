import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Dlewor {

    // constants to allow colored text and backgrounds
    // Hello World for colored text and background
    // System.out.print(ANSI_GREEN_BACKGROUND + ANSI_BLACK + "Hello ");
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void guessWord(String targetWord, String [] commandLineInput, ArrayList<String> wordsRange) {
        Scanner scnr = new Scanner(System.in);
        int[] matchInfo;
        int round = 1;

        while (round <= 6) {
            //User Input
            System.out.print("Enter word (" + round + "): ");
            String userInput = scnr.next();
            if (userInput.length() != 5) {
                System.out.println("Please input a five characters word!");
                continue;
            }

            // Determine the search method based on file type
            boolean wordFound;
            if (commandLineInput[0].equals("vocab.nytimes.random.txt")) {
                wordFound = linearSearch(userInput, wordsRange, 0, wordsRange.size()-1);
            } else { // For sorted file
                wordFound = binarySearch(userInput, wordsRange, 0, wordsRange.size()-1);
            }

            //Check whether if the word exists in the dictionary and perform comparisons
            if (wordFound) {
                round = round + 1;
                matchInfo = matchDlewor(targetWord, userInput);
                printDelwor(matchInfo, userInput);
                System.out.println();
            } else {
                System.out.println("Not in word list, please try again");
                continue;
            }
        }

        // Failed to guess the correct answer after six trail
        System.out.println("Six trials have run out, please try again next time!");
        System.out.println("The correst answer is: " + targetWord);
    }

    public static void printDelwor(int[] matchInfo, String userInput) {
        for (int i=0; i<userInput.length(); i++) {
            if (matchInfo[i] == 0) {
                System.out.print(ANSI_WHITE_BACKGROUND + ANSI_BLACK + userInput.charAt(i));
            }
            if (matchInfo[i] == 1) {
                System.out.print(ANSI_YELLOW_BACKGROUND + ANSI_BLACK + userInput.charAt(i));
            }
            if (matchInfo[i] == 2) {
                System.out.print(ANSI_GREEN_BACKGROUND + ANSI_BLACK + userInput.charAt(i));
            }
        }
        System.out.print(ANSI_RESET);

        // Found match case
        if (foundMatch(matchInfo)) {
            System.out.println();
            System.out.println("Yesss!!! You find the answer!");
            System.exit(-1);
        }
    }

    public static boolean foundMatch(int[] matchInfo) {
        int countSuccess = 0;
        int index = 0;
        while (index < matchInfo.length) {
            if (matchInfo[index] == 2) {
                countSuccess++;
            }
            index++;
        }
        if (countSuccess == matchInfo.length) {
            return true;
        } else {
            return false;
        }
    }

    public static int[] matchDlewor(String targetWord, String userInput) {
        // Create array for match info, 2 means both index and character are matched, 1 means only correct letter, 0 means not matched
        int[] matchInfo = new int[userInput.length()];
        for (int i=0; i<matchInfo.length; i++) {
            matchInfo[i] = 0; // Initialize match array with all 0
        }
        // Create Objects for word
        Word target = new Word(targetWord);
        char[] targetChar = new char[targetWord.length()];
        for (int i=0; i<targetWord.length(); i++) {
            targetChar[i] = targetWord.charAt(i);
        }
        target.setCharOfWordArray(targetChar);

        Word UserInput = new Word(userInput);
        char[] UserChar = new char[userInput.length()];
        for (int i=0; i<userInput.length(); i++) {
            UserChar[i] = userInput.charAt(i);
        }
        UserInput.setCharOfWordArray(UserChar);

        // Find whether the characters are correct, assign 1
        for (int i=0; i<userInput.length(); i++) {
            if (target.checkIfHasChar(userInput.charAt(i))) {
                matchInfo[i] = 1;
            }
        }

        // Find whether it exists perfect match, assign 2
        for (int i=0; i<userInput.length(); i++) {
            if (target.getCharAtIndex(i) == UserInput.getCharAtIndex(i)) {
                matchInfo[i] = 2;
            }
        }
        //test
//        for (int i=0; i<userInput.length(); i++) {
//            System.out.println(matchInfo[i]);
//        }
        return matchInfo;
    }

    public static boolean linearSearch(String userInput, ArrayList<String> wordsRange, int left, int right) {
        if (wordsRange.get(left).equals(userInput) || wordsRange.get(right).equals(userInput)) {
            return true;
        }
        if (right < left){
            return false;
        }
        return linearSearch(userInput, wordsRange, left+1, right-1);
    }

    public static boolean binarySearch(String userInput, ArrayList<String> wordsRange, int left, int right) {
        int mid = (left + right)/2;

        if (left > right) {
            return false;
        }
        if (userInput.compareTo(wordsRange.get(mid)) < 0) {
            return binarySearch(userInput, wordsRange, left, mid - 1);
        }
        if (userInput.compareTo(wordsRange.get(mid)) > 0) {
            return binarySearch(userInput, wordsRange, mid + 1, right);
        }
        if (userInput.equals(wordsRange.get(mid))){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        //Read in files by command-line input
        String filename = null;
        FileReader paper = null;

        if (args.length == 1) {
            filename = args[0];
            try {
                paper = new FileReader("src/" + filename);
            } catch (FileNotFoundException e) {
                System.out.println("Input file does not exist, please try again.");
            }
        } else {
            System.out.println("Please enter one filename only");
            System.exit(-1);
        }

        //Read in files line by line, select five characters word only
        ArrayList<String> wordsRange = new ArrayList<String>();
        Scanner scan = new Scanner(paper); //set the scanner to read from a file
        while (scan.hasNextLine()) {
            String newWord = scan.nextLine();
            if (newWord.length() == 5) { //Select five characters word only
                wordsRange.add(newWord);
            }
        }

        //Welcome greeting and random selection of word
        System.out.println("Welcome to Dlewor(TM)!");
        System.out.println("Guess a five-letter word!");
        Random ran = new Random();
        int index = ran.nextInt(wordsRange.size());
        String targetWord = wordsRange.get(index);
        //System.out.println(wordsRange.get(index)); //For testing

        //Guessing starts
        guessWord(targetWord, args, wordsRange);
    }
}
