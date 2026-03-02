import java.util.*;

public class HTB {

    private static final int N = 5; // 5-grams

    // ngram -> set of document IDs
    private Map<String, Set<String>> index = new HashMap<>();

    // extract n-grams
    private List<String> extractNGrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {
            String gram = String.join(" ", Arrays.copyOfRange(words, i, i + N));
            grams.add(gram);
        }
        return grams;
    }

    // add document to database
    public void addDocument(String docId, String text) {
        for (String gram : extractNGrams(text)) {
            index.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }

    // analyze similarity
    public void analyze(String docId, String text) {
        List<String> grams = extractNGrams(text);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {
            if (index.containsKey(gram)) {
                for (String existingDoc : index.get(gram)) {
                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        for (String doc : matchCount.keySet()) {
            double similarity = (matchCount.get(doc) * 100.0) / grams.size();
            System.out.println("Match with " + doc + " → " + similarity + "%");
        }
    }

    public static void main(String[] args) {
        HTB detector = new HTB();

        detector.addDocument("essay_089",
                "data structures and algorithms are important for computer science");

        detector.addDocument("essay_092",
                "data structures and algorithms are very important in computer science");

        detector.analyze("essay_123",
                "data structures and algorithms are important in computer science");
    }
}