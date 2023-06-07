import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Assn5 {
    public static Term binarySearch(ArrayList<Term> termArray, int left, int right, String search) {
        if (right >= left) {
            int middle =  left + (right - left) / 2;

            // Check to make sure the search term is not longer than the current array term
            if (search.length() <= termArray.get(middle).getWord().length()) {
                // Check if element at center of search
                if (search.equals(termArray.get(middle).getWord().substring(0, search.length()))) {
                    return termArray.get(middle);
                }
            }

            // If element smaller than middle, search left subtree
            if (termArray.get(middle).getWord().compareTo(search) > 0) {
                return binarySearch(termArray, left, middle - 1, search);
            }

            // Else search the right subtree
            return binarySearch(termArray, middle + 1, right, search);
        }

        // Else, element is not in the list
        return null;
    }

    public static void main(String[] args){

        // Read in SortedWords.txt file into an array
        ArrayList<Term> wordTextArray = new ArrayList<>();

        try {
            File wordFile = new File("SortedWords.txt");
            Scanner fileReader = new Scanner(wordFile);
            String totalTerms = fileReader.nextLine();
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String[] stringArray = data.split("\\s+");

                Term newTerm = new Term(stringArray[1], Long.parseLong(stringArray[2]));
                wordTextArray.add(newTerm);
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }


        // Receive user input
        System.out.println("Welcome to the auto-correct word finder.");
        System.out.println("Please enter the prefix you'd like to search for: ");
        System.out.println("(Separate multiple prefixes by ' | ' to look up any number prefixes)");
        System.out.println("(i.e.: >> ab | ca | ba )");

        Scanner userInput = new Scanner(System.in);
        String prefix = userInput.nextLine();

        System.out.println("Please enter the number of matches you'd like to display: ");
        int count = Integer.parseInt(userInput.nextLine());


        // Create list of all words that match prefix while removing from overall list
        String[] prefixInputs = prefix.split(" \\| ");

        ArrayList<Term> prefixMatchArray = new ArrayList<>();
        for (int i = 0; i < wordTextArray.size(); i++) {
            for (String string: prefixInputs){
                Term foundWord = binarySearch(wordTextArray, 0, wordTextArray.size() - 1, string);
                if (foundWord == null) break;

                prefixMatchArray.add(foundWord);
                wordTextArray.remove(foundWord);
            }
        }

        System.out.println("There are " + prefixMatchArray.size() + " total words matching your prefix.");

//        Debugging test statements for full list of matching prefixes
//        System.out.println("All words with prefix substring \"" + prefix + "\": ");
//        System.out.println(prefixMatchArray);


        //Create the heap
        LeftistHeap wordMatches = new LeftistHeap();
        for (Term term: prefixMatchArray) {
            wordMatches.insert(term);
        }


        // Print output statement for the sorted matches in the leftist heap max queue
        System.out.println("The leftist-heap max-queue tree: ");
        System.out.println(wordMatches.toString());

        // Create a list of just count-# words while removing from heap
        ArrayList<Term> formattedList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            formattedList.add(wordMatches.deleteMax());
            if (wordMatches.isEmpty()) break;
        }

        // Print out count-# of words
        System.out.println(formattedList.size() + " Heap-sorted words from most to least likely to be used for prefix \"" + prefix + "\": ");
        for (Term term : formattedList) {
            if (term != null) {
                System.out.print(term.toString());
            }
        }
    }


}
