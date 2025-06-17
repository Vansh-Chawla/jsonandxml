import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.util.*;

public class CS1003P2 {
    public static void main(String[] args) {
        try {
            if (args.length < 2) { // Checks if there are less than 2 arguments
                System.out.println("Usage: java CS1003P2 <json_file> <stopwords_file> [filterKey filterValue]..."); // Displays a usage message
                return;
            }

            String jsonFilePath = args[0]; // First argument is the json file path
            String stopwordsFilePath = args[1]; // Second argument is the stopwords file path
            validateFilePath(jsonFilePath); // Validates the jsonfilepath
            validateFilePath(stopwordsFilePath); // Validates the stopwordsfilepath

            Map<String, String> filters = new LinkedHashMap<>(); // Initiates a LinkedHashMap
            for (int i = 2; i < args.length - 1; i += 2) { // Iterates only the FilterKeys
                filters.put(args[i].toLowerCase(), args[i + 1].toLowerCase()); // Converts all the filters to lower case
            }

            for (Map.Entry<String, String> entry : filters.entrySet()) { // Iterates over each entry in the filters map
                System.out.println("Filter " + entry.getKey() + " = " + entry.getValue()); // Prints the filter key and its corresponding value
            }
            System.out.println(); // Prints an empty line for formatting

            List<JsonObject> entries = loadJSON(jsonFilePath); // Loads the Json File into a list
            List<JsonObject> filteredEntries = filterEntries(entries, filters); // Filtering the entries based on the provided filters

            for (JsonObject entry : filteredEntries) { // Iterates over each filtered entry and print its details
                printEntryDetails(entry);
            }

            StringBuilder motivations = new StringBuilder(); // Initializes a StringBuilder to store all motivation texts
            for (JsonObject entry : filteredEntries) { // Iterates over each filtered entry and append its motivation text to the StringBuilder
                motivations.append(getMotivationText(entry)).append(" ");
            }

            Set<String> stopwords = loadStopwords(stopwordsFilePath); // Loads the stopwards File into a set

            Map<String, Integer> wordCounts = countWords(motivations.toString(), stopwords); // Counts word frequencies in the motivation text
            System.out.println("\nMost frequent words in the motivation fields:\n");
            printTopWords(wordCounts, 10); // Prints the top 10 most frequent words
            System.out.println(); // New Line for formatting
        } catch (IllegalArgumentException e) { // Catches invalid arguments
            System.out.println("Error: " + e.getMessage()); // Handles the invalid arguments
        } catch (JsonParsingException e) { // Catches json parsing errors
            System.out.println("Error parsing JSON: " + e.getMessage()); // Handles json parsing errors
        } catch (IOException e) { // Catches errors related to file I/O operations
            System.out.println("IO Error: " + e.getMessage()); // Handles the I/O errors
        }
    }

    public static List<JsonObject> loadJSON(String jsonFilePath) throws IOException, JsonParsingException {
        List<JsonObject> entries = new ArrayList<>(); // Initializing a list to store the json entries
        try (FileInputStream fis = new FileInputStream(new File(jsonFilePath)); // Opening the JSON file for reading
             JsonReader reader = Json.createReader(fis)) { // Creating a JsonReader to parse the JSON file
            JsonObject jsonObject = reader.readObject(); // Reading the file into a jsonObject

            if (!jsonObject.containsKey("prizes")) { // Checks if the "prizes" field exists and is not null
                throw new IllegalArgumentException("JSON file is missing the 'prizes' field: " + jsonFilePath); // Throws exception if null
            }

            JsonArray prizes = jsonObject.getJsonArray("prizes"); // Iterating over each value in the array
            if (prizes == null) {
                throw new IllegalArgumentException("The 'prizes' field is null in the JSON file: " + jsonFilePath);
            }

            for (JsonValue prizeValue : prizes) {
                if (prizeValue.getValueType() == JsonValue.ValueType.OBJECT) { // Checking if the value is a JsonObject
                    entries.add((JsonObject) prizeValue); // Adding the JSON object to the entries list
                }
            }
        }
        return entries;
    }

