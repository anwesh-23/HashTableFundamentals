//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    UsernameChecker checker = new UsernameChecker();

    IO.println("checkAvailability(\"john_doe\") → "
            + checker.checkAvailability("john_doe"));

    IO.println("checkAvailability(\"jane_smith\") → "
            + checker.checkAvailability("jane_smith"));

    IO.println("\nSuggestions for john_doe:");
    IO.println(checker.suggestAlternatives("john_doe"));

    // simulate multiple attempts
    checker.checkAvailability("admin");
    checker.checkAvailability("admin");
    checker.checkAvailability("admin");

    IO.println("\nMost attempted username → "
            + checker.getMostAttempted());
}

class UsernameChecker {

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

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;
            if (!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        if (username.contains("_")) {
            String dotVersion = username.replace("_", ".");
            if (!users.containsKey(dotVersion)) {
                suggestions.add(dotVersion);
            }
        }

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
