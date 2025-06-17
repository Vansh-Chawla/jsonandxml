import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CS1003P2MyTest {

    public static void main(String[] args) {
        // Create a temporary JSON file and stopwords file for testing
        createTestFiles();

        // Run the tests
        testNoFilters();
        testMultipleFilters();
        testInvalidJSONFilePath();
        testInvalidStopwordsFilePath();
        testInvalidFilterKey();
        testEmptyJSONFile();
        testJSONFileWithMultipleLaureatesAndNoMotivation();
        testJSONFileWithOnlyOverallMotivation();
        testJSONFileWithOnlyMotivation();
        testJSONFileWithNoLaureates();
        testJSONFileWithDuplicateEntries();
        testJSONFileWithMissingFields();
        testMotivationFieldsWithOnlyStopwords();
        testEmptyStopwordsFile();

        // Clean up temporary files
        cleanupTestFiles();
    }

    private static void createTestFiles() {
        try {
            // Create a sample JSON file
            FileWriter jsonWriter = new FileWriter("test.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\",\"category\":\"physics\",\"laureates\":[{\"firstname\":\"Albert\",\"surname\":\"Einstein\",\"motivation\":\"\\\"for his discovery of the photoelectric effect\\\"\"}]}]}");
            jsonWriter.close();

            // Create a sample stopwords file
            FileWriter stopwordsWriter = new FileWriter("stopwords.txt");
            stopwordsWriter.write("the\nof\nin\n");
            stopwordsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanupTestFiles() {
        new File("test.json").delete();
        new File("stopwords.txt").delete();
    }

    private static void testNoFilters() {
        System.out.println("Testing with no filters...");
        CS1003P2.main(new String[]{"test.json", "stopwords.txt"});
        System.out.println();
    }

    private static void testMultipleFilters() {
        System.out.println("Testing with multiple filters...");
        CS1003P2.main(new String[]{"test.json", "stopwords.txt", "year", "2021", "category", "physics"});
        System.out.println();
    }

    private static void testInvalidJSONFilePath() {
        System.out.println("Testing with invalid JSON file path...");
        CS1003P2.main(new String[]{"invalid.json", "stopwords.txt"});
        System.out.println();
    }

    private static void testInvalidStopwordsFilePath() {
        System.out.println("Testing with invalid stopwords file path...");
        CS1003P2.main(new String[]{"test.json", "invalid.txt"});
        System.out.println();
    }

    private static void testInvalidFilterKey() {
        System.out.println("Testing with invalid filter key...");
        CS1003P2.main(new String[]{"test.json", "stopwords.txt", "invalidKey", "value"});
        System.out.println();
    }

    private static void testEmptyJSONFile() {
        System.out.println("Testing with empty JSON file...");
        try {
            FileWriter jsonWriter = new FileWriter("empty.json");
            jsonWriter.write("{}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"empty.json", "stopwords.txt"});
            new File("empty.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testJSONFileWithMultipleLaureatesAndNoMotivation() {
        System.out.println("Testing JSON file with multiple laureates and no motivation...");
        try {
            FileWriter jsonWriter = new FileWriter("multiple_laureates.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\",\"category\":\"physics\",\"laureates\":[{\"firstname\":\"Albert\",\"surname\":\"Einstein\"},{\"firstname\":\"Marie\",\"surname\":\"Curie\"}]}]}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"multiple_laureates.json", "stopwords.txt"});
            new File("multiple_laureates.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testJSONFileWithOnlyOverallMotivation() {
        System.out.println("Testing JSON file with only overall motivation...");
        try {
            FileWriter jsonWriter = new FileWriter("overall_motivation.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\",\"category\":\"physics\",\"overallMotivation\":\"\\\"for their contributions to the understanding of the universe\\\"\"}]}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"overall_motivation.json", "stopwords.txt"});
            new File("overall_motivation.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testJSONFileWithOnlyMotivation() {
        System.out.println("Testing JSON file with only motivation...");
        try {
            FileWriter jsonWriter = new FileWriter("motivation.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\",\"category\":\"physics\",\"laureates\":[{\"firstname\":\"Albert\",\"surname\":\"Einstein\",\"motivation\":\"\\\"for his discovery of the photoelectric effect\\\"\"}]}]}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"motivation.json", "stopwords.txt"});
            new File("motivation.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testJSONFileWithNoLaureates() {
        System.out.println("Testing JSON file with no laureates...");
        try {
            FileWriter jsonWriter = new FileWriter("no_laureates.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\",\"category\":\"physics\"}]}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"no_laureates.json", "stopwords.txt"});
            new File("no_laureates.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testJSONFileWithDuplicateEntries() {
        System.out.println("Testing JSON file with duplicate entries...");
        try {
            FileWriter jsonWriter = new FileWriter("duplicate_entries.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\",\"category\":\"physics\",\"laureates\":[{\"firstname\":\"Albert\",\"surname\":\"Einstein\",\"motivation\":\"\\\"for his discovery of the photoelectric effect\\\"\"}]},{\"year\":\"2021\",\"category\":\"physics\",\"laureates\":[{\"firstname\":\"Albert\",\"surname\":\"Einstein\",\"motivation\":\"\\\"for his discovery of the photoelectric effect\\\"\"}]}]}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"duplicate_entries.json", "stopwords.txt"});
            new File("duplicate_entries.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testJSONFileWithMissingFields() {
        System.out.println("Testing JSON file with missing fields...");
        try {
            FileWriter jsonWriter = new FileWriter("missing_fields.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\"}]}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"missing_fields.json", "stopwords.txt"});
            new File("missing_fields.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testMotivationFieldsWithOnlyStopwords() {
        System.out.println("Testing motivation fields with only stopwords...");
        try {
            FileWriter jsonWriter = new FileWriter("stopwords_motivation.json");
            jsonWriter.write("{\"prizes\":[{\"year\":\"2021\",\"category\":\"physics\",\"laureates\":[{\"firstname\":\"Albert\",\"surname\":\"Einstein\",\"motivation\":\"\\\"the of in\\\"\"}]}]}");
            jsonWriter.close();
            CS1003P2.main(new String[]{"stopwords_motivation.json", "stopwords.txt"});
            new File("stopwords_motivation.json").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void testEmptyStopwordsFile() {
        System.out.println("Testing with empty stopwords file...");
        try {
            FileWriter stopwordsWriter = new FileWriter("empty_stopwords.txt");
            stopwordsWriter.write("");
            stopwordsWriter.close();
            CS1003P2.main(new String[]{"test.json", "empty_stopwords.txt"});
            new File("empty_stopwords.txt").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}