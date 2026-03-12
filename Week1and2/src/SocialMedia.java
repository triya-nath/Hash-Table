import java.util.*;

public class SocialMedia {

    // username -> userId
    private HashMap<String, Integer> users;

    // username -> attempt frequency
    private HashMap<String, Integer> attempts;

    public SocialMedia() {
        users = new HashMap<>();
        attempts = new HashMap<>();
    }

    // Register user (for simulation)
    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // track attempts
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !users.containsKey(username);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // modify characters
        suggestions.add(username.replace("_", "."));
        suggestions.add(username + "_official");

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String maxUser = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    // Main method for testing
    public static void main(String[] args) {

        SocialMedia sm = new SocialMedia();

        // existing users
        sm.registerUser("triya_nath", 101);
        sm.registerUser("admin", 102);

        System.out.println("Check triya_nath: " + sm.checkAvailability("triya_nath"));
        System.out.println("Check isha_chakravorty: " + sm.checkAvailability("isha_chakravorty"));

        System.out.println("\nSuggestions for triya_nath:");
        System.out.println(sm.suggestAlternatives("triya_nath"));

        // simulate attempts
        for(int i=0;i<5;i++) sm.checkAvailability("admin");
        for(int i=0;i<3;i++) sm.checkAvailability("triya_nath");

        System.out.println("\nMost Attempted Username:");
        System.out.println(sm.getMostAttempted());
    }
}