    public static Set<String> loadStopwords(String stopwordsFilePath) throws IOException {
        Set<String> stopwords = new HashSet<>(); // Creating a set to store the stopwords
        File file = new File(stopwordsFilePath);
        if (file.length() == 0) { // Check if the file is empty
            throw new IllegalArgumentException("Stopwords file is empty: " + stopwordsFilePath);
        }
        try (Scanner scanner = new Scanner(file)) { // Putting all thw words in the Set
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase(); // Converting the words into lowercase and removing any whitespace for any unnecessary errors
                if (!word.isEmpty()) {
                    stopwords.add(word); // Adding the words to the Set
                }
            }
        }
        return stopwords;
    }

    public static List<JsonObject> filterEntries(List<JsonObject> entries, Map<String, String> filters) {
        List<JsonObject> filtered = new ArrayList<>(); // Initializing a list to store the filtered entries
        for (JsonObject entry : entries) { // Iterating over each entry in the input list
            boolean match = true; // Setting the match as true
            for (Map.Entry<String, String> filter : filters.entrySet()) { // Iterating over each filter in the filters map
                String key = filter.getKey();
                String filterValue = filter.getValue();
                if (entry.containsKey(key)) { // Checking if the entry contains the filter key
                    String entryValue = entry.getString(key, "").toLowerCase(); // Getting the value of the filter key from the entry and convert it to lowercase
                    if (!entryValue.equals(filterValue)) { // If filter is not found
                        match = false; // Setting the match to false
                        break;
                    }
                } else {
                    match = false;
                    break;
                }
            }
            if (match) {
                filtered.add(entry); // Adding entry if found in filters to the filtered list
            }
        }
        return filtered;
    }

    public static void printEntryDetails(JsonObject entry) {
        String year = entry.getString("year", "N/A"); // Retrieves the year field or sets it as N/A if not found
        String category = entry.getString("category", "N/A"); // Retrieves the category field or sets it as N/A if not found
        System.out.println("year: " + year);
        System.out.println("category: " + category);

        if (entry.containsKey("overallMotivation")) {  // Checks if the entry contains an overallMotivation field
            String motivation = stripQuotes(entry.getString("overallMotivation", ""));
            System.out.println("Motivation: " + motivation);
        }

        if (entry.containsKey("laureates")) {  // Checking if the entry contains a "laureates" field
            javax.json.JsonArray laureates = entry.getJsonArray("laureates"); // Retrieving the Laureates array
            System.out.println("Number of laureates: " + laureates.size()); // Printing the number of laureates
            for (JsonObject laureate : laureates.getValuesAs(JsonObject.class)) { // Iterating through the array
                System.out.println("\t- firstname: " + laureate.getString("firstname", "")); // Printing first name
                System.out.println("\t- surname: " + laureate.getString("surname", "")); // Printinf surname
                String motivation = laureate.containsKey("motivation") // Check if the laureate has a "motivation" field
                        ? laureate.getString("motivation", "") // If it does exist, get its value
                        : "";  // If no motivation, set to empty
                motivation = stripQuotes(motivation);
                System.out.println("\t- motivation: " + motivation); // Prints the motivation
                System.out.println();
            }
        }

        System.out.println();
    }

    public static String getMotivationText(JsonObject entry) {
        StringBuilder motivationText = new StringBuilder(); // Initiates a new StringBuilder
        if (entry.containsKey("laureates")) { // Checks if the map key contains laureates
            javax.json.JsonArray laureates = entry.getJsonArray("laureates"); // Retrieving thr Array
            for (JsonObject laureate : laureates.getValuesAs(JsonObject.class)) { // Iterate over each laureate in the laureates array
                String motivation = laureate.containsKey("motivation") // Check if the laureate has a "motivation" field
                        ? laureate.getString("motivation", "") // If it does, it uses the motivation field
                        : laureate.getString("overallMotivation", ""); // Else it gets the overall Motivation
                motivationText.append(stripQuotes(motivation)).append(" "); // Strip quotes here as well
            }
        } else if (entry.containsKey("motivation") || entry.containsKey("overallMotivation")) { // If the entry does not contain a laureates field, check for motivation or overallMotivation directly
            String motivation = entry.containsKey("motivation")
                    ? entry.getString("motivation", "")
                    : entry.getString("overallMotivation", "");
            motivationText.append(stripQuotes(motivation)).append(" "); // Strip quotes here as well
        }
        return motivationText.toString();
    }

    public static String stripQuotes(String text) {
        text = text.trim(); // Removes whitespaces from the text
        if (text.startsWith("\"") && text.endsWith("\"") && text.length() >= 2) { // Checks if the first and last characters are quotation marks and checks if the length is more than 2
            return text.substring(1, text.length() - 1); // Returns the statement without the quotation
        }
        return text;
    }

    public static Map<String, Integer> countWords(String text, Set<String> stopwords) {
        Map<String, Integer> counts = new HashMap<>(); // Initializing a HashMap to store word counts
        text = text.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase(); // Cleaning the text by replacing all characters which are not part of the alphabet and number system with spaces and converting to lowercase
        String[] words = text.split("\\s+"); // Spliting the cleaned text into individual words based on whitespace
        for (String word : words) { // Iterating over each word in the array
            if (word.isEmpty() || word.length() == 1 || stopwords.contains(word))
                continue; // Skiping the current iteration if the word is invalid or a stopword
            counts.put(word, counts.getOrDefault(word, 0) + 1); // Updating the word count in the HashMap
        }
        return counts;
    }

    public static void printTopWords(Map<String, Integer> counts, int n) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(counts.entrySet()); // Converting the map entries to a list

        // USing Selection Sort
        for (int i = 0; i < list.size() - 1; i++) {
            int maxIndex = i; // Assume the current index has the maximum value
            for (int j = i + 1; j < list.size(); j++) {
                Map.Entry<String, Integer> current = list.get(j);
                Map.Entry<String, Integer> max = list.get(maxIndex);

                // Comparing by value in descending order, and by key in ascending order if values are equal
                if (current.getValue() > max.getValue() ||
                        (current.getValue().equals(max.getValue()) && current.getKey().compareTo(max.getKey()) < 0)) {
                    maxIndex = j; // Update the index of the maximum element
                }
            }

            // Swap the found maximum element with the element at index i
            if (maxIndex != i) {
                Map.Entry<String, Integer> temp = list.get(i);
                list.set(i, list.get(maxIndex));
                list.set(maxIndex, temp);
            }
        }

        // Print the top n words
        for (int i = 0; i < n && i < list.size(); i++) {
            Map.Entry<String, Integer> entry = list.get(i);
            System.out.println("\t" + entry.getValue() + "\t" + entry.getKey());
        }
    }

    private static void validateFilePath(String filePath) throws IllegalArgumentException {
        File file = new File(filePath); // Creates a new object with the file path
        if (!file.exists()) { // Checks if file exists
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }
        if (!file.canRead()) { // Checks if the file is a readable file
            throw new IllegalArgumentException("File cannot be read: " + filePath);
        }
    }
}