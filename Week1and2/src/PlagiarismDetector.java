import java.util.*;

public class PlagiarismDetector {

    // n-gram -> set of document IDs
    private HashMap<String, Set<String>> ngramIndex;

    // documentId -> list of n-grams
    private HashMap<String, List<String>> documentNgrams;

    private int N = 5; // size of n-gram

    public PlagiarismDetector() {
        ngramIndex = new HashMap<>();
        documentNgrams = new HashMap<>();
    }

    // Extract n-grams from document text
    private List<String> extractNgrams(String text) {

        List<String> ngrams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add document to database
    public void addDocument(String documentId, String text) {

        List<String> ngrams = extractNgrams(text);
        documentNgrams.put(documentId, ngrams);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(documentId);
        }
    }

    // Analyze document for plagiarism
    public void analyzeDocument(String documentId) {

        List<String> ngrams = documentNgrams.get(documentId);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            Set<String> docs = ngramIndex.get(gram);

            if (docs != null) {

                for (String doc : docs) {

                    if (!doc.equals(documentId)) {
                        matchCount.put(doc, matchCount.getOrDefault(doc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String otherDoc = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with \""
                    + otherDoc + "\"");

            System.out.printf("Similarity: %.2f%%\n", similarity);

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED");
            }

            System.out.println();
        }
    }

    // Main method for testing
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "data structures and algorithms are important for computer science students";
        String essay2 = "data structures and algorithms are very important for computer science";
        String essay3 = "machine learning and artificial intelligence are modern technologies";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay3);

        System.out.println("Analyzing essay_092.txt\n");

        detector.analyzeDocument("essay_092.txt");
    }
}