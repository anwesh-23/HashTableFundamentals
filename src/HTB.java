import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HTB {

    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();

        System.out.println("checkAvailability(\"john_doe\") -> "
                + checker.checkAvailability("john_doe"));

        System.out.println("checkAvailability(\"jane_smith\") -> "
                + checker.checkAvailability("jane_smith"));

        System.out.println("\nSuggestions for john_doe:");
        System.out.println(checker.suggestAlternatives("john_doe"));

        // simulate multiple attempts
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");

        System.out.println("\nMost attempted username -> "
                + checker.getMostAttempted());
    }

    // make static so main can access it easily
    static class UsernameChecker {

        // username -> userId
        private ConcurrentHashMap<String, Integer> users = new ConcurrentHashMap<>();

        // username -> attempt frequency
        private ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();

        public UsernameChecker() {
            users.put("john_doe", 101);
            users.put("admin", 1);
            users.put("thor", 202);
        }

        // O(1) availability check
        public boolean checkAvailability(String username) {
            attempts.put(username, attempts.getOrDefault(username, 0) + 1);
            return !users.containsKey(username);
        }

        // suggest similar usernames
        public List<String> suggestAlternatives(String username) {

            List<String> suggestions = new ArrayList<>();

            // append numbers
            for (int i = 1; i <= 3; i++) {
                String suggestion = username + i;
                if (!users.containsKey(suggestion)) {
                    suggestions.add(suggestion);
                }
            }

            // replace underscore with dot
            if (username.contains("_")) {
                String dotVersion = username.replace("_", ".");
                if (!users.containsKey(dotVersion)) {
                    suggestions.add(dotVersion);
                }
            }

            // add random number suggestion
            String randomSuggestion = username + (int)(Math.random() * 100);
            if (!users.containsKey(randomSuggestion)) {
                suggestions.add(randomSuggestion);
            }

            return suggestions;
        }

        // get most attempted username
        public String getMostAttempted() {
            return attempts.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("No attempts yet");
        }
    }
}